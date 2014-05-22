package com.hive.Clazz;

import com.hive.Log.HiveLog;
import com.hive.Redis.HiveRedis;
import com.hive.SaveToDisk.Save;
import com.hive.Sql.HiveDatabase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mika on 14-5-12.
 */
public class Vertical {

    private String url;
    private String hostname;
    private HiveDatabase hiveDatabase;
    private HiveRedis hiveRedis;
    private HiveLog hiveLog;

    public Vertical(String url, String username, HiveLog hiveLog, HiveDatabase hiveDatabase, HiveRedis hiveRedis) {
        this.hiveLog = hiveLog;
        this.hiveDatabase = hiveDatabase;
        this.hiveRedis = hiveRedis;
        this.hiveRedis.setKey(username);
        this.url = url;
        this.getHostName();
    }

    public String getHostName() {
        String[] host = url.split("/");
        hostname = host[2];
        return hostname;
    }

    public void doSaveData(String path) {
        try {
            Document doc = Jsoup.connect(url).timeout(50000).userAgent("Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
            String title = doc.title();
            String tinfo = doc.html();
            Save save = new Save(title, url, tinfo, path);
            save.doSave();
            // get all page Urls
            Elements pageAllUrls = doc.select("a[href]");
            for (Element pageAllurl : pageAllUrls) {
                String pageUrl = pageAllurl.attr("abs:href");
                String[] host = pageUrl.split("/");
                String test;
                if(host.length > 2)
                   test = host[2];
                else
                    test = "";
                //if is hosturl and dont save it,just do save
                if ((test.equals(hostname)) && !(hiveDatabase.isUniqueURL(pageUrl))) {//if dont have this url
                    hiveRedis.pushValue(pageUrl);
                    hiveDatabase.insertUrl(pageUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
