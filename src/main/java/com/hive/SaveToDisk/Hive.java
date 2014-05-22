package com.hive.SaveToDisk;

/**
 * Created by mika on 14-5-6.
 */
public class Hive {
    private String title;
    private String crawltime;
    private String modifytime;
    private String source;
    private String language;
    private String encode;
    private String body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCrawltime() {
        return crawltime;
    }

    public void setCrawltime(String crawltime) {
        this.crawltime = crawltime;
    }

    public String getModifytime() {
        return modifytime;
    }

    public void setModifytime(String modifytime) {
        this.modifytime = modifytime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
