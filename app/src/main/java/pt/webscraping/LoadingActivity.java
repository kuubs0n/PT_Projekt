package pt.webscraping;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class LoadingActivity extends Activity {

    private InterstitialAd _mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // fullscreen ad
        MobileAds.initialize(this, "ca-app-pub-7454303942261775~2781793443");
        _mInterstitialAd = new InterstitialAd(this);
        _mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        _mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                showLoadingScreen();
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("93A32EEEBE42D84FCA84328BB291232B")
                .build();

        if (!_mInterstitialAd.isLoading() && !_mInterstitialAd.isLoaded()) {
            _mInterstitialAd.loadAd(adRequest);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (_mInterstitialAd != null && _mInterstitialAd.isLoaded())
        {
            _mInterstitialAd.show();
        }
        else
        {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoadingScreen()
    {
        Toast.makeText(this, "Ad closed, display loading icon...", Toast.LENGTH_SHORT).show();
    }
}
