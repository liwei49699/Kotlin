package com.liwei.kotlin.snapshot;

public class CommonValue {
	public final static String preCommandID = "preCommandID";
	
	public static StyleType styleType = StyleType.STYLE_BLACK; // 风格   0 - 黑色  1 - 白色
	
	/**
	 * 这个值不要随便更改
	 */
	public static StyleType current_style = StyleType.STYLE_BLACK;

	public static ColorType colorType = ColorType.RED_RISE_GREEN_DECLINE;

	public static ColorType current_color_style = ColorType.RED_RISE_GREEN_DECLINE;
	
	/**
	 * ****************************************************************
	 * 文件名称  : StyleType.java
	 * 作         者  : jwang.Ranger
	 * 创建时间  : 2014-7-15 上午10:07:47
	 * 文件描述  : 黑白风格的常量
	 * 修改历史  : 2014-7-15 1.00 初始版本
	 *****************************************************************
	 */
	public enum StyleType{
		//黑色
		STYLE_BLACK,
		//白色
		STYLE_WHITE,
		//金色
		STYLE_GOLDEN,
		//存储的key
		SKIN_KEY,
		SKIN_KEY1;
	}

	public enum ColorType {
		//红涨绿跌
		RED_RISE_GREEN_DECLINE,
		//绿涨红跌
		GREEN_RISE_RED_DECLINE,
		//存储的key
		COLOR_KEY;
	}



}
