package pt.webscraping;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.webscraping.entities.ProductView;
import pt.webscraping.entities.SearchResult;

/**
 * Created by Kuba on 11-Apr-2017.
 */

public class ResultsActivity extends Activity {

    private ArrayList<ProductView> _listOfProducts = new ArrayList<>();

    private String _searchQuery;

    @BindView(R.id.textViewQuery)
    TextView textViewQuery;

    @BindView(R.id.linearLayoutResults)
    LinearLayout linearLayout;

    // random id used to determine our notification
    private static int NOTIFICATION_ID = 3453;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);

        _searchQuery = SearchResult.searchQuery;

        // dismiss notification when user enter results
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.cancel(NOTIFICATION_ID);

        if(SearchResult.results.keySet().isEmpty()){
            textViewQuery.setVisibility(View.VISIBLE);
            textViewQuery.setText("NO RESULTS");
        }else{
            int counter = 0;
            for(String templateName : SearchResult.results.keySet()){
                counter += SearchResult.results.get(templateName).size();
                TextView textViewShopName = new TextView(this);
                textViewShopName.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                textViewShopName.setGravity(Gravity.CENTER_HORIZONTAL);
                textViewShopName.setText(templateName + "(" + SearchResult.results.get(templateName).size() + ")");
                linearLayout.addView(textViewShopName);

                RecyclerView recyclerView = new RecyclerView(this);
                recyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                initializeRecyclerView(recyclerView);
                initializeAdapter(recyclerView, SearchResult.results.get(templateName));
                linearLayout.addView(recyclerView);
            }
            textViewQuery.setText(_searchQuery + "(" + counter + ")");
        }
    }

    private void initializeRecyclerView(RecyclerView recycler){
        recycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(linearLayoutManager);
    }

    private void initializeAdapter(RecyclerView recycler, ArrayList<ProductView> products) {
        RVAdapter adapter = new RVAdapter(products, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                openProductInBrowser(position);
            }
        });
        recycler.setAdapter(adapter);
    }

    private void openProductInBrowser(int position){
        Intent openBrowser = new Intent();
        openBrowser.setAction(Intent.ACTION_VIEW);
        openBrowser.addCategory(Intent.CATEGORY_BROWSABLE);
        openBrowser.setData(Uri.parse(_listOfProducts.get(position).link));
        startActivity(openBrowser);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
        finish();
    }


}
