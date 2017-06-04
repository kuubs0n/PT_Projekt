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

import pt.webscraping.entities.SearchResult;
import pt.webscraping.entities.Template;

public class GetContentIntentService extends IntentService
{
    private int _downloadStatus = 0;

    // our search query from input
    private String _searchQuery;

    // Neccesery stuff for Notifications
    private NotificationCompat.Builder _nBuilder;
    private NotificationManager _nManager;

    // random id used to determine our notification
    private static int NOTIFICATION_ID = 3453;

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

            Log.d("web.scraper", "GetContentIntentService - onReceive");
        }
    };

    public GetContentIntentService()
    {
        super("GetContentIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SearchResult.results.clear();
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
        for(Template template : SearchResult.selectedTemplates)
        {
            _asyncTasks.add(new GetProductsAsyncTask(this, template, SearchResult.searchQuery));
        }

        // service is waiting to complete all downloads in async tasks
        while (SearchResult.selectedTemplates.size() != _downloadStatus) { }
    }

    @Override
    public void onDestroy() {
        _asyncTasks.clear();

        Log.d("web.scraper", "GetContentIntentService - onDestroy");

        // notification stuff
        Intent targetIntent = new Intent(this, ResultsActivity.class);
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
            .setAction("pt.webscraping.RESULTS_READY");
        sendBroadcast(broadcastState);
    }

    /*
     * Notifications
     */
    private void createNotification() {
        _nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String notificationText = getResources().getString(R.string.notification_download_results_text, 1, SearchResult.selectedTemplates.size());

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
                //.addAction(cancelAction)
                .setProgress(100, 0, false)
                .setContentIntent(contentIntent);

        _nManager.notify(NOTIFICATION_ID, _nBuilder.build());
    }

    // update current notification
    private void updateNotification() {
        float progress = _downloadStatus * 100 / SearchResult.selectedTemplates.size();

        String notificationText = getResources().getString(R.string.notification_download_results_text, _downloadStatus, SearchResult.selectedTemplates.size());
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