package com.example.lovespace.config;/**
 * Created by stephen on 2017/5/13.
 */

/**
 * 包名： com.example.lovespace.config
 * 类名:  DemoServers
 * 类功能：
 * 创建者：  stephen
 * 创建日期: 2017/5/13
 */
public class DemoServers {
    //
    // 好友列表信息服务器地址
    //
    private static final String API_SERVER_TEST = "http://223.252.220.238:8080/api/"; // 测试
    private static final String API_SERVER = "https://app.netease.im/api/"; // 线上

    public static final String apiServer() {
        return ServerConfig.testServer() ? API_SERVER_TEST : API_SERVER;
    }
}
