package io.github.altangerelg.wundertest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import io.github.altangerelg.wundertest.adapters.RecyclerAdapter;
import io.github.altangerelg.wundertest.models.Car;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private java.util.List<Car> cars = new ArrayList<>();
    private String jsonData;
    static ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyeclerV);

        new JsonTask().execute("https://s3-us-west-2.amazonaws.com/wunderbucket/locations.json");
    }

    protected class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.e("Response: ", "> " + line);

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            Log.e(TAG, "onPostExecute: " + result);
            jsonData = result;

            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                Log.e(TAG, "onCreate: " + jsonObject.getString("placemarks"));

                Log.e(TAG, "length: " + jsonObject.getJSONArray("placemarks").length());

                for (int i = 0; i < jsonObject.getJSONArray("placemarks").length(); i++){

                    JSONObject jsonObject1 = jsonObject.getJSONArray("placemarks")
                            .getJSONObject(i);

                    String address = jsonObject1.getString("address");

                    JSONArray jsonArray = jsonObject1.getJSONArray("coordinates");
                    Double latitudeCar = (Double) jsonArray.get(0);
                    Double longitudeCar = (Double) jsonArray.get(1);

                    LatLng coordinates = new LatLng(latitudeCar, longitudeCar);

                    String engineType = jsonObject1.getString("engineType");

                    String exterior = jsonObject1.getString("exterior");

                    int fuel = jsonObject1.getInt("fuel");

                    String interior = jsonObject1.getString("interior");

                    String name = jsonObject1.getString("name");

                    String vin = jsonObject1.getString("vin");

                    Car car = new Car(address, coordinates, engineType, exterior, fuel, interior,
                            name, vin);

                    cars.add(car);
                }

                adapter = new RecyclerAdapter(MainActivity.this, cars);

                adapter.notifyDataSetChanged(); //refresh

                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                //иймэрхүү OnClickListener эсвэл OnItemTouchListener нь товч, лист-н item дээр дарсаны дараа юу хийхийг бичнэ
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, recyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        /*
                        Intent intent = new Intent(this, CourtDetailActivity.class);
                        intent.putExtra("selectedCourtId", courtsOrig.indexOf(courts.get(position)));
                        startActivity(intent);*/
                        Intent intent = new Intent(MainActivity.this, ShowMapActivity.class);
                        intent.putExtra("eLatitude", cars.get(position).getCoordinates().latitude);
                        intent.putExtra("eLongitude", cars.get(position).getCoordinates().longitude);
                        intent.putExtra("name", cars.get(position).getName());
                        intent.putExtra("data", jsonData);

                        //TODO location-uudiig damjuulah...
                        startActivity(intent);

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
                //adapter2.notifyDataSetChanged(); //refresh
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

