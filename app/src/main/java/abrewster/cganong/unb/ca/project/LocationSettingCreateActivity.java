package abrewster.cganong.unb.ca.project;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class LocationSettingCreateActivity extends AppCompatActivity{


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationsetting_create);
        final DBHelper db = new DBHelper(this);
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = ((TextView) findViewById(R.id.location_input)).getText().toString();
                String address = ((TextView) findViewById(R.id.address_input)).getText().toString();
                boolean bluetooth = ((Switch) findViewById(R.id.bluetooth_switch)).isChecked();
                boolean wifi = ((Switch) findViewById(R.id.wifi_switch)).isChecked();
                boolean ringer = ((Switch) findViewById(R.id.ringer_switch)).isChecked();
                int ringer_volume = ((SeekBar) findViewById(R.id.ringer_volume)).getProgress();
                boolean vibrate = ((Switch) findViewById(R.id.vibrate_switch)).isChecked();
                boolean rotation = ((Switch) findViewById(R.id.rotation_switch)).isChecked();
                int brightness = ((SeekBar) findViewById(R.id.brightness)).getProgress();
                if (db.insertSettings(location,address,bluetooth,wifi,ringer,ringer_volume,vibrate,rotation,brightness)) {
                    Context c = v.getContext();
                    Intent intent = new Intent(c,LocationSettingListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    c.startActivity(intent);

                } else {

                }

            }
        });
    }
}
