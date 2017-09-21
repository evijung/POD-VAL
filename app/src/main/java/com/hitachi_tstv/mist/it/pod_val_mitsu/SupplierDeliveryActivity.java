package com.hitachi_tstv.mist.it.pod_val_mitsu;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupplierDeliveryActivity extends AppCompatActivity {

    @BindView(R.id.txt_name)
    TextView nameTextView;
    @BindView(R.id.progess_truck)
    BootstrapProgressBar truckProgress;
    @BindView(R.id.et_comment)
    EditText commentEditText;
    @BindView(R.id.btn_save)
    Button saveButton;
    @BindView(R.id.btn_arrival)
    Button arrivalButton;
    @BindView(R.id.btn_confirm)
    Button confirmButton;

    String planDtlIdString, suppCodeString, suppNameString,totalPercentageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_delivery);
        ButterKnife.bind(this);

        SyncGetTripDetailPickup syncGetTripDetailPickup = new SyncGetTripDetailPickup(this);
        syncGetTripDetailPickup.execute();
    }

    class SyncGetTripDetailPickup extends AsyncTask<Void, Void, String> {
        Context context;

        public SyncGetTripDetailPickup(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("isAdd", "true")
                        .add("planDtl2_id",planDtlIdString)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(MyConstant.urlGetTripDetailPickup).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("VAL-Tag-SupDA", s);

            try {
                JSONArray jsonArray = new JSONArray(s);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    @OnClick({R.id.progess_truck, R.id.btn_save, R.id.btn_arrival, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.progess_truck:
                break;
            case R.id.btn_save:
                break;
            case R.id.btn_arrival:
                break;
            case R.id.btn_confirm:
                break;
        }
    }
}
