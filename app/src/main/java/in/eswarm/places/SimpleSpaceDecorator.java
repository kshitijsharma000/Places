package in.eswarm.places;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class SimpleSpaceDecorator extends RecyclerView.ItemDecoration {
    int space;
    int edge;

    public SimpleSpaceDecorator(int space, int edge) {
        this.space = space;
        this.edge = edge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space * 2;
        outRect.left = edge;
        outRect.right = edge;

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space * 2;
        }
    }
}
