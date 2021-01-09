package com.liwei.kotlin.snapshot.utils;

/**
 * # **********************************************************************************************
 * # ClassName:      ShareStateEnum
 * # Description:    分享状态
 * # Author:         dzzhao.summer
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    dzzhao.summer     2018/12/20 14:49
 * # Modifications:  initial
 * # **********************************************************************************************
 */

public enum ShareStateEnum {
    /**
     * 分享开始
     */
    SHARE_STATE_START,
    /**
     * 分享完成
     */
    SHARE_STATE_COMPLETE,
    /**
     * 分享取消
     */
    SHARE_STATE_CANCEL,
    /**
     * 分享发生错误
     */
    SHARE_STATE_ERROR
}
