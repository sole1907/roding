package roding.soconcepts.com.roding.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.util.CommonHelper;
import roding.soconcepts.com.roding.util.FontHelper;
import roding.soconcepts.com.roding.util.RodingPreferences;
import roding.soconcepts.com.roding.util.RodingRestClient;
import roding.soconcepts.com.roding.util.ViewUtil;

public class RequestUsageActivity extends BaseActivity {

    private static final String TAG = "RequestUsageActivity";
    private EditText memberIdText;
    private TextView fullnameText;
    private EditText fromDate;
    private EditText toDate;
    private ProgressDialog prgDialog;
    private LinearLayout detailsLayout;
    private Button getMemberBtn;
    private Button submitBtn;
    private boolean isFromDate = false;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MMM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

            if (isFromDate) {
                fromDate.setText(sdf.format(myCalendar.getTime()));
            } else {
                toDate.setText(sdf.format(myCalendar.getTime()));
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_usage);
        FontHelper.applyFont(this, findViewById(R.id.activity_request_usage), "fonts/TrebuchetMS.ttf");

        setup();

        memberIdText = (EditText) findViewById(R.id.member_id);
        fullnameText = (TextView) findViewById(R.id.full_name);
        fromDate = (EditText) findViewById(R.id.from_date);
        fromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isFromDate = true;
                new DatePickerDialog(RequestUsageActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        toDate = (EditText) findViewById(R.id.to_date);
        toDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isFromDate = false;
                new DatePickerDialog(RequestUsageActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        detailsLayout = (LinearLayout) findViewById(R.id.details_layout);

        getMemberBtn = (Button) findViewById(R.id.getMember);
        getMemberBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isFormValid()) {
                    findMember();
                }
            }
        });

        submitBtn = (Button) findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isFormValid()) {
                    sendEmail();
                }
            }
        });
    }

    private boolean isFormValid() {
        boolean isFormValid = ViewUtil.requiredField(memberIdText);
        boolean isFormValid1 = ViewUtil.requiredField(fromDate);
        boolean isFormValid2 = ViewUtil.requiredField(toDate);

        return isFormValid && isFormValid1 && isFormValid2;
    }

    private void findMember() {
        prgDialog = new ProgressDialog(RequestUsageActivity.this);
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
                    //detailsLayout.setVisibility(View.GONE);
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
                            //detailsLayout.setVisibility(View.VISIBLE);
                            String otherNames = message.getString("othernames");
                            String surname = message.getString("surname");
                            fullnameText.setText("Name: " + surname + " " + otherNames);
                            fullnameText.setVisibility(View.VISIBLE);
                            memberIdText.setEnabled(false);
                            getMemberBtn.setEnabled(false);
                            submitBtn.setVisibility(View.VISIBLE);
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
                detailsLayout.setVisibility(View.GONE);
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

    protected void sendEmail() {
        String[] TO = {"info@rodinghealthcareltd.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Request Usage from: " + RodingPreferences.read(getBaseContext(), "company"));
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Member ID : " + memberIdText.getText().toString() + "\nMember Name : " + fullnameText.getText().toString() + "\nFrom Date : " + fromDate.getText().toString() + "\nTo Date: " + toDate.getText().toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(RequestUsageActivity.this, "There is no email client installed.", Toast.LENGTH_LONG).show();
        }
    }
}
