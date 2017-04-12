package pt.webscraping;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.webscraping.entities.Product;
import pt.webscraping.entities.ProductView;
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

        //odebranie szablonow
        Intent i = getIntent();
        templates = (ArrayList<Template>) i.getSerializableExtra("templates");

        prepareFilters();
    }

    private void prepareFilters(){
        for(Template t : templates){
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
    public void searchClick(View view) {

        ArrayList<ProductView> results = new ArrayList<>();
        String query = editTextQuery.getText().toString();

        if(!query.isEmpty()) {
            for(Template t : templates){
                new GetDocumentAsyncTask(t, query, (Document doc) -> {
                    prepareProgressDialog();
                    results.addAll(ParseHTML.parseProducts(doc, t));
                    Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                    intent.putExtra("listOfProducts", results);
                    startActivity(intent);
                    progressDialog.dismiss();
                });
            }
            //reklamy
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