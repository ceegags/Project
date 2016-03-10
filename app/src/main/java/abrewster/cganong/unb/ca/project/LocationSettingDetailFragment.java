package abrewster.cganong.unb.ca.project;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;


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
    public static final String LOCATION_ID = "location_id";
    public static final String ADDRESS_ID = "address_id";
    private LocationSetting mLocationSetting;

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

        if (getArguments().containsKey(LOCATION_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(LOCATION_ID));
            String id = getArguments().getString(LOCATION_ID);
            Activity activity = this.getActivity();
            DBHelper db = new DBHelper(activity);
            mLocationSetting = db.getSettings(id);
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
        ((Switch) rootView.findViewById(R.id.bluetooth_switch)).setChecked(mLocationSetting.isBluetooth());
        ((Switch) rootView.findViewById(R.id.wifi_switch)).setChecked(mLocationSetting.isWifi());
        ((SeekBar) rootView.findViewById(R.id.ringer_volume)).setProgress(mLocationSetting.getRinger_volume());
        ((Switch) rootView.findViewById(R.id.vibrate_switch)).setChecked(mLocationSetting.isVibrate());
        ((Switch) rootView.findViewById(R.id.rotation_switch)).setChecked(mLocationSetting.isRotation());
        ((SeekBar) rootView.findViewById(R.id.brightness)).setProgress(mLocationSetting.getBrightness());

        return rootView;
    }


}
