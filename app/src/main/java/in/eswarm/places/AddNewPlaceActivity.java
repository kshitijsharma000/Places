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
import com.snappydb.KeyIterator;
import com.snappydb.SnappydbException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class AddNewPlaceActivity extends AppCompatActivity {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    AdapterCategoryList adapter;
    ArrayList<Model_data.Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

        CardView cardView = (CardView) findViewById(R.id.card_add_new_place);
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

        recyclerView.addOnItemTouchListener(new DragController(this, recyclerView, new Clicklistener() {
            @Override
            public void Onclick(View view, int position) {
                System.out.println("inside recycler item on click : " + position);
                Model_data.Place place = adapter.getItem(position);
                Intent intent = new Intent(AddNewPlaceActivity.this, DetailActivity.class);
                intent.putExtra("PlaceObject", (Serializable) place);
                startActivity(intent);
            }

            @Override
            public void OnLongclick(View view, int position) {
                System.out.println("inside recycler item on long click : " + position);
            }
        }));

        adapter = new AdapterCategoryList(new ArrayList<Model_data.Place>(), this);
        get_data_from_user_places_db();
        update_recyclerview();
    }

    private void get_data_from_user_places_db() {
        Model_data.Place place;
        try {
            DB snappy = DBFactory.open(getApplicationContext());
            String[] keys = snappy.findKeys("PlaceId");
            for(String key : keys){
                //place = snappy.get(key);
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Model_data.Place model_place = new Model_data.Place();
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


    private class DragController implements RecyclerView.OnItemTouchListener {
        private RecyclerView recyclerView;
        private GestureDetector gestureDetector;
        private Clicklistener clicklistener;

        public DragController(Context context, final RecyclerView recyclerView, final Clicklistener clicklistener) {
            this.recyclerView = recyclerView;
            this.clicklistener = clicklistener;
            this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    //return super.onSingleTapUp(e);
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.OnLongclick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.Onclick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface Clicklistener {
        public void Onclick(View view, int position);

        public void OnLongclick(View view, int position);
    }

}
