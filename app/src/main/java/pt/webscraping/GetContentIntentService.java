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
    private ArrayList<ProductView> _results = new ArrayList<>();

    // ArrayList of async tasks, the number depends on templates (bookshops) count
    private ArrayList<GetProductsAsyncTask> _asyncTasks = new ArrayList<>();

    // intent filter when broadcast receiver inform about updated data
    private IntentFilter filter = new IntentFilter("pt.webscraping.RESULTS_UPDATE");

    private BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // increment to inform that async task ended his work
            _downloadStatus += 1;
            Log.d("web.scraper", "downloadStatus = " + _downloadStatus);
            // update notification text with progress
            updateNotification();

            // add results received from async task
            _results.addAll((ArrayList<ProductView>) intent.getSerializableExtra("products"));
        }
    };

    public GetContentIntentService()
    {
        super("GetContentIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // receive templates to use with
        _templates = (ArrayList<Template>) intent.getSerializableExtra("templates");

        // receive search query typed by user
        _searchQuery = (String) intent.getStringExtra("searchQuery");

        // create and display notification for future use
        createNotification();

        // register receiver when service is starting
        registerReceiver(broadcast, filter);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("web.scraper", "GetContentIntentService - onHandleIntent");

        // loop through templates to create async tasks
        for(Template template : _templates)
        {
            _asyncTasks.add(new GetProductsAsyncTask(this, template, _searchQuery));
        }

        // service is waiting to complete all downloads in async tasks
        while (_templates.size() != _downloadStatus) { }
    }

    @Override
    public void onDestroy() {
        _asyncTasks.clear();

        // notification stuff
        Intent targetIntent = new Intent(this, ResultsActivity.class);
        targetIntent.putExtra("listOfProducts", _results);
        targetIntent.putExtra("searchQuery", _searchQuery);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        _nBuilder
                .setContentIntent(contentIntent)
                .setContentText(getString(R.string.notification_download_results_info))
                .setProgress(0, 0, false);

        // remove cancel action
        _nBuilder.mActions.clear();

        _nManager.notify(NOTIFICATION_ID, _nBuilder.build());

        // disable receiver because service is stopping
        unregisterReceiver(broadcast);

        // broadcast receiver to update our activity that we have all results
        Intent broadcastState = new Intent()
            .setAction("pt.webscraping.RESULTS_READY")
            .putExtra("listOfProducts", _results)
            .putExtra("searchQuery", _searchQuery);
        sendBroadcast(broadcastState);
    }

    /*
     * Notifications
     */
    private void createNotification() {
        _nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String notificationText = getResources().getString(R.string.notification_download_results_text, 1, _templates.size());

        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // TODO: make it working xD
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
    private void updateNotification() {
        float progress = _downloadStatus * 100 / _templates.size();

        String notificationText = getResources().getString(R.string.notification_download_results_text, _downloadStatus, _templates.size());
        _nBuilder
                .setContentText(notificationText)
                .setProgress(100, Math.round(progress), false);

        _nManager.notify(NOTIFICATION_ID, _nBuilder.build());
    }

    // TODO: not yet used
    public void cancelNotification() {
        _nManager.cancel(NOTIFICATION_ID);

        _asyncTasks.forEach( (task) -> task.cancel(true) );
    }
}