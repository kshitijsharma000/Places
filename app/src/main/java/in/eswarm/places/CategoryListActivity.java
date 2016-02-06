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

import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog dialog;
    AdapterCategoryList adapter;
    ArrayList<Model_data> modelDataArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list_activity);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerCategoryList);

        recyclerView.addOnItemTouchListener(new DragController(this, recyclerView, new Clicklistener() {
            @Override
            public void Onclick(View view, int position) {
                System.out.println("inside recycler item on click : " + position);
                //ActivityData data = adapter_favView.getItem(position);
                //todo replace by new adapter
                Intent intent = new Intent(CategoryListActivity.this, DetailActivity.class);
                //intent.putExtra("activityObject", data);
                //todo put item index
                startActivity(intent);
            }

            @Override
            public void OnLongclick(View view, int position) {
                System.out.println("inside recycler item on long click : " + position);
            }
        }));

        adapter = new AdapterCategoryList(new ArrayList<Model_data>(), this);
        get_data_from_db();
        update_recyclerview();
    }

    private void get_data_from_db() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setTitle("Loading");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        //Todo network query here..
        dialog.hide();
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
