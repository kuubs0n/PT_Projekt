package pt.webscraping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pt.webscraping.entities.Template;

/**
 * Created by szymon on 10.04.2017.
 */

public class StartActivity extends Activity {

    public class RefreshListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            initializeTemplates();
            // Code to undo the user's last action
            Log.d("web.scraper", "RefreshListener - onClick");
        }
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initializeTemplates();
    }

    public void initializeTemplates()
    {
        if (NetworkConnectionChecker.check(this))
        {
            getTemplates();
        }
        else
        {
            Snackbar refreshSnackbar = Snackbar.make(
                    findViewById(R.id.CoordinatorLayout),
                    R.string.results_no_internet_connection,
                    Snackbar.LENGTH_INDEFINITE);
            refreshSnackbar.setAction(R.string.refresh, new RefreshListener());
            refreshSnackbar.show();
        }
    }

    public void onResume()
    {
        super.onResume();
        initializeTemplates();
    }

    public void switchActivity(ArrayList<Template> templateList){

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("templates", templateList);

        startActivity(intent);
        this.finish();
    }

    public void getTemplates() {

        ArrayList<Template> templateList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference templatesRef = database.getReference("templates");

        templatesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot t : dataSnapshot.getChildren()) {
                    templateList.add(new Template(t));
                }
                switchActivity(templateList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("loadPost:onCancelled", databaseError.toException().toString());
            }
        });
    }
}
