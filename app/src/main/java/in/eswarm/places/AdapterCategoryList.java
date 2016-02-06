package in.eswarm.places;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kshitij on 2/6/2016.
 */
public class AdapterCategoryList extends RecyclerView.Adapter<AdapterCategoryList.ViewHolder> {

    private ArrayList<Model_data.Place> places;
    private Context context;


    public AdapterCategoryList(ArrayList<Model_data.Place> places, Context context) {
        this.places = places;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("inside view holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_category_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        System.out.println("inside on bind view holder");
        holder.textView_SiteTitle.setText(places.get(position).getName());
        holder.textView_SiteTimings.setText(places.get(position).getTiming_start() + "-" + places.get(position).getTiming_end());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public Model_data.Place getItem(int position) {
        return places.get(position);
    }

    public void setItemList(ArrayList<Model_data.Place> places) {
        this.places = places;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_SiteTitle;
        TextView textView_SiteTimings;
        TextView textView_SiteDistance;
        View siteImage;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_SiteTitle = (TextView) itemView.findViewById(R.id.site_title);
            textView_SiteTimings = (TextView) itemView.findViewById(R.id.site_timings);
            textView_SiteDistance = (TextView) itemView.findViewById(R.id.site_distance);
            siteImage = itemView.findViewById(R.id.layout_bg);
        }
    }
}
