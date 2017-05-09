package pt.webscraping;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;

import java.util.ArrayList;

import pt.webscraping.entities.ProductView;

public class LoadingActivity extends Activity
{

    private ArrayList<ProductView> _results;

    private IntentFilter filter = new IntentFilter("pt.webscraping.RESULTS_READY");

    private AdProvider _ad;

    private BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, R.string.notification_download_results_info, Toast.LENGTH_SHORT).show();

            _results = (ArrayList<ProductView>) intent.getSerializableExtra("listOfProducts");

            onResultsReady();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // fullscreen ad
        AdListener listener = new AdListener() {
            @Override
            public void onAdLoaded() {
                // TODO: debug purposes
                onResultsReady();
            }
            @Override
            public void onAdClosed() {
                redirectActivity();
            }
        };

        _ad = AdProvider.getInstance(this);
        _ad.initialize();
        _ad.createInterstitialAd(listener);

        if (_ad.isReady()) {
            _ad.loadAd();
        }
    }

    @Override
    public void onResume() {
        Log.d("web.scraper", "LoadingActivity - onResume.");

        super.onResume();

        registerReceiver(broadcast, filter);
    }

    @Override
    public void onPause() {
        Log.d("web.scraper", "LoadingActivity - onPause.");

        unregisterReceiver(broadcast);

        super.onPause();
    }

    public void onResultsReady() {
        Log.d("web.scraper", "LoadingActivity - onResultsReady.");

        if (_ad.isLoaded()) {
            // TODO: only for debug purposes, removed to disable fullscreen ad
            _ad.showInterstitialAd();
            // TODO: only for debug purposes, redirect
            //redirectActivity();
        }
    }

    public void redirectActivity() {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("listOfProducts", _results);
        startActivity(intent);
    }
}