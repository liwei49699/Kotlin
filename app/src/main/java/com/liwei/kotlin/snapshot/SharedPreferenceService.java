
package com.liwei.kotlin.snapshot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.liwei.kotlin.BaseApplication;

import java.util.Set;



/**
 * ****************************************************************
 * 文件名称  : SharedPreferenceService.java
 * 作         者  : jwang.Ranger
 * 创建时间  : 2014-7-2 下午8:02:13
 * 文件描述  : xml文件存储管理器
 * 修改历史  : 2014-7-2 1.00 初始版本
 *****************************************************************
 */
@SuppressLint("CommitPrefEdits")
public class SharedPreferenceService {

	SharedPreferences sp;
	SharedPreferences.Editor editor;
	private Context mContext = null;

	private static SharedPreferenceService manager = null;

	@Deprecated
	private SharedPreferenceService(Context context) {
		mContext = context.getApplicationContext();
		sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		editor = sp.edit();
	}

	@Deprecated
	public static SharedPreferenceService getInstance(Context context) {
		if (manager == null) {
			manager = new SharedPreferenceService(context);
		}
		return manager;
	}


	private SharedPreferenceService() {
		sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
		editor = sp.edit();
	}

	public SharedPreferences getSp() {
		return sp;
	}

	public static SharedPreferenceService getInstance() {
		if (manager == null) {
			manager = new SharedPreferenceService();
		}
		return manager;
	}


	public void put(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public void put(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public void put(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	public void put(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * Note that you must not modify the set instance returned by this call. The consistency of the stored data is not guaranteed if you do, nor is your ability to modify the instance at all.
	 * get和putStringSet的object不能是同一个，不能在get之后，进行更改，然后又put进去，这样是无法更改的
	 * @param key
	 * @param set
	 */
	public void put(String key, Set<String> set){
		editor.putStringSet(key, set);
		editor.commit();
	}

	public String get(String key, String defValue) {
		return sp.getString(key, defValue);
	}

	public int get(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	public long get(String key, long defValue) {
		return sp.getLong(key, defValue);
	}

	public boolean get(String key, boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	public Set<String> get(String key, Set<String> defSet){
		return sp.getStringSet(key, defSet);
	}

	public void put(CommonValue.StyleType key, CommonValue.StyleType styleType){
		editor.putString(key.toString(), styleType.toString());
		editor.commit();
	}

	public CommonValue.StyleType get(CommonValue.StyleType key, CommonValue.StyleType styleType) {
		String str = sp.getString(key.toString(), styleType.toString());
		if(CommonValue.StyleType.STYLE_BLACK.toString().equals(str)){
			return CommonValue.StyleType.STYLE_BLACK;
		}
		return CommonValue.StyleType.STYLE_WHITE;
	}

	public void remove(String key) {
		editor.remove(key);
		editor.commit();
	}
}
