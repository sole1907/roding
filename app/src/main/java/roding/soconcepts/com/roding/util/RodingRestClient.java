package roding.soconcepts.com.roding.util;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Soul on 11/18/2015.
 */
public class RodingRestClient {

    /* Method that performs RESTful webservice invocations
    *
    * @param params
    */
    public static void invokeWS(Context context, RequestParams entity, String action, String method, AsyncHttpResponseHandler responseHandler) {
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setSSLSocketFactory(CustomSSLSocketFactory.getSSLSocketFactory(context));
        //client.addHeader("Content-Type", "multipart/form-data");
        client.addHeader("Accept", "application/json");
        if (method.equalsIgnoreCase("GET")) {
            client.get(context, "http://roding.tigrimigri.com/roding/public/api/auth/" + action, entity, responseHandler);
        } else {
            client.post(context, "http://roding.tigrimigri.com/roding/public/api/auth/" + action, entity, responseHandler);
        }
    }

}
