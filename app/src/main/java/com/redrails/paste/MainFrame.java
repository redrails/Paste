package com.redrails.paste;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toby on 26/05/16.
 */
public class MainFrame extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.mainframe, container, false);


        TinyDB tdb = new TinyDB(getActivity());
        List<String> pasteLists = tdb.getListString("myPastes");

        final ListView listPastes = (ListView) mainView.findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, pasteLists);
        listPastes.setAdapter(adapter);

        listPastes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = listPastes.getAdapter().getItem(position);
                FragmentManager fm = getFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("pasteToView", obj.toString());
                ViewPasteFragment vpf = new ViewPasteFragment();
                vpf.setArguments(bundle);
                fm.beginTransaction().replace(R.id.content_frame, vpf).commit();
            }
        });

        setPasteCount();

        return mainView;

    }

    public void setPasteCount(){
        try {
            new GetPasteCount(getActivity()).execute();
        } catch (Exception e){

        }
    }

}