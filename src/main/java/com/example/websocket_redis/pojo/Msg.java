package com.example.websocket_redis.pojo;

public class Msg {
    private String id;
    private String content;

    public Msg() {
    }

    public Msg(String id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
