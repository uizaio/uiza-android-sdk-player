package io.uiza.extensions;

import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SampleUtils {
    private SampleUtils() {
    }

    public static void setVertical(RecyclerView recyclerView) {
        attachLayout(recyclerView, LinearLayout.VERTICAL, 1, false);
    }

    public static void setVertical(RecyclerView recyclerView, int spanCount, boolean reverse) {
        attachLayout(recyclerView, LinearLayout.VERTICAL, spanCount, reverse);
    }

    public static void setHorizontal(RecyclerView recyclerView, int spanCount, boolean reverse) {
        attachLayout(recyclerView, LinearLayout.HORIZONTAL, spanCount, reverse);
    }

    private static void attachLayout(RecyclerView recyclerView, int orientation, int spanCount, boolean reverse) {
        if (spanCount == 1)
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), orientation, reverse));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), spanCount, orientation, reverse));
        recyclerView.setHasFixedSize(true);
    }

    public static void scrollToTop(RecyclerView recyclerView, int elementNum) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int offset = (((LinearLayoutManager) layoutManager).getOrientation() == RecyclerView.VERTICAL) ? recyclerView.getHeight() : recyclerView.getWidth();
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(elementNum, offset);
        }
        if (layoutManager instanceof GridLayoutManager) {
            int offset = (((GridLayoutManager) layoutManager).getOrientation() == RecyclerView.VERTICAL) ? recyclerView.getHeight() : recyclerView.getWidth();
            ((GridLayoutManager) layoutManager).scrollToPositionWithOffset(elementNum, offset);
        }
    }
}
