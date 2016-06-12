package com.redrails.paste;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by toby on 04/06/16.
 */
class GetPasteCount extends AsyncTask<String, String, String> {
    Context context;
    public GetPasteCount(Context c){
        context = c;
    }

    String result = "";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(URLValues.LASTPASTEURL);
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            result = br.readLine();

            return result;

        } catch (Exception e) {
            Log.e("Get Url", "Error in downloading: " + e.toString());
        }
        return null;
    }


    @Override
    protected void onPostExecute(final String result) {
        super.onPostExecute(result);
        TextView pasteCount = (TextView)((Activity) context).findViewById(R.id.pasteCountTextView);
        pasteCount.setText(result);
    }

}