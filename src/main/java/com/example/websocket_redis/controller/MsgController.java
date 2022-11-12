package com.example.websocket_redis.controller;


import com.example.websocket_redis.pojo.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgController {
    @Autowired
    private RedisTemplate redisTemplate;

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

}
