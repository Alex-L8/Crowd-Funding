package lcx.constant;

/**
 * Create by LCX on 2/6/2022 11:36 AM
 */
public class CrowdConstant {
    public static final String ATTR_NAME_EXCEPTION = "exception";
    public static final String ATTR_NAME_LOGIN_ADMIN = "loginAdmin";
    public static final String ATTR_NAME_PAGE_INFO = "pageInfo";
    public static final String ATTR_NAME_MESSAGE = "message";
    public static final String ATTR_NAME_LOGIN_MEMBER = "loginMember";
    public static final String ATTR_NAME_TEMPLE_PROJECT = "tempProject";
    public static final String ATTR_NAME_PORTAL_DATA = "portal_data";

    public static final String MESSAGE_LOGIN_FAILED = "抱歉！，账号或密码错误，请重新输入！";
    public static final String MESSAGE_LOGIN_ACCT_ALREADY_IN_USE = "账号已被占用！";
    public static final String MESSAGE_ACCESS_FORBIDDEN = "请登录后访问！";
    public static final String MESSAGE_STRING_INVALIDATE = "数据不合法，禁止传入空数据！";
    public static final String MESSAGE_HEADER_PIC_UPLOAD_FAILED = "头图上传失败";
    public static final String MESSAGE_CODE_NOT_EXISTS = "验证码已过期!请检查手机号是否正确或重新发送!";
    public static final String MESSAGE_CODE_INVALID = "验证码不正确！";
    public static final String MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE = "查询结果不唯一";
    public static final String MESSAGE_ACCESS_DENIED = "抱歉！您没有权限访问";
    public static final String MESSAGE_HEADER_PIC_EMPTY = "头图不可为空！";
    public static final String MESSAGE_DETAIL_PIC_EMPTY = "详情图片不可为空！";
    public static final String MESSAGE_DETAIL_PIC_UPLOAD_FAILED = "详情图片上传失败";
    public static final String MESSAGE_TEMPLE_PROJECT_MISSING = "临时存储的Project对象丢失！";

    public static final String REDIS_CODE_PREFIX = "REDIS_CODE_PREFIX_";
    public static final String DETAIL_PROJECT_VO = "detailProjectVO";
    public static final String ORDER_PROJECT_VO = "orderProjectVO";
    public static final String ADDRESS_VO_LIST = "addressVOList";
}
