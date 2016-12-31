package roding.soconcepts.com.roding.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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
import java.util.HashMap;
import java.util.List;

import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.adapter.HospitalAdapter;
import roding.soconcepts.com.roding.dal.HospitalDAL;
import roding.soconcepts.com.roding.dal.StateDAL;
import roding.soconcepts.com.roding.model.Hospital;
import roding.soconcepts.com.roding.model.State;
import roding.soconcepts.com.roding.util.FontHelper;
import roding.soconcepts.com.roding.util.GPSTracker;

public class HospitalLocatorActivity extends BaseActivity {

    private List<Hospital> hospitals = new ArrayList<>();
    private static final String TAG = "HospitalLocatorActivity";
    GPSTracker gps = null;
    private Spinner spinner = null;
    private String selectState = "-- Select State --";
    private HashMap<String, String> spinnerMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_locator);
        FontHelper.applyFont(this, findViewById(R.id.hospital_locator_activity), "fonts/TrebuchetMS.ttf");

        setup();

        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        List<State> states = StateDAL.getInstance(getBaseContext()).getAllStates();
        System.out.println("State Size :: " + states.size());
        String[] spinnerArray = new String[states.size() + 1];
        spinnerMap = new HashMap<String, String>();
        spinnerArray[0] = selectState;
        for (int i = 0; i < states.size(); i++) {
            spinnerMap.put(states.get(i).getState(), states.get(i).getCode());
            spinnerArray[i + 1] = states.get(i).getState();
        }

        final EditText hospitalText = (EditText) findViewById(R.id.hospital_text);
        spinner = (Spinner) findViewById(R.id.state_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.view_spinner_item,
                spinnerArray
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                String state = spinner.getItemAtPosition(arg2).toString();
                System.out.println("The Selected State is :: " + state + " :: " + spinnerMap.get(state));
                String hospitalName = hospitalText.getText().toString();
                if (hospitalName.isEmpty()) {
                    hospitalName = null;
                }
                if (!state.equals(selectState)) {
                    hospitals = HospitalDAL.getInstance(getBaseContext()).getAllHospitalsInStates(state, spinnerMap.get(state), hospitalName);
                } else {
                    if (hospitalName != null) {
                        hospitals = HospitalDAL.getInstance(getBaseContext()).getAllHospitalsInStates(null, null, hospitalName);
                    } else {
                        hospitals = HospitalDAL.getInstance(getBaseContext()).getAllHospitals();
                    }
                }

                System.out.println("The Selected State Hospital Size is :: " + hospitals.size());
                System.out.println("Hospital Recycler Size is :: " + hospitals.size());
                HospitalAdapter recyclerAdapter = new HospitalAdapter(hospitals);
                mRecyclerView.setAdapter(recyclerAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        hospitalText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String state = spinner.getSelectedItem().toString();
                System.out.println("The Selected State is :: " + state + " :: " + spinnerMap.get(state));
                String hospitalName = s.toString();
                System.out.println("The Selected Hospital is :: " + state + " :: " + spinnerMap.get(state));
                if (hospitalName.isEmpty()) {
                    hospitalName = null;
                }
                if (!state.equals(selectState)) {
                    hospitals = HospitalDAL.getInstance(getBaseContext()).getAllHospitalsInStates(state, spinnerMap.get(state), hospitalName);
                    System.out.println("The Selected State Hospital Size is :: " + hospitals.size());
                } else {
                    if (hospitalName != null) {
                        hospitals = HospitalDAL.getInstance(getBaseContext()).getAllHospitalsInStates(null, null, hospitalName);
                    } else {
                        hospitals = HospitalDAL.getInstance(getBaseContext()).getAllHospitals();
                    }
                    System.out.println("The Selected State Hospital Size is :: " + hospitals.size());
                }

                HospitalAdapter recyclerAdapter = new HospitalAdapter(hospitals);
                mRecyclerView.setAdapter(recyclerAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            }
        });
    }

    private void showDirection(double srcLat, double srcLng, double dstLat, double dstLng) {
        String geoUriString = "http://maps.google.com/maps?" +
                "saddr=" + srcLat + "," + srcLng + "&daddr=" + dstLat + "," + dstLng;

        Intent mapCall = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUriString));
        startActivity(mapCall);
    }

    class GetLocationInfo extends AsyncTask<String, Void, JSONObject> {

        double latitude;
        double longitude;

        protected JSONObject doInBackground(String... addresses) {
            StringBuilder stringBuilder = new StringBuilder();
            try {

                String address = addresses[0].replaceAll(" ", "%20");
                this.latitude = Double.parseDouble(addresses[1]);
                this.longitude = Double.parseDouble(addresses[2]);

                HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response;
                stringBuilder = new StringBuilder();


                response = client.execute(httppost);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                jsonObject = null;
                e.printStackTrace();
            }

            return jsonObject;
        }

        protected void onPostExecute(JSONObject jsonObject) {
            Log.d(TAG, "Response from Server :: " + jsonObject);
            double dstLatitude = 0d;
            double dstLongitude = 0d;
            if (jsonObject != null) {
                try {

                    dstLongitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lng");

                    dstLatitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lat");
                    Log.d(TAG, "Response 2 from Server :: " + dstLatitude + "::" + dstLongitude);
                    showDirection(latitude, longitude, dstLatitude, dstLongitude);
                } catch (JSONException e) {
                    //return false;
                    e.printStackTrace();
                }
            }
        }
    }
}
