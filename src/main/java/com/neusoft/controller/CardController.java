package com.neusoft.controller;

import com.neusoft.bean.*;
import com.neusoft.mapper.TopicCategoryMapper;
import com.neusoft.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("jie")
public class CardController {
    @Autowired
    TopicCategoryMapper topicCategoryMapper;
    @Autowired
    TopicMapper topicMapper;
    @RequestMapping("add")
    public ModelAndView add (){
        List<TopicCategory> topicCategories = topicCategoryMapper.GetAllCategory();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categories",topicCategories);
        modelAndView.setViewName("jie/add");
        return modelAndView;
    }
    @RequestMapping("xiecard")
    @ResponseBody
    public RegRespObj xiecard (Topic topic, HttpServletRequest request, HttpServletResponse response){
        RegRespObj regRespObj = new RegRespObj();
        HttpSession session = request.getSession();
        User user1 = (User) session.getAttribute("userinfo");
        topic.setUserid(user1.getId());
        topic.setCreateTime(new Date());
        int i = topicMapper.insertSelective(topic);
        if (i>0){
            regRespObj.setStatus(0);
            regRespObj.setAction(request.getServletContext().getContextPath() + "/");
            //如果不写 @ResponseBody  需要加这个 进行转换成JSON
            //response.getWriter().println(JSON.toJSONString(regRespObj));
        }
        return regRespObj;
    }

}
