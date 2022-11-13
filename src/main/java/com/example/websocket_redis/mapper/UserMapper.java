package com.example.websocket_redis.mapper;

import com.example.websocket_redis.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface UserMapper {
    @Select("SELECT name FROM user WHERE name = #{name}")
    public String getUseName(String userName);

    @Select("SELECT password FROM user WHERE name = #{name}")
    public String getPassword(String userName);

    @Insert("INSERT INTO user VALUES (#{name},#{password})")
    public void register(User user);
}
