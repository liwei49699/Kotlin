package com.liwei.kotlin.snapshot.utils;

/**
 * # **********************************************************************************************
 * # ClassName:      ShareTypeEnum
 * # Description:    分享类型
 * # Author:         dzzhao.summer
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    dzzhao.summer     2018/12/20 14:46
 * # Modifications:  initial
 * # **********************************************************************************************
 */

public enum ShareTypeEnum {
    /**
     * 微信
     */
    SHARE_WECHAT,
    /**
     * 朋友圈
     */
    SHARE_WECHAT_MOMENTS,
    /**
     * 微信收藏
     */
    SHARE_WECHAT_FAVORITE,
    /**
     * 新浪微博
     */
    SHARE_SINA_WEIBO,
    /**
     * QQ
     */
    SHARE_QQ,
    /**
     * QQ空间
     */
    SHARE_QQ_ZONE,
    /**
     * 邮箱
     */
    SHARE_EMAIL,
    /**
     * 短信
     */
    SHARE_MESSAGE,
    /**
     * 保存相册
     */
    SHARE_ALBUM,
    /**
     * 更多，调用系统的分享功能
     */
    SHARE_MORE,

    /**
     * 复制链接，位于第二行
     */
    SHARE_COPY_URL,
    /**
     * 刷新，位于第二行
     */
    SHARE_REFRESH,
    /**
     * 类型错误
     */
    SHARE_TYPE_ERROR
}
