package io.uiza.extensions;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private FloatingActionButton actionButton;
    private LinearLayoutManager layoutManager;

    public PaginationScrollListener(LinearLayoutManager layoutManager, FloatingActionButton actionButton) {
        this.layoutManager = layoutManager;
        this.actionButton = actionButton;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(actionButton != null) {
            if (dy > 0 && actionButton.getVisibility() == View.VISIBLE) {
                actionButton.hide();
            } else if (dy < 0 && actionButton.getVisibility() != View.VISIBLE) {
                actionButton.show();
            }
        }
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >=
                    totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();
}
