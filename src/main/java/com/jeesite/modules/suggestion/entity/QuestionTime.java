package com.jeesite.modules.suggestion.entity;

public class QuestionTime {
	private String commitTime;       //提交时间
	private String examinePassTime;   //审核通过时间
	private String commitSolutionTime;   //等待指派用时
	private String confirmTime;		//执行阶段用时
	private String deptConfirmTime;   //等待认可用时
	
	private String questionType;
	private String questionTitle;
	private String officeName ;
	private String  questUserName ;
	
	public String getQuestionType() {
		return questionType;
	}
	public String getQuestionTitle() {
		return questionTitle;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}
	public String getCommitTime() {
		return commitTime;
	}
	public String getExaminePassTime() {
		return examinePassTime;
	}
	public String getCommitSolutionTime() {
		return commitSolutionTime;
	}
	public String getConfirmTime() {
		return confirmTime;
	}
	public String getDeptConfirmTime() {
		return deptConfirmTime;
	}
	public void setCommitTime(String commitTime) {
		this.commitTime = commitTime;
	}
	public void setExaminePassTime(String examinePassTime) {
		this.examinePassTime = examinePassTime;
	}
	public void setCommitSolutionTime(String commitSolutionTime) {
		this.commitSolutionTime = commitSolutionTime;
	}
	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}
	public void setDeptConfirmTime(String deptConfirmTime) {
		this.deptConfirmTime = deptConfirmTime;
	}
	public String getOfficeName() {
		return officeName;
	}
	public String getQuestUserName() {
		return questUserName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public void setQuestUserName(String questUserName) {
		this.questUserName = questUserName;
	}
	public QuestionTime(String commitTime, String examinePassTime, String commitSolutionTime, String confirmTime,
			String deptConfirmTime) {
		super();
		this.commitTime = commitTime;
		this.examinePassTime = examinePassTime;
		this.commitSolutionTime = commitSolutionTime;
		this.confirmTime = confirmTime;
		this.deptConfirmTime = deptConfirmTime;
	}
	
	public QuestionTime( String commitSolutionTime, String confirmTime) {
		super();
		this.commitSolutionTime = commitSolutionTime;
		this.confirmTime = confirmTime;
	}
	public QuestionTime() {
	}
	
}
