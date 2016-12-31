package roding.soconcepts.com.roding.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.adapter.HospitalAdapter;
import roding.soconcepts.com.roding.adapter.MedicationAdapter;
import roding.soconcepts.com.roding.model.Medication;
import roding.soconcepts.com.roding.util.CommonHelper;
import roding.soconcepts.com.roding.util.FontHelper;
import roding.soconcepts.com.roding.util.RodingPreferences;
import roding.soconcepts.com.roding.util.RodingRestClient;
import roding.soconcepts.com.roding.util.ViewUtil;

public class SubmitClaimActivity extends BaseActivity {

    private static final String TAG = "SubmitClaimActivity";
    private EditText memberIdText;
    private EditText attendanceDateText;
    private EditText diagnosisText;
    private EditText fullnameText;
    private EditText textMessage;
    private EditText textMedication;
    private EditText textAmount;
    private EditText textQuantity;
    private EditText textEnteredBy;
    private TextView tvHospitalIdName;
    private TextView tvPatientIdName;
    private TextView tvTotalAmount;
    private TextView tvEnteredBy;
    private ProgressDialog prgDialog;
    private LinearLayout searchLayout;
    private LinearLayout detailsLayout;
    private LinearLayout summaryLayout;
    private Button sendBtn;
    private Button addBtn;
    private double totalAmount;
    final ArrayList<Medication> medications = new ArrayList<>();

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            attendanceDateText.setText(sdf.format(myCalendar.getTime()));
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_claim);
        FontHelper.applyFont(this, findViewById(R.id.activity_submit_claim), "fonts/TrebuchetMS.ttf");

        setup();

        memberIdText = (EditText) findViewById(R.id.member_id);
        attendanceDateText = (EditText) findViewById(R.id.attendance_date);
        attendanceDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(SubmitClaimActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        diagnosisText = (EditText) findViewById(R.id.diagnosis);
        fullnameText = (EditText) findViewById(R.id.full_name);
        textMessage = (EditText) findViewById(R.id.email_message);
        textMedication = (EditText) findViewById(R.id.medication);
        textAmount = (EditText) findViewById(R.id.amount);
        textEnteredBy = (EditText) findViewById(R.id.enteredBy);
        textQuantity = (EditText) findViewById(R.id.quantity);
        tvHospitalIdName = (TextView) findViewById(R.id.hospital_id_name);
        tvPatientIdName = (TextView) findViewById(R.id.patient_id_name);
        tvTotalAmount = (TextView) findViewById(R.id.total_amount);
        tvEnteredBy = (TextView) findViewById(R.id.entered_by_text);
        detailsLayout = (LinearLayout) findViewById(R.id.details_layout);
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        summaryLayout = (LinearLayout) findViewById(R.id.summary_layout);

        sendBtn = (Button) findViewById(R.id.sendBtn);
        sendBtn.setText(getString(R.string.next));
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (sendBtn.getText().equals("Next")) {
                    if (textEnteredBy.getVisibility() == View.VISIBLE) {
                        if (isForm4Valid()) {
                            textEnteredBy.setVisibility(View.GONE);
                            detailsLayout.setVisibility(View.GONE);
                            summaryLayout.setVisibility(View.VISIBLE);
                            sendBtn.setText(getString(R.string.submit));

                            tvHospitalIdName.setText("ID: " + RodingPreferences.read(getApplicationContext(), "id") + "\nName: " + RodingPreferences.read(getApplicationContext(), "hospital"));
                            tvPatientIdName.setText("ID: " + memberIdText.getText().toString().trim() + "\nName: " + fullnameText.getText().toString() + "\nDiagnosis: " + diagnosisText.getText().toString() + "\nAttendance Date: " + attendanceDateText.getText().toString());

                            totalAmount = 0d;
                            for (Medication medication : medications) {
                                totalAmount += medication.getTotalPrice();
                            }

                            tvTotalAmount.setText(String.valueOf(totalAmount));
                            tvEnteredBy.setText("Entered By: " + textEnteredBy.getText().toString());
                        }
                    } else {
                        if (isForm1Valid()) {
                            findMember();
                        }
                    }
                } else {
                    submitClaim();
                }
            }
        });

        final MedicationAdapter recyclerAdapter = new MedicationAdapter(medications, 1);
        final MedicationAdapter recyclerAdapter2 = new MedicationAdapter(medications, 2);

        addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isForm3Valid()) {
                    double unitPrice = Double.parseDouble(textAmount.getText().toString());
                    double quantity = Double.parseDouble(textQuantity.getText().toString());
                    double totalPrice = unitPrice * quantity;
                    medications.add(new Medication(textMedication.getText().toString().trim(), unitPrice, quantity, totalPrice));
                    recyclerAdapter.notifyDataSetChanged();
                    recyclerAdapter2.notifyDataSetChanged();
                    textAmount.setText("");
                    textQuantity.setText("");
                    textMedication.setText("");
                }
            }
        });


        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(recyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        final RecyclerView mRecyclerView2 = (RecyclerView) findViewById(R.id.recycler_view_2);
        mRecyclerView2.setAdapter(recyclerAdapter2);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    private boolean isForm1Valid() {
        boolean isFormValid = ViewUtil.requiredField(attendanceDateText);
        boolean isFormValid1 = ViewUtil.requiredField(memberIdText);
        boolean isFormValid2 = ViewUtil.requiredField(diagnosisText);

        return isFormValid && isFormValid1 && isFormValid2;
    }

    private boolean isForm2Valid() {
        boolean isFormValid = ViewUtil.requiredField(fullnameText);
        boolean isFormValid1 = ViewUtil.requiredField(textAmount);
        boolean isFormValid2 = ViewUtil.requiredField(textMessage);

        return isFormValid && isFormValid1 && isFormValid2;
    }

    private boolean isForm3Valid() {
        boolean isFormValid = ViewUtil.requiredField(textMedication);
        boolean isFormValid1 = ViewUtil.requiredField(textAmount);
        boolean isFormValid2 = ViewUtil.requiredField(textQuantity);

        return isFormValid && isFormValid1 && isFormValid2;
    }

    private boolean isForm4Valid() {
        boolean isFormValid = ViewUtil.requiredField(textEnteredBy);
        if (isFormValid && medications.isEmpty()) {
            isFormValid = false;
            AlertDialog alertDialog = new AlertDialog.Builder(SubmitClaimActivity.this).create();
            alertDialog.setTitle("Medication(s) Missing!");
            alertDialog.setMessage("Please add at least one medication!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        return isFormValid;
    }

    private void findMember() {
        prgDialog = new ProgressDialog(SubmitClaimActivity.this);
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
                    detailsLayout.setVisibility(View.GONE);
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
                            detailsLayout.setVisibility(View.VISIBLE);
                            searchLayout.setVisibility(View.GONE);
                            String otherNames = message.getString("othernames");
                            String surname = message.getString("surname");
                            fullnameText.setText(surname + " " + otherNames);
                            textEnteredBy.setVisibility(View.VISIBLE);
                            //sendBtn.setText(getString(R.string.send));
                            memberIdText.setEnabled(false);
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

    private void submitClaim() {
        prgDialog = new ProgressDialog(SubmitClaimActivity.this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        prgDialog.show();

        RequestParams entity = new RequestParams();
        JSONArray medicationsArray = new JSONArray();
        for (Medication medication : medications) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("medication", medication.getMedication());
                obj.put("quantity", medication.getQuantity());
                obj.put("totalPrice", medication.getTotalPrice());
                medicationsArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        entity.put("hospitalId", RodingPreferences.read(getApplicationContext(), "id"));
        entity.put("hospitalName", RodingPreferences.read(getApplicationContext(), "hospital"));
        entity.put("patientId", memberIdText.getText().toString().trim());
        entity.put("patientName", fullnameText.getText().toString());
        entity.put("diagnosis", diagnosisText.getText().toString());
        entity.put("attendancedate", attendanceDateText.getText().toString());
        entity.put("medications", medicationsArray);
        entity.put("totalPrice", totalAmount);
        entity.put("enteredBy", textEnteredBy.getText().toString());
        System.out.println("request is :: " + entity.toString());

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
                try {
                    detailsLayout.setVisibility(View.GONE);
                    // JSON Object
                    JSONObject obj = new JSONObject(new String(responseBody));
                    Log.d(TAG, "Response from Server :: " + new String(responseBody));
                    if (obj == null) {
                        prgDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                    } else {
                        boolean success = obj.getBoolean("success");
                        String message = obj.getString("message");

                        if (success) {
                            prgDialog.dismiss();
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SubmitClaimActivity.this, HomeActivity.class));
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
                        msg = "We were unable to process your request. Please try again";
                    }
                    CommonHelper.showConnectionErrorToast(getApplicationContext(), statusCode, msg);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };

        RodingRestClient.invokeWS(getBaseContext(), entity, "submitClaim?token=" + RodingPreferences.read(getApplicationContext(), "token"), "POST", responseHandler);
    }
}
