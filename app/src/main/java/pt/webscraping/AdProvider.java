package pt.webscraping;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by Mateusz on 20.04.2017.
 */

public class AdProvider
{
    // only one instance per application
    private static AdProvider instance = null;

    //
    private static Context _context;

    private AdRequest _adRequest;

    private InterstitialAd _mInterstitialAd;

    private AdProvider() { }

    public static AdProvider getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new AdProvider();
        }
        _context = context;
        return instance;
    }

    public void initialize()
    {
        MobileAds.initialize(_context, _context.getString(R.string.admob_app_id));
    }

    public AdRequest createRequest()
    {
        _adRequest =  new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("93A32EEEBE42D84FCA84328BB291232B") // Mati OnePlusOne
                .build();
        return _adRequest;
    }

    public AdRequest getRequest()
    {
        return _adRequest;
    }

    public void createInterstitialAd(AdListener listener)
    {
        _mInterstitialAd = new InterstitialAd(_context);
        _mInterstitialAd.setAdUnitId(_context.getString(R.string.admob_interstitial_ad_unit_id));
        _mInterstitialAd.setAdListener(listener);
    }

    public void showInterstitialAd()
    {
        _mInterstitialAd.show();
    }

    public boolean isReady()
    {
        return (!_mInterstitialAd.isLoading() && !_mInterstitialAd.isLoaded());
    }

    public boolean isLoaded()
    {
        return (_mInterstitialAd != null) && (_mInterstitialAd.isLoaded());
    }

    public void loadAd()
    {
        _mInterstitialAd.loadAd(_adRequest);
    }
}