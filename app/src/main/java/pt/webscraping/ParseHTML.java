package pt.webscraping;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collection;

import pt.webscraping.entities.ProductView;
import pt.webscraping.entities.Template;

/**
 * Created by Mateusz on 12-Apr-2017.
 */

public class ParseHTML {

    public static ArrayList<ProductView> parseProducts(Document doc, Template template){
        ArrayList<ProductView> products = new ArrayList<>();

        ArrayList<Element> productsEl  = doc.select(template.product.selector);

        for(Element product : productsEl){
            products.add(new ProductView(
                    product.select(template.product.title).toString(),
                    product.select(template.product.author).toString(),
                    product.select(template.product.link).toString(),
                    product.select(template.product.price).toString(),
                    product.select(template.product.photoURL).toString()
            ));
        }
        return products;
    }
}
