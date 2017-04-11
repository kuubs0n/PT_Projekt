package pt.webscraping.entities;

import java.io.Serializable;

/**
 * Created by szymon on 21.03.2017.
 */

public class Product implements Serializable{
    public String selector;
    public String title;
    public String author;
    public String link;
    public String price;
    public String photoURL;

    public Product(String selector, String title, String author, String link, String price, String photoURL){
        this.selector = selector;
        this.title = title;
        this.author = author;
        this.link = link;
        this.price = price;
        this.photoURL = photoURL;
    }

    public Product(){}
}