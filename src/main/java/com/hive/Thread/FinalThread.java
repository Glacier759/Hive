package com.hive.Thread;

import com.hive.Log.HiveLog;
import com.hive.Redis.HiveRedis;
import com.hive.SaveToDisk.Save;
import com.hive.Sql.HiveDatabase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.URL;

/**
 * Created by mika on 14-5-12.
 */
public class FinalThread implements Runnable {
    private int flag;
    private long keySize;
    private String username;
    private HiveRedis hiveRedis;
    private HiveLog hiveLog;
    private HiveDatabase hiveDatabase;
    private String hostname;
    private String path;

    public FinalThread(int flag, String username, long keySize, String path, String hostname, HiveLog hiveLog, HiveDatabase hiveDatabase, HiveRedis hiveRedis) {
        this.flag = flag;
        this.username = username;
        this.keySize = keySize;
        this.path = path;
        this.hostname = hostname;
        this.hiveRedis = hiveRedis;
        this.hiveLog = hiveLog;
        this.hiveDatabase = hiveDatabase;
    }
    public synchronized String RedisPop(){
        return hiveRedis.popValue();
    }
    public synchronized void RedisPush(String value){
        hiveRedis.pushValue(value);
    }
    @Override
    public void run() {
        long sign = 0;
        while (true) {
            try {
                //1.get data form Redis
                System.out.println(hostname);
                hiveRedis.setKey(username);
                System.out.println(hiveRedis.getLength());
//                String url = hiveRedis.popValue();
                String url = RedisPop();
                //2. Vertical or Search
                if (flag == 1) {
                    System.out.println(url);
                    String[] host = url.split("/");
                    String test;
                    if (host.length > 2)
                        test = host[2];
                    else
                        test = "";
                    if (!test.equals(hostname)) {
                        continue;
                    }
                } else if (flag == 2) {
                    sign++;
                    if (sign == keySize) {
                        break;
                    }
                }
                //3. get page all Url and compare with DB ,save to Redis.

                Document doc = Jsoup.connect(url).timeout(50000).userAgent("Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
                Elements pageAllUrls = doc.select("a[href]");
                String title = doc.title();
//                String pinfo = doc.html();
                String pinfo = doc.getElementsByTag("p").text();
                for (Element pageAllurl : pageAllUrls) {
                    String pageUrl = pageAllurl.attr("abs:href");

//                        hiveRedis.pushValue(pageUrl);
                    RedisPush(pageUrl);
                    if (!(hiveDatabase.isUniqueURL(pageUrl))) {//if dont have this url
                        hiveDatabase.insertUrl(pageUrl);
                        Save save = new Save(title, pageUrl, pinfo, path);
                        save.doSave();
                    }
                }
            } catch (IOException e) {
            }
        }

    }
}
