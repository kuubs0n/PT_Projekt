package pt.webscraping;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import pt.webscraping.entities.ProductView;
import pt.webscraping.entities.Template;

public class GetContentIntentService extends IntentService
{
    private int _downloadStatus = 1;

    // ArrayList of templates received from MainActivity
    private ArrayList<Template> _templates;

    // our search query from input
    private String _searchQuery;

    // Neccesery stuff for Notifications
    private NotificationCompat.Builder _nBuilder;
    private NotificationManager _nManager;

    // random id used to determine our notification
    private static int NOTIFICATION_ID = 3453;

    // ArrayList with result received from our bookshops
    //private ArrayList<ProductView> _results = new ArrayList<>();
    private static ArrayList<ProductView> _results = new ArrayList<>();

    private ArrayList<GetProductsAsyncTask> _asyncTasks = new ArrayList<>();

    private IntentFilter filter = new IntentFilter("pt.webscraping.RESULTS_UPDATE");
    private BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("web.scraper", "BroadcastReceiver - onReceive - RESULTS_UPDATE");

            _downloadStatus += 1;

            updateNotification();

            _results.addAll((ArrayList<ProductView>) intent.getSerializableExtra("products"));
        }
    };

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

        // receive search query typed by user
        _searchQuery = (String) intent.getStringExtra("searchQuery");

        // create and display notification for future use
        createNotification();

        registerReceiver(broadcast, filter);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.d("web.scraper", "GetContentIntentService - onHandleIntent");

        for(Template template : _templates)
        {
            _asyncTasks.add(new GetProductsAsyncTask(this, template, _searchQuery));
        }

        while (_templates.size() != _downloadStatus) { }

    }

    @Override
    public void onDestroy()
    {
        // notification stuff
        Intent targetIntent = new Intent(this, ResultsActivity.class);
        targetIntent.putExtra("listOfProducts", _results);
        targetIntent.putExtra("searchQuery", _searchQuery);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        _nBuilder
                .setContentIntent(contentIntent)
                .setContentText(getString(R.string.notification_download_results_info))
                .setProgress(0, 0, false);
        _nBuilder.mActions.clear();
        _nManager.notify(NOTIFICATION_ID, _nBuilder.build());

        unregisterReceiver(broadcast);

        // broadcast receiver to update our activity that we have all results
        Intent broadcastState = new Intent()
            .setAction("pt.webscraping.RESULTS_READY")
            .putExtra("listOfProducts", _results)
            .putExtra("searchQuery", _searchQuery);
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

        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        PendingIntent cancelIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Action cancelAction = new NotificationCompat.Action.Builder(R.drawable.ic_stat_stop, getString(R.string.cancel), cancelIntent).build();

        _nBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.szymonon)
                .setContentTitle(getString(R.string.notification_download_title))
                .setContentText(notificationText)
                .addAction(cancelAction)
                .setProgress(100, 0, false)
                .setContentIntent(contentIntent);

        _nManager.notify(NOTIFICATION_ID, _nBuilder.build());
    }

    // update current notification
    private void updateNotification()
    {
        float progress = _downloadStatus * 100 / _templates.size();

        String notificationText = getResources().getString(R.string.notification_download_results_text, _downloadStatus, _templates.size());
        _nBuilder
                .setContentText(notificationText)
                .setProgress(100, Math.round(progress), false);

        _nManager.notify(NOTIFICATION_ID, _nBuilder.build());
    }

    // TODO: not yet used
    public void cancelNotification() {
        Toast.makeText(this, "Cancel :<", Toast.LENGTH_SHORT).show();
        _nManager.cancel(NOTIFICATION_ID);

        _asyncTasks.forEach( (task) -> task.cancel(true) );
    }
}