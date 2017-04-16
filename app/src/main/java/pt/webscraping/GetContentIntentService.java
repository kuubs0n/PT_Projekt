package pt.webscraping;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import pt.webscraping.entities.ProductView;
import pt.webscraping.entities.Template;

public class GetContentIntentService extends IntentService
{
    // ArrayList of templates received from MainActivity
    private ArrayList<Template> _templates;

    // our search query from input
    private String _searchQuery;

    // first template is first step, the next one is the next one step in downloading data
    private int _dStep = 1;

    // Neccesery stuff for Notifications
    private NotificationCompat.Builder _nBuilder;
    private NotificationManager _nManager;

    // random id used to determine our notification
    private static int NOTIFICATION_ID = 3453;

    private ArrayList<ProductView> _results = new ArrayList<>();

    public GetContentIntentService()
    {
        super("GetContentIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("web.scraper", "GetContentIntentService - start.");

        // receive templates to use with
        _templates = (ArrayList<Template>) intent.getSerializableExtra("templates");
        _searchQuery = (String) intent.getStringExtra("searchQuery");

        // create and display notification for future use
        createNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.d("web.scraper", "GetContentIntentService - onHandleIntent");

        for(Template template : _templates)
        {
            new GetDocumentAsyncTask(template, _searchQuery, (Document doc) -> {
                _results.addAll(ParseHTML.parseProducts(doc, template));


            });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            updateNotification();
            _dStep++;
        }

    }




    @Override
    public void onDestroy()
    {
        // notification stuff
        Intent targetIntent = new Intent(this, ResultsActivity.class);
        targetIntent.putExtra("listOfProducts", _results);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        _nBuilder
                .setContentIntent(contentIntent)
                .setContentText(getString(R.string.notification_download_results_info))
                //.setNumber(_dStep)
                .setProgress(0, 0, false);
        _nManager.notify(NOTIFICATION_ID, _nBuilder.build());

        // broadcast receiver
        Intent broadcastState = new Intent();
        broadcastState.setAction("pt.webscraping.RESULTS_READY");
        broadcastState.putExtra("results", _results);
        sendBroadcast(broadcastState);

        Log.d("web.scraper", "GetContentIntentService - done.");
    }

    /*
     * Notifications
     */
    private void createNotification()
    {
        _nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String notificationText = getResources().getString(R.string.notification_download_results_text, 1, _templates.size());

        _nBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.szymonon)
                .setContentTitle(getString(R.string.notification_download_title))
                .setContentText(notificationText)
                .setProgress(100, 0, false);

        // TODO: Przekierowanie na okno ładowania wyników z reklamą i statusem czy coś...
        //Intent targetIntent = new Intent(this, ResultsActivity.class);
        Intent targetIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        _nBuilder.setContentIntent(contentIntent);

        _nManager.notify(NOTIFICATION_ID, _nBuilder.build());
    }

    // update current notification
    private void updateNotification()
    {
        int progress = _dStep / _templates.size() * 100;

        String notificationText = getResources().getString(R.string.notification_download_results_text, _dStep, _templates.size());
        _nBuilder
                .setContentText(notificationText)
                //.setNumber(_dStep)
                .setProgress(100, progress, false);

        // Because the ID remains unchanged, the existing notification is
        // updated.
        _nManager.notify(NOTIFICATION_ID, _nBuilder.build());
    }
}