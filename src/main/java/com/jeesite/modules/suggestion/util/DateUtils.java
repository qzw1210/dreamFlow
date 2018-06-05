package com.jeesite.modules.suggestion.util;

import java.util.Date;

public class DateUtils {
	public static String getDatePoor(Date endDate, Date nowDate) {
		StringBuffer stringBuffer = new StringBuffer();
		if(endDate==null&&nowDate==null||endDate.getTime()<nowDate.getTime()){
			stringBuffer.append("日期格式转换错误");
			return stringBuffer.toString();
		}
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    // long ns = 1000;
	    // 获得两个时间的毫秒时间差异
	    long diff = endDate.getTime() - nowDate.getTime();
	    // 计算差多少天
	    long day = diff / nd;
	    // 计算差多少小时
	    long hour = diff % nd / nh;
	    // 计算差多少分钟
	    long min = diff % nd % nh / nm;
	    // 计算差多少秒//输出结果
	    // long sec = diff % nd % nh % nm / ns;
	    if(day!=0){
	    	stringBuffer.append(day + "天");
	    }
	    if(day!=0||hour!=0){
	    	stringBuffer.append(hour + "小时");
	    }
	    stringBuffer.append(min + "分钟");
	    return stringBuffer.toString();
	}
}
