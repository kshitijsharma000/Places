package in.eswarm.places;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import in.eswarm.places.network.Image_Handler;

/**
 * Created by Kshitij on 2/6/2016.
 */
public class AdapterAutoComplete extends RecyclerView.Adapter<AdapterAutoComplete.ViewHolder> {

    private ArrayList<Data.Place> mPlaces;
    private Context context;

    public AdapterAutoComplete(ArrayList<Data.Place> places, Context context) {
        this.mPlaces = places;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("inside adapter autocomplete view holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_place_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        System.out.println("inside adapter auto complete on bind view holder");
        holder.textView_SiteTitle.setText(mPlaces.get(position).getName());
        holder.textView_SiteTimings.setText(mPlaces.get(position).getTiming_start() + "-" + mPlaces.get(position).getTiming_end());
        System.out.println("url : " + mPlaces.get(position).getUrl());
        Image_Handler.get_image_from_url(mPlaces.get(position).getUrl(), holder.imageView);
        holder.imageView.setTag(position);
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
        ImageButton button_fav;
        ImageButton button_share;
        ImageButton button_direction;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_SiteTitle = (TextView) itemView.findViewById(R.id.site_title);
            textView_SiteTimings = (TextView) itemView.findViewById(R.id.site_timings);
            textView_SiteDistance = (TextView) itemView.findViewById(R.id.site_distance);
            imageView = (NetworkImageView) itemView.findViewById(R.id.bg_card_image);
            button_fav = (ImageButton) itemView.findViewById(R.id.button_fav);
            button_share = (ImageButton) itemView.findViewById(R.id.button_share);
            button_direction = (ImageButton) itemView.findViewById(R.id.button_direction);

            button_fav.setOnClickListener(this);
            button_share.setOnClickListener(this);
            button_direction.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_fav:
                    System.out.println("click fav");
                    break;
                case R.id.button_share:
                    System.out.println("click share");
                    break;
                case R.id.button_direction:
                    System.out.println("click direction");
                    break;
                case R.id.bg_card_image:
                    int pos = (Integer) v.getTag();
                    Data.Place place = mPlaces.get(pos);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("PlaceObject", place);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
