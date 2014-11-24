package message.jdbc.utils;

import org.apache.commons.lang.StringUtils;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-6-17 下午8:31
 */
public class ParamType {
    public static final int CLOB = 1;
    public static final int LONGSTRING = 2;
    public static final int NORMAL = 0;

    private int valueType;
    private String paramName;
    private String realParamName;

    public int getValueType() {
        return valueType;
    }

    public void setValueType(int valueType) {
        this.valueType = valueType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getRealParamName() {
        return realParamName;
    }

    public void setRealParamName(String realParamName) {
        this.realParamName = realParamName;
    }

    public static ParamType getParamType(String paramName) {
        ParamType pt = new ParamType();
        pt.setParamName(paramName);
        pt.setRealParamName(paramName);
        pt.setValueType(ParamType.NORMAL);

        if(paramName.endsWith("isClob")){
            pt.setRealParamName(StringUtils.substringBefore(paramName, "isClob"));
            pt.setValueType(ParamType.CLOB);
        } else if(paramName.endsWith("isLongString")){
             pt.setRealParamName(StringUtils.substringBefore(paramName, "isLongString"));
            pt.setValueType(ParamType.LONGSTRING);
        }
        
        return pt;
    }
}
