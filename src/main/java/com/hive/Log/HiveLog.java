package com.hive.Log;



import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import ch.ethz.ssh2.*;
/**
 * Created by renlixiang on 14-5-7.
 */
public class HiveLog {

    private String Path = "./";

    public HiveLog() {
        NetworkTest();
        ServiceTest();
    }

    private boolean NetworkTest() {
        return this.NetworkTest("220.181.111.188");
    }
    private boolean NetworkTest( String host ) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ping "+host+" -w 1");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            this.ErrLog(this.getClass().getName()+ "  is error!");
            e.printStackTrace();
        }
        BufferedReader NetworkList = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null, NetworkMsg = "";
        try {
            while( (line = NetworkList.readLine()) != null ) {
                NetworkMsg = NetworkMsg +"\n" + line;;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            this.ErrLog(this.getClass().getName()+ "  is error!");
            e.printStackTrace();
        }
        if ( NetworkMsg.indexOf("0% packet loss") >= 0 ) {
            this.SysLog("[Start]\t\t\t\t网络连接状况");
            return true;
        }
        this.SysLog("[Stop]\t\t\t\t网络连接状况");
        return false;
    }

    private boolean ServiceTest() {
        String RedisMsg = SSHTesing( "222.24.63.100", "renlixiang", "rlx", "ps -A | grep redis" );
        if ( RedisMsg.indexOf("redis-server") < 0 ) {
            this.SysLog("[Stop]\t\t\t\tredis服务状况");
            return false;
        } else {
            this.SysLog("[Start]\t\t\t\tredis服务状况");
        }
        return true;
    }

    private String SSHTesing( String host, String username, String password, String cmd ) {
        Connection conn = null;
        try {
            conn = getOpenedConnection( host, username, password );
        } catch (Exception e) {
            // TODO Auto-generated catch block
            this.ErrLog(this.getClass().getName()+ "  is error!");
            e.printStackTrace();
        }
        Session sess = null;
        try {
            sess = conn.openSession();
        } catch (IOException e) {
            this.ErrLog(this.getClass().getName()+ "  is error!");
            e.printStackTrace();
        }
        try {
            sess.execCommand(cmd);
        } catch (IOException e) {
            this.ErrLog(this.getClass().getName()+ "  is error!");
            e.printStackTrace();
        }

        InputStream stdout = new StreamGobbler(sess.getStdout());
        @SuppressWarnings("resource")
        BufferedReader BR = new BufferedReader( new InputStreamReader(stdout) );

        String line = null, PrintMsg = "";
        try {
            while( (line = BR.readLine()) != null ) {
                PrintMsg = PrintMsg + "\n" + line;
            }
        } catch (IOException e) {
            this.ErrLog(this.getClass().getName()+ "  is error!");
            e.printStackTrace();
        }
        return PrintMsg;
    }

    private Connection getOpenedConnection( String host, String username, String password ) {
        Connection conn = new Connection(host);
        try {
            conn.connect();
        } catch (IOException e) {
            this.ErrLog(this.getClass().getName()+ "  is error!");
            e.printStackTrace();
        }
        boolean isAuthenticated = true;
        try {
            isAuthenticated = conn.authenticateWithPassword(username, password);
        } catch (IOException e) {
            this.ErrLog(this.getClass().getName()+ " Connection   is error!");
            e.printStackTrace();
        }
        if ( isAuthenticated == false ) {
            try {
                throw new IOException("Authenication failed");
            } catch (IOException e) {
                this.ErrLog(this.getClass().getName()+ "  is error!");
                e.printStackTrace();
            }
        }
        return conn;
    }

    public void SysLog( String Information ) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        try {
            FileUtils.writeStringToFile(new File(this.Path+"Sys_Log.txt"), df.format(new Date(System.currentTimeMillis())) + "\t\t"+Information+"\r\n", "UTF-8", true);
        } catch (IOException e) {
            this.ErrLog(this.getClass().getName()+ "  is error!");
            e.printStackTrace();
        }
    }
    public void ErrLog( String Information ) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        try {
            FileUtils.writeStringToFile(new File(this.Path+"Err_Log.txt"), df.format(new Date(System.currentTimeMillis())) + "\t\t"+Information+"\r\n", "UTF-8", true);
        } catch (IOException e) {
            this.ErrLog(this.getClass().getName()+ "  is error!");
            e.printStackTrace();
        }
    }
    public void setPath( String Path ) {
        if ( Path.lastIndexOf("/") != Path.length()-1 ) {
            Path += "/";
        }
        File Dir = new File(Path);
        if ( Dir.isDirectory() ) {
            this.Path = Path;
        }
        else {
            this.Path = Path;
            SysLog("新目录被创建  " + Path);
        }
        NetworkTest();
        ServiceTest();
    }
}
