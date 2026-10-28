package util;

public class TextUtil {

    public TextUtil() {
    }

    public String truncate(String text, int maxLen) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLen) {
            return text;
        }
        if (maxLen <= 3) {
            return text.substring(0, maxLen);
        }
        return text.substring(0, maxLen - 3) + "...";
    }

    public String line(char c, int length) {
        if (length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public String line(String s, int length) {
        if (s == null || s.isEmpty() || length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(length * s.length());
        for (int i = 0; i < length; i++) {
            sb.append(s);
        }
        return sb.toString();
    }
}

