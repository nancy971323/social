package com.nanco.social.common.util;

/**
 * 要求：需防止SQL Injection 以及XSS 攻擊 - HTML轉義工具類，用於防止XSS攻擊
 */
public class HtmlEscapeUtil {

    /**
     * 要求：需防止SQL Injection 以及XSS 攻擊 - 轉義HTML特殊字符
     * 
     * @param input 輸入字符串
     * @return 轉義後的字符串
     */
    public static String escape(String input) {
        if (input == null) {
            return null;
        }

        StringBuilder escaped = new StringBuilder(input.length());

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '<':
                    escaped.append("&lt;");
                    break;
                case '>':
                    escaped.append("&gt;");
                    break;
                case '"':
                    escaped.append("&quot;");
                    break;
                case '\'':
                    escaped.append("&#x27;");
                    break;
                case '&':
                    escaped.append("&amp;");
                    break;
                default:
                    escaped.append(c);
            }
        }

        return escaped.toString();
    }
}
