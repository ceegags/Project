package abrewster.cganong.unb.ca.project;

import android.app.Activity;
import android.database.Cursor;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;


/**
 * A fragment representing a single LocationSetting detail screen.
 * This fragment is either contained in a {@link LocationSettingListActivity}
 * in two-pane mode (on tablets) or a {@link LocationSettingDetailActivity}
 * on handsets.
 */
public class LocationSettingDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private Location mLocation;

    /**
     * The dummy content this fragment is presenting.
     */

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocationSettingDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            String id = getArguments().getString(ARG_ITEM_ID);
            Activity activity = this.getActivity();
            DBHelper db = new DBHelper(activity);
            mLocation = db.getSettings(id);
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
               appBarLayout.setTitle(id);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.locationsetting_detail, container, false);

        // Show the dummy content as text in a TextView.
        /*if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.locationsetting_detail)).setText(mItem.details);
        }*/
        ((Switch) rootView.findViewById(R.id.bluetooth_switch)).setChecked(mLocation.isBluetooth());
        ((Switch) rootView.findViewById(R.id.wifi_switch)).setChecked(mLocation.isWifi());
        ((Switch) rootView.findViewById(R.id.ringer_switch)).setChecked(mLocation.isRinger());
        ((SeekBar) rootView.findViewById(R.id.ringer_volume)).setProgress(mLocation.getRinger_volume());
        ((Switch) rootView.findViewById(R.id.vibrate_switch)).setChecked(mLocation.isVibrate());
        ((Switch) rootView.findViewById(R.id.rotation_switch)).setChecked(mLocation.isRotation());
        ((SeekBar) rootView.findViewById(R.id.brightness)).setProgress(mLocation.getBrightness());

        return rootView;
    }
}
