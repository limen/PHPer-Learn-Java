package com.limengxiang.basics.model;

public class UserModel {
    private String username;
    private String mobile;

    public String toString() {
        return "username:" + username +
                ", mobile:" + mobile;
    }

    /**
     * 构造方法1
     */
    public UserModel() {

    }

    /**
     * 构造方法2
     * @param username
     */
    public UserModel(String username) {

    }

    public void hello(String name) {

    }

    public void hello(Integer age) {

    }

    public void hello(String name, String home) {

    }
}
