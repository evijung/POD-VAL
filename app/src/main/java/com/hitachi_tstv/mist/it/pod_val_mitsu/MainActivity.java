package com.hitachi_tstv.mist.it.pod_val_mitsu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.imgMALogo)
    ImageView logoImageView;
    @BindView(R.id.edtMAUsername)
    EditText usernameEditText;
    @BindView(R.id.edtMAPassword)
    EditText passwordEditText;
    @BindView(R.id.btnMALogin)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



    }

    @OnClick(R.id.btnMALogin)
    public void onViewClicked() {
        Intent intent = new Intent(MainActivity.this, TripActivity.class);
        startActivity(intent);
    }
}