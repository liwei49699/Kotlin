package com.liwei.kotlin;

import com.liwei.kotlin.snapshot.utils.ShareTypeEnum;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;

public interface ShareCallBack {

    void onCancel(Platform arg0, int arg1, ShareTypeEnum shareType);

    void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2, ShareTypeEnum shareType);

    void onError(Platform arg0, int arg1, Throwable arg2, ShareTypeEnum shareType);

    void onStart(ShareTypeEnum shareType);

}
