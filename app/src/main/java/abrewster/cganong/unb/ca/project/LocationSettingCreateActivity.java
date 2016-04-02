package abrewster.cganong.unb.ca.project;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationSettingCreateActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleApiClient mGoogleApiClient;
    private Button findButton;
    private EditText editText;
    private String TAG = "Perm";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationsetting_create);
        final DBHelper db = new DBHelper(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        findButton = (Button) findViewById(R.id.find_button);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = ((TextView) findViewById(R.id.location_input)).getText().toString();
                String address = ((TextView) findViewById(R.id.address_input)).getText().toString();
                boolean bluetooth = ((Switch) findViewById(R.id.bluetooth_switch)).isChecked();
                boolean wifi = ((Switch) findViewById(R.id.wifi_switch)).isChecked();
                int ringer_volume = ((SeekBar) findViewById(R.id.ringer_volume)).getProgress();
                boolean vibrate = ((Switch) findViewById(R.id.vibrate_switch)).isChecked();
                boolean rotation = ((Switch) findViewById(R.id.rotation_switch)).isChecked();
                int brightness = ((SeekBar) findViewById(R.id.brightness)).getProgress();
                if (db.insertSettings(location,address,bluetooth,wifi,ringer_volume,vibrate,rotation,brightness)) {
                    Context c = v.getContext();
                    Intent intent = new Intent(c,LocationSettingListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    c.startActivity(intent);
                    finish();

                } else {
                    Log.i("DB","Insert failed");
                }

            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "requestPermissions: No permissions");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            Log.i(TAG, "requestPermissions: Have permissions");
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult: Granted");
                    getLocation();

                } else {
                    Log.i(TAG, "onRequestPermissionsResult: Denied");
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        findButton.setEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.i("LOCATION",lastLocation.toString());
            if (lastLocation != null) {
                editText = (EditText) findViewById(R.id.address_input);
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddresses = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1);
                    if (null != listAddresses && listAddresses.size() > 0) {
                        Address address = listAddresses.get(0);
                        editText.setText(address.getAddressLine(0)+", "+address.getAddressLine(1)+", "+address.getAddressLine(2));
                    }
                } catch (IOException e) {

                }

            } else {
                Log.i(TAG, "Get location: Null location");
            }
        }
        else {
            Log.i(TAG, "Get location without permissions...");
        }
    }
}
