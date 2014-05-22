package com.hive.SaveToDisk;

import com.hive.Extract.ParseP;

/**
 * Created by mika on 14-5-7.
 */
public class Save {
    private String title;
    private String link;
    private String pinfo;
    private String path;

    public Save(String title,String link,String pinfo,String path){
        this.title = title;
        this.link = link;
        this.pinfo = pinfo;
        this.path = path;
    }
    public void doSave(){
        SaveToDBImpl stdb = new SaveToDBImpl(title,link,pinfo);
        stdb.doSaveToDB(path);
    }

}
