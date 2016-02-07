package in.eswarm.places;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.places.Place;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import org.json.JSONObject;
import java.util.ArrayList;

public class PlacesListActivity extends AppCompatActivity implements DataRetriever.DataListener {

    RecyclerView mRecyclerView;
    ProgressDialog mDialog;
    AdapterPlaceList mAdapter;
    ArrayList<Data.Place> mPlaces;
    DataRetriever mDataRetriever;

    public static final String CITY_NAME_EXTRA = "CIY_NAME";
    public static final String CATEGORY_NAME_EXTRA = "CAT_NAME";
    public static final String IS_LOCAL = "LOCAL";

    public static final String TAG = PlacesListActivity.class.getSimpleName();

    private String mCityName;
    private String mCategoryName;
    private boolean mIsLocal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        mPlaces = new ArrayList<>();

        Intent intent = getIntent();
        mCityName =  intent.getStringExtra(CITY_NAME_EXTRA);
        mCategoryName = intent.getStringExtra(CATEGORY_NAME_EXTRA);
        mIsLocal = intent.getBooleanExtra(IS_LOCAL, false);


        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading...");
        mDialog.setTitle("Fetching Data");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerCategoryList);

        mRecyclerView.addOnItemTouchListener(new DragController(this, mRecyclerView, new DragController.Clicklistener() {
            @Override
            public void Onclick(View view, int position) {
                System.out.println("inside recycler item on click : " + position);
                /*Data.Place place = mAdapter.getItem(position);
                Intent intent = new Intent(PlacesListActivity.this, DetailActivity.class);
                intent.putExtra("PlaceObject", place);
                startActivity(intent);*/
            }

            @Override
            public void OnLongclick(View view, int position) {
                System.out.println("inside recycler item on long click : " + position);
            }
        }));

        mAdapter = new AdapterPlaceList(new ArrayList<Data.Place>(), this);

        if(!mIsLocal) {
            mDataRetriever = new DataRetriever(this);
            mDataRetriever.getPlaces(mCityName, mCategoryName);
        }
        else {
            try {
                DB snappydb = DBFactory.open(this);
                Data.Place place[] = snappydb.getObjectArray(mCategoryName, Data.Place.class);

                for(int i=0; i<place.length; i++)
                    mPlaces.add(place[i]);

                mAdapter.setItemList(mPlaces);
                mAdapter.notifyDataSetChanged();

            }
            catch (SnappydbException e)
            {
                Log.e(TAG, "Snappy db exception " + e.getMessage());
            }

        }
        setRecyclerView();
    }

    private void setRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SimpleSpaceDecorator(15,10));
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void requestStart() {
        mDialog.show();
    }

    @Override
    public void dataReceived(JSONObject jsonObject) {
        mDialog.hide();
        mPlaces = DataRetriever.JSONToPlaces(jsonObject);
        mAdapter.setItemList(mPlaces);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void error() {
        mDialog.hide();
    }
}
