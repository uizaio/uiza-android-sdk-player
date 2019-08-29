package io.uiza.core.view.snappysmoothscroller;

import android.graphics.PointF;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class StaggeredGridLayoutScrollVectorDetector implements SnappySmoothScroller.ScrollVectorDetector {

    final static int LAYOUT_START = -1;

    final static int LAYOUT_END = 1;

    private StaggeredGridLayoutManager layoutManager;

    public StaggeredGridLayoutScrollVectorDetector(StaggeredGridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    private int getFirstChildPosition() {
        final int childCount = layoutManager.getChildCount();
        View firstView = layoutManager.getChildAt(0);
        if (firstView == null) return 0;
        return childCount == 0 ? 0 : layoutManager.getPosition(firstView);
    }

    private int calculateScrollDirectionForPosition(int position) {
        if (layoutManager.getChildCount() == 0) {
            return layoutManager.getReverseLayout() ? LAYOUT_END : LAYOUT_START;
        }
        final int firstChildPos = getFirstChildPosition();
        return position < firstChildPos != layoutManager.getReverseLayout() ? LAYOUT_START : LAYOUT_END;
    }

    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        final int direction = calculateScrollDirectionForPosition(targetPosition);
        if (direction == 0) {
            return null;
        }
        if (layoutManager.getOrientation() == StaggeredGridLayoutManager.HORIZONTAL) {
            return new PointF(direction, 0);
        } else {
            return new PointF(0, direction);
        }
    }
}
