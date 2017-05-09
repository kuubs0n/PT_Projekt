package pt.webscraping;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import pt.webscraping.entities.ProductView;
import pt.webscraping.entities.Template;

/**
 * Created by Mateusz on 12-Apr-2017.
 */

public class ParseHTML {

    public static ArrayList<ProductView> parseProducts(Document doc, Template template){
        ArrayList<ProductView> products = new ArrayList<>();

        ArrayList<Element> productsEl  = doc.select(template.product.element);

        for(Element p : productsEl){

            products.add(new ProductView(
                    p.select(template.product.title).text(),
                    p.select(template.product.author).text(),
                    p.select(template.product.link).attr("abs:href"),
                    p.select(template.product.price).text(),
                    p.select(template.product.photoURL).attr("src")
            ));
        }
        return products;
    }
}
