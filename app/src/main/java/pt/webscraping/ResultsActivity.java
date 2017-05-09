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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.webscraping.entities.ProductView;

/**
 * Created by Kuba on 11-Apr-2017.
 */

public class ResultsActivity extends Activity {

    private ArrayList<ProductView> _listOfProducts;
    private String _searchQuery;

    @BindView(R.id.recyclerView) RecyclerView _recyclerView;

    @BindView(R.id.textViewQuery)
    TextView textViewEmpty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);

        _listOfProducts = (ArrayList<ProductView>) getIntent().getSerializableExtra("listOfProducts");
        _searchQuery = (String) getIntent().getSerializableExtra("searchQuery");

        // dismiss notification when user enter results
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.cancel(3453);

        if(_searchQuery != null) {
            textViewEmpty.setText(_searchQuery);
        }

        initializeRecyclerView();
        initializeAdapter();
    }

    private void initializeRecyclerView(){
        _recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        _recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(_listOfProducts, new CustomItemClickListener(){
            @Override
            public void onItemClick(View v, int position){
                openProductInBrowse(position);
            }
        });
        _recyclerView.setAdapter(adapter);
    }

    private void openProductInBrowse(int position) {
        Intent openBrowser = new Intent();
        openBrowser.setAction(Intent.ACTION_VIEW);
        openBrowser.addCategory(Intent.CATEGORY_BROWSABLE);
        openBrowser.setData(Uri.parse(_listOfProducts.get(position).link));
        startActivity(openBrowser);
    }

}
