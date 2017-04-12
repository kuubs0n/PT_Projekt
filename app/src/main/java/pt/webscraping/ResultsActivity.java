package pt.webscraping;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.webscraping.entities.Product;
import pt.webscraping.entities.ProductView;

/**
 * Created by Kuba on 11-Apr-2017.
 */

public class ResultsActivity extends Activity {

    private ArrayList<ProductView> _listOfProducts;

    @BindView(R.id.recyclerView) RecyclerView _recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);

        _listOfProducts = (ArrayList<ProductView>) getIntent().getSerializableExtra("listOfProducts");

        initializeRecyclerView();
        initializeData();
        initializeAdapter();
    }

    private void initializeRecyclerView(){
        _recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        _recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initializeData() {
        /*_listOfProducts = new ArrayList<>();
        _listOfProducts.add(new Product("selector", "Kaczynski", "Autor: Tusk", "link", "19,99 PLN", ""));
        _listOfProducts.add(new Product("selector2", "Kaczynski", "Autor: Tusk", "link2", "29,99 PLN", ""));*/
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(_listOfProducts);
        _recyclerView.setAdapter(adapter);
    }

}
