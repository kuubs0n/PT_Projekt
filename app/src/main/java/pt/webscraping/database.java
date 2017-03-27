package pt.webscraping;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pt.webscraping.entities.Template;

/**
 * Created by szymon on 20.03.2017.
 */

public class database {

    private FirebaseDatabase database;

    public database(){

        database = FirebaseDatabase.getInstance();
        this.getTemplates();
    }

    public void getTemplates(){

        DatabaseReference templatesRef = database.getReference("templates");

        templatesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Template> templateList = new ArrayList<>();

                for (DataSnapshot t: dataSnapshot.getChildren()) {
                    new Template(t);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("loadPost:onCancelled", databaseError.toException().toString());
            }
        });
    }


}
