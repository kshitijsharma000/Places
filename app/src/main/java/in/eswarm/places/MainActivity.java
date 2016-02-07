package in.eswarm.places;

import android.app.ProgressDialog;
import android.content.Intent;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.json.JSONObject;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DataRetriever.DataListener {


    RecyclerView mRecyclerView;
    ProgressDialog mDialog;
    AdapterCategoryList mAdapter;
    DataRetriever mDataRetriever;
    public static final String TAG = MainActivity.class.getSimpleName();
    private CollapsingToolbarLayout mCollapsing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setStatusBarColor(Color.WHITE);

        mCollapsing =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing);

        mCollapsing.setTitle("Bengaluru");

        /*if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddNewPlaceActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //ImageView imageView = (ImageView) findViewById(R.id.cityImage);
        //imageView.setImageResource(R.drawable.blore);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerCategoryList);

        mRecyclerView.addOnItemTouchListener(new DragController(this, mRecyclerView, new DragController.Clicklistener() {
            @Override
            public void Onclick(View view, int position) {
                System.out.println("inside recycler item on click : " + position);
                Data.Category category = mAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, PlacesListActivity.class);
                intent.putExtra(PlacesListActivity.CITY_NAME_EXTRA, category.getCity_name());
                intent.putExtra(PlacesListActivity.CATEGORY_NAME_EXTRA, category.getName());
                intent.putExtra(PlacesListActivity.IS_LOCAL, category.isLocal());
                //intent.putExtra("PlaceObject", place);
                startActivity(intent);
            }

            @Override
            public void OnLongclick(View view, int position) {
                System.out.println("inside recycler item on long click : " + position);
            }
        }));

        //mCityNameMin = (TextView) findViewById(R.id.city_name_min);
        //mImageFrame = (LinearLayout) findViewById(R.id.imageFrame);
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading...");
        mDialog.setTitle("Fetching Data");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mAdapter = new AdapterCategoryList(new ArrayList<Data.Category>(), this);
        mDataRetriever = new DataRetriever(this);
        mDataRetriever.getCategories("Bengaluru");
        setRecyclerView();


    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    private void setRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SimpleSpaceDecorator(15, 10));
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.user_profile) {
            System.out.println("inside user profiles");
        } else if (id == R.id.saved_collections) {

        } else if (id == R.id.cities) {

        } else if (id == R.id.bangalore) {
            mDataRetriever.getCategories("Bengaluru");
            setRecyclerView();
            mCollapsing.setTitle("Bengaluru");
        } else if (id == R.id.Chennai) {
            ImageView imageView = (ImageView) findViewById(R.id.backdrop);
            imageView.setImageResource(R.drawable.chennai);
            mDataRetriever.getCategories("Bengaluru");
            mCollapsing.setTitle("Chennai");
            setRecyclerView();
        } else if (id == R.id.Delhi) {
            ImageView imageView = (ImageView) findViewById(R.id.backdrop);
            imageView.setImageResource(R.drawable.delhi);
            mDataRetriever.getCategories("Bengaluru");
            setRecyclerView();
            mCollapsing.setTitle("Delhi");
        } else if (id == R.id.Mumbai) {
            ImageView imageView = (ImageView) findViewById(R.id.backdrop);
            imageView.setImageResource(R.drawable.mumbai);
            mDataRetriever.getCategories("Bengaluru");
            setRecyclerView();
            mCollapsing.setTitle("Mumbai");
        } else if (id == R.id.settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void requestStart() {
        mDialog.show();
    }

    @Override
    public void dataReceived(JSONObject jsonObject) {
        ArrayList<Data.Category> categories = DataRetriever.JSONToCategories(jsonObject);
        ArrayList<Data.Category> catLocal = getLocalCategories();
        categories.addAll(catLocal);
        mAdapter.setItemList(categories);
        mAdapter.notifyDataSetChanged();
        mDialog.hide();
    }

    private ArrayList<Data.Category> getLocalCategories()
    {
        ArrayList<Data.Category> categoryList = new ArrayList<>();
        try {
            DB snappy = DBFactory.open(getApplicationContext());
            String keys[] = snappy.findKeys("collection");
            for (int i = 0; i < keys.length; i++) {
                Log.d(TAG, "Keys " + keys[0]);
                Data.Category category = new Data.Category(keys[i], "", 0 , "", true);
                categoryList.add(category);
            }


        }
        catch (SnappydbException e) {
            e.printStackTrace();
            Log.e(TAG, "Error in snappy db");
        }
        return  categoryList;
    }

    @Override
    public void error() {
        mDialog.hide();
    }



}
