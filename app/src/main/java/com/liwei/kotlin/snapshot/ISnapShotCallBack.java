package com.liwei.kotlin.snapshot;

/**
 * # **********************************************************************************************
 * # ClassName:      ISnapShotCallBack.java
 * # Description:    截屏事件回调
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/8/17    13:49
 * # Modifications:  initial
 * # **********************************************************************************************
 */
public interface ISnapShotCallBack {
    void onSnapShotTaken(String path);
}
