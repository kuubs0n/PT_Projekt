package pt.webscraping;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pt.webscraping.entities.ListItem;
import pt.webscraping.entities.SearchResult;
import pt.webscraping.entities.Template;

/**
 * Created by szymon on 09.05.2017.
 */

public class GetProductsAsyncTask extends AsyncTask<Void, Void, Void> {

    private Template template;
    private Context context;
    private String query;

    public GetProductsAsyncTask(Context context, Template template, String query) {
        this.context = context;
        this.template = template;
        this.query = query;
        this.execute();
    }

    private String encode(String query) throws UnsupportedEncodingException {
        return URLEncoder.encode(query, "UTF-8");
    }

    @Override
    protected Void doInBackground(Void... params) {

        Document doc = null;
        ArrayList<ListItem> products = new ArrayList<>();

        boolean isNextPage = true;

        try {
            String url = template.url.getUrl(encode(query));

            while(true) {

                doc = Jsoup.connect(url).get();
                products.addAll(ParseHTML.parseProducts(doc, template));

                Elements nextPageEl = doc.select(template.pagination.next);

                if(nextPageEl.isEmpty()){
                    break;
                }
                if(nextPageEl.first().attr("abs:href").equals(doc.location())){
                    break;
                }
                url = nextPageEl.first().attr("abs:href");
            }

        } catch (Exception e) {
            Log.e("getDocuments:Jsoup.get", e.getMessage());
        }

        Comparator<ListItem> comparator = new Comparator<ListItem>() {
            @Override
            public int compare(ListItem o1, ListItem o2) {
                return ((Integer) (int) Math.round(Double.parseDouble(o1.price)) - (int) Math.round(Double.parseDouble(o2.price)));
            }
        };

        Collections.sort(products, comparator);

        SearchResult.results.put(template.name, products);

        return null;
    }

    @Override
    protected void onPostExecute(Void params) {
        Log.d("web.scraper", "GetProductsAsyncTask - onPostExecute");
        // broadcast receiver send!

        Intent broadcastState = new Intent()
                .setAction("pt.webscraping.RESULTS_UPDATE");
        this.context.sendBroadcast(broadcastState);
    }
}

