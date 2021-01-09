package com.liwei.kotlin.snapshot.widgets;



import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * # **********************************************************************************************
 * # ClassName:      Operation.java
 * # Description:    用户操作数据
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/10/10    15:32
 * # Modifications:  initial
 * # **********************************************************************************************
 */
public class Operation {
    /**
     * 圈画
     */
    public static final int OP_LINE = 0;
    /**
     * 矩形
     */
    public static final int OP_RECT = 1;
    /**
     * 马赛克
     */
    public static final int OP_MOSAIC = 2;
    /**
     * 文字
     */
    public static final int OP_TEXT = 3;
    /**
     * 裁剪
     */
    public static final int OP_CUT = 4;

    /**
     * 操作类型IntDef
     */
    @IntDef({OP_LINE, OP_RECT, OP_MOSAIC, OP_TEXT, OP_CUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OperationType {

    }

    /**
     * 操作类型
     */
    @OperationType
    public int type;


    /**
     * 构造
     */
    public Operation(@OperationType int type) {
        this.type = type;
    }
}