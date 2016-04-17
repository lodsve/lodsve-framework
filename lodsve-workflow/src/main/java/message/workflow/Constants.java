package message.workflow;

/**
 * Constants.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 17:33
 */
public final class Constants {
    private Constants() {
    }

    public static final String DEFAULT_WORKFLOW_XML_PATH = "classpath*:/workflow/*.xml";
    public static final String ATTR_TITLE = "title";
    public static final String ATTR_NAME = "name";
    public static final String TAG_NODE = "node";

    public static final String TAG_URLS = "urls";
    public static final String TAG_UPDATE_URL = "update-url";
    public static final String TAG_VIEW_URL = "view-url";

    public static final String TAG_TO = "to";
    public static final String TAG_METHOD = "method";
    public static final String TAG_CONDITIONAL = "conditional";
    public static final String ATTR_HANDLER = "handler";
    public static final String ATTR_DOMAIN = "domain";
    public static final String ATTR_NODE = TAG_NODE;
    public static final String ATTR_URL = "url";
    public static final String ATTR_TYPE = "type";
}
