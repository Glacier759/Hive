package com.hive.Clazz;


import com.hive.Baidu.BaiduSpider;
import com.hive.Log.HiveLog;
import com.hive.Redis.HiveRedis;
import com.hive.SaveToDisk.Save;
import com.hive.Sql.HiveDatabase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mika on 14-5-12.
 */
public class Search {
    private int maxpn = 100;
    private String sText;
    private String path;
    private HiveLog hiveLog;
    private HiveDatabase hiveDatabase;
    private HiveRedis hiveRedis;
    private String username;

    public Search(String username, String sText, String path,HiveLog hiveLog,HiveDatabase hiveDatabase,HiveRedis hiveRedis) {
        this.username = username;
        this.sText = sText;
        this.path = path;
        this.hiveLog = hiveLog;
        this.hiveDatabase =hiveDatabase;
        this.hiveRedis = hiveRedis;
        this.hiveRedis.setKey(username);
    }

    public void doSavetoRedis() {

            BaiduSpider baiduSpider = new BaiduSpider(maxpn, sText, username);
            List<String> list = baiduSpider.doParseBaidu();
            Iterator<String> it = list.listIterator();
            while (it.hasNext()) {
                String purl = it.next();
                System.out.println(purl);
                String[] host = purl.split("/");
                String hostname = host[2];
                hiveRedis.pushValue(purl);
                Document doc = null;
                try {
                doc = Jsoup.connect(purl).timeout(10000).userAgent("Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();

                } catch (IOException e) {
                    continue;
                }
                String title = doc.title();
//                String tinfo = doc.html();
                String tinfo = doc.getElementsByTag("p").text();
                Save save = new Save(title, purl, tinfo, path);
                save.doSave();
                Elements pageAllUrls = doc.select("a[href]");
                for (Element pageAllurl : pageAllUrls) {
                    String pageUrl = pageAllurl.attr("abs:href");
                    if(pageUrl.equals("") || pageUrl == null){
                        continue;
                    }
                    String[] test = pageUrl.split("/");
                    if(test.length < 2){
                        continue;
                    }
                    if ((hostname.equals(test[2])) && !(hiveDatabase.isUniqueURL(pageUrl))) {//if dont have this url
                        hiveRedis.pushValue(pageUrl);
                        hiveDatabase.insertUrl(pageUrl);
                    }
                }

            }
            System.gc();

    }

    public Long getRedisSize() {
        return hiveRedis.getLegnth(username);
    }
}
