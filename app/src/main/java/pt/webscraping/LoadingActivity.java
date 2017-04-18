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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import pt.webscraping.entities.ProductView;

public class LoadingActivity extends Activity {

    //reklama pełnoekranowa
    private InterstitialAd _mInterstitialAd;

    private ArrayList<ProductView> _results;

    private IntentFilter filter = new IntentFilter("pt.webscraping.RESULTS_READY");
    private BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Pobrano wyniki wyszukiwania...", Toast.LENGTH_SHORT).show();

            _results = (ArrayList<ProductView>) intent.getSerializableExtra("listOfProducts");

            onResultsReady();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // fullscreen ad
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        _mInterstitialAd = new InterstitialAd(this);
        _mInterstitialAd.setAdUnitId(getString(R.string.admob_interstitial_ad_unit_id));

        _mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                showLoadingScreen();
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("93A32EEEBE42D84FCA84328BB291232B") // mati one plus one
                .build();

        _mInterstitialAd.loadAd(adRequest);
        /*
        if (!_mInterstitialAd.isLoading() && !_mInterstitialAd.isLoaded()) {

            _mInterstitialAd.loadAd(adRequest);
        }
        */
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        /*
        if (_mInterstitialAd != null && _mInterstitialAd.isLoaded())
        {
            _mInterstitialAd.show();
        }
        else
        {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
        */
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

    public void onResultsReady()
    {
        Log.d("web.scraper", "LoadingActivity - onResultsReady.");

        _mInterstitialAd = null;

        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("listOfProducts", _results);
        startActivity(intent);
    }

    private void showLoadingScreen()
    {
        Toast.makeText(this, "Ad closed, display loading icon...", Toast.LENGTH_SHORT).show();
    }
}
