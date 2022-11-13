package com.example.websocket_redis.controller;

import com.example.websocket_redis.mapper.UserMapper;
import com.example.websocket_redis.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
@RestController
//模拟登录操作
@Slf4j
public class CertificationController {

    @Autowired
    private UserMapper userMapper;

    //login表单提交时会在Session中
    @RequestMapping("/toLogin")
    public Result toLogin(String user, String pwd, HttpSession httpSession){
        //数据库获取数据
        String name = userMapper.getUseName(user);
        String password = userMapper.getPassword(user);
        Result result = new Result();
        httpSession.setMaxInactiveInterval(30*60);
        log.info(user+"登录验证中..");

        if(httpSession.getAttribute("user") != null){
            result.setFlag(false);
            result.setMessage("不支持一个浏览器登录多个用户！");
            return result;
        }
        if(name.equals(user)&&password.equals(pwd)){
            result.setFlag(true);
            result.setMessage("登录验证成功");
            System.out.println("登录验证成功");
            httpSession.setAttribute("user", user);
        }else {
            result.setFlag(false);
            result.setMessage("登录验证失败");
        }
        return result;

    }
    //在main页面通过ajax获取username
    @RequestMapping("/getUsername")
    public String getUsername(HttpSession httpSession){
        //用于从session中获取当前登录的用户名并响应回浏览器
        String username = (String) httpSession.getAttribute("user");
        return username;
    }
}
