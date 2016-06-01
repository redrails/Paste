package com.redrails.paste;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toby on 26/05/16.
 */
public class MakePasteFragment extends Fragment {

    private String data;
    final static String TAG = "";
    boolean post = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.make_paste, container, false);

        final Button paste = (Button)mainView.findViewById(R.id.sendPaste);
        final EditText pasteText = (EditText)mainView.findViewById(R.id.pasteContentSend);
        Bundle bundle = this.getArguments();
        try {
            String passedText = bundle.getString("sharedpaste");
            pasteText.setText(passedText);
        } catch (NullPointerException e){
            System.out.println("no shared data received");
        }
        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pasteSendPrep = pasteText.getText().toString();

                if(TextUtils.isEmpty(pasteSendPrep) || pasteSendPrep.trim().length() < 10){
                    pasteText.setError("Your paste must include at least 10 characters.");
                    return;
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle("Sending paste")
                        .setMessage("Are you sure you want to send this paste?")
                        .setIcon(R.drawable.ic_upload_warning)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                CheckBox mdStatus = (CheckBox)getActivity().findViewById(R.id.markDownStatus);
                                String mdtoString = "";
                                if(mdStatus.isChecked()){
                                    mdtoString = "yes";
                                } else {
                                    mdtoString = "no";
                                }
                                sendPostRequest(pasteText.getText().toString(), mdtoString);

                                new GetStringFromUrl(getActivity()).execute(URLValues.LASTPASTEURL);

                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

        return mainView;

    }

    private void sendPostRequest(String pasteInput, final String md) {

        final String mdS = md;

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {

                String pasteData = params[0];
                System.out.println("*** doInBackground ** paramUsername " + pasteData);

                HttpClient httpClient = new DefaultHttpClient();

                // In a POST request, we don't pass the values in the URL.
                //Therefore we use only the web page URL as the parameter of the HttpPost argument
                HttpPost httpPost = new HttpPost(URLValues.POSTURL);

                // Because we are not passing values over the URL, we should have a mechanism to pass the values that can be
                //uniquely separate by the other end.
                //To achieve that we use BasicNameValuePair
                //Things we need to pass with the POST request
                BasicNameValuePair pasteValPair = new BasicNameValuePair("paste", pasteData);
                BasicNameValuePair mdValPair = new BasicNameValuePair("markdown", mdS);

                // We add the content that we want to pass with the POST request to as name-value pairs
                //Now we put those sending details to an ArrayList with type safe of NameValuePair
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(pasteValPair);
                nameValuePairList.add(mdValPair);

                try {
                    // UrlEncodedFormEntity is an entity composed of a list of url-encoded pairs.
                    //This is typically useful while sending an HTTP POST request.
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);

                    // setEntity() hands the entity (here it is urlEncodedFormEntity) to the request.
                    httpPost.setEntity(urlEncodedFormEntity);

                    try {
                        // HttpResponse is an interface just like HttpPost.
                        //Therefore we can't initialize them
                        HttpResponse httpResponse = httpClient.execute(httpPost);

                        // According to the JAVA API, InputStream constructor do nothing.
                        //So we can't initialize InputStream although it is not an interface
                        InputStream inputStream = httpResponse.getEntity().getContent();

                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        StringBuilder stringBuilder = new StringBuilder();

                        String bufferedStrChunk = null;

                        while((bufferedStrChunk = bufferedReader.readLine()) != null){
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("First Exception caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
                        System.out.println("Second Exception caz of HttpResponse :" + ioe);
                        ioe.printStackTrace();
                    }

                } catch (UnsupportedEncodingException uee) {
                    System.out.println("An Exception given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                if(result != null){
                    Toast.makeText(getActivity(), "Your paste is being posted...", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(pasteInput);
    }

}
