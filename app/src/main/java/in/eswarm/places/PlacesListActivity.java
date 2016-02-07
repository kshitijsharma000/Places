package in.eswarm.places;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONObject;
import java.util.ArrayList;

public class PlacesListActivity extends AppCompatActivity implements DataRetriever.DataListener {

    RecyclerView mRecyclerView;
    ProgressDialog mDialog;
    AdapterPlaceList mAdapter;
    ArrayList<Data.Place> mPlaces;
    DataRetriever mDataRetriever;

    public static String CITY_NAME_EXTRA;
    public static String CATEGORY_NAME_EXTRA;

    private String mCityName;
    private String mCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Intent intent = getIntent();
        mCityName =  intent.getStringExtra(CITY_NAME_EXTRA);
        mCategoryName = intent.getStringExtra(CATEGORY_NAME_EXTRA);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading...");
        mDialog.setTitle("Fetching Data");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerCategoryList);

        mRecyclerView.addOnItemTouchListener(new DragController(this, mRecyclerView, new DragController.Clicklistener() {
            @Override
            public void Onclick(View view, int position) {
                System.out.println("inside recycler item on click : " + position);
                Data.Place place = mAdapter.getItem(position);
                Intent intent = new Intent(PlacesListActivity.this, DetailActivity.class);
                intent.putExtra("PlaceObject", place);
                startActivity(intent);
            }

            @Override
            public void OnLongclick(View view, int position) {
                System.out.println("inside recycler item on long click : " + position);
            }
        }));

        mAdapter = new AdapterPlaceList(new ArrayList<Data.Place>(), this);
        mDataRetriever = new DataRetriever(this);
        mDataRetriever.getPlaces(mCityName, mCategoryName);
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
