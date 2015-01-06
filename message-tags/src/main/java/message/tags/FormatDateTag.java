package message.tags;

import message.utils.PropertyPlaceholderHelper;
import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * 自定义的格式化时间标签
 *
 * @author sunhao(sunhao.java@gmail.com)
 */
public class FormatDateTag extends org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag {
    private static final Logger logger = LoggerFactory.getLogger(FormatDateTag.class);

    private static final long serialVersionUID = 5556661395114907904L;

    private static ResourceBundle bundle;

    static {
        bundle = ResourceBundle.getBundle("date");
    }

    /**
     * 日期显示的格式<br/>
     * 默认显示yyyy年MM月dd日 HH时mm分
     * <ul>
     * <li>1 显示为yyyy-MM-dd HH:mm</li>
     * <li>0 显示为yyyy年MM月dd日 HH时mm分</li>
     * </ul>
     */
    private Integer dateType = 0;

    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    public int doEndTag() throws JspException {
        String out = "";
        Date nowDate = new Date();
        if (value == null) {
            return EVAL_PAGE;
        }
        SimpleDateFormat format = new SimpleDateFormat();

        long mm = Math.abs(nowDate.getTime() - value.getTime());    //计算间隔的时间（微秒）
        int dd = Math.abs(nowDate.getDate() - value.getDate());        //计算间隔的天数
        int m = Math.abs(nowDate.getMonth() - value.getMonth());    //计算间隔的月份数

        if ((nowDate.getDate() - value.getDate()) < 0 || m != 0) {
            dd = dd + m * 30;
        }

        /**
         * 显示完整时间
         * 条件：间隔天数大于等于2
         */
        if (dd >= 2) {
            if (dateType == 1) {
                format.applyPattern(getProperty("SIMPLE_DATE_FORMAT"));
            } else {
                format.applyPattern(getProperty("DATEFORMAT_CHINESE_FORMAT"));
            }

            out = format.format(value);
            print(out);
            return EVAL_PAGE;
        }

        /**
         * 显示昨天XXX
         * 条件：间隔天数为1，并且间隔月数为0
         */
        if (dd == 1 && m == 0) {
            int hours = value.getHours();
            int minutes = value.getMinutes();
            String time = "";
            if (hours < 6) {
                time = getProperty("DATEFORMAT_MORNING");
            } else if (hours < 12) {
                time = getProperty("DATEFORMAT_AM");
            } else if (hours == 12) {
                time = getProperty("DATEFORMAT_PM");
            } else if (hours < 18) {
                time = getProperty("DATEFORMAT_PM");
                hours -= 12;
            } else if (hours < 24) {
                time = getProperty("DATEFORMAT_NIGHT");
                hours -= 12;
            } else if (hours == 24) {
                time = getProperty("DATEFORMAT_NIGHT");
                hours = 0;
            }
            out = PropertyPlaceholderHelper.replacePlaceholder(getProperty("DATEFORMAT_DATE_FORMAT"),
                    new Object[]{time, hours, minutes}, getProperty("DATEFORMAT_DATE_FORMAT"));
            print(out);
            return EVAL_PAGE;
        }

        /**
         * 显示多少小时之前
         * 条件：1小时<间隔小时数<=24小时
         */
        if (mm <= 24 * 60 * 60 * 1000 && mm > 1 * 60 * 60 * 1000 && dd == 0) {
            String hours = String.valueOf((int) mm / (1000 * 60 * 60));
            out = hours + getProperty("DATEFORMAT_BEFOREH");
            print(out);
            return EVAL_PAGE;
        }
        /**
         * 显示多少分钟之前
         * 条件：1分钟<间隔分钟数<=60分钟
         */
        if (mm <= 60 * 60 * 1000 && mm > 1 * 60 * 1000 && dd == 0) {
            String minutes = String.valueOf((int) mm / (1000 * 60));
            out = minutes + getProperty("DATEFORMAT_BEFOREM");
            print(out);
            return EVAL_PAGE;
        }
        /**
         * 显示多少秒之前
         * 条件：1秒<间隔秒数<=60秒
         */
        if (mm <= 60 * 1000 && mm > 1000 && dd == 0) {
            String seconds = String.valueOf((int) mm / (1000));
            out = seconds + getProperty("DATEFORMAT_BEFORES");
            print(out);
            return EVAL_PAGE;
        }
        /**
         * 显示1秒前
         * 条件：间隔时间<=1秒
         */
        if (mm <= 1000 && dd == 0) {
            out = "1" + getProperty("DATEFORMAT_BEFORES");
            print(out);
            return EVAL_PAGE;
        }

        if (dateType == 1) {
            format.applyPattern(getProperty("SIMPLE_DATE_FORMAT"));
        } else {
            format.applyPattern(getProperty("DATEFORMAT_CHINESE_FORMAT"));
        }

        out = format.format(value);
        print(out);

        return EVAL_PAGE;
    }

    private void print(String out) throws JspTagException {
        try {
            pageContext.getOut().print(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getProperty(String key) {
        return bundle == null || StringUtils.isEmpty(key) ? "" : bundle.getString(key);
    }
}
