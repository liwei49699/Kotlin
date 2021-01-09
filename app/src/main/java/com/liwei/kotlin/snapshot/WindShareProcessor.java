//package com.liwei.kotlin.snapshot;
//
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Handler;
//
//import com.liwei.kotlin.ShareCallBack;
//import com.liwei.kotlin.snapshot.utils.ShareTypeEnum;
//
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;
//
//import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_WECHAT;
//
///**
// * ****************************************************************
// * 文件名称  : WindShareProcessor.java
// * 作         者  : jwang.Ranger
// * 创建时间  : 2014-8-7 上午9:14:42
// * 文件描述  : 分享工具类
// * 修改历史  : 2014-8-7 1.00 初始版本
// * ****************************************************************
// */
//public class WindShareProcessor {
//    /**
//     * 授权成功标志
//     */
//    public static final int AUTHORIZE_SUCCESS = 0X100;
//    /**
//     * 上下文
//     */
//    protected Context mContext;
//    /**
//     * handle
//     */
//    protected Handler mHandler;
//    /**
//     * 分享回调
//     */
//    protected ShareCallBack mCallBack;
//    /**
//     * 分享类型
//     */
//    protected ShareTypeEnum mShareType;
//
//    /**
//     * 构造函数
//     * @param context 上下文
//     */
//    public WindShareProcessor(Context context) {
//        mContext = context;
//        mHandler = new Handler();
//        ShareSdk.getInstance(mContext);
//    }
//
//    /**
//     * 微信分享
//     *
//     * @param shareParams 分享内容
//     */
//    public void shareToWeChat(final Platform.ShareParams shareParams) {
//        if (!isInstallApp("com.tencent.mm", "微信")) {
//            return;
//        }
//        if (null == shareParams) {
//            return;
//        }
//        final DefaultPlatformActionListener platListener = new DefaultPlatformActionListener();
//        mShareType = SHARE_WECHAT;
//        share(shareParams, platListener, Wechat.NAME);
//    }
//
//    /**
//     * 微信分享
//     *
//     * @param build 接口
//     */
//    public void shareToWeChat(final BuildShareParamsListener build) {
//        if (null == build) {
//            return;
//        }
//        ShareParams params = new ShareParams();
//        build.buildShareParams(params);
//        shareToWeChat(params);
//    }
//
//    public void openWxMiniProgram(String id) {
//        if (!isInstallApp("com.tencent.mm", "微信")) {
//            return;
//        }
//        ShareParams params = new ShareParams();
//        //小程序原始ID
//        params.setWxUserName(id);
//        params.setShareType(Platform.OPEN_WXMINIPROGRAM);
////        params.setWxPath("pages/index/index");//分享小程序页面路径
////        params.setTitle("标题分享");
////        params.setText("http://www.mob.com");
////        params.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
////        params.setUrl("http://www.baidu.com");
//        final DefaultPlatformActionListener platListener = new DefaultPlatformActionListener();
//        share(params, platListener, Wechat.NAME);
//    }
//
//    /**
//     * 朋友圈分享
//     *
//     * @param shareParams 参数
//     */
//    public void shareToWeChatMoments(final ShareParams shareParams) {
//        if (!isInstallApp("com.tencent.mm", "微信") || null == shareParams) {
//            return;
//        }
//        mShareType = SHARE_WECHAT_MOMENTS;
//        final DefaultPlatformActionListener platListener = new DefaultPlatformActionListener();
//        int type = shareParams.getShareType();
//        if (type == Wechat.SHARE_EMOJI || type == Wechat.SHARE_APPS || type == Wechat.SHARE_FILE) {
//            shareParams.setShareType(Wechat.SHARE_IMAGE);
//        }
//        share(shareParams, platListener, WechatMoments.NAME);
//    }
//
//    /**
//     * 分享朋友圈
//     *
//     * @param build 接口函数
//     */
//    public void shareToWeChatMoments(final BuildShareParamsListener build) {
//        if (null == build) {
//            return;
//        }
//        ShareParams params = new ShareParams();
//        build.buildShareParams(params);
//        shareToWeChatMoments(params);
//    }
//
//    /**
//     * 微信收藏
//     *
//     * @param build 接口
//     */
//    public void shareToWeChatFavorite(final BuildShareParamsListener build) {
//        if (null == build || !isInstallApp("com.tencent.mm", "微信")) {
//            return;
//        }
//
//        mShareType = SHARE_WECHAT_FAVORITE;
//        final DefaultPlatformActionListener platListener = new DefaultPlatformActionListener();
//        ShareParams params = new ShareParams();
//        build.buildShareParams(params);
//        share(params, platListener, WechatFavorite.NAME);
//    }
//
//    /**
//     * QQ分享
//     *
//     * @param shareParams 参数
//     */
//    public void shareToQQ(final ShareParams shareParams) {
//        if (!isInstallApp("com.tencent.mobileqq", "QQ") || null == shareParams) {
//            return;
//        }
//        mShareType = SHARE_QQ;
//        final DefaultPlatformActionListener platListener = new DefaultPlatformActionListener();
//        share(shareParams, platListener, QQ.NAME);
//    }
//
//    /**
//     * QQ空间分享
//     *
//     * @param shareParams 参数
//     */
//    public void shareToQQZone(final ShareParams shareParams) {
//        if (!isInstallApp("com.tencent.mobileqq", "QQ") || null == shareParams) {
//            return;
//        }
//        final DefaultPlatformActionListener platListener = new DefaultPlatformActionListener();
//        mShareType = SHARE_QQ_ZONE;
//        share(shareParams, platListener, QZone.NAME);
//    }
//
//    /**
//     * 新浪微博分享
//     *
//     * @param shareParams 参数
//     */
//    public void shareToSinaWeibo(final ShareParams shareParams) {
//        if (null == shareParams) {
//            return;
//        }
//        mShareType = SHARE_SINA_WEIBO;
//        final DefaultPlatformActionListener platListener = new DefaultPlatformActionListener();
//        // 以下为原生微博分享(替换原先)
//        ADFInterfaceComm.getInterface().shareWeiBo(shareParams, platListener);
//    }
//
//    /**
//     * 新浪微博分享
//     *
//     * @param build 接口
//     */
//    public void shareToSinaWeibo(final BuildShareParamsListener build) {
//        // 以下为原生微博分享(替换原先)
//        ShareParams params = new ShareParams();
//        build.buildShareParams(params);
//        shareToSinaWeibo(params);
//    }
//
//    /**
//     * 邮件分享
//     *
//     * @param params 参数
//     */
//    public void shareToEmail(final ShareParams params) {
//        if (null == params) {
//            return;
//        }
//        try {
//            mShareType = SHARE_EMAIL;
//            StockShareParams shareParams = (StockShareParams) params;
//            EmailUtil.sendEmail(mContext,
//                    shareParams.getTitle(), shareParams.getText(),
//                    null, null);
//
//
////            Intent email = new Intent(Intent.ACTION_SEND_MULTIPLE);
////            email.putExtra(Intent.EXTRA_EMAIL, "");
////            email.putExtra(Intent.EXTRA_CC, "");
////            email.putExtra(Intent.EXTRA_BCC, "");
////            // 标题
////            email.putExtra(android.content.Intent.EXTRA_SUBJECT, shareParams.getTitle());
////            // 内容
////            email.putExtra(android.content.Intent.EXTRA_TEXT, shareParams.getText());
////            ArrayList<Uri> shareImageList = shareParams.getShareImageList();
////            if (shareImageList != null) {
////                ArrayList<Uri> list = new ArrayList<Uri>();
////                for (Uri uri : shareImageList) {
////                    list.add(Uri.parse("file://" + uri.getPath()));
////                }
////                email.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list);
////            }
////            email.setType("image/*");
////            email.setType("message/rfc882");
////            // 调用系统的邮件系统
////            mContext.startActivity(email);
//        } catch (ActivityNotFoundException e) {
//            ToastTool.showToast("无可用邮件客户端!");
//        } catch (Exception e) {
//            ToastTool.showToast("分享失败!");
//        }
//    }
//
//    /**
//     * 短信分享
//     *
//     * @param shareParams 参数
//     */
//    public void shareToMsg(final ShareParams shareParams) {
//        if (null == shareParams) {
//            return;
//        }
//        mShareType = SHARE_MESSAGE;
//        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//        sendIntent.putExtra("sms_body", shareParams.getText());
//        sendIntent.setType("vnd.android-dir/mms-sms");
//        try {
//            mContext.startActivity(sendIntent);
//        } catch (Exception e) {
//            if (mCallBack != null) {
//                mCallBack.onError(null, 0, e, mShareType);
//            }
//        }
//    }
//
//    /**
//     * 分享到其他已安装APP
//     *
//     * @param build 接口
//     */
//    public void shareToOther(BuildShareParamsListener build) {
//        Platform.ShareParams params = new Platform.ShareParams();
//        build.buildShareParams(params);
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.putExtra(Intent.EXTRA_TEXT, params.getText());
//        mContext.startActivity(intent);
//    }
//
//    /**
//     * 分享
//     *
//     * @param params       组装请求参数
//     * @param platListener 回调监听
//     * @param mediaType    平台类别
//     */
//    protected void share(Platform.ShareParams params, PlatformActionListener platListener, String mediaType) {
//        Platform plat = ShareSDK.getPlatform(mediaType);
//        if (plat == null) {
//            MobSDK.init(mContext);
//            plat = ShareSDK.getPlatform(mediaType);
//        }
//        plat.setPlatformActionListener(platListener);
//        plat.share(params);
//    }
//
//    /**
//     * 取消授权
//     *
//     * @param platListener 回调
//     * @param mediaType 类型
//     */
//    public void cancelAuthorize(PlatformActionListener platListener,
//                                String mediaType) {
//
//        Platform plat = ShareSDK.getPlatform(mediaType);
//        if (plat == null) {
//            MobSDK.init(mContext);
//            plat = ShareSDK.getPlatform(mediaType);
//        }
//        plat.removeAccount(true);
//    }
//
//    public void setCallBack(ShareCallBack callBack) {
//        this.mCallBack = callBack;
//    }
//
//    /**
//     * 分享回调
//     */
//    public class DefaultPlatformActionListener implements PlatformActionListener {
//        @Override
//        public void onCancel(final Platform arg0, final int arg1) {
//            mHandler.post(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (mCallBack != null) {
//                        mCallBack.onCancel(arg0, arg1, mShareType);
//                    }
//                }
//            });
//        }
//
//        @Override
//        public void onComplete(final Platform arg0, final int arg1,
//                               final HashMap<String, Object> arg2) {
//            mHandler.post(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (mCallBack != null) {
//                        mCallBack.onComplete(arg0, arg1, arg2, mShareType);
//                    }
//                }
//            });
//        }
//
//        @Override
//        public void onError(final Platform arg0, final int arg1, final Throwable arg2) {
//            mHandler.post(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (mCallBack != null) {
//                        mCallBack.onError(arg0, arg1, arg2, mShareType);
//                    }
//                }
//            });
//        }
//    }
//
//    private boolean isInstallApp(String packageName, String hint) {
//        if (!PackageManagerUtil.isAvilible(mContext, packageName)) {
//            ToastTool.showToast("请先下载" + hint);
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 生成分享参数的接口类
//     */
//    public interface BuildShareParamsListener {
//        /**
//         * 生成分享参数的函数
//         * @param params 参数
//         */
//        void buildShareParams(ShareParams params);
//    }
//}
