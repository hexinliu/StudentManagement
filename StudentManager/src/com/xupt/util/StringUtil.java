package com.xupt.util;

/**
 * @author 馨
 *String类的共用操作
 */
public class StringUtil {
	public static boolean isEmpty(String str){
		if (str == null || "".equals(str))
			return true;
		return false;
	}
}
