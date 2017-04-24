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
                    template.product.title.attribute == null ? p.select(template.product.title.element).text()
                            : p.select(template.product.title.element).attr(template.product.title.attribute),
                    template.product.author.attribute == null ? p.select(template.product.author.element).text()
                            : p.select(template.product.author.element).attr(template.product.author.attribute),
                    template.product.link.attribute == null ? p.select(template.product.link.element).text()
                            : p.select(template.product.link.element).attr(template.product.link.attribute),
                    template.product.price.attribute == null ? p.select(template.product.price.element).text()
                            : p.select(template.product.price.element).attr(template.product.price.attribute),
                    template.product.photoURL.attribute == null ? p.select(template.product.photoURL.element).text()
                            : p.select(template.product.photoURL.element).attr(template.product.photoURL.attribute)
            ));
        }
        return products;
    }
}
