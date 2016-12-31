package roding.soconcepts.com.roding.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import roding.soconcepts.com.roding.util.ViewUtil;

public class ContactUsActivity extends BaseActivity {

    private static final String TAG = "ContactUsActivity";
    private EditText emailText;
    private EditText textMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        FontHelper.applyFont(this, findViewById(R.id.contact_us_activity), "fonts/TrebuchetMS.ttf");

        setup();

        emailText = (EditText) findViewById(R.id.email);
        textMessage = (EditText) findViewById(R.id.email_message);

        Button sendBtn = (Button) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isFormValid())
                    sendEmail();
            }
        });

        final String[] permissions = new String[]{android.Manifest.permission.CALL_PHONE};
        Button callBtn = (Button) findViewById(R.id.callBtn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:07080601040"));
                if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ContactUsActivity.this, android.Manifest.permission.CALL_PHONE)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                        builder.setMessage("To call roding customer service, you have to allow us access to your location.");
                        builder.setTitle("Phone Services");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ContactUsActivity.this, permissions, 0);
                            }
                        });

                        builder.show();
                    } else {
                        ActivityCompat.requestPermissions(ContactUsActivity.this, permissions, 0);
                    }
                }
                startActivity(callIntent);
            }
        });
    }

    private boolean isFormValid() {
        boolean isFormValid = ViewUtil.requiredField(emailText);
        boolean isFormValid1 = ViewUtil.requiredField(textMessage);

        return isFormValid && isFormValid1;
    }

    protected void sendEmail() {
        String[] TO = {"info@rodinghealthcareltd.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Roding Request from: " + emailText.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, textMessage.getText().toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactUsActivity.this, "There is no email client installed.", Toast.LENGTH_LONG).show();
        }
    }
}
