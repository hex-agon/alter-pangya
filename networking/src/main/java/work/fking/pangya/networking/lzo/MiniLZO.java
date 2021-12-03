package work.fking.pangya.networking.lzo;

import static work.fking.pangya.networking.lzo.LZOState.*;

public final class MiniLZO {

    private MiniLZO() {
    }

    private static int lzo1x_compress(byte[] input, int inLength, byte[] out, CompressionState state, int[] dict) {
        state.inputPos = 4;
        state.lzoState = TOP;

        while (state.lzoState != DONE) {
            switch (state.lzoState) {
                case TOP -> handleTop(input, state, dict);
                case TRY_MATCH -> handleTryMatch(input, state);
                case LITERAL -> handleLiteral(inLength, state, dict);
                case MATCH -> handleMatch(input, inLength, out, state, dict);
                case M3_M4_LEN -> handleM3M4Len(out, state);
                case M3_M4_OFFSET -> handleM3M4Offset(out, state);
                case LAST -> handleLast(inLength, state);
            }
        }
        state.size = state.pos;
        return inLength - state.ii;
    }

    private static void handleTop(byte[] input, CompressionState state, int[] dict) {
        state.dictIdx = 0x21 * (
                ((input[state.inputPos + 4] & 0xff) << 6
                        ^ (input[state.inputPos + 3] & 0xff) << 5
                        ^ (input[state.inputPos + 1] & 0xff) << 5
                        ^ input[state.inputPos] & 0xff
                ) >> 5
        ) & 0x3fff;
        state.m_pos = dict[state.dictIdx];
        state.m_pos = state.inputPos - (state.inputPos - state.m_pos);
        if (state.m_pos <= 0 || (state.m_off = state.inputPos - state.m_pos) <= 0 || state.m_off > 0xbfff) {
            state.lzoState = LITERAL;
            return;
        }
        if (state.m_off <= 0x0800 || input[state.m_pos + 3] == input[state.inputPos + 3]) {
            state.lzoState = TRY_MATCH;
            return;
        }
        state.dictIdx = state.dictIdx & 0x7ff ^ 0x201f;

        state.m_pos = dict[state.dictIdx];
        if (state.m_pos < 0 || (state.m_off = state.inputPos - state.m_pos) <= 0 || state.m_off > 0xbfff) {
            state.lzoState = LITERAL;
            return;
        }
        if (state.m_off <= 0x0800 || input[state.m_pos + 3] == input[state.inputPos + 3]) {
            state.lzoState = TRY_MATCH;
            return;
        }
        state.lzoState = LITERAL;
    }

    private static void handleTryMatch(byte[] input, CompressionState state) {
        if (input[state.m_pos] == input[state.inputPos] && input[state.m_pos + 1] == input[state.inputPos + 1]) {
            if (input[state.m_pos + 2] == input[state.inputPos + 2]) {
                state.lzoState = MATCH;
                return;
            }
        }
        state.lzoState = LITERAL;
    }

    private static void handleLiteral(int inLength, CompressionState state, int[] dict) {
        dict[state.dictIdx] = state.inputPos;
        state.inputPos++;
        if (state.inputPos >= inLength - 8 - 5) {
            state.lzoState = DONE;
            return;
        }
        state.lzoState = TOP;
    }

    private static void handleMatch(byte[] input, int inLength, byte[] out, CompressionState state, int[] dict) {
        dict[state.dictIdx] = state.inputPos;
        if ((state.inputPos - state.ii) > 0) {
            int value = state.inputPos - state.ii;

            if (value <= 3) {
                out[state.pos - 2] |= (byte) value;
            } else if (value <= 18) {
                out[state.pos++] = (byte) (value - 3);
            } else {
                int tt = value - 18;
                out[state.pos++] = 0;
                while (tt > 255) {
                    tt -= 255;
                    out[state.pos++] = 0;
                }
                out[state.pos++] = (byte) tt;
            }
            do {
                out[state.pos++] = input[state.ii++];
            } while (--value > 0);
        }

        state.inputPos += 3;
        if (input[state.m_pos + 3] != input[state.inputPos++]
                || input[state.m_pos + 4] != input[state.inputPos++]
                || input[state.m_pos + 5] != input[state.inputPos++]
                || input[state.m_pos + 6] != input[state.inputPos++]
                || input[state.m_pos + 7] != input[state.inputPos++]
                || input[state.m_pos + 8] != input[state.inputPos++]
        ) {
            --state.inputPos;
            state.m_len = state.inputPos - state.ii;

            if (state.m_off <= 0x0800) {
                state.m_off -= 1;

                out[state.pos++] = (byte) (state.m_len - 1 << 5 | (state.m_off & 7) << 2);
                out[state.pos++] = (byte) (state.m_off >> 3);
            } else if (state.m_off <= 0x4000) {
                state.m_off -= 1;
                out[state.pos++] = (byte) (32 | state.m_len - 2);
                state.lzoState = M3_M4_OFFSET;
                return;
            } else {
                state.m_off -= 0x4000;
                out[state.pos++] = (byte) (16 | (state.m_off & 0x4000) >> 11 | state.m_len - 2);
                state.lzoState = M3_M4_OFFSET;
                return;
            }
        } else {
            int m = state.m_pos + 8 + 1;
            while (state.inputPos < inLength && input[m] == input[state.inputPos]) {
                m++;
                state.inputPos++;
            }
            state.m_len = state.inputPos - state.ii;

            if (state.m_off <= 0x4000) {
                state.m_off -= 1;
                if (state.m_len <= 33) {
                    out[state.pos++] = (byte) (32 | state.m_len - 2);
                } else {
                    state.m_len -= 33;
                    out[state.pos++] = 32;
                    state.lzoState = M3_M4_LEN;
                    return;
                }
            } else {
                state.m_off -= 0x4000;
                if (state.m_len <= 9) {
                    out[state.pos++] = (byte) (16 | (state.m_off & 0x4000) >> 11 | state.m_len - 2);
                } else {
                    state.m_len -= 9;
                    out[state.pos++] = (byte) (16 | (state.m_off & 0x4000) >> 11);
                    while (state.m_len > 255) {
                        state.m_len -= 255;
                        out[state.pos++] = 0;
                    }
                    out[state.pos++] = (byte) state.m_len;
                }
            }
            out[state.pos++] = (byte) ((state.m_off & 63) << 2);
            out[state.pos++] = (byte) (state.m_off >> 6);
        }
        state.lzoState = LAST;
    }

    private static void handleM3M4Len(byte[] out, CompressionState state) {
        while (state.m_len > 255) {
            state.m_len -= 255;
            out[state.pos++] = 0;
        }
        out[state.pos++] = (byte) state.m_len;
        state.lzoState = M3_M4_OFFSET;
    }

    private static void handleM3M4Offset(byte[] out, CompressionState state) {
        out[state.pos++] = (byte) ((state.m_off & 63) << 2);
        out[state.pos++] = (byte) (state.m_off >> 6);
        state.lzoState = LAST;
    }

    private static void handleLast(int inLength, CompressionState state) {
        state.ii = state.inputPos;
        if (state.inputPos >= inLength - 8 - 5) {
            state.lzoState = DONE;
            return;
        }
        state.lzoState = TOP;
    }

    /**
     * Compress the data.
     * Compressed length is returned via out_len. Pass the integer array for
     * dictionary (so that user can reuse the same over multiple calls. Ensure
     * to zero out the dict contents).
     *
     * @param in       Input byte array to be compressed
     * @param inLength Input length
     * @param out      compressed output byte array. Ensure out_len =  (in_len + in_len / 16 + 64 + 3)
     * @param dict     Dictionary array. Zero out before reuse.
     * @return
     */
    public static int lzo1x_compress(byte[] in, int inLength, byte[] out, int[] dict) {
        int inOffset = 0;
        int pos = 0;
        int t;

        if (inLength <= 13) {
            t = inLength;
        } else {
            var compressionState = new CompressionState();
            t = lzo1x_compress(in, inLength, out, compressionState, dict);
            pos += compressionState.size;
        }

        if (t > 0) {
            int ii = inOffset + inLength - t;

            if (pos == 0 && t <= 238) {
                out[pos++] = (byte) (t + 17);
            } else {
                if (t <= 3) {
                    out[pos - 2] |= (byte) t;
                } else if (t <= 18) {
                    out[pos++] = (byte) (t - 3);
                } else {
                    int tt = t - 18;
                    out[pos++] = 0;
                    while (tt > 255) {
                        tt -= 255;
                        out[pos++] = 0;
                    }
                    out[pos++] = (byte) tt;
                }
            }
            out[pos++] = in[ii++];

            while (--t > 0) {
                out[pos++] = in[ii++];
            }
        }

        out[pos++] = (byte) 17;
        out[pos++] = 0;
        out[pos++] = 0;

        return pos;
    }

    private static class CompressionState {

        private LZOState lzoState;
        private int dictIdx;
        private int inputPos;
        private int pos;
        private int ii;
        private int m_pos;
        private int m_off;
        private int m_len;
        private int size;
    }
}
