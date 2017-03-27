package pt.webscraping.entities;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by szymon on 21.03.2017.
 */

public class Template {
    public String name;
    public URL url;
    public Product product;
    public Pagination pagination;

    public Template(DataSnapshot dataSnapshot){

        this.name = dataSnapshot.child("name").getValue(String.class);
        this.url = dataSnapshot.child("URL").getValue(URL.class);
        this.product = dataSnapshot.child("product").getValue(Product.class);
        this.pagination = dataSnapshot.child("pagination").getValue(Pagination.class);
    }
}