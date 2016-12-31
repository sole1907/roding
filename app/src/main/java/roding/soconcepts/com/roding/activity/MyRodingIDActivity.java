package roding.soconcepts.com.roding.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.util.FontHelper;
import roding.soconcepts.com.roding.util.RodingPreferences;

public class MyRodingIDActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_roding_id);
        FontHelper.applyFont(this, findViewById(R.id.my_roding_id_activity), "fonts/TrebuchetMS.ttf");

        setup();

        TextView tvFullname = (TextView) findViewById(R.id.tv_fullname);
        TextView tvIdNo = (TextView) findViewById(R.id.tv_idno);
        TextView tvStatus = (TextView) findViewById(R.id.tv_status);
        LinearLayout statusLayout = (LinearLayout) findViewById(R.id.layout_status);

        String role = RodingPreferences.read(getApplicationContext(), "role");
        String id = RodingPreferences.read(getApplicationContext(), "id");

        if (role.equals("Enrolee")) {
            String otherNames = RodingPreferences.read(getApplicationContext(), "othernames");
            String surname = RodingPreferences.read(getApplicationContext(), "surname");
            String active = RodingPreferences.read(getApplicationContext(), "active").equals("true") ? "ACTIVE" : "INACTIVE";

            tvFullname.setText(otherNames + " " + surname);
            tvIdNo.setText(id);
            statusLayout.setVisibility(View.VISIBLE);
            tvStatus.setText(active);
        } else if (role.equals("Provider")) {
            String hospital = RodingPreferences.read(getApplicationContext(), "hospital");

            tvFullname.setText(hospital);
            tvIdNo.setText(id);
            statusLayout.setVisibility(View.GONE);
        }
    }
}
