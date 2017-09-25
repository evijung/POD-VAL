package com.hitachi_tstv.mist.it.pod_val_mitsu;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    @BindView(R.id.btn_arrival)
    Button arrivalButton;
    @BindView(R.id.btn_confirm)
    Button confirmButton;

    String planDtlIdString, suppCodeString, suppNameString, totalPercentageString;
    @BindView(R.id.spnSDAPercentage)
    Spinner percentageSpinner;
    @BindView(R.id.editText)
    EditText PalletEditText;

    String[] loginStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_delivery);
        ButterKnife.bind(this);

        planDtlIdString = "1";

        SyncGetTripDetailPickup syncGetTripDetailPickup = new SyncGetTripDetailPickup(this);
        syncGetTripDetailPickup.execute();
    }


    String[] getSizeSpinner(int size) {
        String[] sizeStrings;
        switch (size) {
            case 100:
                sizeStrings = new String[1];
                sizeStrings[0] = "100";
                break;
            case 90:
                sizeStrings = new String[2];
                sizeStrings[0] = "90";
                sizeStrings[1] = "100";
                break;
            case 80:
                sizeStrings = new String[3];
                sizeStrings[0] = "80";
                sizeStrings[1] = "90";
                sizeStrings[2] = "100";
                break;
            case 70:
                sizeStrings = new String[4];
                sizeStrings[0] = "70";
                sizeStrings[1] = "80";
                sizeStrings[2] = "90";
                sizeStrings[3] = "100";
                break;
            case 60:
                sizeStrings = new String[5];
                sizeStrings[0] = "60";
                sizeStrings[1] = "70";
                sizeStrings[2] = "80";
                sizeStrings[3] = "90";
                sizeStrings[4] = "100";
                break;
            case 50:
                sizeStrings = new String[6];
                sizeStrings[0] = "50";
                sizeStrings[1] = "60";
                sizeStrings[2] = "70";
                sizeStrings[3] = "80";
                sizeStrings[4] = "90";
                sizeStrings[5] = "100";
                break;
            case 40:
                sizeStrings = new String[7];
                sizeStrings[0] = "40";
                sizeStrings[1] = "50";
                sizeStrings[2] = "60";
                sizeStrings[3] = "70";
                sizeStrings[4] = "80";
                sizeStrings[5] = "90";
                sizeStrings[6] = "100";
                break;
            case 30:
                sizeStrings = new String[8];
                sizeStrings[0] = "30";
                sizeStrings[1] = "40";
                sizeStrings[2] = "50";
                sizeStrings[3] = "60";
                sizeStrings[4] = "70";
                sizeStrings[5] = "80";
                sizeStrings[6] = "90";
                sizeStrings[7] = "100";
                break;
            case 20:
                sizeStrings = new String[9];
                sizeStrings[0] = "20";
                sizeStrings[1] = "30";
                sizeStrings[2] = "40";
                sizeStrings[3] = "50";
                sizeStrings[4] = "60";
                sizeStrings[5] = "70";
                sizeStrings[6] = "80";
                sizeStrings[7] = "90";
                sizeStrings[8] = "100";
                break;
            case 10:
                sizeStrings = new String[10];
                sizeStrings[0] = "10";
                sizeStrings[1] = "20";
                sizeStrings[2] = "30";
                sizeStrings[3] = "40";
                sizeStrings[4] = "50";
                sizeStrings[5] = "60";
                sizeStrings[6] = "70";
                sizeStrings[7] = "80";
                sizeStrings[8] = "90";
                sizeStrings[9] = "100";
                break;
            case 0:
                sizeStrings = new String[11];
                sizeStrings[0] = "0";
                sizeStrings[1] = "10";
                sizeStrings[2] = "20";
                sizeStrings[3] = "30";
                sizeStrings[4] = "40";
                sizeStrings[5] = "50";
                sizeStrings[6] = "60";
                sizeStrings[7] = "70";
                sizeStrings[8] = "80";
                sizeStrings[9] = "90";
                sizeStrings[10] = "100";
                break;
            default:
                sizeStrings = null;
                break;
        }

        return sizeStrings;
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
                        .add("planDtl2_id", planDtlIdString)
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
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    suppCodeString = jsonObject.getString("supp_code");
                    suppNameString = jsonObject.getString("supp_nameEn");
                    if (jsonObject.getString("total_percent_load").equals("null")) {
                        totalPercentageString = "0";
                    } else {
                        totalPercentageString = jsonObject.getString("total_percent_load");
                    }
                }

                truckProgress.setProgress(Integer.parseInt(totalPercentageString));

                String[] size = getSizeSpinner(Integer.parseInt(totalPercentageString));

                SpinnerAdaptor spinnerAdaptor = new SpinnerAdaptor(context, size);
                percentageSpinner.setAdapter(spinnerAdaptor);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class SyncUpdateArrival extends AsyncTask<Void, Void, String> {
        Context context;
        String planDtl2String, usernameString;

        public SyncUpdateArrival(Context context, String planDtl2String, String usernameString) {
            this.context = context;
            this.planDtl2String = planDtl2String;
            this.usernameString = usernameString;

        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                UtilityClass utilityClass = new UtilityClass(context);
                utilityClass.setLatLong(0);

                String lat = utilityClass.getLatString();
                String lng = utilityClass.getLongString();

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("isAdd", "true")
                        .add("planDtl2_id", "")
                        .add("Lat", lat)
                        .add("Lng", lng)
                        .add("drv_username", "")
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(MyConstant.urlUpdateArrival).post(requestBody).build();
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

            if (s.equals("Success")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,context.getResources().getString(R.string.save_success),Toast.LENGTH_LONG);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,context.getResources().getString(R.string.save_error),Toast.LENGTH_LONG);
                    }
                });
            }
        }
    }

    class SyncUpdateDeparture extends AsyncTask<Void, Void, String> {
        Context context;

        public SyncUpdateDeparture(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("PlanDtl2_ID","")
                        .add("isAdd","true")
                        .add("Driver_Name","")
                        .add("Lat","")
                        .add("Lng","")
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(MyConstant.urlUpdateDeparture).post(requestBody).build();
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



        }
    }

    @OnClick({R.id.btn_arrival, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_arrival:
                SyncUpdateArrival syncUpdateArrival = new SyncUpdateArrival(SupplierDeliveryActivity.this, planDtlIdString, loginStrings[0]);
                syncUpdateArrival.execute();
                break;
            case R.id.btn_confirm:
                break;
        }
    }
}
