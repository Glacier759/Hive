package com.hive.Baidu;

import com.hive.Log.HiveLog;
import com.hive.Redis.HiveRedis;
import com.thoughtworks.xstream.mapper.Mapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mika on 14-5-7.
 */
public class BaiduSpider {
    private String username;
    private int maxpn;
    private String sText;
    private Document doc;
    private static HiveLog hiveLog;
//    private HiveRedis hiveRedis;

    /**
     * @param maxpn
     * @param sText
     * @param username
     */
    public BaiduSpider(int maxpn, String sText,String username) {
        this.maxpn = maxpn;
        this.sText = sText;
        this.username = username;
        hiveLog = new HiveLog();
//        hiveRedis = new HiveRedis();
//        hiveRedis.ConnectRedis();
    }


    public List<String> doParseBaidu() {
        try {
            List<String> listurl = new LinkedList<String>();
            for (int i = 1; i < maxpn; i ++) {
                doc = Jsoup.connect("http://www.so.com/s?pn=" + i + "&ie=utf-8&src=360sou_home&q=" + sText).timeout(5000).userAgent("Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
                Elements urls = doc.select("li[class=res-list]");
                for(Element url : urls){
                         String u = url.getElementsByTag("a").attr("href");
                    if(url.getElementsByTag("a").attr("href").isEmpty())
                        continue;
                    else listurl.add(u);
                }
            }
//            Iterator<String> it = listurl.listIterator();
//            while(it.hasNext()){
//                System.out.println(it.next());
//            }
            return listurl;

        } catch (IOException e) {
            hiveLog.ErrLog(this.getClass().getName() + "  error!");
            e.printStackTrace();
            return null;
        }
    }

//    public void saveToRedis(Iterator<String> it) {
//            hiveRedis.setKey(username);
//            while (it.hasNext()) {
//                String s = it.next();
//                hiveRedis.pushValue(s);
//            }
//    }
//    public void Print(){
//        long size = hiveRedis.getLength();
//        for(int i=0;i<size;i++){
//            System.out.println(hiveRedis.popValue());
//        }
//        hiveRedis.clearRedis();
//    }
}
