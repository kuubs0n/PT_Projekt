package pt.webscraping.entities;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Created by szymon on 21.03.2017.
 */

public class URL implements Serializable{
    public String protocol;
    public String domainName;
    public String path;
    public String query;
    public String page;
    public String pattern;
    public Boolean encode;


    public String getUrl(){
        return MessageFormat.format(pattern, protocol, domainName, path, query, page);
    }

    public String getBaseUrl() {
        return protocol + domainName;
    }
}