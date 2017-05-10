package pt.webscraping;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import pt.webscraping.entities.ProductView;
import pt.webscraping.entities.Template;

/**
 * Created by szymon on 09.05.2017.
 */

public class GetProductsAsyncTask extends AsyncTask<Void, Void, ArrayList<ProductView>> {

    private Template template;

    public GetProductsAsyncTask(Template template, String query) {
        this.template = template;
        includeQuery(query);
        this.execute();
    }

    private void includeQuery(String query) {
        template.url.query += query;
    }

    @Override
    protected ArrayList<ProductView> doInBackground(Void... params) {

        Document doc = null;
        ArrayList<ProductView> products = new ArrayList<>();

        boolean isNextPage = true;

        try {
            String url = template.url.getUrl();

            while(isNextPage == true) {

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
        return products;
    }

    @Override
    protected void onPostExecute(ArrayList<ProductView> products) {
        GetContentIntentService.downloaded += 1;
        GetContentIntentService._results.addAll(products);
    }
}

