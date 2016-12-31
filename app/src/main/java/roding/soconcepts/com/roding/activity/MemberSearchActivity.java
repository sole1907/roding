package roding.soconcepts.com.roding.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.util.CommonHelper;
import roding.soconcepts.com.roding.util.FontHelper;
import roding.soconcepts.com.roding.util.RodingPreferences;
import roding.soconcepts.com.roding.util.RodingRestClient;
import roding.soconcepts.com.roding.util.ViewUtil;

public class MemberSearchActivity extends BaseActivity {

    EditText memberIdText;
    ProgressDialog prgDialog;
    private static final String TAG = "MemberSearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_search);
        FontHelper.applyFont(this, findViewById(R.id.member_search_activity), "fonts/TrebuchetMS.ttf");

        setup();

        String role = RodingPreferences.read(getApplicationContext(), "role");

        memberIdText = (EditText) findViewById(R.id.member_id);
        final LinearLayout enroleeLayout = (LinearLayout) findViewById(R.id.enrolee_details);
        enroleeLayout.setVisibility(View.GONE);

        Button searchButton = (Button) findViewById(R.id.searchBtn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    prgDialog = new ProgressDialog(MemberSearchActivity.this);
                    prgDialog.setMessage("Please wait...");
                    prgDialog.setCancelable(false);
                    prgDialog.show();

                    final String memberId = memberIdText.getText().toString().trim();

                    RequestParams entity = new RequestParams();
                    entity.put("memberid", memberId);

                    AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                        // When the response returned by REST has Http response code '200'
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            // Hide Progress Dialog
                            try {
                                enroleeLayout.setVisibility(View.GONE);
                                // JSON Object
                                JSONObject obj = new JSONObject(new String(responseBody));
                                Log.d(TAG, "Response from Server :: " + new String(responseBody));
                                if (obj == null) {
                                    prgDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                                } else {
                                    boolean success = obj.getBoolean("success");
                                    JSONObject message = obj.getJSONObject("message");

                                    if (success) {
                                        prgDialog.dismiss();
                                        enroleeLayout.setVisibility(View.VISIBLE);
                                        String otherNames = message.getString("othernames");
                                        String surname = message.getString("surname");
                                        String company = message.getString("company");
                                        String hospital = message.getString("hospital");
                                        String plan = message.getString("plan");
                                        String sex = message.getString("sex");
                                        String dob = message.getString("dob");
                                        String mobile = message.getString("mobile");
                                        String active = message.getString("active").equals("true") ? "YES" : "NO";

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
                                    } else {
                                        prgDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                prgDialog.dismiss();
                                // TODO Auto-generated catch block
                                Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }

                        // When the response returned by REST has Http response code other than '200'
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            // Hide Progress Dialog
                            prgDialog.dismiss();
                            enroleeLayout.setVisibility(View.GONE);
                            try {
                                System.out.println("Failure Response is :: " + new String(responseBody));
                                JSONObject obj = new JSONObject(new String(responseBody));
                                String msg = obj.has("message") ? obj.getString("message") : null;
                                if (!msg.equalsIgnoreCase("Token has expired")) {
                                    msg = "We were unable to retrieve details for the member id. Please check that it is correct";
                                }
                                CommonHelper.showConnectionErrorToast(getApplicationContext(), statusCode, msg);
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    };

                    RodingRestClient.invokeWS(getBaseContext(), entity, "queryMember?token=" + RodingPreferences.read(getApplicationContext(), "token"), "POST", responseHandler);
                }
            }
        });
    }

    private boolean isFormValid() {
        boolean isFormValid = ViewUtil.requiredField(memberIdText);

        return isFormValid;
    }
}
