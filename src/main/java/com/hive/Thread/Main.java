package com.hive.Thread;


import com.hive.Start.InitClazz;

/**
 * Created by mika on 14-5-11.
 */
public class Main {
    //    public static void main(String[] args){
//        String username = "yangge6";
//        String url = "http://www.csdn.net/";
//        String tinfo = "钜派公司";
//        String tTag = "钜派公司";
//        int flag = 2;
//        InitClazz initClazz = new InitClazz(username,url,tinfo,tTag,flag);
//        initClazz.run();
//        initClazz.Clear();
//
//    }
    public static void startHive(String username,String url,String tinfo,String tTag,int flag) {
//        String username = "yangge6";
//        String url = "http://www.csdn.net/";
//        String tinfo = "钜派公司";
//        String tTag = "钜派公司";
//        int flag = 2;
        InitClazz initClazz = new InitClazz(username, url, tinfo, tTag, flag);
        initClazz.run();
        initClazz.Clear();

    }
}
