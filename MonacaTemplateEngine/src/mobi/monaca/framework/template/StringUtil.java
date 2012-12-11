package mobi.monaca.framework.template;

public class StringUtil {

    /** Normalize double quote string like a "..." for a template language. */
    public static String normalizeDoubleQuoteString(String source) {
        source = source.substring(1, source.length() - 1);
        StringBuilder builder = new StringBuilder();

        for (int i = 0, len = source.length(); i < len; i++) {
            if (source.charAt(i) == '\\') {
                switch (source.charAt(++i)) {

                case 't':
                    builder.append("\t");
                    break;

                case 'r':
                    builder.append("\r");
                    break;

                case 'n':
                    builder.append("\n");
                    break;

                default:
                    builder.append(source.charAt(i));
                    break;

                }
            } else {
                builder.append(source.charAt(i));
            }
        }

        return builder.toString();
    }

    /** Normalize single quote string like a '...' for a template language. */
    public static String normalizeSingleQuoteString(String source) {
        source = source.substring(1, source.length() - 1);
        StringBuilder builder = new StringBuilder();

        for (int i = 0, len = source.length(); i < len; i++) {
            if (source.charAt(i) == '\\') {
                i++;
                if (source.charAt(i) == '\\') {
                    builder.append('\\');
                } else if (source.charAt(i) == '\'') {
                    builder.append('\'');
                } else {
                    builder.append('\\');
                    builder.append(source.charAt(i));
                }
            } else {
                builder.append(source.charAt(i));
            }
        }

        return builder.toString();
    }

}
