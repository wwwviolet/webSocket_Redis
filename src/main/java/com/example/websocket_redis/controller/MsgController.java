package com.example.websocket_redis.controller;


import com.example.websocket_redis.ws.ChatEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MsgController {
    @Autowired
    private RedisTemplate redisTemplate;

    public static List<String> list = new ArrayList<>();

    @PostMapping("/sendMsg")
    public String sendMsg(String msg,String userName){
//        System.out.println(userName);
//        System.out.println(msg);
        //将Json数据通过redis发布的内容进行转发,转发给接收者
        String[] JsonData= new String[2];
        JsonData[0] = userName;
        JsonData[1] = msg;
        redisTemplate.convertAndSend("topic", JsonData);
        return "ok";
    }

    @RequestMapping("/getCharData")
    public String chatDataSend(){
        StringBuilder str= new StringBuilder();
        for (String o : list){
            str.append(o).append("\n");
            redisTemplate.opsForList().leftPush("chat",o);
        }
//        list.add(chatData);
//        httpSession.setAttribute("chatData",list);
        return str.toString();
    }

}
