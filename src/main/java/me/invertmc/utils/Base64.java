package me.invertmc.utils;

public class Base64 {

    private static final char[] B_64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    public static String encode(int value) {
        return encode(value, 0);
    }

    public static String encode(int value, int length) {
        int start = 0;
        while(value > Math.pow(64, start)*B_64.length) {
            start++;
        }

        String code = "";

        while(start >= 0) {
            double column = Math.pow(B_64.length, start);

            int index = (int) (value / column);

            code += B_64[index];

            value -= index * column;

            start--;
        }

        if(code.length() > length && length > 0) {
            return code.substring(0, length);
        }

        while(code.length() < length) {
            code = B_64[0] + code;
        }

        return code;
    }

    public static int decode(String code) {
        int value = 0;

        int column = code.length()-1;
        for(char c : code.toCharArray()) {
            int indexValue = find(c);

            value += indexValue * Math.pow(B_64.length, column);

            column--;
        }

        return value;
    }

    private static int find(char ch) {
        for(int i = 0; i < B_64.length; i++) {
            if(B_64[i] == ch) {
                return i;
            }
        }
        return 0;
    }
}
