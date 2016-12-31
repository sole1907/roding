package roding.soconcepts.com.roding.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import roding.soconcepts.com.roding.dal.StateDAL;
import roding.soconcepts.com.roding.model.Hospital;
import roding.soconcepts.com.roding.util.FontHelper;
import roding.soconcepts.com.roding.util.GPSTracker;

/**
 * Created by mac on 9/3/16.
 */
public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.CustomViewHolder>
{
    List<Hospital> detailList = new ArrayList<>();
    GPSTracker gps = null;
    ViewGroup parent = null;

    public HospitalAdapter(List<Hospital> myList)
    {
        detailList = myList;
    }
    @Override
    public int getItemCount()
    {
        return detailList.size();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, final int viewType)
    {
        this.parent = parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospitalview_item, null);
        FontHelper.applyFont(parent.getContext(), v, "fonts/TrebuchetMS.ttf");

        CustomViewHolder customViewHolder = new CustomViewHolder(v);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(HospitalAdapter.CustomViewHolder holder, final int position)
    {
        final Hospital hospital = detailList.get(position);
        holder.tvHospital.setText(hospital.toString());
        System.out.println("Hospital State is :: " + hospital.getState());
        if (hospital.getAddress() == null || hospital.getState() == null || StateDAL.getInstance(parent.getContext()).getStateByCode(hospital.getState()) == null) {
            holder.btnDirections.setVisibility(View.GONE);
        } else {
            holder.btnDirections.setVisibility(View.VISIBLE);
            holder.btnDirections.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String address = hospital.getAddress() + ", " + StateDAL.getInstance(parent.getContext()).getStateByCode(hospital.getState()).getState() + ", NIGERIA";
                    System.out.println("Get Direction Address == " + address);
                    showLocation(address);
                }
            });
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder
    {
        protected Button btnDirections;
        protected TextView tvHospital ;
        public CustomViewHolder(View view)
        {
            super(view);
            this.btnDirections = (Button) view.findViewById(R.id.btn_directions);
            this.tvHospital = (TextView) view.findViewById(R.id.tv_hospital);
        }
    }

    private void showLocation(String dstAddress) {
        // create class object
        gps = new GPSTracker(parent.getContext());

        // check if GPS enabled
        if (gps.canGetLocation())

        {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            double dstLatitude = 0d;
            double dstLongitude = 0d;

            Geocoder coder = new Geocoder(parent.getContext());
            List<Address> addresses = null;

            try {
                addresses = coder.getFromLocationName(dstAddress, 5);
                if (addresses != null && addresses.size() > 0) {
                    Address location = addresses.get(0);
                    dstLatitude = location.getLatitude();
                    dstLongitude = location.getLongitude();
                    showDirection(latitude, longitude, dstLatitude, dstLongitude);
                } else {
                    new GetLocationInfo().execute(dstAddress, String.valueOf(latitude), String.valueOf(longitude));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // \n is for new line
            /*Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude + "\n" +
                    "D Lat: " + dstLatitude + "\n DLong: " + dstLongitude, Toast.LENGTH_LONG).show();*/
        } else

        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    private void showDirection(double srcLat, double srcLng, double dstLat, double dstLng) {
        String geoUriString = "http://maps.google.com/maps?" +
                "saddr=" + srcLat + "," + srcLng + "&daddr=" + dstLat + "," + dstLng;

        Intent mapCall = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUriString));
        parent.getContext().startActivity(mapCall);
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
                    showDirection(latitude, longitude, dstLatitude, dstLongitude);
                } catch (JSONException e) {
                    //return false;
                    e.printStackTrace();
                }
            }
        }
    }
}
