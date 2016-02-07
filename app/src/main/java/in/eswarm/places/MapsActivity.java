package in.eswarm.places;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends GAPIActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String localPlaceId;
    private Double lat;
    private Double loong;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (getIntent().getExtras() != null) {
            Data.Place localplace = (Data.Place) getIntent().getExtras().get("PlaceObject");
            System.out.println("place name : " + localplace.getName());
            System.out.println("place id : " + localplace.getPlace_id());
            localPlaceId = localplace.getPlace_id();
            getLatLong();
        }
    }

    private void getLatLong() {
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, localPlaceId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            final Place myPlace = places.get(0);

                            String temp = myPlace.getLatLng().toString();
                            String loc = temp.split(":")[1];
                            lat = Double.valueOf(loc.split(",")[0].replace("(", ""));
                            loong = Double.valueOf(loc.split(",")[1].replace(")", ""));

                            Log.i("map activity", "Place latlong: " + myPlace.getLatLng().toString());
                            Log.i("map activity", "Place lat: " + lat.toString() + " long : " + loong.toString());

                            mapFragment.getMapAsync(MapsActivity.this);
                        } else {
                            Log.e("map activity", "Place not found");
                        }
                        places.release();
                    }
                });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng sydney = new LatLng(lat, loong);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
    }
}
