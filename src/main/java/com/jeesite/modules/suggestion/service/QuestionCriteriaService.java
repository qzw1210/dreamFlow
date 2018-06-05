package com.jeesite.modules.suggestion.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.suggestion.dao.QuestionDao;
import com.jeesite.modules.suggestion.entity.Question;
import com.jeesite.modules.suggestion.entity.QuestionTime;
import com.jeesite.modules.suggestion.entity.QuestionUser;
import com.jeesite.modules.suggestion.util.DateUtils;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.EmployeeService;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.DictUtils;
import com.jeesite.modules.sys.utils.UserUtils;


/**
 * 问题管理Service
 */
@Service
@Transactional(readOnly=true)
public class QuestionCriteriaService extends CrudService<QuestionDao, Question> {
	
	@Autowired
	QuestionService questionService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private OfficeService officeService;
	
	
	@Transactional(readOnly=false)
	public Page<QuestionUser> createQuestionUserPage(HttpServletRequest request,HttpServletResponse response,
			Question question) {
		Page<Employee> page = findEmployeePage(new Page<Employee>(request,response),question);
		Page<QuestionUser> questionUserPage =createQuestionUserPage(page, question);
		return questionUserPage;
	}
	public Page<QuestionUser> createQuestionUserPageStatsBySuggestUser(HttpServletRequest request,
			HttpServletResponse response, Question question) {
		Page<Employee> page = findEmployeePage(new Page<Employee>(request,response),question);
		Page<QuestionUser> questionUserPage =createQuestionUserPage(page, question);
		return questionUserPage;
	}

	public List<QuestionUser> createQuestionUserListStatsByQuestOffice(HttpServletRequest request,
			HttpServletResponse response, Question question) {
		List<QuestionUser> questionUserList = new ArrayList();
		List<Office>offices = officeService.findList(new Office());
		for(Office office : offices){
			QuestionUser questionUser = new QuestionUser();
			question.setOffice(office);
			Long count = questionService.findCount(question);
			questionUser.setCount(count);
			questionUser.setOffice(office);
			questionUserList.add(questionUser);
		}
		return questionUserList;
	}
	
	/**
	 * 获取所有员工分页
	 */
	public Page<Employee> findEmployeePage(Page<Employee> page,Question question){
//		Office office = new Office();
//		String officeCode = queryCriteria.getOfficeCode();
//		if(officeCode!=null)office.setOfficeCode(officeCode);
		Employee employee = new Employee();
		employee.setOffice(question.getOffice());
		page = employeeService.findPage(page, employee);
		return page;
	}
	/**
	 *  根据Employee分页创建QuestionUser分页
	 */
	public Page<QuestionUser> createQuestionUserPage(Page<Employee> page,Question question){
		List<Employee> emps =null;
		List<QuestionUser> questionUsers = new ArrayList<>(); 
		Page<QuestionUser> questionUserPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
		if(page==null)emps = new ArrayList<Employee>();
		else emps = page.getList();
		for(Employee employee:emps){
			String userCode = employee.getEmpCode();
			User user = UserUtils.get(userCode);
			question.setUser(user);
			Office office = employee.getOffice();
			Long count = questionService.findCount(question);
			QuestionUser questionUser = new QuestionUser(user, office, count);
			questionUsers.add(questionUser);
			question.setUser(null);
		}
		questionUserPage.setList(questionUsers);
		return questionUserPage;
	}
	
	public Page<Question> findQuestionPage(HttpServletRequest request, HttpServletResponse response,
			Question question) {
		Page<Question> questionPage = questionService.findPage(new Page<Question>(request, response), question);
		return questionPage;
	}
	public List<QuestionUser> createQuestionUserListStatsByClassify(HttpServletRequest request,
			HttpServletResponse response, Question question) {
		List<QuestionUser> questionUsers = new ArrayList<>();
		List<DictData> list = DictUtils.getDictList("sg_question_type");
		for(DictData dictData:list){
			String questionType = dictData.getDictValue();
			question.setQuestionType(questionType);
			Long count = questionService.findCount(question);
			QuestionUser questionUser = new QuestionUser(count, dictData);
			questionUsers.add(questionUser);
			question.setQuestionType(null);
		}
		return questionUsers;
	}
	
	/**
	 *   建议阶段处理用时分析
	 */
	public Page<QuestionUser> createQuestionPageStatsByTAOPSP(HttpServletRequest request,HttpServletResponse response, Question question) {
		Page<Question> questionPage = questionService.adminFindPage(new Page<Question>(request, response), question);
		Page<QuestionUser> page = new Page<>(questionPage.getPageNo(), questionPage.getPageSize(),questionPage.getCount());
		List<QuestionUser> questionUserList = new ArrayList<>();
		List<Question> questionList = questionPage.getList();
		for(Question newQuestion : questionList){
			String deptConfirmTime = DateUtils.getDatePoor(newQuestion.getDeptConfirmTime(), newQuestion.getConfirmTime());
			String  confirmTime =DateUtils.getDatePoor(newQuestion.getConfirmTime(), newQuestion.getCommitSolutionTime());
			String commitSolutionTime = DateUtils.getDatePoor(newQuestion.getCommitSolutionTime(), newQuestion.getExaminePassTime());
			String examinePassTime =DateUtils.getDatePoor(newQuestion.getExaminePassTime(), newQuestion.getCommitTime());
			String commitTime =DateUtils.getDatePoor(newQuestion.getCommitTime(), newQuestion.getCreateDate());
			QuestionTime questionTime = new QuestionTime(commitTime, examinePassTime, commitSolutionTime, confirmTime, deptConfirmTime);
			questionTime.setQuestionTitle(newQuestion.getQuestionTitle());
			questionTime.setQuestionType(newQuestion.getQuestionType());
			QuestionUser questionUser = new QuestionUser(questionTime);
			questionUserList.add(questionUser);
		}
		page.setList(questionUserList);
		return page;
	}
	public Page<QuestionUser> createQuestionPageStatsBySPTAL(HttpServletRequest request, HttpServletResponse response,
			Question question) {
		Page<Question> questionPage = questionService.adminFindPage(new Page<Question>(request, response), question);
		Page<QuestionUser> page = new Page<>(questionPage.getPageNo(), questionPage.getPageSize(),questionPage.getCount());
		List<QuestionUser> questionUserList = new ArrayList<>();
		List<Question> questionList = questionPage.getList();
		for(Question newQuestion : questionList){
			//执行时间
			String  confirmTime =DateUtils.getDatePoor(newQuestion.getConfirmTime(), newQuestion.getCommitSolutionTime());
			//等待指派时间
			String commitSolutionTime = DateUtils.getDatePoor(newQuestion.getCommitSolutionTime(), newQuestion.getExaminePassTime());
			QuestionTime questionTime = new QuestionTime(commitSolutionTime, confirmTime);
			questionTime.setQuestionTitle(newQuestion.getQuestionTitle());
			questionTime.setQuestionType(newQuestion.getQuestionType());
			questionTime.setOfficeName(newQuestion.getOffice().getOfficeName());
			questionTime.setQuestUserName(newQuestion.getUser().getUserName());
			QuestionUser questionUser = new QuestionUser(questionTime);
			questionUserList.add(questionUser);
		}
		page.setList(questionUserList);
		return page;
	}
}