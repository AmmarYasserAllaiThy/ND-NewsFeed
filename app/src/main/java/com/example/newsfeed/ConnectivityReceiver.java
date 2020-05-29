package com.example.newsfeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static android.content.Context.CONNECTIVITY_SERVICE;


// TODO: Just review
public class ConnectivityReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = ConnectivityReceiver.class.getSimpleName() + "_DEBUG";
    private static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (connectivityReceiverListener != null)
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected(context), 0);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (conManager == null) {
            Log.d(LOG_TAG, "CONNECTIVITY_SERVICE = null");
            return false;
        }
        NetworkInfo activeNetworkInfo = conManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void setConnectivityListener(ConnectivityReceiverListener listener) {
        connectivityReceiverListener = listener;
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected, int id);
    }
}