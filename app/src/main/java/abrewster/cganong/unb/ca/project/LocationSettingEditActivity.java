package abrewster.cganong.unb.ca.project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LocationSettingEditActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleApiClient mGoogleApiClient;
    private Button findButton;
    private EditText editText;
    private TextView nameText;
    private String TAG = "Perm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_setting_edit);
        String location = getIntent().getStringExtra(LocationSettingDetailFragment.LOCATION_ID);
        final DBHelper db = new DBHelper(this);
        final LocationSetting locationSetting = db.getSettings(location);
        nameText = (TextView) findViewById(R.id.location_input);
        editText = (EditText) findViewById(R.id.address_input);
        nameText.setText(locationSetting.getLocation());
        editText.setText(locationSetting.getAddress());
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
                String address = ((EditText) findViewById(R.id.address_input)).getText().toString();
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddresses = geocoder.getFromLocationName(address, 1);
                    if (null != listAddresses && listAddresses.size() > 0) {
                        Address addr = listAddresses.get(0);
                        address = addr.getAddressLine(0) + ", " + addr.getAddressLine(1) + ", " + addr.getAddressLine(2);
                    }
                } catch (IOException e) {

                }


                if (db.updateSettings(
                        locationSetting.getLocation(),
                        address,
                        locationSetting.isBluetooth(),
                        locationSetting.isWifi(),
                        locationSetting.getRinger_volume(),
                        locationSetting.isVibrate(),
                        locationSetting.isRotation(),
                        locationSetting.getBrightness())) {
                    Context c = v.getContext();
                    Intent intent = new Intent(c, LocationSettingDetailActivity.class);
                    intent.putExtra(LocationSettingDetailFragment.LOCATION_ID, locationSetting.getLocation());
                    intent.putExtra(LocationSettingDetailFragment.ADDRESS_ID, address);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    c.startActivity(intent);
                    finish();

                } else {
                    Log.i("DB", "Update failed");
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
            if (lastLocation != null) {
                Log.i("LOCATION",lastLocation.toString());
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
