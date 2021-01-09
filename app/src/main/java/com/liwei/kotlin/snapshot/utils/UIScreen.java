package com.liwei.kotlin.snapshot.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.Display;

public class UIScreen {
	private static UIScreen _mainScreen;
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static int statusBarHeight = 0;
	public static int statusBarHeightEx = 0;
	public static float density ;
	public static float scaleDensity ;
	public static final int PORTRAIT = 0;//竖屏
	public static final int LANDSCAPE = 1;//横屏

	private static int screenWidthDp;
	private static int screenHeightDp;
	/**
	 * 构造一个和设备屏幕一样大的UIScreen实例
	 */
	private UIScreen(Activity baseActivity) {
		this(baseActivity, false);
	}

	private UIScreen(Activity baseActivity, boolean isMulti) {
		Display display = baseActivity.getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		statusBarHeight = getStatusBarHeight(baseActivity.getResources());
		density = baseActivity.getApplicationContext().getResources().getDisplayMetrics().density;
		scaleDensity = baseActivity.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
		if (isMulti) {
			screenWidth = display.getWidth() / 2;
		}
		display = null;
		saveScreenDp(baseActivity);
	}

	/**
	 * 构造一个和设备屏幕一样大的UIScreen实例
	 * @param orientation 横屏或者竖屏
	 */
	private UIScreen(Activity baseActivity, int orientation) {
		Display display = baseActivity.getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		if(orientation == PORTRAIT){
			if(screenWidth > screenHeight){
				int temp = screenWidth;
				screenWidth = screenHeight;
				screenHeight = temp;
			}
		}else if(orientation == LANDSCAPE){
			if(screenWidth < screenHeight){
				int temp = screenWidth;
				screenWidth = screenHeight;
				screenHeight = temp;
			}
		}
		density = baseActivity.getApplicationContext().getResources().getDisplayMetrics().density;
		display = null;
		scaleDensity = baseActivity.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
//		UIFontStyle.largeSize = screenHeight / 36;
//		UIFontStyle.bigSize = screenHeight / 40;
//		UIFontStyle.defaultSize = screenHeight / 50;
//		UIFontStyle.smallSize = screenHeight / 57;
//		UIFontStyle.smallertSize = screenHeight / 66;
		saveScreenDp(baseActivity);
	}

	private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

	public static int getStatusBarHeight(Resources res) {
		int result = 0;
		if (res != null) {
			int resourceId = res.getIdentifier(STATUS_BAR_HEIGHT_RES_NAME, "dimen", "android");
			if (resourceId > 0) {
				result = res.getDimensionPixelSize(resourceId);
			}
		}
		return result;
	}


	/**
	 * 获取主屏幕的单根实例
	 *
	 * @return 返回主屏幕的单根实例
	 */
	public static UIScreen getMainScreen(Activity baseActivity) {
		if (_mainScreen == null) {
			_mainScreen = new UIScreen(baseActivity);
		}
		return _mainScreen;
	}

	/**
	 * 重置主屏幕实例
	 *
	 * @return 主屏幕的单根实例
	 */
	public static UIScreen resetMainScreen(Activity baseActivity) {
		_mainScreen = new UIScreen(baseActivity);
		return _mainScreen;
	}

	public static UIScreen resetMultiMainScreen(Activity baseActivity) {
		_mainScreen = new UIScreen(baseActivity, true);
		return _mainScreen;
	}

	public static int getWidthByDensity() {
		return (int)(screenWidth/density);
	}

	/**
	 * 重置主屏幕实例
	 *
	 * @return 主屏幕的单根实例
	 */
	public static UIScreen resetMainScreen(Activity baseActivity, int orientation) {
		_mainScreen = new UIScreen(baseActivity,orientation);
		return _mainScreen;
	}

	/**
	 * 检查屏幕大小是否发生变化
	 *
	 * @return boolean 是否发生变化
	 */
	public static void checkScreenChange(Activity baseActivity, Configuration newConfig) {
		if (newConfig != null && baseActivity != null) {
			int newWidthDp = newConfig.screenWidthDp;
			int newHeightDp = newConfig.screenHeightDp;
			if (newWidthDp != screenWidthDp || newHeightDp != screenHeightDp) {
				resetMainScreen(baseActivity);
			}
		}
	}

	private static void saveScreenDp(Activity baseActivity) {
		Configuration config = baseActivity.getResources().getConfiguration();
		screenWidthDp = config.screenWidthDp;
		screenHeightDp = config.screenHeightDp;
	}

	/**
	 * 判断屏幕是否是展开状态
	 *
	 * @return true: 展开 false不展开
	 */
	public static boolean isScreenExpand() {
		return Math.abs(screenHeightDp - screenWidthDp) < 100;
	}
}
