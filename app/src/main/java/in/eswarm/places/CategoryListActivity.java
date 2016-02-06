package in.eswarm.places;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import in.eswarm.places.network.Appcontroller;

public class CategoryListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog dialog;
    AdapterCategoryList adapter;
    ArrayList<Model_data.Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setTitle("Fetching Data");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerCategoryList);

        recyclerView.addOnItemTouchListener(new DragController(this, recyclerView, new Clicklistener() {
            @Override
            public void Onclick(View view, int position) {
                System.out.println("inside recycler item on click : " + position);
                Model_data.Place place = adapter.getItem(position);
                Intent intent = new Intent(CategoryListActivity.this, DetailActivity.class);
                //intent.putExtra("PlaceObject", place);
                startActivity(intent);
            }

            @Override
            public void OnLongclick(View view, int position) {
                System.out.println("inside recycler item on long click : " + position);
            }
        }));

        adapter = new AdapterCategoryList(new ArrayList<Model_data.Place>(), this);
        get_data_from_server();
        update_recyclerview();
    }

    private void get_data_from_server() {
        //String url = Constants.baseurl + Constants.cities;
        String url = "http://eswarm.in:1985/bengaluru/historical/places";
        System.out.println("url : " + url);

        JsonObjectRequest list_request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("got data : " + jsonObject.toString());
                places = new ArrayList<>();
                try {
                    JSONArray jArray = jsonObject.getJSONArray("json_list");
                    for (int i = 0; i < jArray.length(); i++) {
                        add_to_dataset(jArray.getJSONObject(i));
                    }
                    System.out.println("places : "+ places.size());
                    adapter.setItemList(places);
                    adapter.notifyDataSetChanged();
                    dialog.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("error in json parsing");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("error in getting data");
                dialog.hide();
            }
        });

        Appcontroller.getmInstance().addtoRequestqueue(list_request);
    }

    private void add_to_dataset(JSONObject jsonObject) {
        Model_data.Place place = new Model_data.Place();
        try {
            place.setCategory_name(jsonObject.getString("category_name"));
            place.setPlace_id(jsonObject.getString("place_id"));
            place.setCity_name(jsonObject.getString("city_name"));
            place.setName(jsonObject.getString("name"));
            place.setDescription(jsonObject.getString("description"));
            place.setTiming_start(jsonObject.getString("timing_start"));
            place.setTiming_end(jsonObject.getString("timing_end"));
            place.setLong_coord(jsonObject.getString("long_coord"));
            place.setLat_coord(jsonObject.getString("lat_coord"));
            place.setLikes(jsonObject.getInt("likes"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        places.add(place);
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
