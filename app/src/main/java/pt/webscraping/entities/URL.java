package pt.webscraping.entities;

import java.io.Serializable;

/**
 * Created by szymon on 21.03.2017.
 */

public class URL implements Serializable{
    public String protocol;
    public String domainName;
    public String path;
    public String query;
    public String page;


    public String getUrl(){
        return protocol + domainName + path + query + page;
    }
}