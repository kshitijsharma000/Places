package in.eswarm.places.network;

import android.util.Log;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import in.eswarm.places.R;


public class Image_Handler {

    private static ImageLoader imageLoader;

    public static void get_image_from_url(final String image_url, final ImageView imageView) {
        ImageLoader imageLoader = Appcontroller.getmInstance().getImageLoader();
        imageLoader.get(image_url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer.getBitmap() != null) {
                    imageView.setImageBitmap(imageContainer.getBitmap());
                }
                else
                {

                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Image Loader", "Image load error : " + volleyError.getMessage() + " Setting default image");

            }
        });
    }

    public static void get_image_from_url(String image_url, NetworkImageView imageview) {
        imageLoader = Appcontroller.getmInstance().getImageLoader();
        imageview.setImageUrl(image_url, imageLoader);
    }

    public static void get_image_from_url(String image_url) {
        imageLoader = Appcontroller.getmInstance().getImageLoader();
        imageLoader.get(image_url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {
                    System.out.println("image recieved..");
                } else
                    System.out.println("error in image recieved..");
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("volley error");
            }
        });
    }
}
