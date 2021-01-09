package com.liwei.kotlin.snapshot.widgets;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * # **********************************************************************************************
 * # ClassName:      LineInfo.java
 * # Description:    画线的实体
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/8/17    13:51
 * # Modifications:  initial
 * # **********************************************************************************************
 */
public class LineInfo {
    private List<PointF> pointList;
    private LineType lineType;

    /**
     * 线类型
     */
    public enum LineType {
        /**
         * 矩形框
         */
        Rect,
        /**
         * 普通
         */
        NormalLine,
        /**
         * 马赛克
         */
        MosaicLine,
        /**
         * none
         */
        None
    }

    public LineInfo(LineType type) {
        pointList = new ArrayList<>();
        lineType = type;
    }

    public void addPoint(PointF point) {
        pointList.add(point);
    }

    public List<PointF> getPointList() {
        return pointList;
    }

    public LineType getLineType() {
        return lineType;
    }
}
