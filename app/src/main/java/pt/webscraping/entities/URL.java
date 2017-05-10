package pt.webscraping.entities;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.StringJoiner;

/**
 * Created by szymon on 21.03.2017.
 */

public class URL implements Serializable{
    public String protocol;
    public String domain;
    public String path;
    public String query;
    public String page;
    public String pattern;
    public Boolean encode;


    public String getUrlDepreciated(){
        return MessageFormat.format(pattern, protocol, domain, path, query, page);
    }

    //protocol domainName path
    public String getUrl() throws NoSuchFieldException, IllegalAccessException {
        String[] fields = this.pattern
                                .trim()
                                .split(" ");

        StringBuilder sb = new StringBuilder();

        Class<?> self = this.getClass();

        for(String field : fields){
            sb.append(
                (String) self.getDeclaredField(field).get(this)
            );
        }
        return sb.toString();
    }

    public String getBaseUrl() {
        return protocol + domain;
    }
}