package com.jeesite.modules.suggestion.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.bpm.util.BpmUtils;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.suggestion.dao.QuestionDao;
import com.jeesite.modules.suggestion.entity.Question;
import com.jeesite.modules.suggestion.util.Stage;
import com.jeesite.modules.suggestion.util.Status;
import com.jeesite.modules.sys.utils.UserUtils;


/**
 * 问题管理Service
 */
@Service
@Transactional(readOnly=true)
public class QuestionService extends CrudService<QuestionDao, Question> {

	
	/**
	 * 查询问题
	 */
	@Override
	public Question get(Question question) {
		return super.get(question);
	}
	
 

	/**
	 * 查询可分页的列表
	 */
	@Override
	public Page<Question> findPage(Page<Question> page, Question question) {
		if(!UserUtils.getUser().isAdmin()) {
			question.getSqlMap().getWhere().andBracket("user_code", QueryType.EQ, UserUtils.getUser().getId())
			.or("manager", QueryType.EQ, UserUtils.getUser().getId())
			.or("quest_user", QueryType.EQ, UserUtils.getUser().getId())
			.or("quest_manager", QueryType.EQ, UserUtils.getUser().getId())
			.endBracket();
		}
		return super.findPage(page, question);
	}
	
	/**
	 * 查询可分页的列表
	 */
	public Page<Question> adminFindPage(Page<Question> page, Question question) {
		return super.findPage(page, question);
	}

	/**
	 * 保存岗位
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Question question) {
		super.save(question);
		FileUploadUtils.saveFileUpload(question.getId(), "question_image");
		FileUploadUtils.saveFileUpload(question.getId(), "question_file");
	}

	/**
	 * 更新岗位状态
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Question question) {
		super.updateStatus(question);
	}

	/**
	 * 删除岗位
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Question question) {
		super.delete(question);
	}

	/**
	 * 提交->审核
	 */
	@Transactional(readOnly=false)
	public void submit(Question question,String taskId,String nextCandidateUser) {
		Map<String,Object> bpmParam=new HashMap<String,Object>();
		if(nextCandidateUser!=null)
			bpmParam.put("_candidateUser_", nextCandidateUser);
		if(question.getBzStatus()==Status.UNCOMMITED.getValue()) {//首次提交
			BpmUtils.startProcess("sg_question_manage", question.getId(), question.getQuestionTitle(),
					bpmParam, UserUtils.getUser().getId());
			question.setCommitTime(new Date());
		}else {//驳回重新提交
			BpmUtils.completeTaskByIdAndAssignee(taskId, bpmParam, 
					UserUtils.getUser().getId(), "重新提交", null);
		}
		question.setBzStatus(Status.UNAUDIT.getValue());
		question.setStage(Stage.AUDIT.getValue());
		super.save(question);
	}
	
	@Transactional(readOnly=false)
	public void next(Question question,String taskId,String nextCandidateUser,
			int flag,String deleteReason ) {
		Map<String,Object> bpmParam=new HashMap<String,Object>();
		if(nextCandidateUser!=null)
			bpmParam.put("_candidateUser_", nextCandidateUser);
		if(flag!=0)
			bpmParam.put("flag", flag);
		BpmUtils.completeTaskByIdAndAssignee(taskId, bpmParam, 
				UserUtils.getUser().getId(),deleteReason , null);
		super.save(question);
	}	
	
}