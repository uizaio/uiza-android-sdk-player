package vn.uiza.uzv3.view.dlg.playlistfolder;

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

import com.daimajia.androidanimations.library.Techniques;

import java.util.List;

import loitp.core.R;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LImageUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.uzv3.util.UZUtil;

public class AdapterPlaylistFolder extends RecyclerView.Adapter<AdapterPlaylistFolder.PlayListHolder> {
    private final String TAG = getClass().getSimpleName();
    private List<Data> dataList;
    private int currentPositionOfDataList;
    private Context context;
    private CallbackPlaylistFolder callbackPlaylistFolder;
    //private int sizeW;
    //private int sizeH;

    public class PlayListHolder extends RecyclerView.ViewHolder {
        private TextView tvDuration;
        private TextView tvDuration2;
        private ImageView ivCover;
        private TextView tvName;
        private TextView tvYear;
        private TextView tvRate;
        private TextView tvDescription;
        private LinearLayout rootView;

        public PlayListHolder(View view) {
            super(view);
            rootView = (LinearLayout) view.findViewById(R.id.root_view);
            tvDuration = (TextView) view.findViewById(R.id.tv_duration);
            tvDuration2 = (TextView) view.findViewById(R.id.tv_duration_2);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvYear = (TextView) view.findViewById(R.id.tv_year);
            tvRate = (TextView) view.findViewById(R.id.tv_rate);
            tvDescription = (TextView) view.findViewById(R.id.tv_description);
            ivCover = (ImageView) view.findViewById(R.id.iv_cover);
        }
    }

    public AdapterPlaylistFolder(Context context, List<Data> dataList, int currentPositionOfDataList, CallbackPlaylistFolder callbackPlaylistFolder) {
        this.context = context;
        this.dataList = dataList;
        this.currentPositionOfDataList = currentPositionOfDataList;
        this.callbackPlaylistFolder = callbackPlaylistFolder;
        //sizeW = LScreenUtil.getScreenWidth() / 5;
    }

    @Override
    public PlayListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.v3_row_playlist_folder, parent, false);
        return new PlayListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlayListHolder playListHolder, final int position) {
        //updateSizeFocus(playListHolder.rootView, false);

        final Data data = dataList.get(position);
        //LLog.d(TAG, "onBindViewHolder" + new Gson().toJson(item));
        UZUtil.setTextDuration(playListHolder.tvDuration, data.getDuration());
        //LLog.d(TAG, "item.getDuration(): " + item.getDuration());
        playListHolder.tvName.setText(data.getName());

        //TODO correct this
        playListHolder.tvYear.setText("2018");
        UZUtil.setTextDuration(playListHolder.tvDuration2, data.getDuration());

        //TODO correct this
        playListHolder.tvRate.setText("12+");
        if (data.getShortDescription() == null || data.getShortDescription().isEmpty()) {
            if (data.getDescription() == null || data.getDescription().isEmpty()) {
                playListHolder.tvDescription.setVisibility(View.GONE);
            } else {
                playListHolder.tvDescription.setText(data.getDescription());
                playListHolder.tvDescription.setVisibility(View.VISIBLE);
            }
        } else {
            playListHolder.tvDescription.setText(data.getShortDescription());
        }
        LImageUtil.load((Activity) context, data.getThumbnail(), playListHolder.ivCover);

        playListHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LLog.d(TAG, TAG + " click: " + item.getName() + ", position: " + position);
                LAnimationUtil.play(v, Techniques.Pulse, new LAnimationUtil.Callback() {
                    @Override
                    public void onCancel() {
                        //do nothing
                    }

                    @Override
                    public void onEnd() {
                        if (callbackPlaylistFolder != null) {
                            callbackPlaylistFolder.onClickItem(data, position);
                        }
                    }

                    @Override
                    public void onRepeat() {
                        //do nothing
                    }

                    @Override
                    public void onStart() {
                        //do nothing
                    }
                });
            }
        });

        playListHolder.rootView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                LLog.d(TAG, "onFocusChange isFocus: " + isFocus);
                //updateSizeFocus(playListHolder.rootView, isFocus);
                if (isFocus) {
                    playListHolder.rootView.setBackgroundResource(R.drawable.bkg_item_playlist_folder);
                } else {
                    playListHolder.rootView.setBackgroundResource(0);
                }
                if (callbackPlaylistFolder != null) {
                    callbackPlaylistFolder.onFocusChange(data, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    /*private void updateSizeFocus(LinearLayout linearLayout, boolean isFocus) {
        if (linearLayout == null) {
            return;
        }
        if (isFocus) {
            linearLayout.getLayoutParams().width = sizeW + sizeW / 3;
            //linearLayout.getLayoutParams().height = sizeH;
        } else {
            linearLayout.getLayoutParams().width = sizeW;
            //linearLayout.getLayoutParams().height = sizeH / 2;
        }
        linearLayout.requestLayout();
    }*/
}