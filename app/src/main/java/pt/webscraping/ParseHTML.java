package pt.webscraping;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collection;

import pt.webscraping.entities.ProductView;
import pt.webscraping.entities.Template;
import pt.webscraping.entities.URL;

/**
 * Created by Mateusz on 12-Apr-2017.
 */

public class ParseHTML {

    public static ArrayList<ProductView> parseProducts(Document doc, Template template){
        ArrayList<ProductView> products = new ArrayList<>();

        ArrayList<Element> productsEl  = doc.select(template.product.selector);

        for(Element product : productsEl){
            products.add(new ProductView(
                    product.select(template.product.title).attr("data-name"),
                    product.select(template.product.author).text(),
                    makeBookUrl(template.url, product.select(template.product.link).attr("href")),
                    product.select(template.product.price).text(),
                    product.select(template.product.photoURL).attr("src")
            ));
        }
        return products;
    }

    public static String makeBookUrl(URL url, String link) {
        return url.getBaseUrl() + link;
    }
}
