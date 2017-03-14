package pt.webscraping;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;

/**
 * Created by szymon on 13.03.2017.
 */

public class getContentAsyncTask extends AsyncTask<Void, Void, Void> {

    private String url;
    private Document document;


    public getContentAsyncTask(String url){
        this.url = url;
        this.execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            document = Jsoup.connect(url)
                .data("", "odkurzacz")
                .post();

        } catch (IOException e) {
            Log.e("doInBackground", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

    }
}
