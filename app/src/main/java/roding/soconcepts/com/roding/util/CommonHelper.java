package roding.soconcepts.com.roding.util;

import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import roding.soconcepts.com.roding.activity.LoginActivity;

/**
 * Created by mac on 6/3/16.
 */
public class CommonHelper {

    public static void sendSMS(Context context, String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(context, "Message Sent, You should get an SMS response soon enough...",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(context, "Message Sending Failed, pls ensure you have enough airtime",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public static void showConnectionErrorToast(Context context, int statusCode, String message) {
        if (statusCode == 404) {
            Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
        } else if (statusCode == 500) {
            if (message != null) {
                if (message.equalsIgnoreCase("Token has expired")) {
                    message = "Your current session has timed out. Pls login again";
                    RodingPreferences.write(context, "token", null);
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
            }
        } else if (statusCode == 401) {
            Toast.makeText(context, "Unauthorized!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Remote host seems to be unreachable!", Toast.LENGTH_LONG).show();
        }
    }
}
