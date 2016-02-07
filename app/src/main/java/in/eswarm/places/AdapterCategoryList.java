package in.eswarm.places;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import in.eswarm.places.network.Image_Handler;

/**
 * Created by Eswar on 07-02-2016
 */
public class AdapterCategoryList extends RecyclerView.Adapter<AdapterCategoryList.ViewHolder> {

    private ArrayList<Data.Category> mCategories;


    public AdapterCategoryList(ArrayList<Data.Category> categories, Context context) {
        this.mCategories = categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("inside view holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_place_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        System.out.println("inside on bind view holder");
        holder.textView_SiteTitle.setText(mCategories.get(position).getName());
        Image_Handler.get_image_from_url(mCategories.get(position).getUrl(), holder.imageView);
        //holder.textView_SiteTimings.setText(mPlaces.get(position).getTiming_start() + "-" + mPlaces.get(position).getTiming_end());
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public Data.Category getItem(int position) {
        return mCategories.get(position);
    }

    public void setItemList(ArrayList<Data.Category> categories) {
        this.mCategories = categories;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_SiteTitle;
        TextView textView_SiteTimings;
        TextView textView_SiteDistance;
        NetworkImageView imageView;

        View siteImage;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_SiteTitle = (TextView) itemView.findViewById(R.id.site_title);
            textView_SiteTimings = (TextView) itemView.findViewById(R.id.site_timings);
            textView_SiteDistance = (TextView) itemView.findViewById(R.id.site_distance);
            siteImage = itemView.findViewById(R.id.layout_bg);
            imageView = (NetworkImageView) itemView.findViewById(R.id.bg_card_image);

        }
    }
}

