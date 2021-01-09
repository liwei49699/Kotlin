package com.liwei.kotlin.snapshot.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * # **********************************************************************************************
 * # ClassName:      EditableImageView.java
 * # Description:    可编辑的ImageView
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/8/17    13:51
 * # Modifications:  initial
 * # **********************************************************************************************
 */
public class EditableImageView extends ImageView {
    // 线条列表
    protected List<LineInfo> lineList;

    /**
     * 当前线条
     */
    protected LineInfo currentLine;
    /**
     * 当前线条类型
     */
    protected LineInfo.LineType currentLineType = LineInfo.LineType.None;

    /**
     * 方框列表
     */
    protected ArrayList<RectF> mListRect = new ArrayList<>();
    protected Paint rectPaint = new Paint();
    protected static final float RECT_STROKE = 4.0f;
    protected Paint normalPaint = new Paint();
    protected static final float NORMAL_LINE_STROKE = 5.0f;

    protected Paint mosaicPaint = new Paint();
    // 马赛克每个大小40*40像素，共三行
    protected static final int MOSAIC_CELL_LENGTH = 20;

    protected Drawable drawable;
    protected Bitmap bitmap;

    // 马赛克绘制中用于记录某个马赛克格子的数值是否计算过
    protected boolean[][] mosaics;
    // 马赛克行数
    protected int mosaicRows;
    // 马赛克列数
    protected int mosaicColumns;

    protected PointF startPoint;
    protected PointF endPoint;

    {
        lineList = new ArrayList<>();
        normalPaint.setColor(Color.RED);
        normalPaint.setStrokeWidth(NORMAL_LINE_STROKE);
        rectPaint.setStrokeWidth(RECT_STROKE);
        rectPaint.setColor(Color.rgb(255, 204, 0));
        rectPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setStyle(Paint.Style.STROKE);
    }


    /**
     * 动作完成的listener
     */
    protected OnActionPerformedListener mOnActionPerformedListener;

    /**
     * 获取动作完成的listener
     */
    public OnActionPerformedListener getOnActionPerformedListener() {
        return mOnActionPerformedListener;
    }

    /**
     * 设置动作完成的listener
     */
    public void setOnActionPerformedListener(OnActionPerformedListener mOnActionPerformedListener) {
        this.mOnActionPerformedListener = mOnActionPerformedListener;
    }

    public EditableImageView(Context context) {
        super(context);
    }

    public EditableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private ImageView mSrcImageView;

    public void setImageView(ImageView srcImageView) {
        mSrcImageView = srcImageView;
    }

    /**
     * 初始化马赛克绘制相关
     */
    protected void init() {
        try {
            drawable = mSrcImageView.getDrawable();
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        mosaicColumns = (int) Math.ceil(bitmap.getWidth() / MOSAIC_CELL_LENGTH);
        mosaicRows = (int) Math.ceil(bitmap.getHeight() / MOSAIC_CELL_LENGTH);
        mosaics = new boolean[mosaicRows][mosaicColumns];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();
        switch (currentLineType) {
            case NormalLine: {
                xPos = calculateXBorder(xPos, normalPaint);
                yPos = calculateYBorder(yPos, normalPaint);
                if (consumeTouchEvent(event, xPos, yPos)) {
                    return true;
                }
                break;
            }
            case MosaicLine: {
                xPos = calculateXBorder(xPos, mosaicPaint);
                yPos = calculateYBorder(yPos, mosaicPaint);
                if (consumeTouchEvent(event, xPos, yPos)) {
                    return true;
                }
                break;
            }
            case Rect: {
                xPos = calculateXBorder(xPos, rectPaint);
                yPos = calculateYBorder(yPos, rectPaint);
                if (consumeTouchEventRect(event, xPos, yPos)) {
                    return true;
                }
                break;
            }
        }

        return super.onTouchEvent(event);
    }

    @Nullable
    private Boolean consumeTouchEvent(MotionEvent event, float xPos, float yPos) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentLine = new LineInfo(currentLineType);
                currentLine.addPoint(new PointF(xPos, yPos));
                lineList.add(currentLine);
                if (mOnActionPerformedListener != null) {
                    switch (currentLineType) {
                        case MosaicLine:
                            mOnActionPerformedListener.onActionPerformed(new Operation(Operation.OP_MOSAIC));
                            break;
                        case NormalLine:
                            mOnActionPerformedListener.onActionPerformed(new Operation(Operation.OP_LINE));
                            break;
                        case None:
                            break;
                        default:
                            mOnActionPerformedListener.onActionPerformed(new Operation(Operation.OP_LINE));
                            break;
                    }
                }
                invalidate();
                // return true消费掉ACTION_DOWN事件，否则不会触发ACTION_UP
                return true;
            case MotionEvent.ACTION_MOVE:
                currentLine.addPoint(new PointF(xPos, yPos));
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                currentLine.addPoint(new PointF(xPos, yPos));
                invalidate();
                break;
        }
        return false;
    }

    @Nullable
    private Boolean consumeTouchEventRect(MotionEvent event, float xPos, float yPos) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startPoint = new PointF(xPos, yPos);
                float endX = xPos + rectPaint.getStrokeWidth();
                float endY = yPos + rectPaint.getStrokeWidth();
                endX = calculateXBorder(endX, rectPaint);
                endY = calculateYBorder(endY, rectPaint);
                endPoint = new PointF(endX, endY);
                invalidate();
                // return true消费掉ACTION_DOWN事件，否则不会触发ACTION_UP
                return true;
            case MotionEvent.ACTION_MOVE:
                endPoint.x = xPos;
                endPoint.y = yPos;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                endPoint.x = xPos;
                endPoint.y = yPos;
                RectF rect = new RectF(Math.min(startPoint.x, endPoint.x),
                        Math.min(startPoint.y, endPoint.y),
                        Math.max(startPoint.x, endPoint.x),
                        Math.max(startPoint.y, endPoint.y));
                mListRect.add(rect);
                if (mOnActionPerformedListener != null) {
                    mOnActionPerformedListener.onActionPerformed(new Operation(Operation.OP_RECT));
                }
                startPoint = new PointF();
                endPoint = new PointF();
                invalidate();
                break;
        }
        return false;
    }

    /**
     * 计算Y边界
     */
    private float calculateYBorder(float yPos, Paint argPaint) {
        if (yPos < 0) {
            yPos = 0 + (argPaint == null ? 0 : argPaint.getStrokeWidth() / 2);
        }
        if (yPos > getHeight()) {
            yPos = getHeight() - (argPaint == null ? 0 : argPaint.getStrokeWidth() / 2);
        }
        return yPos;
    }

    /**
     * 计算X边界
     */
    private float calculateXBorder(float xPos, Paint argPaint) {
        if (xPos < 0) {
            xPos = 0 + (argPaint == null ? 0 : argPaint.getStrokeWidth() / 2);
        }
        if (xPos > getWidth()) {
            xPos = getWidth() - (argPaint == null ? 0 : argPaint.getStrokeWidth() / 2);
        }
        return xPos;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mosaicRows; i++) {
            for (int j = 0; j < mosaicColumns; j++) {
                mosaics[i][j] = false;
            }
        }
        for (LineInfo lineinfo : lineList) {
            if (lineinfo.getLineType() == LineInfo.LineType.NormalLine) {
                drawNormalLine(canvas, lineinfo);
            } else if (lineinfo.getLineType() == LineInfo.LineType.MosaicLine) {
                drawMosaicLine(canvas, lineinfo);
            }
        }

        if (startPoint != null && endPoint != null) {
            canvas.drawRect(Math.min(startPoint.x, endPoint.x),
                    Math.min(startPoint.y, endPoint.y),
                    Math.max(startPoint.x, endPoint.x),
                    Math.max(startPoint.y, endPoint.y), rectPaint);
        }
        if (mListRect.isEmpty()) {
            return;
        }
        for (RectF temp : mListRect) {
            canvas.drawRect(temp, rectPaint);
        }
    }

    /**
     * 绘制马赛克线条
     *
     * @param canvas
     * @param lineinfo
     */
    protected void drawMosaicLine(Canvas canvas, LineInfo lineinfo) {
        if (null == bitmap) {
            init();
        }

        if (null == bitmap) {
            return;
        }

        for (PointF pointInfo : lineinfo.getPointList()) {
            // 对每一个点，填充所在的小格子以及上下两个格子（如果有上下格子）
            int currentRow = (int) ((pointInfo.y - 1) / MOSAIC_CELL_LENGTH);
            int currentCol = (int) ((pointInfo.x - 1) / MOSAIC_CELL_LENGTH);

            fillMosaicCell(canvas, currentRow, currentCol);
            fillMosaicCell(canvas, currentRow - 1, currentCol);
            fillMosaicCell(canvas, currentRow + 1, currentCol);
        }
    }

    /**
     * 填充一个马赛克格子
     *
     * @param cavas
     * @param row   马赛克格子行
     * @param col   马赛克格子列
     */
    protected void fillMosaicCell(Canvas cavas, int row, int col) {
        if (row >= 0 && row < mosaicRows && col >= 0 && col < mosaicColumns) {
            if (!mosaics[row][col]) {
                mosaicPaint.setColor(bitmap.getPixel(col * MOSAIC_CELL_LENGTH, row * MOSAIC_CELL_LENGTH));
                cavas.drawRect(col * MOSAIC_CELL_LENGTH, row * MOSAIC_CELL_LENGTH, (col + 1) * MOSAIC_CELL_LENGTH,
                        (row + 1) * MOSAIC_CELL_LENGTH, mosaicPaint);
                mosaics[row][col] = true;
            }
        }
    }

    /**
     * 绘制普通线条
     *
     * @param canvas
     * @param lineinfo
     */
    protected void drawNormalLine(Canvas canvas, LineInfo lineinfo) {
        if (lineinfo.getPointList().size() <= 1) {
            return;
        }
        for (int i = 0; i < lineinfo.getPointList().size() - 1; i++) {
            PointF startPoint = lineinfo.getPointList().get(i);
            PointF endPoint = lineinfo.getPointList().get(i + 1);
            canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, normalPaint);
        }
    }

    /**
     * 删除最后添加的线
     */
    public void withDrawLastLine() {
        switch (currentLineType) {
            case Rect:
                if (mListRect.size() > 0) {
                    mListRect.remove(mListRect.size() - 1);
                    invalidate();
                }
                return;
        }
        if (lineList.size() > 0) {
            lineList.remove(lineList.size() - 1);
            invalidate();
        }
    }

    /**
     * 判断是否可以继续撤销
     *
     * @return
     */
    public boolean canStillWithdraw() {
        switch (currentLineType) {
            case Rect:
                return mListRect.size() > 0;
            case MosaicLine:
            case NormalLine:
                return lineList.size() > 0;
            case None:
                return false;
            default:
                return lineList.size() > 0;
        }
    }


    /**
     * 设置线条类型
     *
     * @param type
     */
    public void setLineType(LineInfo.LineType type) {
        currentLineType = type;
    }


    /**
     * 清除
     */
    public void clean() {
        if (lineList == null) {
            return;
        }
        lineList.clear();
        postInvalidate();
    }

    /**
     * 编辑标记
     */
    protected boolean isInEdit;

    /**
     * 设置是否处于编辑状态
     */
    public void setEditStatus(boolean argEditStatus) {
        isInEdit = argEditStatus;
    }
}
