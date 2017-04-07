package pt.webscraping;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;

import pt.webscraping.entities.Product;

/**
 * Created by szymon on 13.03.2017.
 */

public class GetContentAsyncTask extends AsyncTask<Void, Void, Void> {

    private String url;
    private Document document;

    private ArrayList<Product> arrayOfProducts = new ArrayList<Product>();

    public GetContentAsyncTask(String baseUrl, String input) throws UnsupportedEncodingException {
        this.url = baseUrl + URLEncoder.encode(input, "UTF-8");

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            document = Jsoup.connect(url).get();
            // na sztywno dla księgani matras.pl
            Elements products = document.select("div.s-item div.s-item-outer");



            for(Element product : products){
                Log.d("PRODUKT URL: ", product.select("span.right-side a:first-child").text().toString());

                Product p = new Product();
                p.setTitle(product.select("span.item-title").text().toString());
                p.setAuthor(product.select("span.item-author").text().toString());
                p.setLink(product.select("span.right-side a").first().attr("href"));
                p.setPrice(product.select("span.item-price").text().toString()); // item-price
                p.setPhotoURL(product.select("span.image img").attr("data-original").toString()); // span.image img[src]

                this.arrayOfProducts.add(p);
            }

        }
        catch (IOException e) {
            Log.e("GetContentAsyncTask", "doInBackground Unhandled Exception" + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO: przesłanie arraylist do widoku - Kuba
    }
}
