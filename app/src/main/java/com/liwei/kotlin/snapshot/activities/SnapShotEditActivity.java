package com.liwei.kotlin.snapshot.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.liwei.kotlin.R;
import com.liwei.kotlin.snapshot.TouchEventListener;
import com.liwei.kotlin.snapshot.dialogs.InputPopupWindow;
import com.liwei.kotlin.snapshot.utils.BitMapUtil;
import com.liwei.kotlin.snapshot.utils.BitmapUtils;
import com.liwei.kotlin.snapshot.utils.IShare;
import com.liwei.kotlin.snapshot.utils.ShareStateEnum;
import com.liwei.kotlin.snapshot.utils.ShareTypeEnum;
import com.liwei.kotlin.snapshot.utils.StringUtils;
import com.liwei.kotlin.snapshot.utils.ToastTool;
import com.liwei.kotlin.snapshot.utils.WindLog;
import com.liwei.kotlin.snapshot.widgets.DraggableFrameLayout;
import com.liwei.kotlin.snapshot.widgets.DraggableTextView;
import com.liwei.kotlin.snapshot.widgets.EditableImageView;
import com.liwei.kotlin.snapshot.widgets.LineInfo;
import com.liwei.kotlin.snapshot.widgets.OnActionPerformedListener;
import com.liwei.kotlin.snapshot.widgets.Operation;
import com.liwei.kotlin.snapshot.widgets.SharePopupView;

import java.io.File;
import java.util.Stack;

import cn.sharesdk.framework.Platform;

import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_ALBUM;
import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_QQ;
import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_QQ_ZONE;
import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_SINA_WEIBO;
import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_WECHAT;
import static com.liwei.kotlin.snapshot.utils.ShareTypeEnum.SHARE_WECHAT_MOMENTS;


/**
 * # **********************************************************************************************
 * # ClassName:      SnapShotEditActivity.java
 * # Description:    截图编辑
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/8/28    15:21
 * # Modifications:  initial
 * # **********************************************************************************************
 */
public class SnapShotEditActivity extends AppCompatActivity implements View.OnClickListener, OnActionPerformedListener<Operation>, TouchEventListener {
    /**
     * BundleKey
     */
    public static final String SNAP_SHOT_PATH_KEY = "snap_shot_path_key";

    /**
     * 裁剪
     */
    public static final int MODE_CUT = 0X0011;
    /**
     * 画笔
     */
    public static final int MODE_NORMAL = 0x0001;
    /**
     * 文字编辑
     */
    public static final int MODE_TEXT = 0x0010;
    /**
     * 马赛克
     */
    public static final int MODE_MOSAIC = 0x0100;
    /**
     * 裁剪大小
     */
    private final static int CROP = 500;

    private static final int COLORNUM = 255;
    private static final int DIP_PX = 4;
    private boolean isImageCut=false;

    /**
     * 请求相册
     */
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0X1000;

    private File protraitFile;
    private String protraitPath;

    private String tmpProtraitPath;

    private Uri cropUri;

    // CHECKSTYLE:ON

    /**
     * 截图路径
     */
    private String mPath_SnapShot;
    /**
     * 截图带水印路径
     */
    private String mPath_SnapShot_WD;
    /**
     * 原图
     */
    private ImageView image_src;
    /**
     * 图片
     */
    private EditableImageView mEditableImageView;
    /**
     * 裁剪
     */
    private TextView mTextView_Cut;
    /**
     * 画笔
     */
    private TextView mTextView_Draw;
    /**
     * 文字
     */
    private TextView mTextView_DrawText;
    /**
     * 马赛克
     */
    private TextView mTextView_Mosaic;

    /**
     * 撤回按钮
     */
    protected ImageButton mButton_Withdraw;
    /**
     * 保存分享
     */
    private TextView saveAndShare;
    /**
     * 取消
     */
    private TextView mText_CallBack;
    /**
     * 画板
     */
    protected FrameLayout mFrameLayout;
    protected DraggableFrameLayout mDraggableFrameLayout;

    protected int EditMode;
    protected InputPopupWindow<DraggableTextView> signPopupWindow;

    private int mFrameSrcWidth;
    private int mFrameSrcHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot_edit);
        initNavigatiobBar();
        mPath_SnapShot = getIntent().getStringExtra(SNAP_SHOT_PATH_KEY);
        //裁剪
        mTextView_Cut = (TextView) findViewById(R.id.text_cut);
        mTextView_Draw = (TextView) findViewById(R.id.text_draw);
        mTextView_DrawText = (TextView) findViewById(R.id.text_draw_text);
        mTextView_Mosaic = (TextView) findViewById(R.id.text_mosaic);
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_plate);
        mDraggableFrameLayout = (DraggableFrameLayout) findViewById(R.id.frame_draggable);
        mButton_Withdraw = (ImageButton) findViewById(R.id.image_withdraw);
        image_src = (ImageView) findViewById(R.id.image_src);
        mEditableImageView = (EditableImageView) findViewById(R.id.image_plate);
        mEditableImageView.setImageView(image_src);
        mEditableImageView.setOnActionPerformedListener(this);
        mFrameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                autoFitImageView();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mFrameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mFrameLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        mButton_Withdraw.setVisibility(View.GONE);
        mButton_Withdraw.setOnClickListener(this);
        mTextView_Cut.setOnClickListener(this);
        mTextView_Draw.setOnClickListener(this);
        mTextView_DrawText.setOnClickListener(this);
        mTextView_Mosaic.setOnClickListener(this);
//        mTextView_Cut.setSelected(true);
    }

    /**
     * 顶部navigationBar的初始化
     */
    private void initNavigatiobBar() {
//        navigationBar.setTitle("分享编辑");
//        navigationBar.setDefaultHomeHidden();
//        navigationBar.setListener(this);
//        //右边保存、分享
//        navigationBar.setRightView("保存/分享");
//        //左边取消
//        navigationBar.setLeftView("取消");
    }

    /**
     * 根据ImageView实际高度对图片进行等比例压缩，并调整IamgeView尺寸和Bitmap尺寸一致
     */
    private void autoFitImageView() {
        if (mFrameSrcWidth == 0) {
            mFrameSrcWidth = mFrameLayout.getWidth();
            mFrameSrcHeight = mFrameLayout.getHeight();
        }
        Bitmap compressedBitmap;
        if (isImageCut) {
            compressedBitmap = BitmapUtils.getCompressedBitmap(protraitPath, mFrameSrcHeight,
                    mFrameSrcWidth);
        } else {
            compressedBitmap = BitmapUtils.getCompressedBitmap(mPath_SnapShot, mFrameSrcHeight,
                    mFrameSrcWidth);
        }
        if (null != compressedBitmap) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(compressedBitmap.getWidth(), compressedBitmap.getHeight());
            layoutParams.gravity = Gravity.CENTER;
            mEditableImageView.setLayoutParams(layoutParams);
            mEditableImageView.requestLayout();

            RelativeLayout.LayoutParams frameParams = (RelativeLayout.LayoutParams) mFrameLayout.getLayoutParams();
            frameParams.width = compressedBitmap.getWidth();
            frameParams.height = compressedBitmap.getHeight();
            mFrameLayout.setLayoutParams(frameParams);
            mFrameLayout.requestLayout();

            FrameLayout.LayoutParams scrParams = new FrameLayout.LayoutParams(compressedBitmap.getWidth(), compressedBitmap.getHeight());
            scrParams.gravity = Gravity.CENTER;
            image_src.setLayoutParams(scrParams);
            image_src.setImageBitmap(compressedBitmap);
            image_src.requestLayout();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        // 撤销
        if (i == R.id.image_withdraw) {
            dispatchWithdraw();
            return;
        }
        //裁剪
        if (i == R.id.text_cut) {
            EditMode = MODE_CUT;
            mDraggableFrameLayout.setCanDrag(true);
            mEditableImageView.setEditStatus(false);
            mTextView_Draw.setSelected(false);
            mTextView_DrawText.setSelected(false);
            mTextView_Mosaic.setSelected(false);
            mTextView_Cut.setSelected(true);
            if (mEditableImageView.canStillWithdraw()) {
                showWithDraw();
            } else {
                hideWidthDraw();
            }
            //裁剪
            Uri mTempCropUri=null;
            if (mPath_SnapShot != null && !mPath_SnapShot.isEmpty()) {
                mTempCropUri = getImageContentUri(this, new File(mPath_SnapShot));
            }
            if (mTempCropUri != null) {
                startActionCrop(mTempCropUri);
                onActionPerformed(new Operation(Operation.OP_CUT));
            } else {
                Toast.makeText(this,"裁剪失败，请稍后重试",Toast.LENGTH_LONG).show();
            }
        }
        // 画圈
        if (i == R.id.text_draw) {
            EditMode = MODE_NORMAL;
            mDraggableFrameLayout.setCanDrag(false);
            mEditableImageView.setEditStatus(true);
            mEditableImageView.setLineType(LineInfo.LineType.NormalLine);
            mTextView_Cut.setSelected(false);
            mTextView_Draw.setSelected(true);
            mTextView_DrawText.setSelected(false);
            mTextView_Mosaic.setSelected(false);
            if (mEditableImageView.canStillWithdraw()) {
                showWithDraw();
            } else {
                hideWidthDraw();
            }
            return;
        }
        // 文字
        if (i == R.id.text_draw_text) {
            EditMode = MODE_TEXT;
            mEditableImageView.setEditStatus(false);
            mDraggableFrameLayout.setCanDrag(true);
            mTextView_Cut.setSelected(false);
            mTextView_Draw.setSelected(false);
            mTextView_DrawText.setSelected(true);
            mTextView_Mosaic.setSelected(false);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final DraggableTextView tempTextView = new DraggableTextView(this);
            tempTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tempTextView.setSelected(true);
                    if (signPopupWindow == null) {
                        signPopupWindow = new InputPopupWindow<>(SnapShotEditActivity.this);
                    }
                    signPopupWindow.setCurrentTextView(tempTextView);
                    signPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            tempTextView.setSelected(false);
                        }
                    });
                    signPopupWindow.showAtLocation(findViewById(R.id.linear_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            });
            tempTextView.setLeft(mDraggableFrameLayout.getLeft() + mDraggableFrameLayout.getWidth() / 2 - StringUtils.dipToPx(30));
            tempTextView.setTop(mDraggableFrameLayout.getTop() + mDraggableFrameLayout.getHeight() / 2 - StringUtils.dipToPx(20));
            mDraggableFrameLayout.addView(tempTextView, layoutParams);
            onActionPerformed(new Operation(Operation.OP_TEXT));
            return;
        }
        // 马赛克
        if (i == R.id.text_mosaic) {
            EditMode = MODE_MOSAIC;
            mDraggableFrameLayout.setCanDrag(false);
            mEditableImageView.setEditStatus(true);
            mEditableImageView.setLineType(LineInfo.LineType.MosaicLine);
            mTextView_Cut.setSelected(false);
            mTextView_Draw.setSelected(false);
            mTextView_DrawText.setSelected(false);
            mTextView_Mosaic.setSelected(true);
            if (mEditableImageView.canStillWithdraw()) {
                showWithDraw();
            } else {
                hideWidthDraw();
            }
            return;
        }
    }

    /**
     * 裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);// 裁剪框比例
//        intent.putExtra("aspectY", 2);
//        intent.putExtra("outputX", CROP);// 输出图片大小
//        intent.putExtra("outputY", CROP*2);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    // 裁剪的绝对路径
    private Uri getUploadTempFile(Uri uri) {
        String fileSavePath = WindLog.getCachePath() + "/image/share/";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(fileSavePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            return null;
        }
        String thePath = getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (isEmpty(thePath)) {
            thePath = getAbsoluteImagePath(this, uri);
        }
        String ext = getFileFormat(thePath);
        ext = isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "wind_crop" + "." + ext;
        // 裁剪头像的绝对路径
        tmpProtraitPath = fileSavePath + cropFileName;
        cropUri = Uri.fromFile(new File(tmpProtraitPath));
        return this.cropUri;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileFormat(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    /**
     * 通过uri获取文件的绝对路径
     *
     * @param uri
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getAbsoluteImagePath(Activity context, Uri uri) {
        String imagePath = "";
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.managedQuery(uri, proj, // Which columns to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imagePath = cursor.getString(column_index);
            }
        }

        return imagePath;
    }


    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    private boolean isEmpty(String input) {
        if (input == null || "".equals(input)) {
            return true;
        }

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = "/sdcard";

    /**
     * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
     *
     * @param mUri
     * @return
     */
    private String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = null;

        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);

        String pre1 = "file://" + SDCARD + File.separator;
        String pre2 = "file://" + SDCARD_MNT + File.separator;

        if (mUriString.startsWith(pre1)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre2.length());
        }
        return filePath;
    }

    /**
     * 弹出分享面板
     */
    protected void showSharePanel() {
//        String sourcePath;
//        if (isImageCut) {
//            sourcePath = protraitPath;
//        } else {
//            sourcePath = mPath_SnapShot;
//        }
//        if (sourcePath == null) {
//            return;
//        }
//        final Bitmap shareBitmap = BitmapUtils.mergeBitmap_TB(sourcePath, mEditableImageView, mDraggableFrameLayout, this);
//        if (shareBitmap == null) {
//            return;
//        }
//        saveShot(shareBitmap);
//
//        new SharePopupView(this, new IShare() {
//            @Override
//            public ShareTypeEnum[] getShareViewType() {
//                return new ShareTypeEnum[] {SHARE_WECHAT, SHARE_WECHAT_MOMENTS, SHARE_SINA_WEIBO, SHARE_QQ, SHARE_QQ_ZONE, SHARE_ALBUM};
//            }
//
//            @Override
//            public Platform.ShareParams getShareParams(ShareTypeEnum type) {
//                Platform.ShareParams params = new Platform.ShareParams();
//                if (type == SHARE_WECHAT || type == SHARE_WECHAT_MOMENTS) {
//                    params.setImagePath(mPath_SnapShot_WD);
//                    params.setShareType(1);
//                } else if (type == SHARE_SINA_WEIBO) {
//                    params.setText("截屏分享");
//                    params.setImageData(shareBitmap);
//                } else if (type == SHARE_QQ || type == SHARE_QQ_ZONE) {
//                    params.setImagePath(mPath_SnapShot_WD);
//                } else if (type == SHARE_ALBUM) {
//                    params.setImageData(shareBitmap);
//                    String path = WindLog.getFilePath() + "/image";
//                    params.setImagePath(path);
//                }
//
//                return params;
//            }
//
//            @Override
//            public void shareResult(ShareTypeEnum type, ShareStateEnum state) {
//
//            }
//        }).show();
    }

    /**
     * 保存至相册
     */
//    protected void saveToAlbum(final Bitmap shareBitmap) {
//        new Thread() {
//            @Override
//            public void run() {
//                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    ToastTool.showToast("未检测到SD卡！", 1);
//                    return;
//                }
//                showProgressMum(false);
//                long currentTimeMillis = System.currentTimeMillis();
//                String name = "Snap" + currentTimeMillis;
//                try {
//                    String path = WindLog.getFilePath() + "/image";
//                    BitMapUtil.saveBitmap(shareBitmap, path, name + ".jpg", 100);
//                    MediaStore.Images.Media.insertImage(getContentResolver(),
//                            path + "/" + name + ".jpg",
//                            name, "万得股票截屏分享" + currentTimeMillis);
//                    ToastTool.showToast("成功保存至此目录：\n" + path + "/", 1);
//                    hideProgressMum();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    hideProgressMum();
//                }
//            }
//        }.start();
//    }

    protected void saveShot(final Bitmap shareBitmap) {
        new Thread() {
            @Override
            public void run() {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    ToastTool.showToast("未检测到SD卡！", 1);
                    return;
                }
//                showProgressMum(false);
                String name = "Snap";
                String path = WindLog.getCachePath() + "/image/share";
                mPath_SnapShot_WD = path + "/" + name + ".jpg";
                try {
                    BitMapUtil.saveBitmap(shareBitmap, path, name + ".jpg", 100);
//                    hideProgressMum();
                } catch (Exception e) {
                    e.printStackTrace();
//                    hideProgressMum();
                }
            }
        }.start();
    }

    /**
     * 显示撤销按钮
     */
    protected void showWithDraw() {
        mButton_Withdraw.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏撤销按钮
     */
    protected void hideWidthDraw() {
        mButton_Withdraw.setVisibility(View.GONE);
    }


    /**
     * 隐藏输入法
     */
    public void hideInput(View v) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(InputMethodService.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


    /**
     * 用户编辑的操作栈
     */
    protected Stack<Operation> operationStack = new Stack<>();

    @Override
    public void onActionPerformed(Operation arguments) {
        operationStack.add(arguments);
        showWithDraw();
    }

    /**
     * 分发撤回事件
     */
    protected void dispatchWithdraw() {
        if (operationStack.isEmpty()) {
            hideWidthDraw();
            return;
        }
        Operation operation = operationStack.pop();
        switch (operation.type) {
            case Operation.OP_LINE:
            case Operation.OP_MOSAIC: {
                // 可以撤销 继续撤销
                if (mEditableImageView.canStillWithdraw()) {
                    mEditableImageView.withDrawLastLine();
                }
                break;
            }
            case Operation.OP_TEXT: {
                // 可以撤销
                if (mDraggableFrameLayout.getChildCount() > 0) {
                    mDraggableFrameLayout.removeViewAt(mDraggableFrameLayout.getChildCount() - 1);
                }
                break;
            }
            case Operation.OP_CUT:
                //加载原图
                if (mPath_SnapShot != null && !mPath_SnapShot.isEmpty()) {
                    isImageCut = false;
                    autoFitImageView();
                }
                break;

        }
        if (operationStack.isEmpty()) {
            hideWidthDraw();
            return;
        }
    }

    /**
     * 对请求权限的结果进行处理
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (PermissionUtil.getTargetSdkVersion(this) < Build.VERSION_CODES.M) {
//            for (int i = 0; i < permissions.length; i++) {
//                grantResults[i] = PermissionUtil.selfPermissionGranted(
//                        this, permissions[i]) ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED;
//            }
//        }
//        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    public void touchEvent(View sender, MotionEvent event) {
//        if (sender.getTag() != null && sender.getTag() instanceof Integer
//                && (Integer) sender.getTag() == UINavigationBar.RIGHT_BUTTON) {
//            //navigationBar右边保存/分享点击事件
//            showSharePanel();
//            UserActionReader.subMitUserAction(ACTION_SNAP_SHARE);
//        } else if (sender.getTag() != null && sender.getTag() instanceof Integer
//                && (Integer) sender.getTag() == UINavigationBar.LEFT_BUTTON) {
//            //navigationBar左边取消点击事件
//            UIAlertView uiAlertView = new UIAlertView(this, "", "", null);
//            //            uiAlertView.setTitle("提示");
//            uiAlertView.setMessage("确定放弃分享？");
//            uiAlertView.setLeftButton("确定", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mEditableImageView.clean();
//                    mFrameLayout.removeAllViews();
//                    finish();
//                }
//            });
//            uiAlertView.setRightButton("取消", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            uiAlertView.show();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            //裁剪后更新图片
            case REQUEST_CODE_GETIMAGE_BYSDCARD:
                // 裁剪头像的绝对路径
                protraitPath = tmpProtraitPath;
                protraitFile = new File(protraitPath);
                // 更新图片
                upDateNewImage();
                break;
        }
    }

    /**
     * Gets the content:// URI from the given corresponding path to a file
     *  由图片的路径得到图片Uri,解决魅族机型裁剪图片模糊和大小问题
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                    new String[] { filePath }, null);
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                return Uri.withAppendedPath(baseUri, "" + id);
            } else {
                if (imageFile.exists()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }
    //更新图片
    private void upDateNewImage() {
        if (!isEmpty(protraitPath) && protraitFile.exists()) {
            isImageCut = true;
            autoFitImageView();
        } else {
            ToastTool.showToast("裁剪失败，请稍后重试", 1);
        }
    }
}



