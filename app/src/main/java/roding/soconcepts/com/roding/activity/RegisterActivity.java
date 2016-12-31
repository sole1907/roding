package roding.soconcepts.com.roding.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import roding.soconcepts.com.roding.R;
import roding.soconcepts.com.roding.util.CommonHelper;
import roding.soconcepts.com.roding.util.FontHelper;
import roding.soconcepts.com.roding.util.RodingRestClient;
import roding.soconcepts.com.roding.util.ViewUtil;

public class RegisterActivity extends Activity {

    Button signInButton;
    Button registerButton;
    private EditText mobileText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private EditText rodingIdText;
    private EditText emailText;
    private ProgressDialog prgDialog;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FontHelper.applyFont(this, findViewById(R.id.activity_register), "fonts/TrebuchetMS.ttf");

        mobileText = (EditText) findViewById(R.id.mobile_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        confirmPasswordText = (EditText) findViewById(R.id.confirm_password_text);
        rodingIdText = (EditText) findViewById(R.id.roding_id_text);
        emailText = (EditText) findViewById(R.id.email_text);

        signInButton = (Button) findViewById(R.id.signinBtn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerButton = (Button) findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    prgDialog = new ProgressDialog(RegisterActivity.this);
                    prgDialog.setMessage("Please wait...");
                    prgDialog.setCancelable(false);
                    prgDialog.show();

                    final String mobile = mobileText.getText().toString().trim();
                    final String password = passwordText.getText().toString().trim();
                    final String rodingId = rodingIdText.getText().toString().trim();
                    final String email = emailText.getText().toString().trim();

                    RequestParams entity = new RequestParams();
                    entity.put("mobile", mobile);
                    entity.put("password", password);
                    entity.put("roding_id", rodingId);
                    entity.put("email", email);

                    AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                        // When the response returned by REST has Http response code '200'
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            // Hide Progress Dialog
                            try {
                                // JSON Object
                                JSONObject obj = new JSONObject(new String(responseBody));
                                Log.d(TAG, "Response from Server :: " + new String(responseBody));
                                if (obj == null) {
                                    prgDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                                } else {
                                    boolean success = obj.getBoolean("success");
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                    if (success) {
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }

                                    prgDialog.dismiss();
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
                            try {
                                JSONObject obj = new JSONObject(new String(responseBody));
                                CommonHelper.showConnectionErrorToast(getApplicationContext(), statusCode, obj.has("message") ? obj.getString("message") : null);
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    };

                    RodingRestClient.invokeWS(getBaseContext(), entity, "signup", "POST", responseHandler);
                }
            }
        });
    }

    private boolean isFormValid() {
        boolean isFormValid = ViewUtil.requiredField(mobileText);
        boolean isFormValid1 = ViewUtil.requiredField(passwordText);
        boolean isFormValid2 = ViewUtil.equalsField(passwordText, confirmPasswordText, "Passwords do not match");
        boolean isFormValid3 = ViewUtil.requiredField(rodingIdText);
        boolean isFormValid4 = ViewUtil.isValidEmailField(emailText);

        return isFormValid && isFormValid1 && isFormValid2 && isFormValid3 && isFormValid4;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
