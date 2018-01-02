package com.example.miw.firstmiwapplication;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.widget.Button;
import android.widget.TextView;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;


public class MainActivity extends AppCompatActivity implements DownloadCallback {

    private ModelAPIService model;
    private boolean isModelBound = false;

    private TextView tv;
    private NetworkFragment nwFragment;
    private boolean mDownloading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Loading the content ...", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                refreshTextView();
            }
        });

        tv = findViewById(R.id.mainText);
        tv.setMovementMethod(new ScrollingMovementMethod());

        nwFragment = NetworkFragment.getInstance(getSupportFragmentManager());

        TextInputEditText urlString = findViewById(R.id.urlString);
        urlString.setText("https://raw.githubusercontent.com/michaelhwalther/android_firstapp/master/data/t.json");

        Button b = findViewById(R.id.btn_go);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshTextView();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        //Refresh via menu item
        if (id == R.id.menu_refresh) {
            refreshTextView();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshTextView() {

        out("Loading ...");
        // TODO: Download file from https://raw.githubusercontent.com/michaelhwalther/android_firstapp/master/data/t.json
        if (!mDownloading && nwFragment != null) {
            // Execute the async download.
            TextInputEditText urlStringBox = findViewById(R.id.urlString);
            nwFragment.startDownload(urlStringBox.getText().toString());
            mDownloading = true;
        }

    }

    @Override
    public void updateFromDownload(String result) {
        if (result != null) {
            out("Update: " + result);
        } else {
            out( "Update: no result");
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (nwFragment != null) {
            nwFragment.cancelDownload();
        }
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                out("ERROR!");
                break;
            case Progress.CONNECTING:
                out("Connecting ...");
                break;
            case Progress.CONNECT_SUCCESS:
                out("Connect succeeded.");
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                out("Inputstream created, starting to read ...");
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                out("" + percentComplete + "%");
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                out("Inputstream completely read.");
                break;
        }
    }

    private void out(String msg) {
        Editable textSoFar = tv.getEditableText();
        if( textSoFar != null ) {
            textSoFar.append('\n');
            textSoFar.append(msg);
        } else {
            tv.setText(msg, TextView.BufferType.EDITABLE);
        }
        while (tv.canScrollVertically(1)) {
            tv.scrollBy(0, 10);
        }
    }
}
