package pt.webscraping;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import pt.webscraping.entities.Template;

/**
 * Created by szymon on 11.04.2017.
 */

public class GetDocumentAsyncTask extends AsyncTask<Void, Void, Document> {

    private Template template;
    private String query;
    private IGetDocumentAsync callback;


    public GetDocumentAsyncTask(Template template, String query, IGetDocumentAsync callback) {
        this.template = template;
        this.query = query;
        this.callback = callback;
        this.execute();
    }

    @Override
    protected Document doInBackground(Void... params) {

        Document doc = null;

        try {
            template.url.query += URLEncoder.encode(query, "UTF-8");
            template.url.page += template.pagination.startsWith;

            String foo = template.url.getUrl();
            doc = Jsoup.connect(template.url.getUrl()).get();

        } catch (IOException e) {
            Log.e("getDocuments:Jsoup.get", e.getMessage());
        }
        return doc;
    }

    @Override
    protected void onPostExecute(Document doc) {
        callback.process(doc);
    }
}