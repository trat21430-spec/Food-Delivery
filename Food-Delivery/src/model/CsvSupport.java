package model;

public final class CsvSupport {

    private CsvSupport() {
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public static String[] split(String line) {
        if (line == null || line.isEmpty()) {
            return new String[0];
        }
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    public static String unescape(String value) {
        if (value == null) {
            return "";
        }
        String text = value.trim();
        if (text.startsWith("\"") && text.endsWith("\"") && text.length() >= 2) {
            text = text.substring(1, text.length() - 1).replace("\"\"", "\"");
        }
        return text;
    }
}
