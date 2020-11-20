package com.example.lab3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeatherForecast extends AppCompatActivity {

    protected ProgressBar pb;
    protected TextView minText, maxText, uvText, curTemp;
    protected Bitmap bm;
    protected ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        pb = (ProgressBar)findViewById(R.id.progressBar1);
        curTemp = (TextView)findViewById(R.id.curTempID);
        minText = (TextView)findViewById(R.id.minTempID);
        maxText = (TextView)findViewById(R.id.maxTempID);
        uvText = (TextView)findViewById(R.id.uvValueID);
        img = (ImageView)findViewById(R.id.imageID);

        ForecastQuery fq = new ForecastQuery();
        fq.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric","http://openweathermap.org/img/w/",".png","https://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389"); //type1
    }
                                                //  type1    type2   type3
    private class ForecastQuery extends  AsyncTask< String, Integer, ArrayList<String>>{

        //type3                      type1
        @SuppressLint("LongLogTag")
        protected ArrayList doInBackground(String ... args){

            ArrayList<String> getResult = new ArrayList();

            try{
                //String encoded = URLEncoder.encode(args[0],"UTF-8");
                String iconName = null;
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8"); //response is data from the server


                //String parameter = null;
                int eventType = xpp.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        if(xpp.getName().equals("temperature"))
                        {
                            String value = xpp.getAttributeValue(null,    "value");
                            getResult.add(value);
                            String min = xpp.getAttributeValue(null,    "min");
                            getResult.add(min);
                            String max = xpp.getAttributeValue(null,    "max");
                            getResult.add(max);
                            publishProgress(25);
                        }
                        else if(xpp.getName().equals("weather"))
                        {
                            iconName = xpp.getAttributeValue(null,    "icon");

                        }

                    }
                    eventType = xpp.next();
                }


                Bitmap image = null;
                String urlString = args[1]+ iconName + args[2];
                // OpenWeatherMap website
                URL url2 = new URL(urlString);
                urlConnection = (HttpURLConnection) url2.openConnection();
                urlConnection.connect();


                int responseCode = urlConnection.getResponseCode();
                if(responseCode == 200){
                    image = BitmapFactory.decodeStream(urlConnection.getInputStream());
                }
                publishProgress(50);


                //store the image to local file
                String fileName = iconName + ".png";
                FileOutputStream outputStream = openFileOutput( fileName, Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();

                boolean getFile = fileExistance(fileName);

                if(getFile){ //if it's true
                    Log.i("fine the local file",fileName);
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(fileName);
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    bm = BitmapFactory.decodeStream(fis);

                    publishProgress(75);
                }
                else{
                    Log.i("didn't fine the file",fileName);
                }


                //get UV value from JSON object
                URL uvURL = new URL(args[3]);
                urlConnection = (HttpURLConnection) uvURL.openConnection();
                InputStream response2 = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response2,"UTF-8"),8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string

                JSONObject jObject  = new JSONObject(result);
                double uvRating =  jObject.getDouble("value");
                getResult.add(String.valueOf(uvRating));
                publishProgress(100);

            } catch (Exception e) {
                //e.printStackTrace();
            }

            return getResult;
        }

        //Type
        public void onProgressUpdate(Integer ... args)
        {
            pb.setProgress(args[0]);
            pb.setVisibility(View.VISIBLE);
        }

        //Type3
        public void onPostExecute(ArrayList<String> value)
        {
            curTemp.setText(getResources().getString(R.string.currTemperature)+ value.get(0));
            minText.setText(getResources().getString(R.string.minTemperature)+value.get(1));
            maxText.setText(getResources().getString(R.string.maxTemperature)+value.get(2));

           // uvText.setText(getResources().getString(R.string.uvRating)+value.get(3));
            img.setImageBitmap(bm);

            pb.setVisibility(View.INVISIBLE);


        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
    }
}
