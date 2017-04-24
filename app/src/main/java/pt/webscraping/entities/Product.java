package pt.webscraping.entities;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

/**
 * Created by szymon on 21.03.2017.
 */

public class Product implements Serializable {
    public String element;
    public String title;
    public String author;
    public String link;
    public String price;
    public String photoURL;
}