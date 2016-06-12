package com.redrails.paste;

import android.preference.PreferenceActivity;

import java.util.List;

/**
 * Created by toby on 12/06/16.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<Header> target)
    {
        loadHeadersFromResource(R.xml.headers_preference, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return SettingsFragment.class.getName().equals(fragmentName);
    }

}
