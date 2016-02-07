package in.eswarm.places;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import in.eswarm.places.Data.Place;
import in.eswarm.places.network.Appcontroller;


/**
 * Created by Eswar on 07-02-2016
 */
public class DataRetriever {

    private static String BASE_URL = "http://eswarm.in:1985/";
    private DataListener mListener;

    public DataRetriever(DataListener listener)
    {
        mListener = listener;
    }

    public void makeRequest(String url) {
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                mListener.dataReceived(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mListener.error();
                System.out.println("error in getting data");
                //mDialog.hide();
            }
        });

        mListener.requestStart();
        Appcontroller.getmInstance().addtoRequestqueue(request);
    }

    public void getCategories(String city) {
        //String url = Constants.baseurl + Constants.cities;
        String url = BASE_URL + city + "/categories";
        makeRequest(url);
        System.out.println("getCategories url : " + url);
    }

    public void getPlaces(String city, String category) {
        String url = "http://eswarm.in:1985/bengaluru/historical/places";
        //String url = BASE_URL + city + "/" + category + "/places";
        makeRequest(url);
        System.out.println("getPlaces url : " + url);
    }

    public interface DataListener {
        void requestStart();
        void dataReceived(JSONObject jsonObject);
        void error();
    }

    public static ArrayList<Place> JSONToPlaces(JSONObject jsonObject)
    {
        System.out.println("got data : " + jsonObject.toString());
        ArrayList<Data.Place> placeList = new ArrayList<>();
        try {
            JSONArray jArray = jsonObject.getJSONArray("json_list");

            for (int i = 0; i < jArray.length(); i++) {
                Data.Place place = new Data.Place();
                try {
                    jsonObject = jArray.getJSONObject(i);

                    place.setCategory_name(jsonObject.getString("category_name"));
                    place.setPlace_id(jsonObject.getString("place_id"));
                    place.setCity_name(jsonObject.getString("city_name"));
                    place.setName(jsonObject.getString("name"));
                    place.setUrl(jsonObject.getString("image_url"));
                    place.setDescription(jsonObject.getString("description"));
                    place.setTiming_start(jsonObject.getString("timing_start"));
                    place.setTiming_end(jsonObject.getString("timing_end"));
                    place.setLong_coord(jsonObject.getString("long_coord"));
                    place.setLat_coord(jsonObject.getString("lat_coord"));
                    place.setLikes(jsonObject.getInt("likes"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                placeList.add(place);
            }

            System.out.println("mPlaces : "+ placeList.size());


        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("error in json parsing");
        }
        return placeList;
    }

    public static ArrayList<Data.Category> JSONToCategories(JSONObject jsonObject)
    {
        System.out.println("got data : " + jsonObject.toString());
        ArrayList<Data.Category> categoryList = new ArrayList<>();
        try {
            JSONArray jArray = jsonObject.getJSONArray("json_list");

            for (int i = 0; i < jArray.length(); i++) {
                try {
                    jsonObject = jArray.getJSONObject(i);
                    String city_name = jsonObject.getString("city_name");
                    String url = jsonObject.getString("image_url");
                    int likes = jsonObject.getInt("likes");
                    String name = jsonObject.getString("name");

                    Data.Category category = new Data.Category(name, city_name, likes, url);
                    categoryList.add(category);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Categories : "+ categoryList.size());

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("error in json parsing");
        }

        return categoryList;
    }


}
