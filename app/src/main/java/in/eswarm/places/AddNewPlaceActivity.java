package in.eswarm.places;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    AdapterAutoComplete mAdapter;
    CardView cardView;
    ArrayList<Data.Place> places;
    EditText mEditText;
    LinearLayout mSaveLayout;
    Button mSaveButton;
    ArrayList<Data.Place> mCollection;
    public static final String TAG = AddNewPlaceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

        mCollection = new ArrayList<>();
        cardView = (CardView) findViewById(R.id.card_add_new_place);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_new_site_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAutoComplete();
            }
        });

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
            }

            @Override
            public void OnLongclick(View view, int position) {
                System.out.println("inside recycler item on long click : " + position);
            }
        }));

        mAdapter = new AdapterAutoComplete(new ArrayList<Data.Place>(), this);
        setRecyclerView();
        mEditText = (EditText) findViewById(R.id.collectionEdit);
        mSaveLayout = (LinearLayout) findViewById(R.id.saveLayout);
        mSaveButton = (Button) findViewById(R.id.saveButton);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCollection.size() == 0) {
                    Toast.makeText(AddNewPlaceActivity.this, "Please add some places", Toast.LENGTH_LONG).show();
                    return;
                }
                if(mEditText.getText().length() == 0)
                {
                    Toast.makeText(AddNewPlaceActivity.this, "Please enter the collection name", Toast.LENGTH_LONG).show();
                    return;
                }
                saveCollection();
            }
        });

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

                addPlace(model_place);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("AutocompleteApi", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void addPlace(Data.Place place)
    {
        Log.d(TAG, "Add place " + place.category_name);
        mCollection.add(place);
        cardView.setVisibility(View.GONE);
        mSaveLayout.setVisibility(View.VISIBLE);
        updateRecycler();
    }

    private void setRecyclerView() {
        mAdapter = new AdapterAutoComplete(mCollection, this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleSpaceDecorator(10, 5));
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void updateRecycler()
    {
        mAdapter.setItemList(mCollection);
        mAdapter.notifyDataSetChanged();
    }

    private void saveCollection() {
        if(mCollection.size() != 0)
        {
        try {
            DB snappy = DBFactory.open(getApplicationContext());
            String keys[] = snappy.findKeys("collection");
            for(int i=0; i<keys.length ; i++ )
            {
                Log.d(TAG, "Keys " + keys[0]);
            }

            snappy.put("collection:" + mEditText.getText(), mCollection.toArray());
            //snappy.put("PlaceId : " + model_place.getPlace_id(), model_place);
            snappy.close();


            Toast.makeText(this, "Your collection is saved", Toast.LENGTH_LONG);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } catch (SnappydbException e) {
            e.printStackTrace();
            Log.e(TAG, "Error in snappy db");
        }
        }
    }
}
