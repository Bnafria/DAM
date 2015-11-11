package com.example.smartwake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity{

    private ImageView icon;

    public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        icon = (ImageView) findViewById(R.id.weather);

        if(isOnlineWifi() == true || isOnline3G() == true) {
            Toast.makeText(getBaseContext(), "Tienes conexión!", Toast.LENGTH_LONG).show();
            icon.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    getWeather(v.getRootView());
                }
            });
        }
        else{
            Toast.makeText(getBaseContext(), "No tienes conexión!", Toast.LENGTH_LONG).show();
        }
    }
	
	public void getWeather(View view){
		Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
	}

    protected Boolean isOnlineWifi(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
    protected Boolean isOnline3G(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

}
