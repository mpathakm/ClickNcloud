package mpathak.clickncloud;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by mpathak on 4/8/2018.
 */

public class BackgroundServices extends Service {
  public DirectoryObserver directoryObserver;

    @Override
    public void onCreate(){
        super.onCreate();
        Toast.makeText(this, "This is on Create", Toast.LENGTH_LONG).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "This is on onStartCommand", Toast.LENGTH_LONG).show();

        try{
            directoryObserver = new DirectoryObserver(new File(Environment.getExternalStorageDirectory(), Constants.Camera_Directory).getAbsolutePath(), getApplicationContext());
        }
        catch (Exception ex) {
            Log.e("Background_Service", "The following error occured while trying to observe the camera directory: " + ex);
        }

         Thread thread = new Thread(new ThreadClass(startId));
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    final class ThreadClass implements Runnable {
        int _serviceId;

        ThreadClass(int serviceId) {
            _serviceId = serviceId;
        }

        @Override
        public void run() {
            directoryObserver.startWatching();
        }
    }
}
