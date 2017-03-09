package pt.webscraping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread downloadThread = new Thread() {
            public void run() {
                Document doc;
                try {
                    doc = Jsoup.connect("http://google.pl/").get();
                    String title = doc.title();
                    Log.d("asd", title);
                } catch (IOException e) {
                    Log.d("asd", "NIE DZIALA");
                }
            }
        };
        downloadThread.start();

    }
}
