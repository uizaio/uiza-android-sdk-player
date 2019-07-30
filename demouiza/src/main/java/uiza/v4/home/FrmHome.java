package uiza.v4.home;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import uiza.R;
import uizacoresdk.interfaces.IOnBackPressed;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LImageUtil;

public class FrmHome extends Fragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        LImageUtil.load(getActivity(), Constants.URL_IMG_THUMBNAIL_2, iv);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_home, container, false);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}