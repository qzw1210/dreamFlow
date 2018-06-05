package com.jeesite.modules.suggestion.util;

public enum Stage {

	 RAISE(1),  //提交
	 AUDIT(2),	//审核
	 DISPATCH(3), //指派
	 EXECUTE(4),  //筹备计划
	 CONFIRM(5),  //等待确认
	 FINIESH(6);	//完成
	 
	 private int value;
	
	 private Stage(int value) {
	    this.value = value;
	 }
	 
	 public int getValue() {
       return value;
    }
}