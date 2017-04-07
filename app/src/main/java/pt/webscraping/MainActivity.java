package pt.webscraping;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.editTextQuery)EditText editTextQuery;
    @BindView(R.id.adView) AdView adView;
    @BindView(R.id.imageButton) ImageButton imageButton;

    private Integer clickCounter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //reklama
        MobileAds.initialize(this, "ca-app-pub-7454303942261775~2781793443");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("93A32EEEBE42D84FCA84328BB291232B") // Mati OnePlusOne
                .build();
        adView.loadAd(adRequest);

        new Database();
    }

    @OnClick(R.id.buttonSearch)
    public void searchItems(View view) {

        String query = editTextQuery.getText().toString();

        if(!query.isEmpty()) {
            Log.d("MainActivity", "Search box value: + " + query);

            try {
                String url = "http://www.matras.pl/szukaj/?query=";

                GetContentAsyncTask task = new GetContentAsyncTask(url, query);
                task.execute();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e("MainActivity", "Unhandled Exception in GetContentAsyncTask :<");
            }

        }
    }

    @OnClick(R.id.imageButton)
    public void easterEgg(View view) {
        clickCounter++;
        if(clickCounter > 5) {
            imageButton.setImageResource(R.drawable.szymonon);
            Toast.makeText(getApplicationContext(), "Nawiedził Cię Szymon Developer! Wpłać 2 złote i korzystaj dowoli! :)", Toast.LENGTH_LONG).show();
        }
    }
}