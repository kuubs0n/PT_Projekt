package pt.webscraping;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.webscraping.entities.ListItem;
import pt.webscraping.entities.SearchResult;
import pt.webscraping.entities.Template;

/**
 * Created by Kuba on 11-Apr-2017.
 */

public class ResultsActivity extends Activity {

    private ArrayList<ListItem> _listOfProducts = new ArrayList<>();
    private String _searchQuery;

    @BindView(R.id.textViewQuery)
    TextView textViewQuery;

    @BindView(R.id.layoucik)
    LinearLayout linearLayout;

    // random id used to determine our notification
    private static int NOTIFICATION_ID = 3453;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_bak);
        ButterKnife.bind(this);

        _searchQuery = SearchResult.searchQuery;

        // dismiss notification when user enter results
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.cancel(NOTIFICATION_ID);
        int counter = 0;
        if(SearchResult.results.keySet().isEmpty()){
            textViewQuery.setVisibility(View.VISIBLE);
            textViewQuery.setText("NO RESULTS");
        }
        else{
            textViewQuery.setText(_searchQuery);

            for(Template template : SearchResult.selectedTemplates){
                List<ListItem> results = SearchResult.results.get(template.name);
                Header header = getHeader(template.name + "(" + String.valueOf(results.size()) + ")");

                RecyclerView recyclerViewTemp = new RecyclerView(this);
                recyclerViewTemp.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));

                MyRecyclerAdapter adapterTemp = new MyRecyclerAdapter(this, header, results);
                LinearLayoutManager linearLayoutManagerTemp = new LinearLayoutManager(this);
                recyclerViewTemp.setLayoutManager(linearLayoutManagerTemp);
                recyclerViewTemp.setAdapter(adapterTemp);
                linearLayout.addView(recyclerViewTemp);

                counter += results.size();
            }
            textViewQuery.setText(textViewQuery.getText() + " ( " + counter + " )");
        }
    }

    private void openProductInBrowser(int position){
        Intent openBrowser = new Intent();
        openBrowser.setAction(Intent.ACTION_VIEW);
        openBrowser.addCategory(Intent.CATEGORY_BROWSABLE);
        openBrowser.setData(Uri.parse(_listOfProducts.get(position).link));
        startActivity(openBrowser);
    }

    public void openProductInBrowser(String url){
        Intent openBrowser = new Intent();
        openBrowser.setAction(Intent.ACTION_VIEW);
        openBrowser.addCategory(Intent.CATEGORY_BROWSABLE);
        openBrowser.setData(Uri.parse(url));
        startActivity(openBrowser);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
        finish();
    }

    public Header getHeader(String headerName){
        Header header = new Header();
        header.setHeader(headerName);
        return header;
    }
}
