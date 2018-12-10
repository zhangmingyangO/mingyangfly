package com.neusoft.controller;
import Utils.MD5Utils;
import com.neusoft.bean.RegRespObj;
import com.neusoft.bean.User;
import com.neusoft.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Controller

@RequestMapping("user")
public class HelloController {
    @Autowired
    UserMapper userMapper;

    @RequestMapping("modifyPWD")
    @ResponseBody
    public  RegRespObj repass(HttpServletRequest request,String pass){
        System.out.println("我恶趣味恶趣味企鹅企鹅11111111111111");
        RegRespObj regRespObj = new RegRespObj();
        HttpSession session = request.getSession();
        User userinfo = (User)session.getAttribute("userinfo");
        String pwd = MD5Utils.getPwd(pass);
        userinfo.setPasswd(pwd);
        userMapper.updateByPrimaryKeySelective(userinfo);
        regRespObj.setStatus(0);
        regRespObj.setAction("/user/set");

        return  regRespObj;
    }
    @RequestMapping("upmessage")
    @ResponseBody
    public RegRespObj upmessage( HttpServletRequest request,User user){
        RegRespObj regRespObj = new RegRespObj();
        User user1 = userMapper.SelectEmail(user.getEmail());
        if (user1==null){
            HttpSession session = request.getSession();
            User userinfo = (User) request.getSession().getAttribute("userinfo");
           // User userinfo = (User) session.getAttribute("userinfo");
            user.setId(userinfo.getId());
            int i = userMapper.updateByPrimaryKeySelective(user);
            if (i>0){
                User user2 = userMapper.selectByPrimaryKey(user.getId());
                session.setAttribute("userinfo",user2);
                regRespObj.setStatus(0);
                regRespObj.setMsg("修改成功");
                regRespObj.setAction(request.getServletContext().getContextPath()+"/user/set");
            }else {
                regRespObj.setMsg("修改失败");
                regRespObj.setStatus(1);
            }
        }else {
            regRespObj.setMsg("修改失败,邮箱重复");
            regRespObj.setStatus(1);
        }

        return regRespObj;
    }
    @RequestMapping("upload")
    @ResponseBody
    public RegRespObj upload(@RequestParam MultipartFile file ,HttpServletRequest request ) throws IOException {
        RegRespObj regRespObj = new RegRespObj();
        if (file.getSize()>0){
            String realPath = request.getServletContext().getRealPath("/static/uploadImgs");
            File file1 = new File(realPath);
            if (!file1.exists()){
                file1.mkdirs();
            }
            UUID uuid = UUID.randomUUID();
            File file2 = new File(realPath+File.separator+uuid+file.getOriginalFilename());
            file.transferTo(file2);
            HttpSession session = request.getSession();
            User userinfo =(User) session.getAttribute("userinfo");
            userinfo.setPicPath(uuid+file.getOriginalFilename());
            session.setAttribute("userinfo",userinfo);
            userMapper.updateByPrimaryKeySelective(userinfo);
            regRespObj.setStatus(0);
        }else {
            regRespObj.setStatus(1);
        }
        return regRespObj;
    }
    @RequestMapping("logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return  "redirect:"+ request.getServletContext().getContextPath() +"/";
    }
    @RequestMapping("set")
    public String set(){

        return "user/set";
    }
    @RequestMapping("dologin")
    @ResponseBody
    public RegRespObj dologin(User user, HttpServletRequest request){
        user.setPasswd(MD5Utils.getPwd(user.getPasswd()));
        User userResult = userMapper.SelectEmailAndpwd(user);
        RegRespObj regRespObj = new RegRespObj();
        if (userResult==null){
            regRespObj.setStatus(1);
            regRespObj.setMsg("登录失败");
        }else {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("userinfo",userResult);
            regRespObj.setStatus(0);
            regRespObj.setAction(request.getServletContext().getContextPath() + "/");
        }
            return regRespObj;
    }
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
        RegRespObj regRespObj = new RegRespObj();
        User user1 = userMapper.SelectEmail(user.getEmail());
        if (user1==null){
            user.setKissNum(100);
            user.setJoinTime(new Date());
            String passwd = user.getPasswd();
            String pwd = MD5Utils.getPwd(passwd);
            user.setPasswd(pwd);
            int count = userMapper.insertSelective(user);
            if (count>0){
                regRespObj.setStatus(0);
                regRespObj.setAction("/");
            }else {
                regRespObj.setStatus(1);
                regRespObj.setMsg("系统错误，请联系客服");
            }
        }else {
            regRespObj.setStatus(1);
            regRespObj.setMsg("邮箱重复，请确认");
        }

        return regRespObj;
    }
    @RequestMapping("/checkEmail")
    @ResponseBody
    public RegRespObj checkEmail(User user){
        RegRespObj regRespObj = new RegRespObj();
        User user1 = userMapper.SelectEmail(user.getEmail());
        if(user1==null){
            regRespObj.setMsg("可以使用");
        }else {
            regRespObj.setMsg("邮箱重复，请更换邮箱");
        }
        return regRespObj;
    }
    @RequestMapping("checkNowPass/{id}")
    @ResponseBody
    public RegRespObj checkNowPass(HttpServletRequest request,@PathVariable String id){
        System.out.println("进来没啊啊");
        RegRespObj regRespObj = new RegRespObj();
       HttpSession session = request.getSession();
       User userinfo = (User) session.getAttribute("userinfo");
       String pwd= MD5Utils.getPwd(id);
        if(pwd.equals(userinfo.getPasswd())){
            regRespObj.setMsg("当前密码正确");
        }else {
            regRespObj.setMsg("当前密码错误");
        }
        return regRespObj;
    }

}
