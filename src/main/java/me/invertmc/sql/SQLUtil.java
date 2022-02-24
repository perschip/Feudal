package me.invertmc.sql;

public class SQLUtil {
	
	/**
	 * Removes everything from string except: letters, numbers, _, -
	 * @param string
	 * @param size
	 * @return
	 */
	public static String filter(String string, int size, char... exception) {
		if(string.length() > size){
			string = string.substring(0, size);
		}
		String newString = "";
		for(char c : string.toCharArray()){
			if(Character.isAlphabetic(c) || Character.isDigit(c) || c == '-' || c == '_' || contains(exception, c)){
				newString += c + "";
			}
		}
		return newString;
	}

	private static boolean contains(char[] exception, char c) {
		for(char e : exception){
			if(e == c){
				return true;
			}
		}
		return false;
	}

	public static String filterBasic(String message, int size) {
		return filterBasic(message, size, false);
	}
	
	public static String filterBasic(String message, int size, boolean escapeDoubleQuotes) {
		if(message.length() > size){
			message = message.substring(0, size);
		}
		return escapeString(message, escapeDoubleQuotes);
	}
	
	public static String escapeString(String x, boolean escapeDoubleQuotes) {
        StringBuilder sBuilder = new StringBuilder(x.length() * 11/10);

        int stringLength = x.length();

        for (int i = 0; i < stringLength; ++i) {
            char c = x.charAt(i);

            switch (c) {
            case 0: /* Must be escaped for 'mysql' */
                sBuilder.append('\\');
                sBuilder.append('0');

                break;

            case '\n': /* Must be escaped for logs */
                sBuilder.append('\\');
                sBuilder.append('n');

                break;

            case '\r':
                sBuilder.append('\\');
                sBuilder.append('r');

                break;

            case '\\':
                sBuilder.append('\\');
                sBuilder.append('\\');

                break;

            case '\'':
                sBuilder.append('\'');
                sBuilder.append('\'');

                break;

            case '"': /* Better safe than sorry */
                if (escapeDoubleQuotes) {
                    sBuilder.append('\\');
                }

                sBuilder.append('"');

                break;

            case '\032': /* This gives problems on Win32 */
                sBuilder.append('\\');
                sBuilder.append('Z');

                break;

            case '\u00a5':
            case '\u20a9':
                // escape characters interpreted as backslash by mysql
                // fall through

            default:
                sBuilder.append(c);
            }
        }

        return sBuilder.toString();
    }
	
}
