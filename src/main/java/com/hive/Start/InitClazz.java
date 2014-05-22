package com.hive.Start;

import com.hive.Clazz.Search;
import com.hive.Clazz.Vertical;
import com.hive.Log.HiveLog;
import com.hive.Redis.HiveRedis;
import com.hive.Sql.HiveDatabase;
import com.hive.Thread.FinalThread;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mika on 14-5-12.
 */
public class InitClazz {
    private String hostPathdir;
    private String tTag;
    private String tinfo;
    private String url;
    private String path;
    private String username;
    private int flag;
    private String hname;

    private HiveLog hiveLog;
    private HiveDatabase hiveDatabase;
    private HiveRedis hiveRedis;

    public InitClazz(String username, String url, String tinfo, String tTag, int flag) {
        this.username = username;
        this.url = url;
        this.tinfo = tinfo;
        this.tTag = tTag;
        this.flag = flag;
        hiveLog = new HiveLog();
        hiveDatabase = new HiveDatabase(username);
        hiveDatabase.ConnectionDB();
        hiveDatabase.creatTable(username);
        hiveRedis = new HiveRedis();
        hiveRedis.ConnectRedis();
    }

    public void run() {
        long keySize = 1;
        // Vertical parse run
        if (flag == 1) {
            path = doMkidr();
            Vertical vertical = new Vertical(url, username, hiveLog, hiveDatabase, hiveRedis);
            vertical.doSaveData(hostPathdir);
            hname = vertical.getHostName();
            // Search parse run
        }
        if (flag == 2) {
            path = doMkidr2();
            Search search = new Search(username, tinfo, path, hiveLog, hiveDatabase, hiveRedis);
            search.doSavetoRedis();
            keySize = search.getRedisSize();
        }

        FinalThread finalThread = new FinalThread(flag, username, keySize, hostPathdir,hname, hiveLog, hiveDatabase, hiveRedis);
        Thread thread = new Thread(finalThread);
        Thread thread1 = new Thread(finalThread);
        thread.start();
        thread1.start();
    }

    /**
     * Vertical's creat dir
     */
    public String doMkidr() {
        StringBuffer path = new StringBuffer(System.getProperty("user.dir") + File.separator + username);
        String[] test = url.split("/");
        String tmp;
        if (test.length > 1)
            tmp = test[2];
        else tmp = "";
        path.append(File.separator + tmp);
        this.hostPathdir = path.toString()+File.separator;
        String[] tags = tTag.split(";");
        for (int i = 0; i < tags.length; i++) {
            path = path.append(File.separator + tags[i]);
        }
        path = path.append(File.separator);
        File file = new File(path.toString());
        file.mkdirs();
        return path.toString();
    }

    /**
     * Search's creat dir
     */
    public String doMkidr2() {
        StringBuffer path = new StringBuffer(System.getProperty("user.dir") + File.separator + username);
//        String[] test = url.split("/");
//        path.append(File.separator + test[2]);
        this.hostPathdir = path.toString();
        String[] tags = tTag.split(";");
        for (int i = 0; i < tags.length; i++) {
            path = path.append(File.separator + tags[i]);
        }
        path = path.append(File.separator);
        File file = new File(path.toString());
        file.mkdirs();
        return path.toString();
    }
    public void Clear(){
        hiveRedis.clearRedis();
    }
}
