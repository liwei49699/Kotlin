package com.liwei.kotlin.snapshot.widgets;

/**
 * # **********************************************************************************************
 * # ClassName:      OnActionPerformedListener
 * # Description:    动作完成的listener
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/9/6    9:41
 * # Modifications:  initial
 * # **********************************************************************************************
 * @param <PARAM>
 */
public interface OnActionPerformedListener<PARAM> {

    void onActionPerformed(PARAM arguments);
}