package pt.webscraping.entities;

import java.io.Serializable;

/**
 * Created by Mateusz on 12-Apr-2017.
 */

public class ListItem implements Serializable{
    public String title;
    public String author;
    public String link;
    public String price;
    public String photoURL;

    public ListItem(String title, String author, String link, String price, String photoURL){
        this.title = title;
        this.author = author;
        this.link = link;
        this.price = price;
        this.photoURL = photoURL;
    }
}