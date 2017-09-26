package com.hitachi_tstv.mist.it.pod_val_mitsu;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.Response;

public class PlanDeliveryActivity extends AppCompatActivity {

    private String[] planDtl2IdStrings, amountStrings, loginStrings;
    private String planDtlIdString, planDtl2IdString, planNameString, positionString, planIdString, timeArrivalString, transporttypeString;


    @BindView(R.id.txt_name)
    TextView factoryTextviewPD;
    @BindView(R.id.lisPDAPlan)
    ListView lisPDAPlan;
    @BindView(R.id.btn_arrival)
    Button btnArrivalPD;
    @BindView(R.id.btn_confirm)
    Button btnDeparturePD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_delivery);
        ButterKnife.bind(this);

        //get Intent data
        loginStrings = getIntent().getStringArrayExtra("Login");
        planIdString = getIntent().getStringExtra("planIdString");
        planDtl2IdString = getIntent().getStringExtra("planDtl2_id");
        planDtlIdString = getIntent().getStringExtra("planDtlId");
        planNameString = getIntent().getStringExtra("stationName");
        transporttypeString = getIntent().getStringExtra("transporttype");
        positionString = getIntent().getStringExtra("position");
        timeArrivalString = getIntent().getStringExtra("timeArrival");

        PlanDeliveryAdapter planDeliveryAdapter = new PlanDeliveryAdapter(PlanDeliveryActivity.this, planDtl2IdStrings, amountStrings, planDtlIdString);
        lisPDAPlan.setAdapter(planDeliveryAdapter);

        factoryTextviewPD.setText(planNameString);
        SynDeliveryData synDeliveryData = new SynDeliveryData(PlanDeliveryActivity.this);
        synDeliveryData.execute();

    }

    private class SynDeliveryData extends AsyncTask<String, Void, String> {
        private Context context;

        public SynDeliveryData(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("isAdd", "true")
                        .add("planDtl2_id", "1")
                        .build();

                Request.Builder builder = new Request.Builder();
                Request request = builder.url(MyConstant.urlGetTripDetailDelivery).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Tag", "IoException..." + e.getStackTrace()[0].getLineNumber());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Tag", "OnpostExecute:::--->" + s);

            //JSONArray jsonArray = new JSONArray(s);
        }
    }


    @OnClick({R.id.btn_arrival, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_arrival:
                UtilityClass utilityClass = new UtilityClass(PlanDeliveryActivity.this);

                Log.d("Tag", "----Lat.." + utilityClass.getLatString());

                if (utilityClass.setLatLong(0)) {
                    SynUpdateArrival synUpdateArrival = new SynUpdateArrival(utilityClass.getLatString(), utilityClass.getLongString(), utilityClass.getTimeString());
                    synUpdateArrival.execute();
                } else {
                    // Toast.makeText(getBaseContext(),getResources().getText(R.string.err_gps1),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_confirm:
                UtilityClass utilityClass1 = new UtilityClass(PlanDeliveryActivity.this);
                if (utilityClass1.setLatLong(0)) {
                    SynUpdateDeparture synUpdateDeparture = new SynUpdateDeparture(utilityClass1.getLatString(), utilityClass1.getLongString(), utilityClass1.getTimeString());
                    synUpdateDeparture.execute();
                } else {
                    Toast.makeText(getBaseContext(), getResources().getText(R.string.err_gps1), Toast.LENGTH_SHORT).show();
                }

                break;
        }


    }


    private class SynUpdateArrival extends AsyncTask<Void, Void, String> {
        String latString, longString, timeString;

        public SynUpdateArrival(String latString, String longString, String timeString) {
            this.latString = latString;
            this.longString = longString;
            this.timeString = timeString;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Log.d("Tag", "Lat/Long : Time ==> " + latString + "/" + longString + " : " + timeString);
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("isAdd", "true")
                        .add("drv_username", "abc")
                        .add("planDtl2_id", "2")
                        .add("lat", latString)
                        .add("lng", longString)
                        .build();

                Request.Builder builder = new Request.Builder();
                Request request = builder.url(MyConstant.urlUpdateArrival).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("Tag", "e doInBack ==>" + e.toString() + "line::" + e.getStackTrace()[0].getLineNumber());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Tag", "onPostExecute:::-----> " + s);
            if (s.equals("Success")) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), R.string.save_success, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), getResources().getText(R.string.save_error), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private class SynUpdateDeparture extends AsyncTask<Void, Void, String> {
        String latString, longString, timeString;

        public SynUpdateDeparture(String latString, String longString, String timeString) {
            this.latString = latString;
            this.longString = longString;
            this.timeString = timeString;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("isAdd", "true")
                        .add("Driver_Name", "abc")
                        .add("PlanDtl2_ID", "2")
//                      .add("user_name", loginStrings[1])
//                      .add("planDtl2Id", planDtl2IdString)
                        .add("lat", latString)
                        .add("lon", longString)
                        .build();

                Request.Builder builder = new Request.Builder();
                Request request = builder.url(MyConstant.urlUpdateDeparture).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("Tag", "e doInBack ==>" + e.toString() + "line::" + e.getStackTrace()[0].getLineNumber());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Tag", "onPostExecute:::---->Departure:::: " + s);
            if (s.equals("OK")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), R.string.save_success, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), getResources().getText(R.string.save_error), Toast.LENGTH_LONG).show();
                    }
                });
            }

        }
    }


}

