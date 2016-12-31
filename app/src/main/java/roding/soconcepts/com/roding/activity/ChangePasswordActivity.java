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

public class ChangePasswordActivity extends BaseActivity {

    EditText currentPasswodText;
    EditText newPasswodText;
    EditText confirmPasswodText;
    Button changePasswordButton;
    private ProgressDialog prgDialog;
    private static final String TAG = "ChangePasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        FontHelper.applyFont(this, findViewById(R.id.activity_change_password), "fonts/TrebuchetMS.ttf");

        setup();

        currentPasswodText = (EditText) findViewById(R.id.current_password_text);
        newPasswodText = (EditText) findViewById(R.id.new_password_text);
        confirmPasswodText = (EditText) findViewById(R.id.confirm_password_text);
        changePasswordButton = (Button) findViewById(R.id.submitBtn);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    prgDialog = new ProgressDialog(ChangePasswordActivity.this);
                    prgDialog.setMessage("Please wait...");
                    prgDialog.setCancelable(false);
                    prgDialog.show();

                    final String password = currentPasswodText.getText().toString().trim();
                    final String newpassword = newPasswodText.getText().toString().trim();

                    RequestParams entity = new RequestParams();
                    entity.put("password", password);
                    entity.put("new", newpassword);

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
                                        prgDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ChangePasswordActivity.this, HomeActivity.class));
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

                    RodingRestClient.invokeWS(getBaseContext(), entity, "changePassword?token=" + RodingPreferences.read(getApplicationContext(), "token"), "POST", responseHandler);
                }
            }
        });
    }

    private boolean isFormValid() {
        boolean isFormValid = ViewUtil.requiredField(currentPasswodText);
        boolean isFormValid1 = ViewUtil.requiredField(newPasswodText);
        boolean isFormValid2 = ViewUtil.equalsField(newPasswodText, confirmPasswodText, "The new passwords you have chosen don't match");

        return isFormValid && isFormValid1 && isFormValid2;
    }

}
