package dialight.misc;

public class Hex {

    private static char[] charRange(char fr, char to) {
        int count = to - fr + 1;
        if(count < 0) throw new RuntimeException("bad char order");
        char[] range = new char[count];
        for (int i = 0; i < count; i++) {
            range[i] = (char) (fr + i);
        }
        return range;
    }
    private static char[] join(char[]... arrs) {
        int total = 0;
        for (char[] arr : arrs) {
            total += arr.length;
        }
        char[] res = new char[total];
        int pos = 0;
        for (char[] arr : arrs) {
            System.arraycopy(arr, 0, res, pos, arr.length);
            pos += arr.length;
        }
        return res;
    }

    private static final char[] toHex = join(charRange('0', '9'), charRange('A', 'Z'));

    public static String encode(byte[] ba) {
        StringBuilder sb = new StringBuilder();
        for (byte b : ba) {
            sb.append(toHex[(b >> 4) & 0x0F]);
            sb.append(toHex[b & 0x0F]);
        }
        return sb.toString();
    }

    public static String toString(byte[] ba) {
        return toString(ba, " ");
    }
    public static String toString(byte[] ba, String split) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ba.length; i++) {
            if(i != 0) sb.append(split);
            byte b = ba[i];
            sb.append(toHex[(b >> 4) & 0x0F]);
            sb.append(toHex[b & 0x0F]);
        }
        return sb.toString();
    }

    public static int fromHex(char c) {
        if(c < '0') return -1;
        if(c <= '9') return c - '0';
        if(c < 'A') return -1;
        if(c <= 'Z') return c - 'A' + 10;
        return -1;
    }

    public static class DecodeException extends RuntimeException {
        public DecodeException(String s) {
            super(s);
        }
    }

    public static byte[] decode(String s) {
        int size = s.length() / 2;
        byte[] res = new byte[size];
        for (int i = 0; i < size; i++) {
            int hb = fromHex(s.charAt(i * 2));
            if(hb == -1) throw new DecodeException("index=" + i + " input=" + s);
            int lb = fromHex(s.charAt(i * 2 + 1));
            if(lb == -1) throw new DecodeException("index=" + i + " input=" + s);
            res[i] = (byte) (lb | (hb << 4));
        }
        return res;
    }

}
