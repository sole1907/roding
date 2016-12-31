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

public class ForgotPasswordActivity extends Activity {

    Button signInButton;
    Button submitButton;
    private EditText emailText;
    private ProgressDialog prgDialog;
    private static final String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        FontHelper.applyFont(this, findViewById(R.id.activity_forgot_password), "fonts/TrebuchetMS.ttf");

        signInButton = (Button) findViewById(R.id.signinBtn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });

        submitButton = (Button) findViewById(R.id.submitBtn);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailText = (EditText) findViewById(R.id.email_text);

                if (isFormValid()) {
                    prgDialog = new ProgressDialog(ForgotPasswordActivity.this);
                    prgDialog.setMessage("Please wait...");
                    prgDialog.setCancelable(false);
                    prgDialog.show();

                    final String email = emailText.getText().toString().trim();

                    RequestParams entity = new RequestParams();
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
                                    String message = obj.getString("message");

                                    if (success) {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
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

                    RodingRestClient.invokeWS(getBaseContext(), entity, "reset", "POST", responseHandler);
                }
            }
        });
    }

    private boolean isFormValid() {
        boolean isFormValid = ViewUtil.isValidEmailField(emailText);

        return isFormValid;
    }

}
