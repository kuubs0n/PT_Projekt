package pt.webscraping;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.webscraping.entities.ProductView;

/**
 * Created by Kuba on 11-Apr-2017.
 */

public class ResultsActivity extends Activity {

    private ArrayList<ProductView> _listOfProducts;

    @BindView(R.id.recyclerView) RecyclerView _recyclerView;

    @BindView(R.id.textViewEmpty)
    TextView textViewEmpty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);

        _listOfProducts = (ArrayList<ProductView>) getIntent().getSerializableExtra("listOfProducts");

        // dismiss notification when user enter results
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.cancel(3453);

        if(_listOfProducts.isEmpty()){
            textViewEmpty.setVisibility(View.VISIBLE);
        }else {
            textViewEmpty.setVisibility(View.INVISIBLE);
            initializeRecyclerView();
            initializeAdapter();
        }
    }

    private void initializeRecyclerView(){
        _recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        _recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(_listOfProducts);
        _recyclerView.setAdapter(adapter);
    }

}
