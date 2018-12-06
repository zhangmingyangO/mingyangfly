package com.neusoft.controller;
import com.neusoft.bean.RegRespObj;
import com.neusoft.bean.User;
import com.neusoft.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

@RequestMapping("user")
public class HelloController {
    @Autowired
    UserMapper userMapper;
    @RequestMapping("login")
    public String login(){

        return "user/login";
    }
    @RequestMapping("reg")
    public String reg(){
        return "user/reg";
    }
    @RequestMapping("doreg")
    @ResponseBody
    public RegRespObj doReg(User user){
        userMapper.insertSelective(user);
        RegRespObj regRespObj = new RegRespObj();
            regRespObj.setStatus(0);
            regRespObj.setAction("/");
        return regRespObj;
    }
}
