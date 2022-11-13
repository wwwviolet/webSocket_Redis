package com.example.websocket_redis.ws;

//import com.example.websocket_redis.interceptor.UserInterceptor;
import com.example.websocket_redis.controller.MsgController;
import com.example.websocket_redis.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/chat",configurator = GetHttpSessionConfigurator.class)
@Component//让springboot管理
//每一个客户端对应一个ChatEndPoint对象与之相对应
public class ChatEndPoint implements MessageListener  {

    //用线程安全的map来保存当前用户,对当前所有的客户端对象对应的ChatEndPoint进行管理
    private static Map<String,ChatEndPoint> onLineUsers = new ConcurrentHashMap<>();
    //声明一个session对象，通过该对象可以发送消息给指定用户，不能设置为静态，每个ChatEndPoint有一个session才能区分.(websocket的session)
    private Session session;
    //保存当前登录浏览器的用户,之前在HttpSession对象中存储了用户名
    private HttpSession httpSession;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    //聊天记录数据
//    @Autowired
//    public static RedisTemplate<String,Object> chatData;
//    public static List<String> chatStr = new ArrayList<>();








    //建立连接时发送系统广播
    //连接建立时被调用
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        //将局部的session对象赋值给成员session
        this.session = session;
        //获取HttpSession对象
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        //赋值给成员变量httpSession
        this.httpSession = httpSession;
        //从httpSession对象中获取用户名,在登录时进行存放
        String username = (String) httpSession.getAttribute("user");
        log.info("上线用户名称："+username);//在spring控制台中显示
        //加入到存放用户对象的map中
        onLineUsers.put(username,this);
        //将当前在线用户的用户名推送给所有的客户端
        //1.获取消息,转换为json格式
        String message = MessageUtils.getMessage(true,null,getNames());
        //2.调用方法进行系统消息的推送
        broadcastAllUsers(message);
    }

    //获取当前登录的所有用户
    private Set<String> getNames(){
        return onLineUsers.keySet();
    }


    //发送系统消息
    private void broadcastAllUsers(String message){
        try{
            //广播:通过服务器对所有客户端进行消息的转发,客户端接收后会调用onMessage
            Set<String> names = onLineUsers.keySet();

            for(String name : names){
                ChatEndPoint chatEndPoint = onLineUsers.get(name);
                chatEndPoint.session.getBasicRemote().sendText(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    //连接关闭时被调用
    //用户断开连接的断后操作
    @OnClose
    public void onClose(Session session){
        String username = (String) httpSession.getAttribute("user");
        log.info("离线用户："+ username);
        //从onLineUsers中删除指定用户
        if (username != null){
            onLineUsers.remove(username);
        }
        httpSession.removeAttribute("user");
        //获取推送的消息
        String message = MessageUtils.getMessage(true,null,getNames());
        //推送给所有客户端本客户端下线
        broadcastAllUsers(message);
    }


    //订阅的接收者
    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] bytes) {
        String userName;
        String msg;
        RedisSerializer<String> stringSerializer = redisTemplate.getStringSerializer();
        String msgStr = stringSerializer.deserialize(message.getBody());
        String JsonData = MessageUtils.splitString(msgStr);
//        String substring = replace2.substring(0, msgStr.length() - 1);
        //对字符串进行处理
        String[] result = JsonData.split(",");
        userName = result[0];
        msg = result[1];
        //将聊天记录保存
        String chatDataStr = userName+":"+msg;
        MsgController.list.add(chatDataStr);
//        MsgController.redisTemplate.opsForList().leftPush("chat", chatDataStr);
        msg = MessageUtils.toMessage(false,userName,msg);
        Chat(userName,msg);
        System.out.println(userName+msg);
        log.info("收到的消息"+msg);

    }

    //发送消息
    private void Chat(String userName,String message){
        try{
            //广播:通过服务器对所有客户端进行消息的转发,客户端接收后会调用onMessage
            Set<String> names = onLineUsers.keySet();
            for(String name : names){
                if (!userName.equals(name)) {
                    ChatEndPoint chatEndPoint = onLineUsers.get(name);
                    chatEndPoint.session.getBasicRemote().sendText(message);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }





}
