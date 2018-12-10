package com.neusoft.mapper;

import com.neusoft.bean.TopicCategory;

import java.util.List;

public interface TopicCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicCategory record);

    int insertSelective(TopicCategory record);

    TopicCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicCategory record);

    int updateByPrimaryKey(TopicCategory record);

    List<TopicCategory> GetAllCategory();

}