package com.jeesite.modules.suggestion.entity;

import java.util.Date;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;

@Table(name="sg_question", alias="a", columns={
		@Column(includeEntity=DataEntity.class),
		@Column(name="question_code", attrName="questionCode", label="问题编码", isPK=true),
		@Column(name="question_type", attrName="questionType", label="问题分类"),
		@Column(name="bz_status", attrName="bzStatus", label="业务状态"),
		@Column(name="stage", attrName="stage", label="阶段"),
		@Column(name="user_code", attrName="user.userCode", label="建议人"),
		@Column(name="manager", attrName="manager.userCode", label="建议人经理"),
		@Column(name="department_code", attrName="userOffice.officeCode", 	label="建议人部门编码", isQuery=false),
		@Column(name="question_dept_code", attrName="office.officeCode", 	label="问题部门编码", isQuery=false),
		@Column(name="question_dept_name", attrName="office.officeName", 	label="问题部门名称", isQuery=false),
		@Column(name="quest_user", attrName="questUser.userCode", label="问题负责人"),
		@Column(name="quest_manager", attrName="questManager.userCode", label="问题部门负责人"),
		@Column(name="plan", attrName="plan", label="计划"),
		@Column(name="reject_msg", attrName="rejectMsg", label="驳回信息"),
		@Column(name="question_title", attrName="questionTitle", label="问题标题"),
		@Column(name="commit_time", attrName="commitTime", label="提交时间"),
		@Column(name="examine_pass_time", attrName="examinePassTime", label="审核通过时间"),
		@Column(name="commit_solution_time", attrName="commitSolutionTime", label="提交解决方案时间"),
		@Column(name="confirm_time", attrName="confirmTime", label="确认完成时间"),
		@Column(name="dept_confirm_time", attrName="deptConfirmTime", label="建议提出部门确认完成时间"),
		@Column(name="finish_time", attrName="finishTime", label="完成时间"),
	},
	// 支持联合查询，如左右连接查询，支持设置查询自定义关联表的返回字段列
    joinTable={
        @JoinTable(type=Type.LEFT_JOIN, entity=Office.class, alias="o", 
            on="o.office_code = a.question_dept_code",attrName="office",
            columns={@Column(includeEntity=Office.class)}),
        @JoinTable(type=Type.LEFT_JOIN, entity=Office.class, alias="uo", 
        on="uo.office_code = a.department_code",attrName="userOffice",
        columns={@Column(includeEntity=Office.class)}),
        @JoinTable(type=Type.LEFT_JOIN, entity=User.class, alias="u", 
            on="u.user_code = a.user_code",attrName="user",
            columns={@Column(includeEntity=User.class)}),
        @JoinTable(type=Type.LEFT_JOIN, entity=User.class, alias="qu", 
        on="qu.user_code = a.quest_user",attrName="questUser",
        columns={@Column(includeEntity=User.class)}),
    }, extWhereKeys="dsf",
    orderBy="a.update_date DESC"
)
public class Question extends DataEntity<Question> {
	
	private static final long serialVersionUID = 1L;
	private String questionCode;		// 问题编码
	private String questionType;		// 问题分类
	private User user;         //建议人
	private Office userOffice;   //建议人所属部门
	private User manager;       //建议人经理
	private User questManager;  //问题部门经理
	private User  questUser;   //问题处理者
	private String status;		// 状态
	private Integer stage;		// 阶段
	private Office office;		// 问题部门
	private String plan;       //计划
	private Integer bzStatus;		// 业务状态
	private String rejectMsg;    //驳回信息
	private String questionTitle;    //驳回信息
	private Date commitTime;       //提交时间
	private Date examinePassTime;   //审核通过时间
	private Date commitSolutionTime;   //提交解决方案时间
	private Date confirmTime;		//确认完成时间
	private Date deptConfirmTime;   //建议提出部门确认时间
	private Date finishTime;    //完成时间
	public Question() {
		this(null);
	}

	public Question(String id){
		super(id);
	}

	public String getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
 
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getBzStatus() {
		return bzStatus;
	}

	public void setBzStatus(Integer bzStatus) {
		this.bzStatus = bzStatus;
	}

	public Office getUserOffice() {
		return userOffice;
	}

	public User getManager() {
		return manager;
	}

	public User getQuestManager() {
		return questManager;
	}

	public User getQuestUser() {
		return questUser;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public String getPlan() {
		return plan;
	}

	public void setUserOffice(Office userOffice) {
		this.userOffice = userOffice;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public void setQuestManager(User questManager) {
		this.questManager = questManager;
	}

	public void setQuestUser(User questUser) {
		this.questUser = questUser;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}
	
	public String getRejectMsg() {
		return rejectMsg;
	}

	public void setRejectMsg(String rejectMsg) {
		this.rejectMsg = rejectMsg;
	}

	
	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Date getCommitTime() {
		return commitTime;
	}

	public Date getExaminePassTime() {
		return examinePassTime;
	}

	public Date getCommitSolutionTime() {
		return commitSolutionTime;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public Date getDeptConfirmTime() {
		return deptConfirmTime;
	}

	public void setExaminePassTime(Date examinePassTime) {
		this.examinePassTime = examinePassTime;
	}

	public void setCommitSolutionTime(Date commitSolutionTime) {
		this.commitSolutionTime = commitSolutionTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public void setDeptConfirmTime(Date deptConfirmTime) {
		this.deptConfirmTime = deptConfirmTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

}