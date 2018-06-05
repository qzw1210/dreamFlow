package com.jeesite.modules.suggestion.entity;

import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;

public class QuestionUser {
	private User user;
	private Office office;
	private Long count;
	private DictData questionDictData;
	private QuestionTime questionTime;
	public User getUser() {
		return user;
	}
	public Office getOffice() {
		return office;
	}
	public Long getCount() {
		return count;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public DictData getQuestionDictData() {
		return questionDictData;
	}
	public void setQuestionDictData(DictData questionDictData) {
		this.questionDictData = questionDictData;
	}
	public QuestionTime getQuestionTime() {
		return questionTime;
	}
	public void setQuestionTime(QuestionTime questionTime) {
		this.questionTime = questionTime;
	}
	public QuestionUser(QuestionTime questionTime) {
		super();
		this.questionTime = questionTime;
	}
	public QuestionUser(User user, Office office, Long count) {
		super();
		this.user = user;
		this.office = office;
		this.count = count;
	}
	
	public QuestionUser(Long count, DictData questionDictData) {
		super();
		this.count = count;
		this.questionDictData = questionDictData;
	}
	public QuestionUser() {
		// TODO Auto-generated constructor stub
	}
}
