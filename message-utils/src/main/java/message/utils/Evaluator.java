package message.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .替换字符串中的简单变量表达式如：${file.path.root}
 *  替换${xx}中的内容
 */
public final class Evaluator {
    /**
     * The open brace character.
     */
    public static final char OPEN_BRACE = '{';

    /**
     * The closed brace character.
     */
    public static final char CLOSED_BRACE = '}';

    public static final char POUND_SIGN = '$';
    public static final String CLOSED_VARIABLE = String.valueOf(CLOSED_BRACE);
    /**
     * The open variable string.
     */
    public static final String OPEN_VARIABLE = String.valueOf(POUND_SIGN) + String.valueOf(OPEN_BRACE);

    /**
     * 私有化构造器
     */
    private Evaluator(){}

    public static String replaceVariable(final String expression, final Map valMap, boolean trimValue) {

        int openIndex = expression.indexOf(OPEN_VARIABLE);

        if (openIndex < 0) {
            return expression;
        }
        //替换的表达式
        String replacedExpr = expression;
        String variableValue;
        String variableName;
        String variableString;
        while (openIndex >= 0) {

            int closedIndex;
            if (openIndex >= 0) {

                closedIndex = replacedExpr.indexOf(CLOSED_VARIABLE, openIndex + 1);
                if (closedIndex > openIndex) {

                    variableName = replacedExpr.substring(openIndex + OPEN_VARIABLE.length(), closedIndex);

                    variableValue = String.valueOf(valMap.get(variableName));
                    if (trimValue) {
                        variableValue = StringUtils.trimToEmpty(variableValue);
                    }
                    variableString = OPEN_VARIABLE + variableName + CLOSED_VARIABLE;
                    replacedExpr = replaceAll(replacedExpr, variableString, variableValue);

                } else {
                    break;
                }
            }

            // Start looking at the beginning of the string, since
            // the length string has changed and characters have moved
            // positions.
            openIndex = replacedExpr.indexOf(OPEN_VARIABLE);
        }

        // If an open brace is left over, then a variable could not be replaced.
        int openBraceIndex = replacedExpr.indexOf(OPEN_VARIABLE);
        if (openBraceIndex > -1) {
            throw new RuntimeException(
                    "A variable has not been closed (index=" + openBraceIndex + ").");
        }

        return replacedExpr;
    }


    public static String replaceAll(final String expression,
                                    final String oldString, final String newString) {

        String replacedExpr = expression;

        if (replacedExpr != null) {
            int charCtr = 0;
            int oldStringIndex = replacedExpr.indexOf(oldString, charCtr);

            while (oldStringIndex > -1) {
                // Remove the old string from the expression.
                final StringBuffer buffer = new StringBuffer(replacedExpr
                        .substring(0, oldStringIndex)
                        + replacedExpr.substring(oldStringIndex
                        + oldString.length()));

                // Insert the new string into the expression.
                buffer.insert(oldStringIndex, newString);

                replacedExpr = buffer.toString();

                charCtr = oldStringIndex + newString.length();

                // Determine if we need to continue to search.
                if (charCtr < replacedExpr.length()) {
                    oldStringIndex = replacedExpr.indexOf(oldString, charCtr);
                } else {
                    oldStringIndex = -1;
                }
            }
        }

        return replacedExpr;
    }


    public static String replaceVariableByRegex(String raw, final Map valMap, boolean trimValue) {
        Pattern pattern = Pattern.compile("#\\{([\\w]+)\\}");
        Matcher matcher = pattern.matcher(raw);
        StringBuffer result = new StringBuffer(raw.length() + 32);
        String placeHolder;
        String value;
        Object obj;
        while (matcher.find()) {
            // 占位符key
            placeHolder = matcher.group(1);
            // 一个占位符对应的值
            obj = valMap.get(placeHolder);
            value = obj == null ? "#{" + placeHolder + "}" : obj.toString();
            if (trimValue) {
                value = StringUtils.trimToEmpty(value);
            }
            matcher.appendReplacement(result, value);
        }
        matcher.appendTail(result);
        return result.toString();
    }

}
