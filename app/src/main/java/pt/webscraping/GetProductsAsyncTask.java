package pt.webscraping;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import pt.webscraping.entities.ProductView;
import pt.webscraping.entities.Template;

/**
 * Created by szymon on 09.05.2017.
 */

public class GetProductsAsyncTask extends AsyncTask<Void, Void, Document> {

    private List<Template> templates;

    public GetProductsAsyncTask(List<Template> templates, String query) {
        this.templates = templates;
        setInitValues(query);
        this.execute();
    }

    private void setInitValues(String query) {
        for(Template template : templates) {
            template.url.query += query;
            template.url.page += template.pagination.startsWith.toString();
        }
    }

    @Override
    protected Document doInBackground(Void... params) {

        Document doc = null;
        ArrayList<ProductView> products = new ArrayList<>();

        boolean isNextPage = true;

        try {
            for(Template template : templates) {
                String url = template.url.getUrl();
                while(isNextPage == true) {

                    doc = Jsoup.connect(url).get();
                    products.addAll(ParseHTML.parseProducts(doc, template));

                    Elements nextPageEl = doc.select(template.pagination.nextLink);
                    isNextPage = ! nextPageEl.isEmpty()
                                || nextPageEl.first().attr("abs:href") != url;
                    if(isNextPage) {
                        url = nextPageEl.first().attr("abs:href");
                    }
                }
            }

        } catch (Exception e) {
            Log.e("getDocuments:Jsoup.get", e.getMessage());
        }
        return doc;
    }

    @Override
    protected void onPostExecute(Document doc) {
        GetContentIntentService.isDone = true;
    }
}

