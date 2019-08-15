package testlibuiza.activity;

/**
 * Created by www.muathu@gmail.com on 11/7/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.uiza.core.util.UzImageUtil;
import java.util.List;
import testlibuiza.app.R;

public class AdapterDummy extends RecyclerView.Adapter<AdapterDummy.DummyHolder> {
    private final String TAG = getClass().getSimpleName();
    private List<Dummy> dummyList;
    private Context context;
    private Callback callback;

    public interface Callback {
        public void onClickItem(Dummy dummy, int position);

        public void onFocusChange(Dummy dummy, int position);
    }

    public class DummyHolder extends RecyclerView.ViewHolder {
        private ImageView ivCover;
        private TextView tvName;
        private LinearLayout rootView;

        public DummyHolder(View view) {
            super(view);
            rootView = (LinearLayout) view.findViewById(R.id.root_view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            ivCover = (ImageView) view.findViewById(R.id.iv_cover);
        }
    }

    public AdapterDummy(Context context, List<Dummy> dummyList, Callback callback) {
        this.context = context;
        this.dummyList = dummyList;
        this.callback = callback;
    }

    @Override
    public DummyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_dummy, parent, false);
        return new DummyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DummyHolder playListHolder, final int position) {
        final Dummy dummy = dummyList.get(position);
        playListHolder.tvName.setText(dummy.getName());
        //LLog.d(TAG, "dummy.getUrl() position " + position + " -> " + dummy.getUrl());
        UzImageUtil.load((Activity) context, dummy.getUrl(), playListHolder.ivCover, R.drawable.uiza_logo_mini);

        playListHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClickItem(dummy, position);
                }
            }
        });

        playListHolder.rootView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    playListHolder.rootView.setBackgroundResource(R.drawable.bkg_item_playlist_folder);
                } else {
                    playListHolder.rootView.setBackgroundResource(0);
                }
                if (callback != null) {
                    callback.onFocusChange(dummy, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dummyList == null ? 0 : dummyList.size();
    }
}