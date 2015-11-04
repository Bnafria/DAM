package com.example.smartwake;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity  extends Activity{
	
	private TextView temp;
	private TextView lluvia;
	private TextView json;
	private ImageView icon;
	private int id;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		
		icon = (ImageView) findViewById(R.id.imageWeather);
		//Incluir resto de iconos
		id = getResources().getIdentifier("com.example.smartwake:drawable/cloud", null, null);
	    temp = (TextView) findViewById(R.id.textWeather1);
	    lluvia = (TextView) findViewById(R.id.textWeather2);

	    
	        
	    //Faltaría lo de coge paraguas y eso. Creo que es lo menos importante jaja
	    
	    new HttpAsyncTask().execute("http://api.openweathermap.org/data/2.5/forecast/daily?lat=40.434797&lon=-3.629165&cnt=1&APPID=18e6cd0de24e940208f6a58263337fa7");


	}
	
	public static String getWeatherJSON(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
 
        return result;
    }
 
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
    
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
            return getWeatherJSON(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            
            //Que debe hacer
            JSONObject mainObject = null;
            JSONArray listObject = null;
            JSONObject tempObject = null;
            JSONArray weatherObject = null;
            JSONObject descriptionObject = null;

            String min = null;
            String max = null;
            double aux = -1;
            int aux1 = -1;
			try {
				mainObject = new JSONObject(result);
	            listObject = mainObject.getJSONArray("list");
	            tempObject = listObject.getJSONObject(0).getJSONObject("temp");
	            weatherObject = listObject.getJSONObject(0).getJSONArray("weather");
	            descriptionObject = weatherObject.getJSONObject(0);

	            //Parsear y redondear temperaturas
	            min = tempObject.getString("min");
	            aux = Double.parseDouble(min);
	            aux = aux - 273.15;
	            aux1 = (int) aux;
	            min = String.valueOf(aux1);
	            
	            max = tempObject.getString("max");
	            aux = Double.parseDouble(max);
	            aux = aux - 273.15;
	            aux1 = (int) aux;
	            max = String.valueOf(aux1);
	            
	            //Parsear nubosidad
	            String mainWeather = descriptionObject.getString("main"); 
	            String descriptionWeather = descriptionObject.getString("description");
	    	    
	    	    if(mainWeather.equals("Rain")){
	    	    	//Hay que mirar descripcion y ver que dice
	    	    	if(descriptionWeather.equals("light rain")){
		    	    	lluvia.setText("LLuvias intermitentes");
	    	    	}
	    	    	else if(descriptionWeather.equals("moderate rain")){
		    	    	lluvia.setText("LLuvias moderadas");
	    	    	}
	    	    	icon.setImageResource(id); //Icono lluvia
	    	    }
	    	    else if(mainWeather.equals("Clouds")){
	    	    	//Hay que mirar descripcion y ver que dice
	    	    	if(descriptionWeather.equals("broken clouds")){
		    	    	lluvia.setText("Nubes dispersas");
	    	    	} 
	    	    	else if(descriptionWeather.equals("few clouds")){
		    	    	lluvia.setText("Nubosidad ligera");
	    	    	}
	    	    	else if(descriptionWeather.equals("scattered clouds")){
		    	    	lluvia.setText("Nubes dispersas");
	    	    	}
	    	    	icon.setImageResource(id); //Icono nubes
	    	    }
	    	    else if(mainWeather.equals("Snow")){
	    	    	//Hay que mirar descripcion y ver que dice
	    	    	if(descriptionWeather.equals("light snow")){
		    	    	lluvia.setText("Nevada leve");
	    	    	}
	    	    	else if(descriptionWeather.equals("moderate snow")){
		    	    	lluvia.setText("Nevadas moderadas");
	    	    	}
	    	    	icon.setImageResource(id); //Icono nieve
	    	    }	
	    	    else if (mainWeather.equals("Clear")){
	    	    	//Hay que mirar descripcion y ver que dice
	    	    	if(descriptionWeather.equals("sky is clear")){
		    	    	lluvia.setText("Cielo despejado");
	    	    	} 
	    	    	icon.setImageResource(id); //Icono sol
	    	    }	    	    
			} 
			catch (JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			temp.setText("Para hoy se preeven máximas de " +max +"º y mínimas de " +min +"º");
            
       }
    }
	
}
