package in.eswarm.places;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;

public class AddNewPlaceActivity extends AppCompatActivity {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    AdapterAutoComplete adapter;
    CardView cardView;
    ArrayList<Data.Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

        cardView = (CardView) findViewById(R.id.card_add_new_place);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_new_site_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAutoComplete();
            }
        });

        if (get_DB_Item_count() != 0) {
            System.out.println("db content is not null");
            cardView.setVisibility(View.GONE);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAutoComplete();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerAddNewPlace);

        recyclerView.addOnItemTouchListener(new DragController(this, recyclerView, new DragController.Clicklistener() {
            @Override
            public void Onclick(View view, int position) {
                System.out.println("inside recycler item on click : " + position);
                /*Data.Place place = adapter.getItem(position);
                Intent intent = new Intent(AddNewPlaceActivity.this, DetailActivity.class);
                intent.putExtra("PlaceObject", place);
                startActivity(intent);*/
            }

            @Override
            public void OnLongclick(View view, int position) {
                System.out.println("inside recycler item on long click : " + position);
            }
        }));

        adapter = new AdapterAutoComplete(new ArrayList<Data.Place>(), this);
        get_data_from_user_places_db();
        update_recyclerview();
    }

    private void launchAutoComplete() {
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

    private void get_data_from_user_places_db() {
        places = new ArrayList<>();
        Data.Place place = new Data.Place();
        try {
            DB snappy = DBFactory.open(getApplicationContext());
            String[] keys = snappy.findKeys("PlaceId");
            for (String key : keys) {
                place = snappy.get(key, place.getClass());
                places.add(place);
                System.out.println("place : " + place.getPlace_id());
                System.out.println("place : " + place.getName());
                System.out.println("place : " + place.getLat_coord());
                System.out.println("place : " + place.getLong_coord());
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        adapter.setItemList(places);
        adapter.notifyDataSetChanged();
    }

    private int get_DB_Item_count() {
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
                Log.i("AutocompleteApi id ", "Place: " + place.getId());
                Log.i("AutocompleteApi name ", "Place: " + place.getName());
                Log.i("AutocompleteApi coord", "Place: " + place.getLatLng());
                model_place.setName(place.getName().toString());
                model_place.setPlace_id(place.getId().toString());
                model_place.setLat_coord(place.getLatLng().toString().split(",")[0].replace("(", ""));
                model_place.setLong_coord(place.getLatLng().toString().split(",")[1].replace(")", ""));
                try {
                    DB snappy = DBFactory.open(getApplicationContext());
                    snappy.put("PlaceId : " + model_place.getPlace_id(), model_place);
                    snappy.close();

                    get_data_from_user_places_db();
                    update_recyclerview();
                    cardView.setVisibility(View.GONE);

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
        recyclerView.addItemDecoration(new SimpleSpaceDecorator(10, 5));
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
