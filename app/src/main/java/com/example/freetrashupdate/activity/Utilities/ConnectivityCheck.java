package com.example.freetrashupdate.activity.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityCheck {

    public static boolean isConnected(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager != null) {

            NetworkInfo[] info = manager.getAllNetworkInfo();

            if(info != null)
                for (int i =0; i< info.length; i++)
                    if(info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
        }
        return false;
    }
}
