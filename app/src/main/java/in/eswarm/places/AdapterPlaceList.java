package in.eswarm.places;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import in.eswarm.places.network.Image_Handler;

/**
 * Created by Kshitij on 2/6/2016.
 */
public class AdapterPlaceList extends RecyclerView.Adapter<AdapterPlaceList.ViewHolder> {

    private ArrayList<Data.Place> mPlaces;

    public AdapterPlaceList(ArrayList<Data.Place> places, Context context) {
        this.mPlaces = places;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("inside view holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_place_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        System.out.println("inside on bind view holder");
        holder.textView_SiteTitle.setText(mPlaces.get(position).getName());
        holder.textView_SiteTimings.setText(mPlaces.get(position).getTiming_start() + "-" + mPlaces.get(position).getTiming_end());
        System.out.println("url : " + mPlaces.get(position).getUrl());
        Image_Handler.get_image_from_url(mPlaces.get(position).getUrl(), holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public Data.Place getItem(int position) {
        return mPlaces.get(position);
    }

    public void setItemList(ArrayList<Data.Place> places) {
        this.mPlaces = places;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_SiteTitle;
        TextView textView_SiteTimings;
        TextView textView_SiteDistance;
        NetworkImageView imageView;
        ImageView image_fav;
        ImageView image_share;
        ImageView image_direction;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_SiteTitle = (TextView) itemView.findViewById(R.id.site_title);
            textView_SiteTimings = (TextView) itemView.findViewById(R.id.site_timings);
            textView_SiteDistance = (TextView) itemView.findViewById(R.id.site_distance);
            imageView = (NetworkImageView) itemView.findViewById(R.id.bg_card_image);
            image_fav = (ImageView) itemView.findViewById(R.id.card_site_fav);
            image_share = (ImageView) itemView.findViewById(R.id.card_site_share);
            image_direction = (ImageView) itemView.findViewById(R.id.card_site_direction);

            image_fav.setOnClickListener(this);
            image_share.setOnClickListener(this);
            image_direction.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.card_site_fav:
                    System.out.println("click fav");
                    break;
                case R.id.card_site_share:
                    System.out.println("click share");
                    break;
                case R.id.card_site_direction:
                    System.out.println("click direction");
                    break;
            }
        }
    }
}
