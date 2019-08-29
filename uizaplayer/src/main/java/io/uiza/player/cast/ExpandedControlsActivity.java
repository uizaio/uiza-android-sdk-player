package io.uiza.player.cast;

import android.view.Menu;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity;
import io.uiza.player.R;

/**
 * Fullscreen media controls.
 */
public class ExpandedControlsActivity extends ExpandedControllerActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.casty_discovery, menu);
        CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.casty_media_route_menu_item);
        return true;
    }
}
