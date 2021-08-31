package work.fking.pangya.iff;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.awt.image.BufferedImage;

public class TGAReader {

    private TGAReader() {
    }

    public static BufferedImage read(byte[] tgaBytes) {
        var buf = Unpooled.wrappedBuffer(tgaBytes);

        var idLength = buf.readUnsignedByte();
        var colorMapType = buf.readUnsignedByte();
        var imgType = buf.readUnsignedByte();
        // header colormap
        var firstEntryIdx = buf.readUnsignedShortLE();
        var length = buf.readUnsignedShortLE();
        var entrySize = buf.readUnsignedByte();
        // header colormap end

        // header img specs
        var originX = buf.readUnsignedShortLE();
        var originY = buf.readUnsignedShortLE();
        var width = buf.readUnsignedShortLE();
        var height = buf.readUnsignedShortLE();
        var pixelDepth = buf.readUnsignedByte();
        var descriptor = buf.readUnsignedByte();

        int[] pixels = new int[width * height];

        if (imgType != 2) { // rgb
            throw new IllegalStateException("Unhandled img type " + imgType);
        }

        if (originX != 0 || originY != 0) {
            throw new IllegalStateException("Unsupported origin x/y");
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                var idx = width * (height - y - 1) + x;

                pixels[idx] = switch (pixelDepth) {
                    case 32 -> readARGBPixel(buf);
                    case 24 -> readRGBPixel(buf);
                    default -> throw new IllegalStateException("Unsupported pixel depth " + pixelDepth);
                };
            }
        }
        var bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
        return bufferedImage;
    }

    private static int readARGBPixel(ByteBuf buf) {
        var blue = buf.readUnsignedByte();
        var green = buf.readUnsignedByte();
        var red = buf.readUnsignedByte();
        var alpha = buf.readUnsignedByte();
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    private static int readRGBPixel(ByteBuf buf) {
        var blue = buf.readUnsignedByte();
        var green = buf.readUnsignedByte();
        var red = buf.readUnsignedByte();
        return red << 16 | green << 8 | blue;
    }
}
