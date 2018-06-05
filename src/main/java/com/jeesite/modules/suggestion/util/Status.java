package com.jeesite.modules.suggestion.util;

public enum Status {

	 UNCOMMITED(1),  //未提交
	 UNAUDIT(2),     //未审核
	 REJECT(3);		//拒绝
	
	 private int value;
	
	 private Status(int value) {
	    this.value = value;
	 }
	 
	 public int getValue() {
        return value;
     }
}
