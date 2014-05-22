package com.hive.SaveToDisk;

import com.hive.Log.HiveLog;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mika on 14-5-6.
 */
public class SaveToDBImpl  {
    private HiveLog hiveLog;
    private String title;
    private String link;
    private String pinfo;

    /**
     *
     *
     * */
    public SaveToDBImpl(String title, String link, String pinfo) {
        hiveLog = new HiveLog();
        this.title = title;
        this.link = link;
        this.pinfo = pinfo;
    }

    /**
     *
     *
     * */
    public void doSaveToDB(String path) {
        XStream xStream = new XStream(new DomDriver());
        Hive hive = new Hive();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        xStream.alias("hive", Hive.class);
        File file = new File(path + new Date().getTime() + ".xml");
        hive.setTitle(title);
        hive.setCrawltime(sdf.format(d));
        hive.setModifytime("");
        hive.setSource(link);
        hive.setLanguage("中文");
        hive.setEncode("utf-8");
        hive.setBody(pinfo);
        try {
            System.out.println(file);
            FileUtils.write(file, xStream.toXML(hive));
            hiveLog.SysLog("save ok!");

            System.out.println("save ok!");
        } catch (IOException e) {
            hiveLog.ErrLog("write error!!");

            e.printStackTrace();
        }
    }
}
