package cn.qqhxj.common.rxtx;

/**
 * @author han xinjian
 **/
public class HexUtil {
    public static byte[] HexStringToBytes(String str) {
        if (str == null || str.length() <= 0) {
            return new byte[0];
        }
        str = str.trim();
        str = str.replaceAll("ox", "");
        str = str.replaceAll(" ", "");
        str = str.replaceAll("oX", "");
        str = str.replaceAll(",", "");
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

}
