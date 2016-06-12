package com.redrails.paste;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Created by toby on 12/06/16.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_preference);

        Preference button = (Preference)findPreference(getString(R.string.settingsAbout));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("About");
                alertDialog.setMessage("Developed by redrails");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Website",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse("http://ihtasham.com");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                getActivity().startActivity(intent);
                            }
                        });
                alertDialog.show();
                return true;
            }
        });

    }



}
