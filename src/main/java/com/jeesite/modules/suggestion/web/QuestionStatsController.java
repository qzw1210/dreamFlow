package com.jeesite.modules.suggestion.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.suggestion.entity.Question;
import com.jeesite.modules.suggestion.entity.QuestionUser;
import com.jeesite.modules.suggestion.service.QuestionCriteriaService;
import com.jeesite.modules.suggestion.service.QuestionService;

@Controller
@RequestMapping(value = "${adminPath}/suggestion/questionStats")
public class QuestionStatsController extends BaseController{
	@Autowired
	private QuestionCriteriaService questionCriteriaService;
	@Autowired
	private QuestionService questionService;
	
	@RequestMapping(value = "suggestUser")
	public String list(Question question, Model model) {
		return "modules/suggestionStats/suggestUserList";
	}
	
	@RequestMapping(value = {"suggestUserListData"})
	@ResponseBody
	public Page<QuestionUser> suggestUserListData(Question question, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionUser> page = questionCriteriaService.createQuestionUserPageStatsBySuggestUser(request,response,question); 
		return page;
	}
	
	@RequestMapping(value = {"suggestUserSuggestions"})
	public String  suggestUserDetail(Question question,Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("question", question);
		return "modules/suggestionStats/suggestUserSuggestions";
	}
	@RequestMapping(value = {"suggestUserSuggestionsData"})
	@ResponseBody
	public Page<Question> suggestUserSuggestionsData(Question question, HttpServletRequest request, HttpServletResponse response) {
		Page<Question> page = questionService.adminFindPage(new Page<Question>(request,response), question);
		return page;
	}
	
	/**
	 *  按部门统计
	 */
	@RequestMapping(value = "questOffice")
	public String questOffice(Question question, Model model) {
		model.addAttribute("question", question);
		return "modules/suggestionStats/questOfficeList";
	}
	
	@RequestMapping(value = {"questOfficeListData"})
	@ResponseBody
	public List<QuestionUser> questOfficeListData(Question question, HttpServletRequest request, HttpServletResponse response) {
		List<QuestionUser> list = questionCriteriaService.createQuestionUserListStatsByQuestOffice(request,response,question); 
		return list;
	}
	
	@RequestMapping(value = {"questOfficeQuestions"})
	public String  questOfficeQuestions(Question question,Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("question", question);
		return "modules/suggestionStats/questOfficeQuestions";
	}
	@RequestMapping(value = {"questOfficeQuestionsData"})
	@ResponseBody
	public Page<Question> questOfficeQuestionsData(Question question, HttpServletRequest request, HttpServletResponse response) {
		Page<Question> page = questionService.adminFindPage(new Page<Question>(request,response), question);
		return page;
	}
	
	/**
	 *   按分类统计
	 */
	@RequestMapping(value = "classify")
	public String classify(Question question, Model model) {
		model.addAttribute("question", question);
		return "modules/suggestionStats/classifyList";
	}
	
	@RequestMapping(value = {"classifyData"})
	@ResponseBody
	public List<QuestionUser> classifyQuestionData(Question question, HttpServletRequest request, HttpServletResponse response) {
		List<QuestionUser> list = questionCriteriaService.createQuestionUserListStatsByClassify(request,response,question); 
		
		for(QuestionUser q :list){
			System.out.println(q.getQuestionDictData().getDictLabel()+"  "+ q.getCount());
		}
		return list;
	}
	
	@RequestMapping(value = {"classifyQuestions"})
	public String  classifyQuestions(Question question,Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("question", question);
		return "modules/suggestionStats/classifyQuestions";
	}
	@RequestMapping(value = {"classifyQuestionsData"})
	@ResponseBody
	public Page<Question> classifyQuestionsData(Question question, HttpServletRequest request, HttpServletResponse response) {
		Page<Question> page = questionService.adminFindPage(new Page<Question>(request,response), question);
		return page;
	}
	/**
	 *   建议阶段处理用时分析
	 */
	@RequestMapping(value = {"timeAnalysisOfProposedStageProcessing"})
	public String timeAnalysisOfProposedStageProcessing(Question question, Model model) {
		return "modules/suggestionStats/timeAnalysisOfPSPList";
	}
	
	@RequestMapping(value = {"timeAnalysisOfProposedStageProcessingListData"})
	@ResponseBody
	public Page<QuestionUser> timeAnalysisOfProposedStageProcessingListData(Question question, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionUser> page = questionCriteriaService.createQuestionPageStatsByTAOPSP(request, response, question);
		return page;
	}
	/**
	 *   建议处理用时分析
	 */
	@RequestMapping(value = {"suggestionProcessingTimeAnalysis"})
	public String suggestionProcessingTimeAnalysis(Question question, Model model) {
		return "modules/suggestionStats/suggestionProcessingTimeAnalysisList";
	}
	
	@RequestMapping(value = {"suggestionProcessingTimeAnalysisListData"})
	@ResponseBody
	public Page<QuestionUser> suggestionProcessingTimeAnalysisListData(Question question, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionUser> page = questionCriteriaService.createQuestionPageStatsBySPTAL(request, response, question);
		return page;
	}
	
	
}
