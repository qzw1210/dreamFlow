package com.jeesite.modules.suggestion.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.datasource.DataSourceHolder;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.suggestion.entity.Question;

/**
 * 问题管理DAO接口
 */
@MyBatisDao(dataSourceName=DataSourceHolder.DEFAULT)
public interface QuestionDao extends CrudDao<Question> {
	
}