package pt.webscraping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.webscraping.entities.SearchResult;
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

    @BindView(R.id.buttonSearch)
    Button buttonSearch;

    // list of bookshops templates
    ArrayList<Template> templates;
    //list of checkboxes
    ArrayList<CheckBox> checkboxes;

    LinearLayout linearLayoutCheckboxes;


    private Integer clickCounter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("web.scraper", "MainActivity - onCreate.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // odebranie szablonow
        Intent i = getIntent();
        //templates = (ArrayList<Template>) i.getSerializableExtra("templates");

        // filtry księgarni
        prepareFilters();
    }

    public void onResume()  {
        super.onResume();

        editTextQuery.setText("");

        // reklama
        AdProvider ad = AdProvider.getInstance(this);
        ad.initialize();
        ad.createRequest();
        adView.loadAd(ad.getRequest());
    }

    private void prepareFilters() {
        linearLayoutCheckboxes = new LinearLayout(this);
        linearLayoutCheckboxes.setLayoutParams( new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,
        ScrollView.LayoutParams.MATCH_PARENT));
        linearLayoutCheckboxes.setOrientation(LinearLayout.VERTICAL);
        checkboxes = new ArrayList<>();
        for(Template t : SearchResult.templates)
        {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setChecked(true);
            checkBox.setText(t.name);
            linearLayoutCheckboxes.addView(checkBox);
            checkboxes.add(checkBox);
        }
        advFilters.addView(linearLayoutCheckboxes);

    }

    @OnClick(R.id.textViewAdvFilters)
    public void toggleFilters(View view) {
        if(advFilters.getVisibility() == View.VISIBLE){
            advFilters.setVisibility(View.INVISIBLE);
            textViewAdvFilters.setText(R.string.advanced_filters_hidden);
        } else {
            advFilters.setVisibility(View.VISIBLE);
            textViewAdvFilters.setText(R.string.advanced_filters_shown);
        }
    }

    @OnClick(R.id.buttonSearch)
    public void searchClick(View view)
    {
        clearFiltersSetError();
        String query = editTextQuery.getText().toString().trim();

        if(!query.isEmpty())
        {
            QueryHistory.add(this, query);
            // service
            SearchResult.selectedTemplates = getSelectedTemplates();

            if(SearchResult.selectedTemplates.isEmpty()){
                advancedFiltersSetError(getString(R.string.filters_no_selected_error));
                return;
            }

            SearchResult.searchQuery = query;

            Intent mServiceIntent = new Intent(this, GetContentIntentService.class);
            this.startService(mServiceIntent);

            // redirect to loading screen
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            editTextQuery.setError(getString(R.string.form_book_name_entered_invalid));
        }
    }

    private void advancedFiltersSetError(String message){
        buttonSearch.setError(message);
    }

    private void clearFiltersSetError(){
        buttonSearch.setError(null);
    }

    private ArrayList<Template> getSelectedTemplates(){

        ArrayList<Template> selectedTemplates = new ArrayList<>();

        for(CheckBox checkBox : checkboxes){
            if(checkBox.isChecked()) {
                for (Template template : SearchResult.templates) {
                    if (template.name.equals(checkBox.getText().toString())) {
                        selectedTemplates.add(template);
                    }
                }
            }
        }
        return selectedTemplates;
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