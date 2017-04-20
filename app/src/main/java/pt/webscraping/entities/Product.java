package pt.webscraping.entities;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

/**
 * Created by szymon on 21.03.2017.
 */

public class Product implements Serializable{
    public String element;
    public Node title;
    public Node author;
    public Node link;
    public Node price;
    public Node photoURL;

    public Product(DataSnapshot dataSnapshot){
        this.element = dataSnapshot.child("element").getValue(String.class);
        this.title = dataSnapshot.child("title").getValue(Node.class);
        this.author = dataSnapshot.child("author").getValue(Node.class);
        this.link = dataSnapshot.child("link").getValue(Node.class);
        this.price = dataSnapshot.child("price").getValue(Node.class);
        this.photoURL = dataSnapshot.child("photoURL").getValue(Node.class);
    }
}