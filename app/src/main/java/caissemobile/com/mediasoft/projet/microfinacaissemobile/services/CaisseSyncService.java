package caissemobile.com.mediasoft.projet.microfinacaissemobile.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CaisseSyncService extends Service {
    // Storage for an instance of the sync adapter
    private static CaisseSyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();
    /*
     * Instantiate the sync adapter object.
     */

    @Override
    public void onCreate() {
        super.onCreate();
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new CaisseSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    public CaisseSyncService() {
        // Create a new authenticator object
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
