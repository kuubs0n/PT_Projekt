package pt.webscraping;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by szymon on 20.03.2017.
 */

public class Database {

    private FirebaseDatabase database;

    public Database(){

        database = FirebaseDatabase.getInstance();
        this.getTemplates();
    }

    public void getTemplates(){

        DatabaseReference templatesRef = database.getReference("templates");

        templatesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Integer x = 2;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("loadPost:onCancelled", databaseError.toException().toString());
            }
        });
    }


}
