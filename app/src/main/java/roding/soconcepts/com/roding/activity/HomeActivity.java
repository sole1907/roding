package roding.soconcepts.com.roding.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.dal.HospitalDAL;
import roding.soconcepts.com.roding.dal.MemberDAL;
import roding.soconcepts.com.roding.dal.StateDAL;
import roding.soconcepts.com.roding.model.Hospital;
import roding.soconcepts.com.roding.model.Member;
import roding.soconcepts.com.roding.model.State;
import roding.soconcepts.com.roding.util.FontHelper;
import roding.soconcepts.com.roding.util.RodingPreferences;
import roding.soconcepts.com.roding.util.RodingRestClient;

public class HomeActivity extends BaseActivity {

    LinearLayout hospitalLocatorLayout;
    LinearLayout myDetailsLayout;
    LinearLayout myRodingIDLayout;
    LinearLayout socialsLayout;
    LinearLayout faqsLayout;
    LinearLayout contactLayout;
    LinearLayout hospitalFaqsLayout;
    LinearLayout submitClaimLayout;
    LinearLayout memberSearchLayout;
    LinearLayout requestUsageLayout;
    private ProgressDialog prgDialog;
    String role = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FontHelper.applyFont(this, findViewById(R.id.home_activity), "fonts/TrebuchetMS.ttf");

        setup();

        prgDialog = new ProgressDialog(HomeActivity.this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        //prgDialog.show();

        role = RodingPreferences.read(getBaseContext(), "role");
        System.out.println("Role on Login is :: " + role);

        if (role.equals("Enrolee") || role.equals("Client")) {
            prgDialog.show();
        }

        if (role.equals("Client")) {
            populateMembers();
        }

        if (role.equals("Enrolee") || role.equals("Client")) {
            populateStates();
            populateHospitals();
        }


        hospitalLocatorLayout = (LinearLayout) findViewById(R.id.hospital_locator_layout);
        hospitalLocatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, HospitalLocatorActivity.class));
            }
        });

        submitClaimLayout = (LinearLayout) findViewById(R.id.submit_claim_layout);
        submitClaimLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SubmitClaimActivity.class));
            }
        });

        myDetailsLayout = (LinearLayout) findViewById(R.id.my_details_layout);
        myDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MyDetailsActivity.class));
            }
        });

        memberSearchLayout = (LinearLayout) findViewById(R.id.member_search_layout);
        memberSearchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (role.equals("Client")) {
                    startActivity(new Intent(HomeActivity.this, CompanyMemberSearchActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this, MemberSearchActivity.class));
                }
            }
        });

        myRodingIDLayout = (LinearLayout) findViewById(R.id.my_roding_id_layout);
        myRodingIDLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MyRodingIDActivity.class));
            }
        });

        socialsLayout = (LinearLayout) findViewById(R.id.socials_layout);
        socialsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SocialsActivity.class));
            }
        });

        faqsLayout = (LinearLayout) findViewById(R.id.faqs_layout);
        faqsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FaqsActivity.class));
            }
        });

        hospitalFaqsLayout = (LinearLayout) findViewById(R.id.hospital_faqs_layout);
        hospitalFaqsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FaqsActivity.class));
            }
        });

        contactLayout = (LinearLayout) findViewById(R.id.contact_layout);
        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ContactUsActivity.class));
            }
        });

        requestUsageLayout = (LinearLayout) findViewById(R.id.request_usage_layout);
        requestUsageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, RequestUsageActivity.class));
            }
        });

        if (role.equals("Enrolee")) {
            hospitalLocatorLayout.setVisibility(View.GONE); // was visible
            submitClaimLayout.setVisibility(View.GONE);
            faqsLayout.setVisibility(View.VISIBLE);
            hospitalFaqsLayout.setVisibility(View.GONE);
            memberSearchLayout.setVisibility(View.GONE);
            requestUsageLayout.setVisibility(View.GONE);
            myRodingIDLayout.setVisibility(View.VISIBLE);
            myDetailsLayout.setVisibility(View.VISIBLE);
        } else if (role.equals("Provider")) {
            hospitalLocatorLayout.setVisibility(View.GONE);
            submitClaimLayout.setVisibility(View.VISIBLE);
            faqsLayout.setVisibility(View.GONE);
            hospitalFaqsLayout.setVisibility(View.VISIBLE);
            requestUsageLayout.setVisibility(View.GONE);
            memberSearchLayout.setVisibility(View.VISIBLE);
            myRodingIDLayout.setVisibility(View.GONE);
            myDetailsLayout.setVisibility(View.GONE);
        } else if (role.equals("Client")) {
            hospitalLocatorLayout.setVisibility(View.GONE); // was visible
            submitClaimLayout.setVisibility(View.GONE);
            faqsLayout.setVisibility(View.VISIBLE);
            hospitalFaqsLayout.setVisibility(View.GONE);
            requestUsageLayout.setVisibility(View.VISIBLE);
            memberSearchLayout.setVisibility(View.VISIBLE);
            myRodingIDLayout.setVisibility(View.GONE);
            myDetailsLayout.setVisibility(View.GONE);
        }
    }

    private void populateStates() {
        RequestParams entity = new RequestParams();
        //entity.put("token", Time2ScorePreferences.read(getApplicationContext(), "token"));

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(new String(responseBody));
                    //Log.d(TAG, "Response from Server :: " + new String(responseBody));
                    if (obj == null) {
                        //prgDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                    } else {
                        boolean success = obj.getBoolean("success");

                        //prgDialog.dismiss();

                        if (success) {
                            StateDAL.getInstance(getBaseContext()).deleteAllStates();
                            JSONArray states = obj.getJSONArray("message");
                            for (int i = 0; i < states.length(); i++) {
                                State state = new State(states.getJSONObject(i).getString("id"), states.getJSONObject(i).getString("state"));
                                StateDAL.getInstance(getBaseContext()).addState(state);
                            }
                        } else {
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    //prgDialog.dismiss();
                    // TODO Auto-generated catch block
                    //Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
                //prgDialog.dismiss();
            }
        };

        RodingRestClient.invokeWS(getBaseContext(), entity, "states", "GET", responseHandler);
    }

    private void populateHospitals() {
        RequestParams entity = new RequestParams();
        //entity.put("token", Time2ScorePreferences.read(getApplicationContext(), "token"));

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(new String(responseBody));
                    //Log.d(TAG, "Response from Server :: " + new String(responseBody));
                    if (obj == null) {
                        prgDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                    } else {
                        boolean success = obj.getBoolean("success");

                        //prgDialog.dismiss();

                        if (success) {
                            HospitalDAL.getInstance(getBaseContext()).deleteAllHospitals();
                            JSONArray hospitals = obj.getJSONArray("message");
                            for (int i = 0; i < hospitals.length(); i++) {
                                JSONObject hObject = hospitals.getJSONObject(i);
                                Hospital hospital = new Hospital(i + 1, hObject.has("name") ? hObject.getString("name") : null, hObject.has("city") ? hObject.getString("city") : null, hObject.has("address") ? hObject.getString("address") : null, hObject.has("state") ? hObject.getString("state") : null, hObject.has("phone") ? hObject.getString("phone") : null, hObject.has("email") ? hObject.getString("email") : null);
                                HospitalDAL.getInstance(getBaseContext()).addHospital(hospital);
                            }
                            prgDialog.dismiss();
                        } else {
                            prgDialog.dismiss();
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    prgDialog.dismiss();
                    // TODO Auto-generated catch block
                    //Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
                prgDialog.dismiss();
            }
        };

        RodingRestClient.invokeWS(getBaseContext(), entity, "hospitals", "GET", responseHandler);
    }

    private void populateMembers() {
        System.out.println("Populating Members ... ");
        RequestParams entity = new RequestParams();
        entity.put("companyid", RodingPreferences.read(getApplicationContext(), "id"));
        System.out.println("CoID is :: " + RodingPreferences.read(getApplicationContext(), "id"));

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(new String(responseBody));
                    Log.d("members", "Response from members Server :: " + new String(responseBody));
                    if (obj == null) {
                        //prgDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                    } else {
                        boolean success = obj.getBoolean("success");

                        //prgDialog.dismiss();

                        if (success) {
                            MemberDAL.getInstance(getBaseContext()).deleteAllMembers();
                            JSONArray members = obj.getJSONArray("message");
                            for (int i = 0; i < members.length(); i++) {
                                JSONObject hObject = members.getJSONObject(i);
                                System.out.println("Add Member is :: " + hObject.getString("id"));
                                Member member = new Member(i + 1, hObject.getString("id"), hObject.getString("surname"), hObject.getString("otherNames"), hObject.getString("active").equals("true"));
                                MemberDAL.getInstance(getBaseContext()).addMember(member);
                            }

                        } else {
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    //Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
            }
        };

        RodingRestClient.invokeWS(getBaseContext(), entity, "members?token=" + RodingPreferences.read(getApplicationContext(), "token"), "POST", responseHandler);
    }

}
