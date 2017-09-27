package com.hitachi_tstv.mist.it.pod_val_mitsu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JobActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView txtTrip;
    @BindView(R.id.textView2)
    TextView txtWork;
    @BindView(R.id.textView4)
    TextView txtTripdate;
    @BindView(R.id.linJATop)
    LinearLayout linJATop;
    @BindView(R.id.lisJAStore)
    ListView lisJAStore;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.button)
    Button buttonFinish;
    @BindView(R.id.lisJABottom)
    LinearLayout lisJABottom;

    String[] loginStrings, placeTypeStrings, planDtlIdStrings, timeArrivalStrings, stationNameStrings, transportTypeStrings;

    String worksheetString,  planNoStrings, endArrivalDateString,startDepartureDateString,datePlanStrings, positionString, planIdString, planDtlIdString;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(JobActivity.this, TripActivity.class);
        intent.putExtra("Login", loginStrings);
        intent.putExtra("Date", datePlanStrings);
        intent.putExtra("PlanId", planIdString);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        ButterKnife.bind(this);

        int i = 0;
        loginStrings = getIntent().getStringArrayExtra("Login");
        datePlanStrings = getIntent().getStringExtra("planDate");
        positionString = getIntent().getStringExtra("position");
        planIdString = getIntent().getStringExtra("planId");
        planDtlIdString = getIntent().getStringExtra("planDtlId");
        txtTripdate.setText(datePlanStrings);
        txtTrip.setText("Trip "+ positionString);

        Log.d("TAG", "Date -->" + datePlanStrings);
        SynGetJobList synGetJobList = new SynGetJobList(JobActivity.this);
        synGetJobList.execute();
    }


    protected class SynGetJobList extends AsyncTask<Void, Void, String> {
        private Context context;


        public SynGetJobList(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                RequestBody requestBody = new FormBody.Builder()
                        .add("isAdd", "true")
                        .add("driver_id", loginStrings[0])
                        .add("plan_id", planIdString)
                        .add("planDtlId", planDtlIdString)
                        .build();
                Request request = builder.post(requestBody).url(MyConstant.urlGetTrip).build();
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
            Log.d("Tag", "Service ==>" + s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObject1 = jsonObject.getJSONObject("tripInfo");

                worksheetString = jsonObject1.getString("work_sheet_no");
                planNoStrings = jsonObject1.getString("planNo");
                startDepartureDateString = jsonObject1.getString("st_departureDate");
                endArrivalDateString = jsonObject1.getString("en_arrivalDate");

                JSONArray jsonArray1 = jsonObject1.getJSONArray("DTL");
                planDtlIdStrings = new String[jsonArray1.length()];
                stationNameStrings = new String[jsonArray1.length()];
                timeArrivalStrings = new String[jsonArray1.length()];
                transportTypeStrings = new String[jsonArray1.length()];
                placeTypeStrings = new String[jsonArray1.length()];
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                    planDtlIdStrings[j] = jsonObject2.getString("planDtl2_id");
                    stationNameStrings[j] = jsonObject2.getString("suppName");
                    timeArrivalStrings[j] = jsonObject2.getString("timeArrival");
                    transportTypeStrings[j] = jsonObject2.getString("transport_type");
                    placeTypeStrings[j] = jsonObject2.getString("placeType");
                }

                if (!startDepartureDateString.equals("")) {
                    btnStart.setVisibility(View.INVISIBLE);
                } else {
                    btnStart.setVisibility(View.VISIBLE);
                    buttonFinish.setEnabled(false);
                }

                if (!endArrivalDateString.equals("")) {
                    buttonFinish.setVisibility(View.INVISIBLE);
                } else {
                    buttonFinish.setVisibility(View.VISIBLE);
                }


                JobAdaptor manageJobAdaptor = new JobAdaptor(JobActivity.this, planDtlIdStrings, stationNameStrings, timeArrivalStrings,placeTypeStrings);
                lisJAStore.setAdapter(manageJobAdaptor);

                lisJAStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (transportTypeStrings[i].equals("PICK UP")) {
                            Intent intent = new Intent(JobActivity.this, SupplierDeliveryActivity.class);
                            intent.putExtra("Date", datePlanStrings);
                            intent.putExtra("Login", loginStrings);
                            intent.putExtra("planDtl2_id", planDtlIdStrings[i]);
                            intent.putExtra("planDtlId", planDtlIdString);
                            intent.putExtra("position", positionString);
                            intent.putExtra("planId", planIdString);
                            intent.putExtra("timeArrival", timeArrivalStrings[i]);
                            intent.putExtra("stationName", stationNameStrings[i]);
                            intent.putExtra("transporttype", transportTypeStrings[i]);
                            startActivity(intent);
                            finish();
                        } else{
                            Intent intent = new Intent(JobActivity.this, PlanDeliveryActivity.class);
                            intent.putExtra("Date", datePlanStrings);
                            intent.putExtra("Login", loginStrings);
                            intent.putExtra("planDtl2_id", planDtlIdStrings[i]);
                            intent.putExtra("planDtlId", planDtlIdString);
                            intent.putExtra("position", positionString);
                            intent.putExtra("planId", planIdString);
                            intent.putExtra("timeArrival", timeArrivalStrings[i]);
                            intent.putExtra("stationName", stationNameStrings[i]);
                            intent.putExtra("transporttype", transportTypeStrings[i]);
                            startActivity(intent);
                            finish();
                        }

                    }
                });

                Log.d("Tag", "Worksheet ==> " + worksheetString);


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Tag", "Error dddddd on post JSONArray ==> " + e + " Line " + e.getStackTrace()[0].getLineNumber());

            }
            txtWork.setText(worksheetString);
        }

    }

    private class SynUpdateTripStatus extends AsyncTask<Void, Void, String> {
        String timeString, latString, longString;
        Context context;

        public SynUpdateTripStatus(String timeString, String latString, String longString, Context context) {
            this.timeString = timeString;
            this.latString = latString;
            this.longString = longString;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("isAdd", "true")
                        .add("planDtl_id",planDtlIdString)
                        .add("drv_username", longString)
                        .add("Lat", latString)
                        .add("Lng", longString)
                        .add("stamp", timeString)
                        .build();

                Request.Builder builder = new Request.Builder();
                Request request = builder.post(requestBody).url(MyConstant.urlGetUpdateDCStart).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Tag", "Error  back ==> " + e + " Line " + e.getStackTrace()[0].getLineNumber());
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Tag", s);
            if (s.equals("Success")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(JobActivity.this, context.getResources().getText(R.string.save_success), Toast.LENGTH_LONG).show();
                        btnStart.setVisibility(View.INVISIBLE);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(JobActivity.this, context.getResources().getText(R.string.save_error), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private class SynUpdateTripStatusFinish extends AsyncTask<Void, Void, String> {
        String timeString, latString, longString,flagStrings;
        Context context;

        public SynUpdateTripStatusFinish(String timeString, String latString, String longString, String flagStrings, Context context) {
            this.timeString = timeString;
            this.latString = latString;
            this.longString = longString;
            this.flagStrings = flagStrings;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("isAdd", "true")
                        .add("planDtl_id",planDtlIdString)
                        .add("drv_username", longString)
                        .add("Lat", latString)
                        .add("Lng", longString)
                        .add("stamp", timeString)
                        .build();

                Request.Builder builder = new Request.Builder();
                Request request = builder.post(requestBody).url(MyConstant.urlUpdateDCFinish).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Tag", "Error  back ==> " + e + " Line " + e.getStackTrace()[0].getLineNumber());
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("Success")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(JobActivity.this, context.getResources().getText(R.string.save_success), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(JobActivity.this, TripActivity.class);
                        intent.putExtra("Login", loginStrings);
                        intent.putExtra("Date", datePlanStrings);
                        intent.putExtra("PlanId", planIdString);
                        startActivity(intent);
                        finish();
                    }
                });
                Log.d("TAG", "Login ==> " + Arrays.toString(loginStrings));
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(JobActivity.this, context.getResources().getText(R.string.save_error), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @OnClick({R.id.btn_start, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:

                final String[] lat = new String[1];
                final String[] lng = new String[1];
                final String[] time = new String[1];
                UtilityClass utilityClass = new UtilityClass(this);
                if (utilityClass.setLatLong(0)) {
                    lat[0] = utilityClass.getLatString();
                    lng[0] = utilityClass.getLongString();
                    time[0] = utilityClass.getDateTime();

                    Log.d("Tag", "Lat/Long : Time ==> " + lat[0] + "/" + lng[0] + " : " + time[0]);


                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Alert");
                    dialog.setCancelable(true);
                    dialog.setMessage("Do you want to save?");

                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SynUpdateTripStatus synUpdateTripStatus = new SynUpdateTripStatus(time[0], lat[0], lng[0],JobActivity.this);
                            synUpdateTripStatus.execute();
                        }
                    });

                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    dialog.show();
                }

                break;
            case R.id.button:
                final String[] latStrings = new String[1];
                final String[] lngStrings = new String[1];
                final String[] timeStrings = new String[1];
                UtilityClass utilityClass1 = new UtilityClass(this);


                if (utilityClass1.setLatLong(0)) {
                    latStrings[0] = utilityClass1.getLatString();
                    lngStrings[0] = utilityClass1.getLongString();
                    timeStrings[0] = utilityClass1.getDateTime();
                    Log.d("Tag", "Lat/Long : Time ==> " + latStrings[0] + "/" + lngStrings[0] + " : " + timeStrings[0]);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Finish");
                    dialog.setCancelable(true);
                    dialog.setMessage("Do you want to finish?");

                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SynUpdateTripStatusFinish synUpdateTripStatusFinish = new SynUpdateTripStatusFinish(timeStrings[0], latStrings[0], lngStrings[0],"finish",JobActivity.this);
                            synUpdateTripStatusFinish.execute();
                        }
                    });

                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    dialog.show();

                }
                break;
        }
    }




}