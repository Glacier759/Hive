package com.hive.Extract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by mika on 14-5-20.
 */
public class ParseP {
    private  Document doc;
    public ParseP(Document doc){
        this.doc = doc;
    }
    public String doParseP(){
        Document doc1 = Jsoup.parse(doc.html());
        Elements pTags = doc1.getElementsByTag("p");
        String Text = "";
        for(Element pTag : pTags){
            String TagText = pTag.text();
            if(Text == ""){
                Text = TagText;
                continue;
            }
            if(TagText.length() > 0){
                Text = Text + "\n" + TagText;
            }

        }
        return Text;
    }

}
