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

/**
 * Created by szymon on 13.03.2017.
 */

public class GetContentAsyncTask extends AsyncTask<Void, Void, Void> {

    private String url;
    private Document document;

    public GetContentAsyncTask(String baseUrl, String input) throws UnsupportedEncodingException {

        this.url = baseUrl + URLEncoder.encode(input, "UTF-8");
        this.execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            document = Jsoup.connect(url).get();
            Elements products = document.select("div.Product-holder-grid");

            for(Element product : products){
                Log.d("PRODUKT: ", product.select("a[title]").text().toString());
            }

        }
        catch (IOException e) {
            Log.e("doInBackground", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

    }
}
