package com.example.lovespace.config;/**
 * Created by stephen on 2017/5/13.
 */

/**
 * 包名： com.example.lovespace.config.preference
 * 类名:  ServerConfig
 * 类功能：
 * 创建者：  stephen
 * 创建日期: 2017/5/13
 */
public final class ServerConfig {

    public enum ServerEnv {
        TEST("t"),
        PRE_REL("p"),
        REL("r"),

        ;
        String tag;

        ServerEnv(String tag) {
            this.tag = tag;
        }
    }

    public static boolean testServer() {
        return ServerEnvs.SERVER == ServerEnv.TEST;
    }
}
