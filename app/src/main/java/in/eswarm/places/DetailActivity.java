package in.eswarm.places;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import in.eswarm.places.network.Image_Handler;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Data.Place place = (Data.Place) getIntent().getExtras().get("place");
        System.out.println("place : " + place.getPlace_id());


        TextView descriptionView = (TextView) findViewById(R.id.detail_site_desc);
        descriptionView.setText(place.description);

        NetworkImageView networkImageView = (NetworkImageView)findViewById(R.id.detail_site_image);
        Image_Handler.get_image_from_url(place.url, networkImageView);

        networkImageView.setDefaultImageResId(R.drawable.bangalore_palace);
        networkImageView.setErrorImageResId(R.drawable.bangalore_palace);

    }
}
