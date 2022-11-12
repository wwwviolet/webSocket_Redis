package com.example.websocket_redis.utils;

import com.example.websocket_redis.pojo.ResultMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


//封装发送的消息内容
public class MessageUtils {
    public static String getMessage(boolean isSystemMessage,String fromName,Object message){
        try{
            ResultMessage resultMessage = new ResultMessage();
            resultMessage.setSystem(isSystemMessage);
            resultMessage.setMessage(message);
            if(fromName != null){
                resultMessage.setFromName(fromName);
            }
//            if(toName !=null ){
//                resultMessage.setToName(toName);
//            }
            //返回一个json格式的数据
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(resultMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String toMessage(boolean isSystemMessage,String fromName,Object message){
        try{
            ResultMessage resultMessage = new ResultMessage();
            resultMessage.setSystem(isSystemMessage);
            resultMessage.setMessage(message);
            if(fromName != null){
                resultMessage.setFromName(fromName);
            }
//            if(toName !=null ){
//                resultMessage.setToName(toName);
//            }
            //返回一个json格式的数据
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(resultMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    //对发布的字符串进行处理,分开名字和携带的消息
    public static String splitString(String msg){
        String replace = msg.replace("[", "");
        String replace1 = replace.replace("]", "");
        String result = replace1.replace("\"", "");

        return result;
    }

}
