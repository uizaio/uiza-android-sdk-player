package uiza.v4.entities;

/**
 * Created by www.muathu@gmail.com on 12/8/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import uiza.R;

public class EntitiesAdapter extends RecyclerView.Adapter<EntitiesAdapter.MovieViewHolder> {
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    private Context context;
    private Callback callback;
    private List<Entity> moviesList;

    public EntitiesAdapter(Context context, List<Entity> moviesList, Callback callback) {
        this.context = context;
        this.moviesList = moviesList;
        this.callback = callback;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_row, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        final Entity entity = moviesList.get(position);
        holder.title.setText(entity.getTitle());
        holder.genre.setText(entity.getGenre());
        holder.year.setText(entity.getYear());

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClick(entity, position);
                }
            }
        });
        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (callback != null) {
                    callback.onLongClick(entity, position);
                }
                return true;
            }
        });
        if (position == moviesList.size() - 1) {
            if (callback != null) {
                callback.onLoadMore();
            }
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public interface Callback {
        public void onClick(Entity entity, int position);

        public void onLongClick(Entity entity, int position);

        public void onLoadMore();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        public LinearLayout rootView;

        public MovieViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            rootView = (LinearLayout) view.findViewById(R.id.root_view);
        }
    }
}