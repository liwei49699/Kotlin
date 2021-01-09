package com.liwei.kotlin.snapshot.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;

import androidx.annotation.DimenRes;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;



/**
 * **************************************************************** 文件名称 :
 * StringUtils.java 作 者 : Aofei.Wang 创建时间 : 2012-2-23 下午03:47:44 文件描述 : 工具类 修改历史
 * : 2012-2-23 1.00 初始版本
 * ****************************************************************
 */
public class StringUtils {

    private static final String ENCODE = "UTF-8";

    private static final String DEFAULT_MAC = "0008ca613fd7";



    /**
     * 去掉windCode的后缀
     *
     * @param windCode
     * @return
     */
    public static String transformWindCode(String windCode) {
        int index = windCode.indexOf(".");
        if (index > -1) {
            return windCode.substring(0, index);
        }
        return windCode;
    }
    /**
     * 扩展函数，文字单位缩小
     */

    public static SpannableString getSpecificMoneyValue(float value) {
        String[] values = specificMarketValue(value);
        SpannableString span = new SpannableString(values[0] + values[1]);
        span.setSpan(new AbsoluteSizeSpan(StringUtils.dipToPx(12)),
                values[0].length(), (values[0] + values[1]).length(),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    /**
     * 8.88 88.88 888.8 8888 8.88万 88.8万 888万 0.88亿 8.88亿 88.8亿 888亿 8888亿
     *
     * @param value
     * @return [0]：值，[1]：单位
     */
    public static String[] specificMarketValue(float value) {

        float absvalue = Math.abs(value);

        String[] data = new String[2];
        data[1] = "";

        if (absvalue < 100) {
            BigDecimal decValue = new BigDecimal(absvalue).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
            if (decValue.floatValue() >= 100) {
                return specificMarketValue(decValue.floatValue());
            }
            data[0] = decValue.toString();
        } else if (absvalue < 1000) {
            BigDecimal decValue = new BigDecimal(absvalue).setScale(1,
                    BigDecimal.ROUND_HALF_UP);
            if (decValue.floatValue() >= 1000) {
                return specificMarketValue(decValue.floatValue());
            }
            data[0] = decValue.toString();
        } else if (absvalue < 10000) {
            BigDecimal decValue = new BigDecimal(absvalue).setScale(0,
                    BigDecimal.ROUND_HALF_UP);
            if (decValue.floatValue() >= 10000) {
                return specificMarketValue(decValue.floatValue());
            }
            data[0] = decValue.toString();
        } else if (absvalue < 100000) {
            BigDecimal decValue = new BigDecimal(absvalue / 10000).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
            if (decValue.floatValue() >= 10) {
                return specificMarketValue(decValue.floatValue() * 10000);
            }
            data[0] = decValue.toString();
            data[1] = "万";
        } else if (absvalue < 1000000) {
            BigDecimal decValue = new BigDecimal(absvalue / 10000).setScale(1,
                    BigDecimal.ROUND_HALF_UP);
            if (decValue.floatValue() >= 100) {
                return specificMarketValue(decValue.floatValue() * 10000);
            }
            data[0] = decValue.toString();
            data[1] = "万";
        } else if (absvalue < 10000000) {
            BigDecimal decValue = new BigDecimal(absvalue / 10000).setScale(0,
                    BigDecimal.ROUND_HALF_UP);
            if (decValue.floatValue() >= 1000) {
                return specificMarketValue(decValue.floatValue() * 10000);
            }
            data[0] = decValue.toString();
            data[1] = "万";
        } else if (absvalue < 100000000) {
            BigDecimal decValue = new BigDecimal(absvalue / 100000000)
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            if (decValue.floatValue() >= 10) {
                return specificMarketValue(decValue.floatValue() * 100000000);
            }
            data[0] = decValue.toString();
            data[1] = "亿";
        } else if (absvalue < 1000000000) {
            BigDecimal decValue = new BigDecimal(absvalue / 100000000)
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            if (decValue.floatValue() >= 100) {
                return specificMarketValue(decValue.floatValue() * 100000000);
            }
            data[0] = decValue.toString();
            data[1] = "亿";
        } else if (absvalue < 10000000000F) {
            BigDecimal decValue = new BigDecimal(absvalue / 100000000)
                    .setScale(1, BigDecimal.ROUND_HALF_UP);
            if (decValue.floatValue() >= 1000) {
                return specificMarketValue(decValue.floatValue() * 100000000);
            }
            data[0] = decValue.toString();
            data[1] = "亿";
        } else {
            BigDecimal decValue = new BigDecimal(absvalue / 100000000)
                    .setScale(0, BigDecimal.ROUND_HALF_UP);
            data[0] = decValue.toString();
            data[1] = "亿";
        }

        if (value < 0) {
            data[0] = "-" + data[0];
        }

        return data;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dipToPx(float dpValue) {
        final float scale = UIScreen.density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxToDip(float pxValue) {
        final float scale = UIScreen.density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int spTopx(float spValue) {
        return (int) (spValue * UIScreen.density + 0.5f);
    }

    public static int stringToPX(String str) {
        if (str == null) {
            return 0;
        }
        float f = 0;
        if (str.indexOf("dip") > 0) {
            return dipToPx(StringUtils.valueOfFloat(str
                    .substring(0, str.indexOf("dip")), 0));
        } else if (str.indexOf("px") > 0) {
            f = StringUtils.valueOfFloat(str
                    .substring(0, str.indexOf("px")), 0);
            return (int) f;
        } else if (str.indexOf("sp") > 0) {
            f = StringUtils.valueOfFloat(str
                    .substring(0, str.indexOf("sp")), 0);
            return (int) f;
        } else {
            return 0;
        }
    }

    /**
     * Get bitmap from file
     *
     * @param context
     * @param id
     * @return
     */
    public static Bitmap readBitmap(Context context, int id) {
        return readBitmap(context, id, 0);
    }

    public static Bitmap readBitmap(Context context, int id, int flag) {
        if (flag == 0) {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Config.ARGB_4444;// 表示16位位图 565代表对应三原色占的位数
            //opt.inInputShareable = true;
            //opt.inPurgeable = true;// 设置图片可以被回收
            InputStream is = context.getResources().openRawResource(id);
            return BitmapFactory.decodeStream(is, null, opt);
        }
        return readBitmap(context, id, 10, 10);
    }

    /**
     * Get bitmap from file
     *
     * @param context
     * @param id
     * @return
     */
    public static Bitmap readBitmap(Context context, int id, int width, int height) {
        try {
            Bitmap bitmap = StringUtils.readBitmap(context, id);

            final Matrix matrix = new Matrix();
            float w = (float) width / (float) bitmap.getWidth();
            float h = (float) height / (float) bitmap.getHeight();
            matrix.postScale(w, h); //长和宽放大缩小的比例
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap = null;
            return bitmap2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get bitmap from file
     *
     * @param bitmap
     * @param height
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int width, int height) {
        try {
            final Matrix matrix = new Matrix();
            float w = (float) width / (float) bitmap.getWidth();
            float h = (float) height / (float) bitmap.getHeight();
            matrix.postScale(w, h); //长和宽放大缩小的比例
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            bitmap = null;
            return bitmap2;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get bitmap from bitmap, no recycle the input bitmap
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap scaleBitmapNoRecycle(Bitmap bitmap, int width, int height) {
        try {
            final Matrix matrix = new Matrix();
            float w = (float) width / (float) bitmap.getWidth();
            float h = (float) height / (float) bitmap.getHeight();
            matrix.postScale(w, h); //长和宽放大缩小的比例
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return bitmap2;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get bitmap from file
     *
     * @param pathName
     * @param width
     * @return
     */
    public static Bitmap scaleBitmapByWidth(String pathName, int width) {
        try {
            Bitmap bitmap = decodeFile(pathName, width);
//			final Matrix matrix = new Matrix();
//			float w = (float)width / (float)bitmap.getWidth();
//			float height  = bitmap.getHeight() * (float)bitmap.getWidth() / bitmap.getWidth();
//			float h = (float)height / (float)bitmap.getHeight();
//			matrix.postScale(w,h); //长和宽放大缩小的比例
//			Bitmap bitmap2 = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
//			//bitmap.recycle();
//			bitmap = null;
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get bitmap from file
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getRoundBitmapByRect(Bitmap bitmap, int width, int height) {
        final Matrix matrix = new Matrix();
        float w = (float) width / (float) bitmap.getWidth();
        float h = (float) height / (float) bitmap.getHeight();
        matrix.postScale(w, h); //长和宽放大缩小的比例
        Bitmap bitmapRes = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap = null;
        return getRoundBitMap(bitmapRes);
    }

    public static Bitmap getRoundBitMap(Bitmap icon) {
        if (icon == null) {
            return null;
        }
        try {
            final Paint pp = new Paint();
            //创建一个和原始图片一样大小位图
            Bitmap roundConcerImage = Bitmap.createBitmap(icon.getWidth(), icon.getHeight(), Config.ARGB_8888);
            //创建带有位图roundConcerImage的画布
            Canvas canvas = new Canvas(roundConcerImage);
            //创建画笔

            //创建一个和原始图片一样大小的矩形
            Rect rect = new Rect(0, 0, icon.getWidth(), icon.getHeight());
            RectF rectF = new RectF(rect);
            // 去锯齿
            pp.setAntiAlias(true);
            //画一个和原始图片一样大小的圆角矩形
            canvas.drawRoundRect(rectF, StringUtils.dipToPx(4), StringUtils.dipToPx(4), pp);
            //设置相交模式
            pp.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            //把图片画到矩形去
            canvas.drawBitmap(icon, null, rect, pp);
            return roundConcerImage;
            //icon = roundConcerImage;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap getRoundBitMap(Bitmap icon, int w, int h) {
        if (icon == null) {
            return null;
        }
        try {
            final Paint pp = new Paint();
            //创建一个和原始图片一样大小位图
            Bitmap roundConcerImage = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            //创建带有位图roundConcerImage的画布
            Canvas canvas = new Canvas(roundConcerImage);
            //创建画笔

            //创建一个和原始图片一样大小的矩形
            Rect rect = new Rect(0, 0, w, h);
            RectF rectF = new RectF(rect);
            // 去锯齿
            pp.setAntiAlias(true);
            //画一个和原始图片一样大小的圆角矩形
            canvas.drawRoundRect(rectF, StringUtils.dipToPx(4), StringUtils.dipToPx(4), pp);
            //设置相交模式
            pp.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            //把图片画到矩形去
            canvas.drawBitmap(icon, null, rect, pp);
            return roundConcerImage;
            //icon = roundConcerImage;
        } catch (Exception e) {
            return null;
        }
    }

    public static String nullToString(String s) {
        return s == null ? "" : s;
    }

    public static int getCurrentWidth(int w) {
        return (int) ((float) w * UIScreen.density / 1.5f);
    }

    /**
     * Decode an immutable bitmap from the specified byte array.
     *
     * @param data   byte array of compressed image data
     * @param offset offset into imageData for where the decoder should begin
     *               parsing.
     * @param length the number of bytes, beginning at offset, to parse
     * @return The decoded bitmap, or null if the image could not be decode ,
     * or, OutOfMemoryError.
     */
    public static Bitmap decodeByteArray(byte[] data, int offset, int length) {
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length);
            return bitmap;
        } catch (OutOfMemoryError err) {
            try {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset,
                        length, opts);
                return bitmap;
            } catch (OutOfMemoryError errEnd) {
            }
        }
        return null;
    }

    /**
     * Decode a file path into a bitmap. If the specified file name is null, or
     * cannot be decoded into a bitmap, the function returns null.
     *
     * @param pathName complete path name for the file to be decoded.
     * @return the resulting decoded bitmap, or null if it could not be decoded,
     * or, OutOfMemoryError.
     */
    public static Bitmap decodeFile(String pathName, int width) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Config.ARGB_4444;
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

            int widthOriginal = options.outWidth;
            int heightOriginal = options.outHeight;

            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inPreferredConfig = Config.ARGB_4444;
            options2.inJustDecodeBounds = false;
            options2.outWidth = width;
            options2.outHeight = heightOriginal * (width / widthOriginal);

            return bitmap = BitmapFactory.decodeFile(pathName, options2);
        } catch (OutOfMemoryError err) {
            try {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
                return bitmap;
            } catch (OutOfMemoryError errEnd) {

            }
        }
        return null;
    }

    public static Bitmap decodeBitmapFile(String pathName) {
        try {
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inPreferredConfig = Config.ARGB_4444;
            //options2.inJustDecodeBounds = false;
//        	options2.outWidth=width;
//        	options2.outHeight=heightOriginal*(width/widthOriginal);

            return BitmapFactory.decodeFile(pathName, options2);
        } catch (OutOfMemoryError err) {
            try {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 2;
                //Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
                return BitmapFactory.decodeFile(pathName, opts);
            } catch (OutOfMemoryError errEnd) {

            }
        }
        return null;
    }

    /**
     * Synonym for decodeResource(Resources, int,
     * android.graphics.BitmapFactory.Options) will null Options.
     *
     * @param res The resources object containing the image data
     * @param id  The resource id of the image data
     * @return The decoded bitmap, or null if the image could not be decode, or,
     * OutOfMemoryError.
     */
    public static Bitmap decodeResource(Resources res, int id) {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(res, id);
            return bitmap;
        } catch (OutOfMemoryError err) {
            try {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeResource(res, id, opts);
                return bitmap;
            } catch (OutOfMemoryError errEnd) {
            }
        }
        return null;
    }

    /**
     * Decode an input stream into a bitmap. If the input stream is null, or
     * cannot be used to decode a bitmap, the function returns null. The
     * stream's position will be where ever it was after the encoded data was
     * read.
     *
     * @param is The input stream that holds the raw data to be decoded into a
     *           bitmap.
     * @return The decoded bitmap, or null if the image data could not be
     * decoded, or, if opts is non-null, if opts requested only the size
     * be returned (in opts.outWidth and opts.outHeight) or, or
     * OutOfMemoryError
     */
    public static Bitmap decodeStream(InputStream is) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (OutOfMemoryError err) {
            try {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
                return bitmap;
            } catch (OutOfMemoryError errEnd) {
            }
        }
        return null;
    }

    /**
     * 这种调用方式可以解决一部分因为decodeFile,decodeStream导致的内存问题（三星的机器上尤为明显）
     * native函数的调用方式上也有区别
     *
     * @param localPath 本地路径
     * @param options   Decode选项
     * @return
     */
    public static Bitmap decodeFileDescriptor(String localPath,
                                              BitmapFactory.Options options) {
        Bitmap tempBitmap = null;
        File file = new File(localPath);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
            if (fs != null) {
                tempBitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(),
                        null, options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tempBitmap;
    }




    /**
     * 字节转化为字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs = hs.append("0").append(stmp);
            } else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }

    public static boolean equals(String str1, String str2) {
        return str1 != null && str1.equals(str2);
    }

    public static boolean startsWith(String str, String start) {
        return str != null && str.startsWith(start);
    }



    private final static ThreadLocal<SimpleDateFormat> dateFormater1 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd");
        }
    };

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(long sdate) {
        Date time = new Date(sdate);

        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater1.get().format(cal.getTime());
        String paramDate = dateFormater1.get().format(time);
        if (curDate.equals(paramDate)) {
            /*int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";*/
            ftime = dateFormater2.get().format(time);
            return ftime;
        }

        long lt = sdate / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
			/*int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";*/
            ftime = dateFormater2.get().format(time);
        } else if (days == 1) {
            ftime = "昨天  " + dateFormater2.get().format(time);
        } else if (days == 2) {
            ftime = "前天  " + dateFormater2.get().format(time);
        } else if (days > 2 && days <= 10) {
            /*ftime = days + "天前";*/
            ftime = dateFormater3.get().format(time);
        } else if (days > 10) {
            ftime = dateFormater3.get().format(time);
        }
        return ftime;
    }

    /**
     * @param time
     * @return
     */
    public static String formateDate(long time) {
        String format = "MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(new Date(time));
    }

    /**
     * @param time
     * @return
     */
    public static String formateYearDate(long time) {
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(new Date(time));
    }


    public static String getDate(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        String a = str.substring(0, 4);
        String b = str.substring(4, 6);
        String c = str.substring(6, 8);
        return (a + "-" + b + "-" + c);
    }

    /**
     * 弱密码校验 
     * "密码不能为连续的数字，如123456";
     * "相同数字不得连续出现4次，如111123";
     * "密码不能过于简单，如123123";
     * "密码不能过于简单，如121212";
     * "密码不能过于简单，如112233";
     * "密码不能过于简单，如111222";
     *
     * @param pwd
     * @return null代表校验成功，否则返回错误提示
     */
    public static String validatePwd(String pwd) {
        if (pwd != null && pwd.length() == 6 && pwd.matches("[0-9]+")) {
            String pass123456 = "0123456789012";
            String pass654321 = "0987654321098";
            if (pass123456.indexOf(pwd) >= 0) {
                return "密码不能为连续的数字，如123456";
            }
            if (pass654321.indexOf(pwd) >= 0) {
                return "密码不能为连续的数字，如654321";
            }
            int pass4_a = StringUtils.valueOfInteger(pwd.substring(0, 4), 0);
            int pass4_b = StringUtils.valueOfInteger(pwd.substring(1, 5), 0);
            int pass4_c = StringUtils.valueOfInteger(pwd.substring(2, 6), 0);
            if (pass4_a % 1111 == 0 || pass4_b % 1111 == 0
                    || pass4_c % 1111 == 0) {
                return "相同数字不得连续出现4次，如111123";
            }
            String pass123 = pwd.substring(0, 3);
            String pass123_replace = pwd.replace(pass123, "");
            if ("".equals(pass123_replace)) {
                return "密码不能过于简单，如123123";
            }
            String pass12 = pwd.substring(0, 2);
            String pass12_replace = pwd.replace(pass12, "");
            if ("".equals(pass12_replace)) {
                return "密码不能过于简单，如121212";
            }
            int pass2_a = StringUtils.valueOfInteger(pwd.substring(0, 2), 0);
            int pass2_b = StringUtils.valueOfInteger(pwd.substring(2, 4), 0);
            int pass2_c = StringUtils.valueOfInteger(pwd.substring(4, 6), 0);
            if (pass2_a % 11 == 0 && pass2_b % 11 == 0 && pass2_c % 11 == 0) {
                return "密码不能过于简单，如112233";
            }
            int pass3_a = StringUtils.valueOfInteger(pwd.substring(0, 3), 0);
            int pass3_b = StringUtils.valueOfInteger(pwd.substring(3, 6), 0);
            if (pass3_a % 111 == 0 && pass3_b % 111 == 0) {
                return "密码不能过于简单，如111222";
            }
        } else {
            return "密码必须为6位数字";
        }
        return null;
    }

    /****************************String强转********************/
    /**
     * @param str 需要转换的字符
     * @return long返回值
     */
    public static long valueOfLong(String str, long defaultL) {
        if (TextUtils.isEmpty(str))
            return defaultL;
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return defaultL;
        }
    }

    /**
     * @param str 需要转换的字符
     * @return int返回值
     */
    public static int valueOfInteger(String str, int defaultI) {
        if (TextUtils.isEmpty(str))
            return defaultI;
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defaultI;
        }
    }

    /**
     * @param str 需要转换的字符
     * @return double返回值
     */
    public static double valueOfDouble(String str, double defaultD) {
        if (TextUtils.isEmpty(str))
            return defaultD;
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return defaultD;
        }
    }


    public static double valueOfDouble(String str) {
        if (TextUtils.isEmpty(str))
            return 0;
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param str 需要转换的字符
     * @return float返回值
     */
    public static float valueOfFloat(String str, float defaultF) {
        if (TextUtils.isEmpty(str))
            return defaultF;
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return defaultF;
        }
    }

    public static float valueOfFloat(String str) {
        if (TextUtils.isEmpty(str))
            return 0;
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param str 需要转换的字符
     * @return boolean返回值
     */
    public static boolean valueOfBoolean(String str, boolean defaultB) {
        if (TextUtils.isEmpty(str))
            return defaultB;
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
            return defaultB;
        }
    }

    /**
     * @param str 需要转换的字符
     * @return byte返回值
     */
    public static byte valueOfByte(String str, byte defaultB) {
        if (TextUtils.isEmpty(str))
            return defaultB;
        try {
            return Byte.parseByte(str);
        } catch (Exception e) {
            return defaultB;
        }
    }

    /**
     * @param str 需要转换的字符
     * @return byte返回值
     */
    public static short valueOfShort(String str, short defaultS) {
        if (TextUtils.isEmpty(str))
            return defaultS;
        try {
            return Short.parseShort(str);
        } catch (Exception e) {
            return defaultS;
        }
    }

    public static float valueOfPercent(String str) {
        if (TextUtils.isEmpty(str) || !str.contains("%")) {
            return 0.00f;
        }

        try {
            return Float.parseFloat(str.replace("%", ""));
        } catch (Exception e) {
            return 0.00f;
        }
    }

    public static int valueOfInt(String str) {
        if (TextUtils.isEmpty(str))
            return 0;
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int valueOfInt(String str, int defaultInt) {
        if (TextUtils.isEmpty(str))
            return defaultInt;
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defaultInt;
        }
    }

    public static String getString(int id, Context context) {
        if (context == null) {
            return "";
        }
        return context.getString(id);
    }

    public static String getString(int id, Object value, Context context) {
        if (context == null) {
            return "";
        }
        return String.format(context.getString(id), value);
    }

//    public static String getString(int id) {
//        return BaseApplication.getInstance().getString(id);
//    }

//    public static String[] getStringArray(int id) {
//        Context applicationContext = BaseApplication.getInstance().getApplicationContext();
//        if (applicationContext == null) {
//            return null;
//        }
//        Resources resources = applicationContext.getResources();
//        if (resources == null) {
//            return null;
//        }
//        return resources.getStringArray(id);
//    }

//    public static float getDimension(@DimenRes int dimenId){
//        return BaseApplication.getInstance().getResources().getDimension(dimenId);
//    }
}
