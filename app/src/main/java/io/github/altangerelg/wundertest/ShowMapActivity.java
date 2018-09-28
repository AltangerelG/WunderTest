package io.github.altangerelg.wundertest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class ShowMapActivity extends AppCompatActivity {
    private static final String TAG = "ShowMapActivity";
    private GoogleMap mMap;
    private Button btnCallMeMaybe, btnMap;
    private Double latitude, longitude;
    private Double currLatitude, currLongitude;
    private String name;
    private String data;
    MapView map = null;
    Road mRoad;
    GeoPoint startPoint2, endPoint;
    RoadManager roadManager;

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //toolbar = (Toolbar) findViewById(R.id.new_toolbar);
        //setupToolbar();

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //openstreetmap
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        Bundle inBundle = getIntent().getExtras();
        if (inBundle != null) {
            //bundle дотроос .get үйлдлээр ямар нэрээр явуулсанаар нь баривчилж байна.
            //ID 0-с эхэлж байгаа болохоор firebase дээрх ID тай тааруулахын тулд 1-г хүчиндэж нэмсэн.
            //Өөрөөр шийдэл олвол засаарай :)
            //currLatitude = (double) inBundle.get("cLatitude");
            //currLongitude = (double) inBundle.get("cLongitude");
            latitude = (double) inBundle.get("eLatitude");
            longitude = (double) inBundle.get("eLongitude");
            data = (String) inBundle.get("data");
            name = (String) inBundle.get("name");
        }

        //
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setBuiltInZoomControls(true);
        //
        IMapController mapController = map.getController();
        mapController.setZoom(16);
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);


        //Drawable newMarker = getResources().getDrawable(R.mipmap.ic_launcher);
        Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
//build the marker
        Marker m = new Marker(map);
//must set the icon to null last
        m.setTitle(name);
        m.setPosition(new GeoPoint(latitude, longitude));
        m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        m.showInfoWindow();

        map.getOverlays().add(m);

        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

        try {
            JSONObject jsonObject = new JSONObject(data);
            Log.e(TAG, "onCreate: " + jsonObject.getString("placemarks"));

            Log.e(TAG, "length: " + jsonObject.getJSONArray("placemarks").length());

            for (int i = 0; i < jsonObject.getJSONArray("placemarks").length(); i++){

                JSONObject jsonObject1 = jsonObject.getJSONArray("placemarks")
                        .getJSONObject(i);

                JSONArray jsonArray = jsonObject1.getJSONArray("coordinates");
                Double latitudeCar = (Double) jsonArray.get(0);
                Double longitudeCar = (Double) jsonArray.get(1);

                String name = jsonObject1.getString("name");

                items.add(new OverlayItem(name, "", new GeoPoint(latitudeCar,longitudeCar))); // Lat/Lon decimal degrees
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //openstreetmap
        //your items


//the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(), items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });
        mOverlay.setFocusItemsOnTap(true);

        map.getOverlays().add(mOverlay);
        /*ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

        items.add(new OverlayItem("Title", "Description", new GeoPoint(latitude, longitude))); // Lat/Lon decimal degrees
//the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(),
                items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                showToast("bogino daragdaw");
                //do something
                return true;
            }

            @Override
            public boolean onItemLongPress(final int index, final OverlayItem item) {
                showToast("udaan daragdaw");
                return false;
            }
        });*/

        /*map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setBuiltInZoomControls(true);
        mapController = (MapController) map.getController();
        mapController.setZoom(13);
        GeoPoint gPt = new GeoPoint(latitude, longitude);
        mapController.setCenter(gPt);*/

        /*Drawable newMarker = getResources().getDrawable(R.mipmap.ic_launcher);
        Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
//build the marker
        Marker m = new Marker(map);
//must set the icon to null last
        m.setTitle("name");
        m.setIcon(newMarker);
        m.setPosition(new GeoPoint(latitude, longitude));
        m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);*/

        //map.getOverlays().add(m);

        //roadManager = new MapQuestRoadManager("rgg3uEkYYK2zqf4nOC2dx6XQCMLqTeC9");

        //roadManager.addRequestOption("routeType=bicycle");

        //startPoint2 = new GeoPoint(currLatitude, currLongitude);
        /*ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        //waypoints.add(startPoint2);
        endPoint = new GeoPoint(latitude, longitude);
        waypoints.add(endPoint);
        //getRoadAsync();

        mRoad = roadManager.getRoad(waypoints);

        Polyline roadOverlay = RoadManager.buildRoadOverlay(mRoad);

        map.getOverlays().add(roadOverlay);

        map.invalidate();*/


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        return;
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}