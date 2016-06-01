package com.redrails.paste;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

class GetStringFromUrl extends AsyncTask<String, Void, String> {

    private Context usingContext;
    TinyDB tdb;
    public GetStringFromUrl(Context c){
        this.usingContext = c;
        tdb = new TinyDB(usingContext);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        // @BadSkillz codes with same changes
        try {

            URL url = new URL(URLValues.LASTPASTEURL);
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = br.readLine();

            return result;

        } catch (Exception e) {
            Log.e("Get Url", "Error in downloading: " + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(final String result) {
        super.onPostExecute(result);

        // TODO change text view id for yourself
        AlertDialog.Builder ab = new AlertDialog.Builder(usingContext);
        System.out.println(result);
        // show result in textView
        if (result == null) {
            ab.setTitle("Error posting!");
            ab.setMessage("An error ocurred while pushing your paste!");
        } else {

            ArrayList<String> current = tdb.getListString("myPastes");
            ArrayList<String> newPasteList = new ArrayList<>();
            for(String paste: current){
                newPasteList.add(paste);
            }
            newPasteList.add(result);
            tdb.putListString("myPastes",newPasteList);

            TextView alertText = new TextView(usingContext);
            ab = new AlertDialog.Builder(usingContext);
            ab.setTitle("Your paste has been pushed!");
            ab.setMessage(URLValues.URLPREFIX + result);

            ab.setPositiveButton("View in browser", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.parse(URLValues.URLPREFIX + result);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    usingContext.startActivity(intent);
                }
            });

            ab.setNeutralButton("Open", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FragmentManager fm = ((Activity)usingContext).getFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putString("pasteToView", result);
                    ViewPasteFragment vpf = new ViewPasteFragment();
                    vpf.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.content_frame, vpf).commit();
                }
            });
        }
        ab.show();
    }
}