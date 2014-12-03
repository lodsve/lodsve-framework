package message.validate.constants;

/**
 * 验证框架的静态常量类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-12-2 上午1:01
 */
public class ValidateConstants {

    //有异常的时候抛出的异常信息
    public static final String VALIDATE_NO = "类:[{0}]中的字段:[{1}]未验证通过,其值为:[{2}],验证规则为:[{3}],请检查无误后重新保存!";

    //框架中定义的注解
    public static final String[] VALIDATE_ANNOTATIONS = new String[]{
        "Chinese", "Double", "Email", "English", "IdCard", "Integer", "Ip", "Limit", "Mobile",
        "NotNull", "Number", "Password", "Qq", "Regex", "Telephone", "Url", "Zip"
    };
}
