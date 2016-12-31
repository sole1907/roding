package roding.soconcepts.com.roding.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.model.Hospital;
import roding.soconcepts.com.roding.util.CommonUtils;
import roding.soconcepts.com.roding.util.FontHelper;
import roding.soconcepts.com.roding.util.GPSTracker;
import roding.soconcepts.com.roding.util.RodingPreferences;

public class MyDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);
        FontHelper.applyFont(this, findViewById(R.id.my_details_activity), "fonts/TrebuchetMS.ttf");

        setup();

        String role = RodingPreferences.read(getApplicationContext(), "role");

        LinearLayout enroleeLayout = (LinearLayout) findViewById(R.id.enrolee_details);
        LinearLayout providerLayout = (LinearLayout) findViewById(R.id.provider_details);
        LinearLayout companyLayout = (LinearLayout) findViewById(R.id.company_details);

        if (role.equals("Enrolee")) {
            enroleeLayout.setVisibility(View.VISIBLE);
            providerLayout.setVisibility(View.GONE);
            companyLayout.setVisibility(View.GONE);
            String otherNames = RodingPreferences.read(getApplicationContext(), "othernames");
            String surname = RodingPreferences.read(getApplicationContext(), "surname");
            String company = RodingPreferences.read(getApplicationContext(), "company");
            String hospital = RodingPreferences.read(getApplicationContext(), "hospital");
            String plan = RodingPreferences.read(getApplicationContext(), "plan");
            String sex = RodingPreferences.read(getApplicationContext(), "sex");
            String dob = RodingPreferences.read(getApplicationContext(), "dob");
            String mobile = RodingPreferences.read(getApplicationContext(), "mobile");
            String active = RodingPreferences.read(getApplicationContext(), "active").equals("true") ? "YES" : "NO";

            TextView tvOtherNames = (TextView) findViewById(R.id.tv_other_names);
            tvOtherNames.setText(otherNames);
            TextView tvSurname = (TextView) findViewById(R.id.tv_surname);
            tvSurname.setText(surname);
            TextView tvCompany = (TextView) findViewById(R.id.tv_company);
            tvCompany.setText(company);
            TextView tvHospital = (TextView) findViewById(R.id.tv_hospital);
            tvHospital.setText(hospital);
            TextView tvPlan = (TextView) findViewById(R.id.tv_plan);
            tvPlan.setText(plan);
            TextView tvSex = (TextView) findViewById(R.id.tv_sex);
            tvSex.setText(sex);
            TextView tvDob = (TextView) findViewById(R.id.tv_dob);
            tvDob.setText(dob.substring(0, 10));
            TextView tvMobile = (TextView) findViewById(R.id.tv_mobile);
            tvMobile.setText(mobile);
            TextView tvActive = (TextView) findViewById(R.id.tv_active);
            tvActive.setText(active);
        } else if (role.equals("Provider")) {
            enroleeLayout.setVisibility(View.GONE);
            providerLayout.setVisibility(View.VISIBLE);
            companyLayout.setVisibility(View.GONE);

            String hospital = RodingPreferences.read(getApplicationContext(), "hospital");
            String address = RodingPreferences.read(getApplicationContext(), "address");
            String state = RodingPreferences.read(getApplicationContext(), "state");
            String email = RodingPreferences.read(getApplicationContext(), "email");
            String phone = RodingPreferences.read(getApplicationContext(), "phone");

            TextView tvHospital = (TextView) findViewById(R.id.tv_p_hospital);
            tvHospital.setText(hospital);
            TextView tvAddress = (TextView) findViewById(R.id.tv_address);
            tvAddress.setText(address);
            TextView tvState = (TextView) findViewById(R.id.tv_state);
            tvState.setText(state);
            TextView tvEmail = (TextView) findViewById(R.id.tv_email);
            tvEmail.setText(email);
            TextView tvPhone = (TextView) findViewById(R.id.tv_phone);
            tvPhone.setText(phone);
        } else if (role.equals("Client")) {
            enroleeLayout.setVisibility(View.GONE);
            providerLayout.setVisibility(View.GONE);
            companyLayout.setVisibility(View.VISIBLE);

            String company = RodingPreferences.read(getApplicationContext(), "company");
            String address = RodingPreferences.read(getApplicationContext(), "address");
            String state = RodingPreferences.read(getApplicationContext(), "state");
            String email = RodingPreferences.read(getApplicationContext(), "email");
            String city = RodingPreferences.read(getApplicationContext(), "city");

            TextView tvCompany = (TextView) findViewById(R.id.tv_c_company);
            tvCompany.setText(company);
            TextView tvAddress = (TextView) findViewById(R.id.tv_c_address);
            tvAddress.setText(address);
            TextView tvState = (TextView) findViewById(R.id.tv_c_state);
            tvState.setText(state);
            TextView tvEmail = (TextView) findViewById(R.id.tv_c_email);
            tvEmail.setText(email);
            TextView tvCity = (TextView) findViewById(R.id.tv_city);
            tvCity.setText(city);
        }
    }
}
