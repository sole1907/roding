package roding.soconcepts.com.roding.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import roding.soconcepts.com.roding.util.RodingPreferences;
import roding.soconcepts.com.roding.util.RodingRestClient;
import roding.soconcepts.com.roding.util.ViewUtil;

public class LoginActivity extends Activity {

    Button signInButton;
    Button registerButton;
    Button forgotPasswordButton;
    private EditText mobileText;
    private EditText passwordText;
    private ProgressDialog prgDialog;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FontHelper.applyFont(this, findViewById(R.id.activity_login), "fonts/TrebuchetMS.ttf");

        signInButton = (Button) findViewById(R.id.signinBtn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileText = (EditText) findViewById(R.id.mobile_text);
                passwordText = (EditText) findViewById(R.id.password_text);

                if (isFormValid()) {
                    prgDialog = new ProgressDialog(LoginActivity.this);
                    prgDialog.setMessage("Please wait...");
                    prgDialog.setCancelable(false);
                    prgDialog.show();

                    final String phone = mobileText.getText().toString().trim();
                    final String password = passwordText.getText().toString().trim();

                    RequestParams entity = new RequestParams();
                    entity.put("email", phone);
                    entity.put("password", password);

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
                                    String message = obj.getString("message");

                                    if (success) {
                                        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        RodingPreferences.write(getApplicationContext(), "token", message);
                                        RodingPreferences.write(getApplicationContext(), "mobile", obj.getString("mobile"));
                                        String role = obj.getString("role");
                                        RodingPreferences.write(getApplicationContext(), "role", role);
                                        JSONObject details = obj.getJSONObject("details");
                                        RodingPreferences.write(getApplicationContext(), "id", details.getString("id"));
                                        if (role.equals("Enrolee")) {
                                            RodingPreferences.write(getApplicationContext(), "surname", details.getString("surname"));
                                            RodingPreferences.write(getApplicationContext(), "othernames", details.getString("othernames"));
                                            RodingPreferences.write(getApplicationContext(), "company", details.getString("company"));
                                            RodingPreferences.write(getApplicationContext(), "hospital", details.getString("hospital"));
                                            RodingPreferences.write(getApplicationContext(), "plan", details.getString("plan"));
                                            RodingPreferences.write(getApplicationContext(), "sex", details.getString("sex"));
                                            RodingPreferences.write(getApplicationContext(), "dob", details.getString("dob"));
                                            RodingPreferences.write(getApplicationContext(), "mobile", details.getString("mobile"));
                                            RodingPreferences.write(getApplicationContext(), "active", details.getString("active"));
                                        } else if (role.equals("Provider")) {
                                            RodingPreferences.write(getApplicationContext(), "hospital", details.getString("hospital"));
                                            RodingPreferences.write(getApplicationContext(), "address", details.getString("address"));
                                            RodingPreferences.write(getApplicationContext(), "state", details.getString("state"));
                                            RodingPreferences.write(getApplicationContext(), "email", details.getString("email"));
                                            RodingPreferences.write(getApplicationContext(), "phone", details.getString("phone"));
                                        } else if (role.equals("Client")) {
                                            RodingPreferences.write(getApplicationContext(), "company", details.getString("company"));
                                            RodingPreferences.write(getApplicationContext(), "address", details.getString("address"));
                                            RodingPreferences.write(getApplicationContext(), "city", details.getString("city"));
                                            RodingPreferences.write(getApplicationContext(), "state", details.getString("state"));
                                            RodingPreferences.write(getApplicationContext(), "email", details.getString("email"));
                                        }

                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    } else {
                                        prgDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                            try {
                                System.out.println("Failure Response is :: " + new String(responseBody));
                                JSONObject obj = new JSONObject(new String(responseBody));
                                CommonHelper.showConnectionErrorToast(getApplicationContext(), statusCode, obj.has("message") ? obj.getString("message") : null);
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Error Occured [Server's might be unavailable]!", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    };

                    RodingRestClient.invokeWS(getBaseContext(), entity, "login", "POST", responseHandler);
                }
            }
        });

        registerButton = (Button) findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        forgotPasswordButton = (Button) findViewById(R.id.forgotPasswordBtn);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private boolean isFormValid() {
        boolean isFormValid = ViewUtil.requiredField(mobileText);
        boolean isFormValid1 = ViewUtil.requiredField(passwordText);

        return isFormValid && isFormValid1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
