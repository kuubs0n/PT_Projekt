package pt.webscraping;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            try {

                String price = p.select(template.product.price).first().ownText().trim();

                products.add(new ProductView(
                        p.select(template.product.title).text(),
                        p.select(template.product.author).text(),
                        p.select(template.product.link).attr("abs:href"),
                        price.replaceAll("[^0-9?!\\,\\.]","").replaceAll(",", "."),
                        p.select(template.product.photoURL).attr("abs:src")
                ));
            } catch(Exception e){
                Log.d("PATTERN EXCEPTION", e.getMessage());
                continue;
            }
        }
        return products;
    }
}
