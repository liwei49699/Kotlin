package com.liwei.kotlin.snapshot;


/**
 * ****************************************************************
 * 文件名称  : KeyValueEnum.java
 * 作         者  : afwang
 * 创建时间  : 2014-6-12 上午11:36:29
 * 文件描述  : Key value 字符串
 * 修改历史  : 2014-6-12 1.00 初始版本
 * ****************************************************************
 */
public class KeyValueEnum {

    public static KeyValueEnum keyValueEnum;

    public static final String ISREADLIST = new String("ISREADLIST");
    public static final String SECTORLIST = new String("SECTORLIST");
    public static final String AUTO_LOGIN = new String("AUTO_LOGIN");
    public static final String PRE_INDEX = new String("PRE_INDEX1");
    public static final String PRE_TAB = new String("PRE_TAB");
    public static final String MARKET_TAB = new String("MARKET_TAB2");  // MARKET_TAB2 对应 Market
    public static final String MARKET_TAB_OLD = new String("MARKET_TAB");  // MARKET_TAB 对应  MarketAndNewsTopicModel(旧版结构)
    public static final String MARKET_TAB_UNSELECT = new String("MARKET_TAB_UNSELECT");
    public static final String KEY_NEWSTOPIC = new String("KEY_NEWSTOPIC");//新闻模块彩虹头
    public static final String KEY_NEWSTOPIC_DEFAULT = new String("KEY_NEWSTOPIC_DEFAULT");//新闻模块彩虹头默认数据
    public static final String KEY_NEWSTOPIC_IMPORT = new String("KEY_NEWSTOPIC_IMPORT");//新闻模块彩虹头5.7.0强制要闻到第一位，重新建立保存数据库的key
    public static final String KEY_NEWSTOPIC_MODEL = new String("KEY_NEWSTOPIC_MODEL");//新闻模块彩虹头model
    /**
     * 新闻模块彩虹头版本号
     */
    public static final String KEY_NEWSTOPIC_VERSION = new String("KEY_NEWSTOPIC_VERSION");
    /**
     * 新闻模块彩虹头数据库记录(即当前显示的顺序)
     */
    public static final String KEY_NEWSTOPIC_SORT_LIST = new String("KEY_NEWSTOPIC_SORT_LIST");
    public static final String KEY_TRADE_LOGINID = new String("KEY_TRADE_LOGINID");//登录的券商id
    public static final String KEY_TRADE_CHECKAGREE = new String("KEY_TRADE_CHECKAGREE");//券商的协议
    public static final String TABBAR_SELECT_ID = "TABBAR_SELECT_ID";//新闻上次选中的tab
    public final static String NEWS_SEARCH_LIST = "NEWS_SEARCH_LIST";// 新闻搜索结果集
    public final static String NEWS_SEARCH_STRING = "NEWS_SEARCH_STRING";// 新闻搜索(数据库key)
    public final static String KEY_SUBJECT_LIST = "KEY_SUBJECT_LIST";// 题材列表

    public final static String PRE_ADDRESS_STRING = "PRE_ADDRESS_STRING";// 上次登录成功的地址
    public static final String KEY_SKY_POOL_STR = "KEY_SKY_POOL_STR"; // SkyPool 数据
    public static final String KEY_SKY_OUTSEA_STR = "KEY_SKY_OUTSEA_STR"; // SkyOutsea 数据
    public static final String KEY_SKY_REGION_STR = "KEY_SKY_REGION_STR"; // 返回所属区域，0：国内，1：港澳台，2：国外，-1：redis异常或没有取到数据
    public final static String SPEN_HELP_RES = "SPEN_HELP_RES";// Spen帮助图片

    public final static String MARKET_FORCE_JUMP_INDEX = "MARKET_FORCE_JUMP_INDEX";// 市场强制跳转下标
    public final static String NEWS_FORCE_JUMP_INDEX = "NEWS_FORCE_JUMP_INDEX";// 新闻强制跳转下标
    public static final String KLINE_AVERAGE = new String("KLINE_AVERAGE"); // k线均线设置
    public static final String KLINE_AVERAGE_COLOR = new String("KLINE_AVERAGE_COLOR"); // k线均线设置

    public static final String KEY_NEWS_DETAIL_TEXT_SIZE = "KEY_NEWS_DETAIL_TEXT_SIZE"; // 新闻详情字体大小

    public final static String SWITCH_LOGO = "SWITCH_LOGO";// 日志开关
    public final static String SWITCH_AUTOTEST = "SWITCH_AUTOTEST";// 自动化测试
    public final static String SWITCH_MONKEYTEST = "SWITCH_MONKEYTEST";// monkey测试
    public final static String SWITCH_EAGLECTEST = "SWITCH_EAGLECTEST";// eaglec测试上报
    public final static String SWITCH_EAGLECMAIN = "SWITCH_EAGLECMAIN";// eaglec主站上报
    public final static String SWITCH_FUCTIONTEST = "SWITCH_FUCTIONTEST";// 功能点自动化测试
    public final static String MAP_FUCTIONTEST = "MAP_FUCTIONTEST";// 大功能自动化测试
    public final static String MAP_FUCTIONTEST2 = "MAP_FUCTIONTEST2";// 小功能点自动化测试
    public final static String PERIOD_FUCTIONTEST = "PERIOD_FUCTIONTEST";// 功能点自动化测试间隔

    /**
     * DOC 6.9.1新增FM已读置灰功能
     */
    public static final String FMREADLIST = new String("FMREADLIST");


    /**
     * 7.0.0机会页面导航功能
     */
    public static final String KEY_CHANCE_NAVI_DEFAULT = "KEY_CHANCE_NAVI_DEFAULT";

    public static final String KEY_CHANCE_NAVI_CUSTOM = "KEY_CHANCE_NAVI_CUSTOM";

    /**
     * 机会页面顶部广告
     */
    public static final String KEY_CHANCE_AD_BANNER = "KEY_CHANCE_AD_BANNER";
    /**
     * 机会页面推荐菜单
     */
    public static final String KEY_CHANCE_RECOMMEND_MENU = "KEY_CHANCE_RECOMMEND_MENU";

    /**
     * 交易登录首页顶部广告
     */
    public static final String KEY_TRADE_AD_BANNER = "KEY_TRADE_AD_BANNER";
    /**
     * 交易登录首页轮播广告，7.6改版
     */
    public static final String KEY_TRADE_AD_BANNER_33 = "KEY_TRADE_AD_BANNER_33";

    /**
     * 交易登录页面底部广告
     */
    public static final String KEY_TRADE_OPEN_ACCOUNT_AD = "KEY_TRADE_OPEN_ACCOUNT_AD";

    /**
     * 交易首页特色服务列表
     */
    public static final String KEY_SPECIAL_SERVICE_MENU = "KEY_SPECIAL_SERVICE_MENU";

    /**
     * 阅读历史-新闻
     */
    public static final String KEY_READ_HISTORY_NEWS = "KEY_READ_HISTORY_NEWS";

    /**
     * 阅读历史-公告
     */
    public static final String KEY_READ_HISTORY_BULLET = "KEY_READ_HISTORY_BULLET";

    /**
     * 阅读历史-研报
     */
    public static final String KEY_READ_HISTORY_RESEARCH = "KEY_READ_HISTORY_RESEARCH";

    /**
     * 交易账号指纹登录
     */
    public static final String KEY_TRADE_FINGERPRINT = "KEY_TRADE_FP";

    /**
     * 听财经选中专辑列表
     */
    public static final String KEY_FM_SELECT_ALBUM = "KEY_FM_SELECT_ALBUM";

    /**
     * 听财经是否选中专辑列表
     */
    public static final String KEY_FM_IS_SELECT_ALBUM = "KEY_FM_IS_SELECT_ALBUM";

    /**
     * 科创顶部菜单
     */
    public static final String KEY_STIB_MENUS = "KEY_STIB_MENUS";

    /**
     * 个人中心 行情绿涨红跌 设置开关 sp key
     */
    public static final String MARKETREDGREEN_SETTING = "marketredgreen_setting";

    /**
     * 涨停捉妖，tab字段排序
     */
    public static final String KEY_SORT_LIMIT_UP = "KEY_SORT_LIMIT_UP";
    public static final String KEY_SORT_LIMIT_SOON = "KEY_SORT_LIMIT_SOON";
    public static final String KEY_SORT_LIMIT_OPEN_BOARD = "KEY_SORT_LIMIT_OPEN_BOARD";
    public static final String KEY_SORT_LIMIT_MAIN = "KEY_SORT_LIMIT_MAIN";
    public static final String KEY_LIMIT_TAG_SHOW = "KEY_LIMIT_TAG_SHOW";

    /**
     * 已读新闻数据
     */
    public static final String NEWS_READ_LIST = new String("news_read_list");

    public static final String F5_INDEX_SETTING = "F5_INDEX_SETTING";
    /**
     * wind智能文本
     */
    public static final String WFT_NEWS_OPEN_STYLE = "WFT_NEWS_OPEN_STYLE";

    protected KeyValueEnum() {

    }

    public static KeyValueEnum getInstance() {
        if (keyValueEnum == null) {
            keyValueEnum = new KeyValueEnum();
        }
        return keyValueEnum;
    }

}
