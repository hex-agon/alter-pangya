package work.fking.pangya.networking.lzo;

public class MiniLZO {

    private static final int C_0_TOP = 1;
    private static final int C_0_TRY_MATCH = 2;
    private static final int C_0_LITERAL = 3;
    private static final int C_0_MATCH = 4;
    private static final int C_0_M_3_M_4_LEN = 5;
    private static final int C_0_M_3_M_4_OFFSET = 6;
    private static final int C_0_LAST = 7;

    private MiniLZO() {
    }

    private static int _lzo1x_1_do_compress(byte[] in, int inLength, byte[] out, MInt outLength, int[] dict) {
        int ip;
        int in_base = 0;
        int out_base = 0;
        int op;
        int in_end = in_base + inLength;
        int ip_end = in_base + inLength - 8 - 5;
        int ii = 0;
        int state = C_0_TOP;
        op = out_base;
        ip = in_base;

        ip += 4;
        int m_pos = in_base;
        int m_off = in_base;
        int m_len = 0;
        int dindex = 0;

        loop0:
        while (true) {
            switch (state) {
                case C_0_TOP:
                    dindex = 0x21 * (((in[ip + 1 + 2 + 1] & 0xff) << 6 ^ (in[ip + 1 + 1 + 1] & 0xff) << 5 ^ (in[ip + 1] & 0xff) << 5 ^ in[ip] & 0xff) >> 5) & (1 << 14) - 1;
                    m_pos = dict[dindex];
                    m_pos = ip - (ip - m_pos);
                    if (m_pos <= in_base || (m_off = ip - m_pos) <= 0 || m_off > 0xbfff) {
                        state = C_0_LITERAL;
                        continue loop0;
                    }
                    if (m_off <= 0x0800 || in[m_pos + 3] == in[ip + 3]) {
                        state = C_0_TRY_MATCH;
                        continue loop0;
                    }
                    dindex = dindex & (1 << 14) - 1 & 0x7ff ^ (((1 << 14) - 1 >> 1) + 1 | 0x1f);

                    m_pos = dict[dindex];
                    if (m_pos < in_base || (m_off = ip - m_pos) <= 0 || m_off > 0xbfff) {
                        state = C_0_LITERAL;
                        continue loop0;
                    }
                    if (m_off <= 0x0800 || in[m_pos + 3] == in[ip + 3]) {
                        state = C_0_TRY_MATCH;
                        continue loop0;
                    }
                    state = C_0_LITERAL;
                    continue loop0;

                case C_0_TRY_MATCH:
                    if (in[m_pos] == in[ip] && in[m_pos + 1] == in[ip + 1]) {
                        if (in[m_pos + 2] == in[ip + 2]) {
                            state = C_0_MATCH;
                            continue loop0;
                        }
                    }

                case C_0_LITERAL:
                    dict[dindex] = ip;
                    ip++;
                    if (ip >= ip_end) {
                        break loop0;
                    }
                    state = C_0_TOP;
                    continue loop0;

                case C_0_MATCH:
                    dict[dindex] = ip;
                    if ((ip - ii) > 0) {
                        int t = ip - ii;

                        op = getOp(out, op, t);
                        do {
                            out[op++] = in[ii++];
                        } while (--t > 0);
                    }

                    ip += 3;
                    if (in[m_pos + 3] != in[ip++] || in[m_pos + 4] != in[ip++] || in[m_pos + 5] != in[ip++] || in[m_pos + 6] != in[ip++] || in[m_pos + 7] != in[ip++] || in[m_pos + 8] != in[ip++]) {
                        --ip;
                        m_len = ip - ii;

                        if (m_off <= 0x0800) {
                            m_off -= 1;

                            out[op++] = (byte) (((m_len - 1) << 5) | ((m_off & 7) << 2));
                            out[op++] = (byte) (m_off >> 3);
                        } else if (m_off <= 0x4000) {
                            m_off -= 1;
                            out[op++] = (byte) (32 | (m_len - 2));
                            state = C_0_M_3_M_4_OFFSET;
                            continue loop0;
                        } else {
                            m_off -= 0x4000;
                            out[op++] = (byte) (16 | ((m_off & 0x4000) >> 11) | (m_len - 2));
                            state = C_0_M_3_M_4_OFFSET;
                            continue loop0;
                        }
                    } else {
                        int m = m_pos + 8 + 1;
                        while (ip < in_end && in[m] == in[ip]) {
                            m++;
                            ip++;
                        }
                        m_len = ip - ii;

                        if (m_off <= 0x4000) {
                            m_off -= 1;
                            if (m_len <= 33) {
                                out[op++] = (byte) (32 | (m_len - 2));
                            } else {
                                m_len -= 33;
                                out[op++] = 32;
                                state = C_0_M_3_M_4_LEN;
                                continue loop0;
                            }
                        } else {
                            m_off -= 0x4000;
                            if (m_len <= 9) {
                                out[op++] = (byte) (16 | ((m_off & 0x4000) >> 11) | (m_len - 2));
                            } else {
                                m_len -= 9;
                                out[op++] = (byte) (16 | ((m_off & 0x4000) >> 11));
                                while (m_len > 255) {
                                    m_len -= 255;
                                    out[op++] = 0;
                                }
                                out[op++] = (byte) m_len;
                            }
                        }
                        out[op++] = (byte) ((m_off & 63) << 2);
                        out[op++] = (byte) (m_off >> 6);
                    }
                    state = C_0_LAST;
                    continue loop0;
                case C_0_M_3_M_4_LEN:
                    while (m_len > 255) {
                        m_len -= 255;
                        out[op++] = 0;
                    }
                    out[op++] = (byte) m_len;

                case C_0_M_3_M_4_OFFSET:
                    out[op++] = (byte) ((m_off & 63) << 2);
                    out[op++] = (byte) (m_off >> 6);
                case C_0_LAST:
                    ii = ip;
                    if (ip >= ip_end) {
                        break loop0;
                    }
                    state = C_0_TOP;
            }
        }

        outLength.v = op - out_base;
        return in_end - ii;
    }

    /**
     * Compress the data.
     * Compressed length is returned via out_len. Pass the integer array for
     * dictionary (so that user can reuse the same over multiple calls. Ensure
     * to zero out the dict contents).
     *
     * @param in        Input byte array to be compressed
     * @param inLength  Input length
     * @param out       compressed output byte array. Ensure out_len =  (in_len + in_len / 16 + 64 + 3)
     * @param outLength Compressed data length
     * @param dict      Dictionary array. Zero out before reuse.
     */
    public static void lzo1x_1_compress(byte[] in, int inLength, byte[] out, MInt outLength, int[] dict) {
        int in_base = 0;
        int out_base = 0;
        int op = 0;
        int t;

        if (inLength <= 8 + 5) {
            t = inLength;
        } else {
            t = _lzo1x_1_do_compress(in, inLength, out, outLength, dict);
            op += outLength.v;
        }

        if (t > 0) {
            int ii = in_base + inLength - t;

            if (op == out_base && t <= 238) {
                out[op++] = (byte) (17 + t);
            } else {
                op = getOp(out, op, t);
            }
            do {
                out[op++] = in[ii++];
            } while (--t > 0);
        }

        out[op++] = (byte) (16 | 1);
        out[op++] = 0;
        out[op++] = 0;

        outLength.v = op - out_base;
    }

    private static int getOp(byte[] out, int op, int t) {
        if (t <= 3) {
            out[op - 2] |= (byte) t;
        } else if (t <= 18) {
            out[op++] = (byte) (t - 3);
        } else {
            int tt = t - 18;
            out[op++] = 0;
            while (tt > 255) {
                tt -= 255;
                out[op++] = 0;
            }
            out[op++] = (byte) tt;
        }
        return op;
    }
}
