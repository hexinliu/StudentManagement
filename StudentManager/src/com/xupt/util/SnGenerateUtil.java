package com.xupt.util;

import java.util.Date;



public class SnGenerateUtil {
	public static String generateSn(int clazzId){
		String sn = "";
		//sn = DateFormatUtil.getFormatDate(new Date(), "yyyyMMdd");
		sn = "S" + clazzId + System.currentTimeMillis();
		return sn;
	}
	
	public static String generateTeacherSn(int clazzId){
		String sn = "";
		//sn = DateFormatUtil.getFormatDate(new Date(), "yyyyMMdd");
		sn = "T" + clazzId + System.currentTimeMillis();
		return sn;
	}
}
