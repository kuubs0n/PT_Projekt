package pt.webscraping;

import android.os.AsyncTask;


/**
 * Created by szymon on 13.03.2017.
 */

public class GetContentAsyncTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //sprobuj jeszcze raz
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO: przesłanie arraylist do widoku - Kuba
    }
}
