package com.brainx.bxcroplibrary.BXBaseClasses;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.ButterKnife;


/**
 * Created by tahir zahoor on 03/07/2017.
 */

public class BaseActivity extends AppCompatActivity {

    //region Properties
    public ProgressDialog progressDialog;
    protected Toolbar toolbar;
    //endregion

    //region Activity LifeCycle
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("processing...");
        progressDialog.setCancelable(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    //endregion

    //region Methods
    protected void setLayout(int layoutId, int toolbarId, String title, boolean homeEnabled) {
        setContentView(layoutId);
        setupToolbar(title, toolbarId, homeEnabled);
    }

    protected void setLayout(int layoutId) {
        setContentView(layoutId);
    }

    private void setupToolbar(String title, int toolbarId, boolean enabled) {
        toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);
        if (!title.isEmpty())
            setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @
                    Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    //endregion
}
