package pt.webscraping;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.webscraping.entities.Template;

public class MainActivity extends Activity {

    @BindView(R.id.editTextQuery)EditText editTextQuery;
    @BindView(R.id.adView) AdView adView;
    @BindView(R.id.imageButton) ImageButton imageButton;
    @BindView(R.id.textViewAdvFilters) TextView textViewAdvFilters;
    @BindView(R.id.advFilters) LinearLayout advFilters;

    ArrayList<Template> templates;
    ProgressDialog progressDialog;

    private Integer clickCounter = 0;

    private IntentFilter filter = new IntentFilter("pt.webscraping.RESULTS_READY");
    private BroadcastReceiver broadcast = new GetContentStateReceiver();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("web.scraper", "MainActivity - onCreate.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // reklama
        MobileAds.initialize(this, "ca-app-pub-7454303942261775~2781793443");
        AdRequest adRequest = new AdRequest.Builder()

                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("93A32EEEBE42D84FCA84328BB291232B") // Mati OnePlusOne
                .build();
        adView.loadAd(adRequest);

        // odebranie szablonow
        Intent i = getIntent();
        templates = (ArrayList<Template>) i.getSerializableExtra("templates");

        //
        prepareFilters();
    }

    @Override
    protected void onStart()
    {
        Log.d("web.scraper", "MainActivity - onStart.");
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        Log.d("web.scraper", "MainActivity - onStop.");
        super.onStop();
    }

    @Override
    public void onResume()
    {
        Log.d("web.scraper", "MainActivity - onResume.");
        super.onResume();
        registerReceiver(broadcast, filter);
    }

    @Override
    public void onPause()
    {
        Log.d("web.scraper", "MainActivity - onPause.");
        unregisterReceiver(broadcast);
        super.onPause();
    }

    private void prepareFilters()
    {
        for(Template t : templates)
        {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(t.name);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            advFilters.addView(checkBox, lp);

        }
    }

    @OnClick(R.id.textViewAdvFilters)
    public void toggleFilters(View view){
        if(advFilters.getVisibility() == View.VISIBLE){
            advFilters.setVisibility(View.INVISIBLE);
        }else{
            advFilters.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.buttonSearch)
    public void searchClick(View view)
    {
        String query = editTextQuery.getText().toString().trim();

        if(!query.isEmpty())
        {
            // service
            Intent mServiceIntent = new Intent(this, GetContentIntentService.class);
            mServiceIntent.putExtra("templates", templates);
            mServiceIntent.putExtra("searchQuery", query);
            this.startService(mServiceIntent);

            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);

            // reklamy
        }
        else
        {
            editTextQuery.setError("Enter a valid book name!");
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


    private void prepareProgressDialog(){
        progressDialog = new ProgressDialog(MainActivity.this,
                R.style.ProgressBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Preparing results");
        progressDialog.show();
    }
}