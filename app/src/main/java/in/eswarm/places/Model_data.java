package in.eswarm.places;

/**
 * Created by Kshitij on 2/6/2016.
 */

public class Model_data {

    static class Place {
        String place_id;
        String city_name;
        String category_name;
        String name;
        String description;
        String url;
        String timing_start;
        String timing_end;
        String long_coord;
        String lat_coord;
        Integer likes;
    }

    static class Category {
        String name;
        String city_name;
        Integer likes;
        String url;
    }

    static class City {
        String name;
        String image_url;
        Integer temperature;
        String ideal_month_start;
        String ideal_month_end;

    }
}

