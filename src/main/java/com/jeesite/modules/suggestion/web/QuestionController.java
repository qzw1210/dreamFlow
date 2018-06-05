package com.jeesite.modules.suggestion.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.suggestion.entity.Question;
import com.jeesite.modules.suggestion.service.QuestionService;
import com.jeesite.modules.suggestion.util.Stage;
import com.jeesite.modules.suggestion.util.Status;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.EmployeePost;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.EmployeeService;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.sys.web.user.EmpUserController;

@Controller
@RequestMapping(value = "${adminPath}/suggestion/question")
public class QuestionController extends BaseController{
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private EmpUserController empUserController;
	
	@ModelAttribute
	public Question get(String questionCode, boolean isNewRecord) {
		return questionService.get(questionCode, isNewRecord);
	}
	
	@RequestMapping(value = "list")
	public String list(Question question, Model model) {
		return "modules/suggestion/questionList";
	}
	
	@RequestMapping(value = {"listData"})
	@ResponseBody
	public Page<Question> listData(Question question, HttpServletRequest request, HttpServletResponse response) {
		Page<Question> page = questionService.findPage(new Page<Question>(request, response), question); 
		return page;
	}
	
	@RequestMapping(value = "form")
	public String form(Question question, Model model) {
		model.addAttribute("question", question);
		return "modules/suggestion/questionForm";
	}
	
	@RequestMapping(value = "show")
	public String show(Question question, Model model) {
		model.addAttribute("question", question);
		return "modules/suggestion/questionShow";
	}

	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Question question, Model model) {
		if(question.getIsNewRecord()) {
			User user = UserUtils.getUser();
			Employee employee = (Employee)user.getRefObj();
			if(employee==null)
				return renderResult(Global.FALSE, text("保存失败，当前用户没有机构部门信息"));
			Office userOffice =  employee.getOffice();
			if(userOffice==null)
				return renderResult(Global.FALSE, text("保存失败，当前用户没有机构部门信息"));
			question.setUser(user);
			question.setUserOffice(userOffice);
			question.setStage(Stage.RAISE.getValue());
			question.setBzStatus(Status.UNCOMMITED.getValue());
		}
		questionService.save(question);
		return renderResult(Global.TRUE, text("保存成功"));
	}
	
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Question question) {
		questionService.delete(question);
		return renderResult(Global.TRUE, text("删除成功"));
	}
	
	/**
	 * 提交->审核
	 */
	@PostMapping(value = "submit")
	@ResponseBody
	public String submit(Question question,String taskId) {
		String myLeader=findMyLeader(question.getUserOffice());
		if(myLeader==null)
			return renderResult(Global.FALSE, text("提交失败，当前用户没有部门负责人"));
		questionService.submit(question, taskId,myLeader);
		return renderResult(Global.TRUE, text("提交成功"));
	}
	
	/**
	 * 审核通过->问题部门接受
	 */
	@PostMapping(value = "auditPass")
	@ResponseBody
	public String auditPass(Question question,String taskId) {
		String myLeader=findMyLeader(question.getOffice());
		if(myLeader==null)
			return renderResult(Global.FALSE, text("提交失败，部门{0}没有负责人",question.getOffice().getFullName()));
		question.setStage(Stage.DISPATCH.getValue());
		question.setQuestManager(new User(myLeader));
		questionService.next(question, taskId,myLeader,1,"审核通过");
		return renderResult(Global.TRUE, text("提交成功"));
	}
	
	/**
	 * 审核驳回->员工提出问题
	 */
	@PostMapping(value = "auditReject")
	@ResponseBody
	public String auditReject(Question question,String taskId) {
		String rejectMasg = question.getRejectMsg();
		if(rejectMasg==null||rejectMasg ==""){
			return renderResult(Global.FALSE, text("请填写驳回理由"));
		}
		question.setStage(Stage.RAISE.getValue());
		question.setBzStatus(Status.REJECT.getValue());
		questionService.next(question, taskId,
				question.getUser().getUserCode(),2,"审核驳回");	
		return renderResult(Global.TRUE, text("提交成功"));
	}
	
	/**
	 * 接收->处理人
	 */
	@PostMapping(value = "receivePass")
	@ResponseBody
	public String receivePass(Question question,String taskId) {
		question.setStage(Stage.EXECUTE.getValue());
		questionService.next(question, taskId,
				question.getQuestUser().getUserCode(),1,"接收并指派");	
		return renderResult(Global.TRUE, text("提交成功"));
	}
	
	/**
	 * 拒收->提出部门审核
	 */
	@PostMapping(value = "receiveReject")
	@ResponseBody
	public String receiveReject(Question question,String taskId) {
		question.setStage(Stage.AUDIT.getValue());
		questionService.next(question, taskId,
				findMyLeader( question.getUserOffice()),2,"拒收");
		return renderResult(Global.TRUE, text("提交成功"));
	}
	
	/**
	 * 输入方案->处理人确认
	 */
	@PostMapping(value = "execute")
	@ResponseBody
	public String execute(Question question,String taskId) {
		questionService.next(question, taskId,question.getQuestUser().getUserCode()
				,0,"输入方案");
		return renderResult(Global.TRUE, text("提交成功"));
	}
	
	/**
	 * 处理人确认->提出部门确认
	 */
	@PostMapping(value = "confirm")
	@ResponseBody
	public String confirm(Question question,String taskId) {
		String myLeader=findMyLeader(question.getUserOffice());
		if(myLeader==null)
			return renderResult(Global.FALSE, text("提交失败，部门{0}没有负责人",question.getUserOffice().getFullName()));
		question.setStage(Stage.CONFIRM.getValue());
		questionService.next(question, taskId,myLeader,0,"确认");
		return renderResult(Global.TRUE, text("提交成功"));
	}
	
	/**
	 * 提出部门确认->结束
	 */
	@PostMapping(value = "deptConfirmPass")
	@ResponseBody
	public String deptConfirmPass(Question question,String taskId) {
		question.setStage(Stage.FINIESH.getValue());
		questionService.next(question, taskId,null,1,"认可");
		return renderResult(Global.TRUE, text("提交成功"));
	}
	
	/**
	 * 提出部门确认->输入方案
	 */
	@PostMapping(value = "deptConfirmReject")
	@ResponseBody
	public String deptConfirmReject(Question question,String taskId) {
		question.setStage(Stage.EXECUTE.getValue());
		questionService.next(question, taskId,question.getQuestUser().getUserCode()
				,2,"不认可");
		return renderResult(Global.TRUE, text("提交成功"));
 
	}
	
	
	/**
	 * 查找部门负责人（部门经理）
	 * @return
	 */
	private String findMyLeader(Office office) {
		Employee employee=new Employee();
		employee.setOffice(office);
		List<Employee> employees=employeeService.findList(employee);
		for(Employee emp:employees) {
			for(EmployeePost employeePost:employeeService.findEmployeePostList(emp)) {
				if("dept".equals(employeePost.getPostCode())) {
					return emp.getEmpCode();
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取问题部门的普通员工
	 * @param excludeCode		排除的ID
	 * @param parentCode	上级Code
	 * @param isAll			是否显示所有机构（true：不进行权限过滤）
	 * @param officeTypes	机构类型（1：公司, 2：部门, 3：小组, 4：其它）
	 * @param companyCode	仅查询公司下的机构
	 * @param isShowCode	是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @param isShowFullName 是否显示全机构名称
	 * @param isLoadUser	是否加载机构下的用户
	 * @param postCode		机构下的用户过滤岗位
	 * @param roleCode		机构下的用户过滤角色
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String parentCode, Boolean isAll,
			String officeTypes, String companyCode, String officeCode, String isShowFullName,
			Boolean isLoadUser, String postCode, String roleCode, String ctrlPermi) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		Office where = new Office();
		where.setStatus(Office.STATUS_NORMAL);
		where.setCompanyCode(companyCode);
		where.setOfficeCode(officeCode);
		List<Office> list = officeService.findList(where);
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			 
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentCode());
			String name = e.getOfficeName();
			if ("true".equals(isShowFullName) || "1".equals(isShowFullName)){
				name = e.getFullName();
			}
			map.put("name", StringUtils.getTreeNodeName("false", e.getViewCode(), name));
			map.put("title", e.getFullName());
			// 一次性后台加载用户，提高性能(推荐方法)
			if (isLoadUser != null && isLoadUser) {
				map.put("isParent", true);
				List<Map<String, Object>> userList;
				userList = empUserController.treeData("u_", e.getOfficeCode(), e.getOfficeCode(), 
						companyCode, postCode, roleCode, isAll, "false");
				mapList.addAll(userList);
			}
			mapList.add(map);
		}
		return mapList;
	}
}
