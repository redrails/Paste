package com.redrails.paste;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by toby on 24/05/16.
 */

/**
 * @author toby
 * @see android.os.AsyncTask
 * class: RetrieveFeedTas - gets HTML contents from URL.
 * */
class RetrieveFeedTask extends AsyncTask<String, Void, String>
{
    String HTML_response= "";

    OnTaskFinished onOurTaskFinished;


    public RetrieveFeedTask(OnTaskFinished onTaskFinished)
    {
        onOurTaskFinished = onTaskFinished;
    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    /**
     * @param urls : a list of all urls supplied to the parser
     * @return HTML: formatted read
     * */
    protected String doInBackground(String... urls)
    {
        try
        {
            URL url = new URL(urls[0]); // enter your url here which to download

            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            for(int i = 0; i<15; i++){
                HTML_response += br.readLine();
            }
            while ((inputLine = br.readLine()) != null)
            {
                HTML_response += "<br />" + inputLine;

            }
            br.close();

            System.out.println("Done");

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return HTML_response;
    }

    @Override
    protected void onPostExecute(String feed)
    {
        onOurTaskFinished.onFeedRetrieved(feed);
    }
}