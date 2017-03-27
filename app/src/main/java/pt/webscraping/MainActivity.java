package pt.webscraping;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.editTextQuery)EditText editTextQuery;
    @BindView(R.id.adView) AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //reklama
        MobileAds.initialize(this, "ca-app-pub-7454303942261775~2781793443");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        new database();
    }

    @OnClick(R.id.buttonSearch)
    public void search(View view){

        String query = editTextQuery.getText().toString();

        if(!query.isEmpty()){

        }
    }
}