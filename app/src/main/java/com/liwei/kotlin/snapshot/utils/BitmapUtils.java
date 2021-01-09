package com.liwei.kotlin.snapshot.utils;

/**
 * # ******************************************************************
 * # ClassName:      BitmapUtils
 * # Description:    模块解耦，从f5迁移而来
 * # Author:         jli.Jerry
 * # Version:        Ver 1.0
 * # Create Date     2020/11/16 10:28
 * # ******************************************************************
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * # **********************************************************************************************
 * # ClassName:      BitmapUtils.java
 * # Description:    图片Utils
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/8/17    13:19
 * # Modifications:  initial
 * # **********************************************************************************************
 */
public class BitmapUtils {
    /**
     * 缩放比例
     */
    private static final int SAMPLE_SIZE = 1;

    /**
     * 获取压缩后的图片
     */
    public static Bitmap getCompressedBitmap(@NonNull String filePath, int needHeight, int needWidth) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            // 第一次只解码原始长宽的值
            options.inJustDecodeBounds = true;
            try {
                BitmapFactory.decodeStream(new FileInputStream(new File(filePath)), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            // 根据原始图片长宽和需要的长宽计算采样比例，必须是2的倍数，
            //  IMAGE_WIDTH_DEFAULT=768， IMAGE_HEIGHT_DEFAULT=1024
            //           int needWidth = (int) (needHeight * 1.0 / options.outHeight * options.outWidth);
            o2.inSampleSize = SAMPLE_SIZE;
            // 每像素采用RGB_565的格式保存
            o2.inPreferredConfig = Bitmap.Config.ARGB_8888;
            // 根据压缩参数的设置进行第二次解码
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(new File(filePath)), null, o2);

            if (needHeight > options.outHeight && needWidth > options.outWidth) {
                // 如果宽和高都小，则直接用原图
                needHeight = options.outHeight;
                needWidth = options.outWidth;
            } else {
                // 以高或者宽为基准缩放
                boolean isHeight = options.outHeight * 1f / needHeight > options.outWidth * 1f / needWidth;
                if (isHeight) {
                    // 以高为基准 计算宽
                    needWidth = needHeight * options.outWidth / options.outHeight;
                } else {
                    // 以宽为基准 计算高
                    needHeight = needWidth * options.outHeight / options.outWidth;
                }
            }
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, needWidth, needHeight, true);

            //          b.recycle();  // b.recycle will cause prev Bitmap.createScaledBitmap null pointer exception on b occasionally
            System.gc();

            return scaledBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取采样比例
     */
    public static int getImageScale(int outWidth, int outHeight, int needWidth, int needHeight) {
        int scale = 1;
        if (outHeight > needHeight || outWidth > needWidth) {
            int maxSize = needHeight > needWidth ? needHeight : needWidth;
            scale = (int) Math.pow(2, (int) Math.round(Math.log(maxSize / (double) Math.max(outHeight, outWidth)) / Math.log(0.5)));
        }
        return scale;
    }


    /**
     * 把两个位图覆盖合成为一个位图，以底层位图的长宽为基准
     *
     * @param backBitmap  在底部的位图
     * @param frontBitmap 盖在上面的位图
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap backBitmap, Bitmap frontBitmap) {

        if (backBitmap == null || backBitmap.isRecycled()
                || frontBitmap == null || frontBitmap.isRecycled()) {
            //            Log.e(TAG, "backBitmap=" + backBitmap + ";frontBitmap=" + frontBitmap);
            return null;
        }
        Bitmap bitmap = backBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Rect baseRect = new Rect(0, 0, backBitmap.getWidth(), backBitmap.getHeight());
        Rect frontRect = new Rect(0, 0, frontBitmap.getWidth(), frontBitmap.getHeight());
        canvas.drawBitmap(frontBitmap, frontRect, baseRect, null);
        return bitmap;
    }

    /**
     * 把两个位图覆盖合成为一个位图，左右拼接
     *
     * @param isBaseMax 是否以宽度大的位图为准，true则小图等比拉伸，false则大图等比压缩
     * @return
     */
    public static Bitmap mergeBitmap_LR(Bitmap leftBitmap, Bitmap rightBitmap, boolean isBaseMax) {

        if (leftBitmap == null || leftBitmap.isRecycled()
                || rightBitmap == null || rightBitmap.isRecycled()) {
            //            JDLog.logError(TAG, "leftBitmap=" + leftBitmap + ";rightBitmap=" + rightBitmap);
            return null;
        }
        // 拼接后的高度，按照参数取大或取小
        int height = 0;
        if (isBaseMax) {
            height = leftBitmap.getHeight() > rightBitmap.getHeight() ? leftBitmap.getHeight() : rightBitmap.getHeight();
        } else {
            height = leftBitmap.getHeight() < rightBitmap.getHeight() ? leftBitmap.getHeight() : rightBitmap.getHeight();
        }

        // 缩放之后的bitmap
        Bitmap tempBitmapL = leftBitmap;
        Bitmap tempBitmapR = rightBitmap;

        if (leftBitmap.getHeight() != height) {
            tempBitmapL = Bitmap.createScaledBitmap(leftBitmap,
                    (int) (leftBitmap.getWidth() * 1f / leftBitmap.getHeight() * height), height, false);
        } else if (rightBitmap.getHeight() != height) {
            tempBitmapR = Bitmap.createScaledBitmap(rightBitmap,
                    (int) (rightBitmap.getWidth() * 1f / rightBitmap.getHeight() * height), height, false);
        }

        // 拼接后的宽度
        int width = tempBitmapL.getWidth() + tempBitmapR.getWidth();

        // 定义输出的bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // 缩放后两个bitmap需要绘制的参数
        Rect leftRect = new Rect(0, 0, tempBitmapL.getWidth(), tempBitmapL.getHeight());
        Rect rightRect = new Rect(0, 0, tempBitmapR.getWidth(), tempBitmapR.getHeight());

        // 右边图需要绘制的位置，往右边偏移左边图的宽度，高度是相同的
        Rect rightRectT = new Rect(tempBitmapL.getWidth(), 0, width, height);

        canvas.drawBitmap(tempBitmapL, leftRect, leftRect, null);
        canvas.drawBitmap(tempBitmapR, rightRect, rightRectT, null);
        return bitmap;
    }


    /**
     * 把两个位图覆盖合成为一个位图，上下拼接
     *
     * @param isBaseMax 是否以高度大的位图为准，true则小图等比拉伸，false则大图等比压缩
     * @return
     */
    public static Bitmap mergeBitmap_TB(Bitmap topBitmap, Bitmap bottomBitmap, boolean isBaseMax) {

        if (topBitmap == null || topBitmap.isRecycled()
                || bottomBitmap == null || bottomBitmap.isRecycled()) {
            //            JDLog.logError(TAG, "topBitmap=" + topBitmap + ";bottomBitmap=" + bottomBitmap);
            return null;
        }
        int width = 0;
        if (isBaseMax) {
            width = topBitmap.getWidth() > bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
        } else {
            width = topBitmap.getWidth() < bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
        }
        Bitmap tempBitmapT = topBitmap;
        Bitmap tempBitmapB = bottomBitmap;

        if (topBitmap.getWidth() != width) {
            tempBitmapT = Bitmap.createScaledBitmap(topBitmap, width,
                    (int) (topBitmap.getHeight() * 1f / topBitmap.getWidth() * width), false);
        } else if (bottomBitmap.getWidth() != width) {
            tempBitmapB = Bitmap.createScaledBitmap(bottomBitmap, width,
                    (int) (bottomBitmap.getHeight() * 1f / bottomBitmap.getWidth() * width), false);
        }

        int height = tempBitmapT.getHeight() + tempBitmapB.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Rect topRect = new Rect(0, 0, tempBitmapT.getWidth(), tempBitmapT.getHeight());
        Rect bottomRect = new Rect(0, 0, tempBitmapB.getWidth(), tempBitmapB.getHeight());

        Rect bottomRectT = new Rect(0, tempBitmapT.getHeight(), width, height);

        canvas.drawBitmap(tempBitmapT, topRect, topRect, null);
        canvas.drawBitmap(tempBitmapB, bottomRect, bottomRectT, null);
        return bitmap;
    }

    /**
     * 把两个位图覆盖合成为一个位图，上下拼接
     *
     * @param isBaseMax 是否以高度大的位图为准，true则小图等比拉伸，false则大图等比压缩
     * @param requestWidth 需要的宽度
     * @return
     */
    public static Bitmap mergeBitmap_TB(Bitmap topBitmap, Bitmap bottomBitmap, boolean isBaseMax, int requestWidth) {

        if (topBitmap == null || topBitmap.isRecycled()
                || bottomBitmap == null || bottomBitmap.isRecycled()) {
            //            JDLog.logError(TAG, "topBitmap=" + topBitmap + ";bottomBitmap=" + bottomBitmap);
            return null;
        }
        int width = 0;
        if (requestWidth > 0) {
            width = requestWidth;
        } else {
            if (isBaseMax) {
                width = topBitmap.getWidth() > bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
            } else {
                width = topBitmap.getWidth() < bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
            }
        }
        Bitmap tempBitmapT = topBitmap;
        Bitmap tempBitmapB = bottomBitmap;

        if (topBitmap.getWidth() != width) {
            tempBitmapT = Bitmap.createScaledBitmap(topBitmap, width,
                    (int) (topBitmap.getHeight() * 1f / topBitmap.getWidth() * width), false);
        }
        if (bottomBitmap.getWidth() != width) {
            tempBitmapB = Bitmap.createScaledBitmap(bottomBitmap, width,
                    (int) (bottomBitmap.getHeight() * 1f / bottomBitmap.getWidth() * width), false);
        }

        int height = tempBitmapT.getHeight() + tempBitmapB.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Rect topRect = new Rect(0, 0, tempBitmapT.getWidth(), tempBitmapT.getHeight());
        Rect bottomRect = new Rect(0, 0, tempBitmapB.getWidth(), tempBitmapB.getHeight());

        Rect bottomRectT = new Rect(0, tempBitmapT.getHeight(), width, height);

        canvas.drawBitmap(tempBitmapT, topRect, topRect, null);
        canvas.drawBitmap(tempBitmapB, bottomRect, bottomRectT, null);
        return bitmap;
    }

    /**
     * 把三个位图覆盖合成为一个位图，上下拼接
     * <p>
     * 以topBitmap为基准 plateBitmap/draggableBitmap完全覆盖其上，bottomBitmap加在其下
     *
     * @param sourcePath       上方位图
     * @param mEditableImageView 覆盖在上方位图上的图片1
     * @param mDraggableFrameLayout 覆盖在上方位图上的图片2
     * @param activity    activity
     * @return
     */
//    public static Bitmap mergeBitmap_TB(String sourcePath, View mEditableImageView, View mDraggableFrameLayout, Activity activity) {
//        // 原图
//        Bitmap sourceBitmap = StringUtils.decodeFileDescriptor(sourcePath, null);
//        return mergeBitmap_TB(sourceBitmap, mEditableImageView, mDraggableFrameLayout, activity);
//    }

//    public static Bitmap mergeBitmap_TB(Bitmap sourceBitmap, View mEditableImageView, View mDraggableFrameLayout, Activity activity) {
//        if (sourceBitmap == null) {
//            return null;
//        }
//        int sourceWidth = sourceBitmap.getWidth();
//        int sourceHeight = sourceBitmap.getHeight();
//        if (sourceWidth <= 0 || sourceHeight <= 0) {
//            return null;
//        }
//        // 底部二维码
////        Bitmap bottomBitmap = WXShareScreenShotUtils.creatQrcode(activity, sourceWidth);
////        Bitmap tempBitmapB = bottomBitmap;
////        if (bottomBitmap.getWidth() != sourceWidth) {
////            tempBitmapB = Bitmap.createScaledBitmap(bottomBitmap, sourceWidth,
////                    (int) (bottomBitmap.getHeight() * 1f / bottomBitmap.getWidth() * sourceWidth), false);
////            bottomBitmap.recycle();
////        }
////        int height = sourceHeight + tempBitmapB.getHeight();
////        Bitmap bitmap = Bitmap.createBitmap(sourceWidth, height, Bitmap.Config.ARGB_8888);
////        Canvas canvas = new Canvas(bitmap);
////        // 绘制原图并释放
////        Rect topRect = new Rect(0, 0, sourceWidth, sourceHeight);
////        canvas.drawBitmap(sourceBitmap, topRect, topRect, null);
////        sourceBitmap.recycle();
////
////        Bitmap waterMarket = ((BitmapDrawable) activity.getResources()
////                .getDrawable(ThemeUtils.getResId(R.drawable.replay_watermark_black, R.drawable.replay_watermark))).getBitmap();
////        if (waterMarket != null && !waterMarket.isRecycled()) {
////            // 绘制水印
////            if (waterMarket.getWidth() > 0f) {
////                Bitmap scaledBitmap = Bitmap.createScaledBitmap(waterMarket, (int) (sourceWidth * 0.8f),
////                        (int) (waterMarket.getHeight() * 1f / waterMarket.getWidth() * sourceWidth * 0.8f), false);
////                canvas.save();
////                canvas.translate((sourceWidth - scaledBitmap.getWidth()) / 2, (sourceHeight - scaledBitmap.getHeight()) / 2);
////                canvas.drawBitmap(scaledBitmap, 0, 0, null);
////                canvas.restore();
////                waterMarket.recycle();
////            }
////
////        }
//
//        // 涂鸦
//        Bitmap plateBitmap = null;
//        if (null != mEditableImageView) {
//            plateBitmap = Bitmap.createBitmap(mEditableImageView.getWidth(), mEditableImageView.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas plateCanvas = new Canvas(plateBitmap);
//            mEditableImageView.draw(plateCanvas);
//        }
//        Bitmap tempBitmapP = plateBitmap;
//        if (plateBitmap != null && plateBitmap.getWidth() != sourceWidth) {
//            tempBitmapP = Bitmap.createScaledBitmap(plateBitmap, sourceWidth,
//                    sourceHeight, false);
//            plateBitmap.recycle();
//        }
//        if (tempBitmapP != null) {
//            canvas.drawBitmap(tempBitmapP, topRect, topRect, null);
//            tempBitmapP.recycle();
//        }
//        Bitmap draggableBitmap = null;
//        if (null != mDraggableFrameLayout) {
//            draggableBitmap = Bitmap.createBitmap(mDraggableFrameLayout.getWidth(), mDraggableFrameLayout.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas dragCanvas = new Canvas(draggableBitmap);
//            mDraggableFrameLayout.draw(dragCanvas);
//        }
//        Bitmap tempBitmapD = draggableBitmap;
//        if (draggableBitmap != null && draggableBitmap.getWidth() != sourceWidth) {
//            tempBitmapD = Bitmap.createScaledBitmap(draggableBitmap, sourceWidth,
//                    sourceHeight, false);
//            draggableBitmap.recycle();
//        }
//        if (tempBitmapD != null) {
//            canvas.drawBitmap(tempBitmapD, topRect, topRect, null);
//            tempBitmapD.recycle();
//        }
//        // 绘制底部二维码并释放
//        Rect bottomRect = new Rect(0, 0, tempBitmapB.getWidth(), tempBitmapB.getHeight());
//        Rect bottomRectT = new Rect(0, sourceHeight, tempBitmapB.getWidth(), height);
//        canvas.drawBitmap(tempBitmapB, bottomRect, bottomRectT, null);
//        tempBitmapB.recycle();
//        return bitmap;
//    }
}

