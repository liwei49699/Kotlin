package com.liwei.kotlin.snapshot.utils;


import cn.sharesdk.framework.Platform;

/**
 * # **********************************************************************************************
 * # ClassName:      IShare
 * # Description:    分享接口类
 * # Author:         dzzhao.summer
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    dzzhao.summer     2018/12/19 10:30
 * # Modifications:  initial
 * # **********************************************************************************************
 */

public interface IShare {
    /**
     * 分享视图类型
     * @return ShareTypeEnum类型数组，表示各分享渠道
     */
    ShareTypeEnum[] getShareViewType();

    /**
     * 获取分享参数
     * @param type 分享类型
     * @return 分享参数。返回若为null，则底层不进行处理，此时业务层可以自己去处理
     */
    Platform.ShareParams getShareParams(ShareTypeEnum type);

    /**
     * 分享状态回调
     * @param type 分享类型
     * @param state 分享状态
     */
    void shareResult(ShareTypeEnum type, ShareStateEnum state);
}
