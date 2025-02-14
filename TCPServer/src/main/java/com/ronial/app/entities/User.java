package com.ronial.app.entities;

public class User {
    private String ip;
    private String nickname;

    public User(String nickname, String ip) {
        this.nickname = nickname;
        this.ip = ip;
    }
    public User(String ip) {
        this.ip = ip;
        this.nickname = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "ip='" + ip + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
