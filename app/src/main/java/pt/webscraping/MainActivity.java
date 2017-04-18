package pt.webscraping;

import android.app.Activity;
import android.content.Intent;
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

    @BindView(R.id.editTextQuery)
    EditText editTextQuery;

    @BindView(R.id.adView)
    AdView adView;

    @BindView(R.id.imageButton)
    ImageButton imageButton;

    @BindView(R.id.textViewAdvFilters)
    TextView textViewAdvFilters;

    @BindView(R.id.advFilters)
    LinearLayout advFilters;

    // list of bookshops templates
    ArrayList<Template> templates;


    private Integer clickCounter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("web.scraper", "MainActivity - onCreate.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // reklama
        MobileAds.initialize(this, getString(R.string.admob_app_id));
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

    private void prepareFilters() {
        for(Template t : templates)
        {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(t.name);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            advFilters.addView(checkBox, lp);

        }
    }

    @OnClick(R.id.textViewAdvFilters)
    public void toggleFilters(View view) {
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
            Intent mServiceIntent = new Intent(this, GetContentIntentService.class)
                .putExtra("templates", templates)
                .putExtra("searchQuery", query);
            this.startService(mServiceIntent);

            // redirect to loading screen
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);
        }
        else
        {
            editTextQuery.setError(getString(R.string.form_book_name_entered_invalid));
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