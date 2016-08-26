package com.redrails.paste;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

/**
 * Created by toby on 26/05/16.
 */
public class ViewPasteFragment extends Fragment {

    String current = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.view_paste, container, false);
        final WebView pasteContent = (WebView) mainView.findViewById(R.id.pasteContent);
        final TextView pasteIdHeader = (TextView) mainView.findViewById(R.id.pasteId);
        Bundle bundle = this.getArguments();

        try {
            //final String passedText = (bundle.getString("pasteToView") == null ? "" : (bundle.getString("pasteToView")));
            final String passedText = (bundle.getString("sharedLink")==null || bundle.getString("sharedLink").isEmpty()
                                        ? bundle.getString("pasteToView") : bundle.getString("sharedLink"));
            System.out.println(passedText);
            pasteContent.loadUrl("about:blank");
            pasteIdHeader.setText("");
            String urlConstructor = URLValues.URLPREFIX + passedText;
            new RetrieveFeedTask(new OnTaskFinished() {
                @Override
                public void onFeedRetrieved(String feeds) {
                    CharSequence beforeToastTxt = "Paste "+passedText+" has been loaded!";
                    Toast beforeToast = Toast.makeText(getActivity(), beforeToastTxt, Toast.LENGTH_LONG);
                    beforeToast.show();
                    pasteIdHeader.setText(passedText);
                    current = passedText;
                    pasteContent.getSettings().setJavaScriptEnabled(true);
                    pasteContent.getSettings().setDomStorageEnabled(true);
                    pasteContent.getSettings().setPluginState(WebSettings.PluginState.ON);
                    pasteContent.loadData(feeds, "text/html", null);
                    CharSequence toastText= "Paste "+passedText+" has been loaded!";
                    Toast toast = Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }).execute(urlConstructor);
        } catch (NullPointerException e){
            System.out.println("no shared data received");
        }

        setHasOptionsMenu(true); // app menu bar

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter the paste number you want to view");

                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pasteContent.loadUrl("about:blank");
                        pasteIdHeader.setText("");
                        String urlConstructor = URLValues.URLPREFIX + input.getText();
                        new RetrieveFeedTask(new OnTaskFinished() {
                            @Override
                            public void onFeedRetrieved(String feeds) {
                                CharSequence beforeToastTxt = "Paste "+input.getText()+" has been loaded!";
                                Toast beforeToast = Toast.makeText(getActivity(), beforeToastTxt, Toast.LENGTH_LONG);
                                beforeToast.show();
                                pasteIdHeader.setText(input.getText());
                                current = input.getText().toString();
                                pasteContent.getSettings().setJavaScriptEnabled(true);
                                pasteContent.loadData(feeds, "text/html", null);
                                CharSequence toastText= "Paste "+input.getText()+" has been loaded!";
                                Toast toast = Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }).execute(urlConstructor);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

            }
        });
        return mainView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.share_menu){

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Paste: "+current);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://ihtasham.com/paste/pastes/"+current);
            startActivity(Intent.createChooser(sendIntent, "Sharing"));
         return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
