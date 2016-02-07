package in.eswarm.places;

import java.io.Serializable;

/**
 * Created by Kshitij on 2/6/2016.
 */

public class Data implements Serializable{

    public static class Place {
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

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTiming_start() {
            return timing_start;
        }

        public void setTiming_start(String timing_start) {
            this.timing_start = timing_start;
        }

        public String getTiming_end() {
            return timing_end;
        }

        public void setTiming_end(String timing_end) {
            this.timing_end = timing_end;
        }

        public String getLong_coord() {
            return long_coord;
        }

        public void setLong_coord(String long_coord) {
            this.long_coord = long_coord;
        }

        public String getLat_coord() {
            return lat_coord;
        }

        public void setLat_coord(String lat_coord) {
            this.lat_coord = lat_coord;
        }

        public Integer getLikes() {
            return likes;
        }

        public void setLikes(Integer likes) {
            this.likes = likes;
        }
    }

    public static class Category {
        private String name;
        private String city_name;
        private Integer likes;
        private String url;

        public Category(String name, String city_name, int likes, String url)
        {
            this.name = name;
            this.city_name = city_name;
            this.likes = likes;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public Integer getLikes() {
            return likes;
        }

        public void setLikes(Integer likes) {
            this.likes = likes;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class City {
        String name;
        String image_url;
        Integer temperature;
        String ideal_month_start;
        String ideal_month_end;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public Integer getTemperature() {
            return temperature;
        }

        public void setTemperature(Integer temperature) {
            this.temperature = temperature;
        }

        public String getIdeal_month_start() {
            return ideal_month_start;
        }

        public void setIdeal_month_start(String ideal_month_start) {
            this.ideal_month_start = ideal_month_start;
        }

        public String getIdeal_month_end() {
            return ideal_month_end;
        }

        public void setIdeal_month_end(String ideal_month_end) {
            this.ideal_month_end = ideal_month_end;
        }
    }

}

