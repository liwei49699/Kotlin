package com.liwei.kotlin.snapshot.widgets;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liwei.kotlin.R;
import com.liwei.kotlin.snapshot.KeyValueEnum;
import com.liwei.kotlin.snapshot.ShareMenuAdapter;
import com.liwei.kotlin.snapshot.ShareModel;
import com.liwei.kotlin.snapshot.SharedPreferenceService;
import com.liwei.kotlin.snapshot.utils.IShare;
import com.liwei.kotlin.snapshot.utils.ShareTypeEnum;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_COPY_URL;
import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_EMAIL;
import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_MORE;
import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_QQ;
import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_QQ_ZONE;
import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_SINA_WEIBO;
import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_WECHAT;
import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_WECHAT_MOMENTS;

/**
 * # **********************************************************************************************
 * # ClassName:      SharePopupView
 * # Description:    分享弹出页面
 * # Author:         dzzhao.summer
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    dzzhao.summer     2018/12/19 9:22
 * # Modifications:  initial
 * # **********************************************************************************************
 */

public class SharePopupView implements View.OnClickListener {

    private static final ShareTypeEnum[] SHARE_MENU =  {SHARE_WECHAT, SHARE_WECHAT_MOMENTS,
            SHARE_SINA_WEIBO,SHARE_QQ_ZONE, SHARE_QQ, SHARE_COPY_URL, SHARE_EMAIL, SHARE_MORE};

    private WeakReference<Context> mContextRef;
    private IShare mIShare;

    private Dialog dialog;
    private ShareTypeEnum[] shareTypes;
    private ShareTypeEnum curShareType;

    private ShareMenuAdapter mAdapter;
    /**
     * 构造函数
     * @param context 上下文
     * @param shareInterface 接口
     */
    public SharePopupView(@NonNull Context context, @NonNull IShare shareInterface) {
        this.mContextRef = new WeakReference<>(context);
        this.mIShare = shareInterface;
        setupViews();
    }

    /**
     * 显示PopupView
     */
    public void show() {
        // monkey测试开启 [该状态下，关闭分享功能]
        boolean monkeytest = SharedPreferenceService.getInstance().get(KeyValueEnum.SWITCH_MONKEYTEST, false);
        if (monkeytest) {
            return;
        }
        if (null == mContextRef) {
            return;
        }
        Context context = mContextRef.get();
        if (null == context) {
            return;
        }
        if (dialog == null) {
            return;
        }
        dialog.show();
    }

    private void disMiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
        onDismiss();
    }

    private void setupViews() {
        if (null == mIShare) {
            return;
        }
        shareTypes = mIShare.getShareViewType();
        if (null == shareTypes || shareTypes.length == 0) {
            return;
        }
        if (dialog == null) {
            initDialog();
        }
    }

    private void initDialog() {
        Context context = mContextRef.get();
        if (null == context) {
            return;
        }
        List<ShareModel> dataList = getShareModelList();
        if (dataList.size() == 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View mainLayout = inflater.inflate(R.layout.adf_view_share, null);
        mainLayout.setOnClickListener(this);
        RecyclerView shareMenu = mainLayout.findViewById(R.id.share_menu);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        shareMenu.setLayoutManager(layoutManager);
        mAdapter = new ShareMenuAdapter(context);
        mAdapter.setOnItemClickListener(new ShareMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, int position) {
                ShareModel item = mAdapter.getItem(position);
                if (item != null) {
//                    processShare(item);
                    disMiss();
                }
            }
        });
        shareMenu.setAdapter(mAdapter);
        initShare();
        dialog = new Dialog(context, R.style.alert_dialog_one);
        dialog.setContentView(mainLayout);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setWindowAnimations(android.R.style.Animation_InputMethod);
        //重新设置
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void initShare() {
        List<ShareModel> list = new ArrayList<>();
        if (shareTypes != null && shareTypes.length > 0) {
            for (ShareTypeEnum e : shareTypes) {
                ShareModel model = new ShareModel(e);
                list.add(model);
            }
            mAdapter.setData(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * dialog在dismiss时，会调此函数
     */
    public void onDismiss() {
    }

    private List<ShareModel> getShareModelList() {
        List<ShareModel> dataList = new ArrayList<>();
        for (ShareTypeEnum type : shareTypes) {
            ShareModel model = new ShareModel(type);
            dataList.add(model);
        }
        return dataList;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.adf_share_contain_view) {
            disMiss();
            return;
        }
        Object object = v.getTag();
        if (!(object instanceof ShareModel)) {
            return;
        }
        try {
//            processShare((ShareModel) object);
            disMiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void processShare(ShareModel model) {
//        curShareType = model.shareType;
//        if (null == mIShare) {
//            return;
//        }
//        final Platform.ShareParams shareParams = mIShare.getShareParams(curShareType);;
//        if (shareParams == null) {
//            return;
//        }
//
//        final Context context = mContextRef.get();
//        if (null == context) {
//            return;
//        }
//        WindShareProcessor shareProcessor = new WindShareProcessor(context);
//        shareProcessor.setCallBack(shareCallBack);
//        if (curShareType == SHARE_WECHAT) {
//            shareProcessor.shareToWeChat(shareParams);
//        } else if (curShareType == SHARE_WECHAT_MOMENTS) {
//            shareProcessor.shareToWeChatMoments(shareParams);
//        } else if (curShareType == SHARE_SINA_WEIBO) {
//            shareProcessor.shareToSinaWeibo(shareParams);
//        } else if (curShareType == SHARE_QQ) {
//            shareProcessor.shareToQQ(shareParams);
//        } else if (curShareType == SHARE_QQ_ZONE) {
//            shareProcessor.shareToQQZone(shareParams);
//        } else if (curShareType == SHARE_EMAIL) {
//            shareProcessor.shareToEmail(shareParams);
//        } else if (curShareType == SHARE_MESSAGE) {
//            shareProcessor.shareToMsg(shareParams);
//        } else if (curShareType == SHARE_ALBUM) {
//            shareToAlbum(context, shareParams);
//        } else if (curShareType == SHARE_COPY_URL) {
//            shareToClipboard(context, shareParams);
//        }
//    }
//
//    private void shareToClipboard(final Context context, final Platform.ShareParams shareParams) {
//        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//        if (null != cmb) {
//            cmb.setPrimaryClip(ClipData.newPlainText(null, shareParams.getUrl()));
//            ToastTool.showToast(R.string.adf_url_clip_board_complete);
//        }
//    }
//
//    private void shareToAlbum(final Context context, final Platform.ShareParams shareParams) {
//        PermissionUtil.requestPermission(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                new PermissionUtil.PermissionListenerWrapper("未授予写入SD卡权限，无法进行保存！") {
//                    @Override
//                    public void onPermissionGot() {
//                        saveToAlbum(context, shareParams);
//                    }
//                }, "保存至相册需要写入SD卡权限，否则无法正常使用此功能");
//    }
//
//    /**
//     * 保存至相册
//     */
//    private void saveToAlbum(final Context context, final Platform.ShareParams shareParams) {
//        final Bitmap shareBitmap = shareParams.getImageData();
//        final String saveImagePath = shareParams.getImagePath();
//        if (shareBitmap == null || saveImagePath == null || context == null) {
//            return;
//        }
//        new Thread() {
//            @Override
//            public void run() {
//                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    ToastTool.showToast("未检测到SD卡！", 1);
//                    return;
//                }
//                long currentTimeMillis = System.currentTimeMillis();
//                String name = "Snap" + currentTimeMillis;
//                try {
//                    BitMapUtil.saveBitmap(shareBitmap, saveImagePath, name + ".jpg", 100);
//                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                            saveImagePath + "/" + name + ".jpg",
//                            name, "万得股票截屏分享" + currentTimeMillis);
//                    ToastTool.showToast("成功保存至此目录：\n" + saveImagePath + "/", 1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    private ShareCallBack shareCallBack = new ShareCallBack() {
//        @Override
//        public void onCancel(Platform arg0, int arg1, ShareTypeEnum shareType) {
//            callBack(shareType, SHARE_STATE_CANCEL);
//        }
//
//        @Override
//        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2, ShareTypeEnum shareType) {
//            callBack(shareType, SHARE_STATE_COMPLETE);
//        }
//
//        @Override
//        public void onError(Platform arg0, int arg1, Throwable arg2, ShareTypeEnum shareType) {
//            callBack(shareType, SHARE_STATE_ERROR);
//        }
//
//        @Override
//        public void onStart(ShareTypeEnum shareType) {
//            callBack(shareType, SHARE_STATE_START);
//        }
//    };
//
//    private void callBack(ShareTypeEnum shareType, ShareStateEnum state) {
//        if (null != mIShare) {
//            mIShare.shareResult(shareType, state);
//        }
//    }
}
