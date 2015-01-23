package com.instantripple.cors.provider;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.SSLContexts;

import android.os.AsyncTask;

public class CorsProvider extends CordovaPlugin {
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        if (action.equals("get")) {
			String url = args.optString(0);
			new HttpAsyncTask(callbackContext).execute(url);
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
			pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
			return true;
        }
        return false;
    }

	public class HttpAsyncTask extends AsyncTask<String, Void, String> {
	private CallbackContext mCallbackContext;
	public HttpAsyncTask(CallbackContext callbackContext) {
		mCallbackContext = callbackContext;
	}
     public String doInBackground(String... urls) {
		return GET(urls[0]);
     }

     public void onPostExecute(String data) {
         if (mCallbackContext != null) {
        	 	PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, data);
 				pluginResult.setKeepCallback(false);
                mCallbackContext.sendPluginResult(pluginResult);
				mCallbackContext = null;
            }
     }

	public String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
		SSLContext sslContext = SSLContexts.custom()
        .useTLS()
        .build();
			SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(
        sslContext,
        new String[] {"TLSv1", "TLSv1.1", "TLSv1.2"},
        null,
        SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

            // create HttpClient
            HttpClient httpclient = HttpClients.custom()
        .setSSLSocketFactory(sf)
        .build();;
 
			// make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert input stream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "ERR";
 
        } catch (Exception e) {
			result = "ERR " + e.toString();
        }
 
        return result;
    }
 
    private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
	}
}