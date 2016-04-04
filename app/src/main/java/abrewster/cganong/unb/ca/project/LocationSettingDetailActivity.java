package abrewster.cganong.unb.ca.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Switch;

/**
 * An activity representing a single LocationSetting detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link LocationSettingListActivity}.
 */
public class LocationSettingDetailActivity extends AppCompatActivity {
    private String location,address;
    private boolean destroyed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationsetting_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        location = getIntent().getStringExtra(LocationSettingDetailFragment.LOCATION_ID);
        address = getIntent().getStringExtra(LocationSettingDetailFragment.ADDRESS_ID);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),LocationSettingEditActivity.class);
                intent.putExtra(LocationSettingDetailFragment.LOCATION_ID,location);
                startActivity(intent);
            }
        });

        FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = v.getContext();
                DBHelper db = new DBHelper(c);
                db.deleteSettings(location);
                destroyed = true;
                Intent intent = new Intent(c,LocationSettingListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                c.startActivity(intent);
                finish();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(LocationSettingDetailFragment.LOCATION_ID,
                    getIntent().getStringExtra(LocationSettingDetailFragment.LOCATION_ID));
            LocationSettingDetailFragment fragment = new LocationSettingDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.locationsetting_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, LocationSettingListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!destroyed) {
            DBHelper db = new DBHelper(this);
            boolean bluetooth = ((Switch) findViewById(R.id.bluetooth_switch)).isChecked();
            boolean wifi = ((Switch) findViewById(R.id.wifi_switch)).isChecked();
            int ringer_volume = ((SeekBar) findViewById(R.id.ringer_volume)).getProgress();
            /*boolean vibrate = ((Switch) findViewById(R.id.vibrate_switch)).isChecked();
            boolean rotation = ((Switch) findViewById(R.id.rotation_switch)).isChecked();
            int brightness = ((SeekBar) findViewById(R.id.brightness)).getProgress();*/
            boolean vibrate =false;
            boolean rotation = false;
            int brightness = 0;
            db.updateSettings(location, address, bluetooth, wifi, ringer_volume, vibrate, rotation, brightness);
        }
    }
}
