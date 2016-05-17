package lodsve.wechat.core;

/**
 * 微信开放API.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/21 下午5:23
 */
public final class WeChatUrl {
    /**
     * 获取access_token
     */
    public static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    /**
     * 获取js_api_ticket
     */
    public static final String GET_JSP_API_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
    /**
     * 获取微信服务器ip
     */
    public static final String GET_IPS = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=%s";
    /**
     * 微信认证
     */
    public static final String WEIXIN_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s";
    /**
     * 获取token
     */
    public static final String WEIXIN_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    /**
     * 刷新token
     */
    public static final String WEIXIN_REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
    /**
     * 创建自定义菜单
     */
    public static final String CREATE_MENUS = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
    /**
     * 获取自定义菜单
     */
    public static final String GET_MENUS = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=%s";
    /**
     * 删除全部自定义菜单
     */
    public static final String DELETE_MENUS = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=%s";
    /**
     * 增加客服接口
     */
    public static final String ADD_CUSTOMER_SERVICE = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=%s";
    /**
     * 删除客服接口
     */
    public static final String DELETE_CUSTOMER_SERVICE = "https://api.weixin.qq.com/customservice/kfaccount/del?access_token=%s";
    /**
     * 修改客服帐号
     */
    public static final String UPDATE_CUSTOMER_SERVICE = "https://api.weixin.qq.com/customservice/kfaccount/update?access_token=%s";
    /**
     * 获取所有客服账号
     */
    public static final String LIST_CUSTOMER_SERVICE = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=%s";
    /**
     * 客服发送消息
     */
    public static final String CUSTOMER_SEND_TIDINGS = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
    /**
     * 设置所属行业
     */
    public static final String API_SET_INDUSTRY = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=%s";
    /**
     * 获取设置的行业信息
     */
    public static final String API_GET_INDUSTRY = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=%s";
    /**
     * 获取模板
     */
    public static final String API_GET_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=%s";
    /**
     * 获取所有模板
     */
    public static final String API_GET_ALL_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=%s";
    /**
     * 删除模板
     */
    public static final String API_DELETE_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=%s";
    /**
     * 发送模板消息
     */
    public static final String API_SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    /**
     * 创建分组
     */
    public static final String ADD_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=%s";
    /**
     * 获取所有分组
     */
    public static final String LIST_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=%s";
    /**
     * 查询用户所在分组
     */
    public static final String FIND_USER_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=%s";
    /**
     * 修改分组名
     */
    public static final String UPDATE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=%s";
    /**
     * 移动用户分组
     */
    public static final String MOVE_USER_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=%s";
    /**
     * 批量移动用户分组
     */
    public static final String BATCH_MOVE_USER_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=%s";
    /**
     * 删除分组
     */
    public static final String DELETE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=%s";
    /**
     * 设置备注名
     */
    public static final String UPDATE_USER_ALIAS = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=%s";
    /**
     * 获取用户基本信息
     */
    public static final String GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=%s";
    /**
     * 批量获取用户基本信息
     */
    public static final String BATCH_GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=%s";
    /**
     * 获取关注者列表
     */
    public static final String LIST_SUBSCRIBER = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=%s&next_openid=%s";

}
