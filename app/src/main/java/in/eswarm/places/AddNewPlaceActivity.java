package in.eswarm.places;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.io.Serializable;
import java.util.ArrayList;

public class AddNewPlaceActivity extends AppCompatActivity {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    AdapterPlaceList adapter;
    ArrayList<Data.Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

        CardView cardView = (CardView) findViewById(R.id.card_add_new_place);

        if(get_DB_Item_count() == 0){
            cardView.setVisibility(View.GONE);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(AddNewPlaceActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.i("AutocompleteApi", "repairable exception");
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.i("AutocompleteApi", "services not available exception");
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerAddNewPlace);

        recyclerView.addOnItemTouchListener(new DragController(this, recyclerView, new DragController.Clicklistener() {
            @Override
            public void Onclick(View view, int position) {
                System.out.println("inside recycler item on click : " + position);
                Data.Place place = adapter.getItem(position);
                Intent intent = new Intent(AddNewPlaceActivity.this, DetailActivity.class);
                intent.putExtra("PlaceObject", place);
                startActivity(intent);
            }

            @Override
            public void OnLongclick(View view, int position) {
                System.out.println("inside recycler item on long click : " + position);
            }
        }));

        adapter = new AdapterPlaceList(new ArrayList<Data.Place>(), this);
        get_data_from_user_places_db();
        update_recyclerview();
    }

    private void get_data_from_user_places_db() {
        Data.Place place = new Data.Place();
        try {
            DB snappy = DBFactory.open(getApplicationContext());
            String[] keys = snappy.findKeys("PlaceId");
            if(keys.length != 0){
                places = new ArrayList<>();
            }
            for(String key : keys){
                place = snappy.get(key, place.getClass());
                places.add(place);
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    private int get_DB_Item_count(){
        try {
            DB snappy = DBFactory.open(getApplicationContext());
            String[] keys = snappy.findKeys("PlaceId");
            return keys.length;
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return 0;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Data.Place model_place = new Data.Place();
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("AutocompleteApi", "Place: " + place.getId());
                Log.i("AutocompleteApi", "Place: " + place.getName());
                Log.i("AutocompleteApi", "Place: " + place.getLatLng());
                model_place.setName(place.getName().toString());
                model_place.setPlace_id(place.getId().toString());
                model_place.setLat_coord(place.getLatLng().toString().split(",")[0].replace("(", ""));
                model_place.setLong_coord(place.getLatLng().toString().split(",")[1].replace(")", ""));
                try {
                    DB snappy = DBFactory.open(getApplicationContext());
                    snappy.put("PlaceId : " + model_place.getPlace_id(), model_place);
                    snappy.close();
                } catch (SnappydbException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                     // TODO: Handle the error.
                Log.i("AutocompleteApi", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void update_recyclerview() {
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleSpaceDecorator(15, 10));
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
