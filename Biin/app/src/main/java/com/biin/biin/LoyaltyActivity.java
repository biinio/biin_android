package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.Entities.BNLoyalty;
import com.biin.biin.Entities.BNLoyaltyCard;
import com.biin.biin.Entities.BNOrganization;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.Managers.BNDataManager;
import com.biin.biin.Utils.BNToolbar;
import com.biin.biin.Utils.BNUtils;
import com.google.android.gms.ads.internal.overlay.zzo;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.text.Line;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class LoyaltyActivity extends AppCompatActivity {

    private static final String TAG = "LoyaltyActivity";
    private final int CAMERA_REQUEST = 101;

    private String cardIdentifier;
    private int position;

    private BNLoyaltyCard card;
    private BNOrganization organization;

    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private TextView barcodeInfo;
    private String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty);

        Intent data = getIntent();
        cardIdentifier = data.getStringExtra(BNUtils.BNStringExtras.BNLoyalty);
        position = data.getIntExtra(BNUtils.BNStringExtras.Position, 0);

        BNDataManager dataManager = BNAppManager.getInstance().getDataManagerInstance();
        BNLoyalty loyalty = dataManager.getBNLoyalties().get(position);
        card = loyalty.getLoyaltyCard();
        organization = dataManager.getBNOrganization(loyalty.getOrganizationIdentifier());

        setUpScreen();
        setUpStars();
    }

    private void setUpScreen() {
        Typeface lato_regular = BNUtils.getLato_regular();
        Typeface lato_black = BNUtils.getLato_black();

        ImageView ivCard = (ImageView) findViewById(R.id.ivLoyaltyCardImage);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(organization.getMedia().get(0).getUrl(), ivCard);

        SimpleDateFormat showFormatter = new SimpleDateFormat(BNUtils.getDisplayDateFormat());
        TextView tvDate = (TextView) findViewById(R.id.tvLoyaltyCardDate);
        tvDate.setTypeface(lato_regular);
        tvDate.setText(showFormatter.format(card.getStartDate()));

        TextView tvTitle = (TextView) findViewById(R.id.tvLoyaltyCardName);
        tvTitle.setTypeface(lato_black);
        tvTitle.setText(card.getTitle());

        TextView tvDetails = (TextView) findViewById(R.id.tvLoyaltyCardDetails);
        tvDetails.setTypeface(lato_regular);
        tvDetails.setText(card.getRule());

        TextView tvGoal = (TextView) findViewById(R.id.tvLoyaltyCardGoal);
        tvGoal.setTypeface(lato_regular);
        tvGoal.setText(card.getGoal());

        TextView tvGift = (TextView) findViewById(R.id.tvLoyaltyCardGift);
        tvGift.setTypeface(lato_black);
        tvGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoyaltyActivity.this, ElementsActivity.class);
                SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(BNUtils.BNStringExtras.BNElement, card.getElementIdentifier());
                editor.commit();
                i.putExtra(BNUtils.BNStringExtras.BNShowMore, false);
                startActivity(i);
            }
        });

        TextView tvToS = (TextView) findViewById(R.id.tvLoyaltyCardConditions);
        tvToS.setTypeface(lato_regular);
        tvToS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoyaltyActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_popup, null);
                ((TextView)dialogView.findViewById(R.id.tvPopUpTitle)).setText(getString(R.string.TermOfUser));
                ((TextView)dialogView.findViewById(R.id.tvPopUpMessage)).setText(getString(R.string.LoyaltyCardConditions) + organization.getBrand());
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                dialogView.findViewById(R.id.ivPopupClose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.tvPopupConfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        ImageView ivQrCode = (ImageView) findViewById(R.id.ivLoyaltyCardQrCode);

        findViewById(R.id.vlLoyaltyQrCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoyaltyActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_qr_reader, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                setUpReaderDialog(dialogView, alertDialog);
                barcodeInfo = (TextView) dialogView.findViewById(R.id.tvPopupAccept);
                dialogView.findViewById(R.id.ivPopupQrClose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        barcodeInfo.setText(getString(R.string.reading));
                        barcodeInfo.setBackgroundColor(getResources().getColor(R.color.colorGrayButton));
                        barcodeInfo.setOnClickListener(null);
                        alertDialog.dismiss();
                        cameraSource.stop();
                    }
                });
                alertDialog.show();
            }
        });

        TextView tvCode = (TextView) findViewById(R.id.tvLoyaltyCardQrCode);
        tvCode.setTypeface(lato_black);

        if(BNUtils.calculateContrast(getResources().getColor(R.color.colorWhite), organization.getPrimaryColor(), organization.getSecondaryColor())) {
            tvCode.setTextColor(organization.getPrimaryColor());
            ivQrCode.setColorFilter(organization.getPrimaryColor());
            tvGift.setTextColor(organization.getPrimaryColor());
            tvTitle.setTextColor(organization.getPrimaryColor());
        }else{
            tvCode.setTextColor(organization.getSecondaryColor());
            ivQrCode.setColorFilter(organization.getSecondaryColor());
            tvGift.setTextColor(organization.getSecondaryColor());
            tvTitle.setTextColor(organization.getSecondaryColor());
        }

        BNToolbar toolbar = new BNToolbar(this, organization.getBrand());
    }

    private void setUpStars() {
        LinearLayout hlLoyaltyLineOpt4 = (LinearLayout)findViewById(R.id.hlLoyaltyLineOpt4);
        LinearLayout hlLoyaltyLineOpt2 = (LinearLayout)findViewById(R.id.hlLoyaltyLineOpt2);

        List<BNLoyaltyCard.BNLoyaltyCard_Slot> slots = card.getSlots();
        int size = slots.size();
        switch (size) {
            case 10:
                hlLoyaltyLineOpt2.setVisibility(View.VISIBLE);
                break;
            case 12:
                hlLoyaltyLineOpt4.setVisibility(View.VISIBLE);
                break;
            case 14:
                hlLoyaltyLineOpt2.setVisibility(View.VISIBLE);
                hlLoyaltyLineOpt4.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        boolean filled = true;
        for (int i = 0; i < size && filled; i++) {
            if(slots.get(i).isFilled()){
                switch (i + 1){
                    case 1:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar1)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 2:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar2)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 3:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar3)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 4:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar4)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 5:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar5)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 6:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar6)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 7:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar7)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 8:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar8)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 9:
                        if(size == 10) {
                            ((ImageView) findViewById(R.id.ivLoyaltyStar13)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        }else {
                            ((ImageView) findViewById(R.id.ivLoyaltyStar9)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        }
                        break;
                    case 10:
                        if(size == 10) {
                            ((ImageView) findViewById(R.id.ivLoyaltyStar14)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        }else {
                            ((ImageView) findViewById(R.id.ivLoyaltyStar10)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        }
                        break;
                    case 11:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar11)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 12:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar12)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 13:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar13)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    case 14:
                        ((ImageView)findViewById(R.id.ivLoyaltyStar14)).setColorFilter(getResources().getColor(R.color.colorStarGold));
                        break;
                    default:
                        break;
                }
            }else{
                filled = false;
            }
        }
    }

    private void setUpReaderDialog(View view, final AlertDialog dialog) {
        cameraView = (SurfaceView) view.findViewById(R.id.svPopupCamera);

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ContextCompat.checkSelfPermission(LoyaltyActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LoyaltyActivity.this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST);
                } else {
                    startCameraView();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            barcodeInfo.setText("OK");
                            barcodeInfo.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            barcodeInfo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    requestAddStar();
                                    barcodeInfo.setText(getString(R.string.reading));
                                    barcodeInfo.setBackgroundColor(getResources().getColor(R.color.colorGrayButton));
                                    barcodeInfo.setOnClickListener(null);
                                    value = "";
                                    dialog.dismiss();
                                }
                            });
                            value = barcodes.valueAt(0).displayValue;
                            Log.e("ReadQR", value);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST) {
            if (permissions[0].equals(android.Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraView();
            } else {
                Log.e("ReadQR Error", "No se tienen permisos de camara.");
            }
        }
    }

    private void startCameraView(){
        try {
            cameraSource.start(cameraView.getHolder());
        } catch (IOException ie) {
            Log.e("CAMERA SOURCE", ie.getMessage());
        } catch (SecurityException se) {
            Log.e("CAMERA PERMISSION", se.getMessage());
        }
    }

    private void requestAddStar(){
        Biinie biinie = BNAppManager.getInstance().getDataManagerInstance().getBiinie();
        String identifier;

        if(biinie != null && biinie.getIdentifier() != null && !biinie.getIdentifier().isEmpty()){
            identifier = biinie.getIdentifier();
        }else {
            SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
            identifier = preferences.getString(BNUtils.BNStringExtras.BNBiinie, "");
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BNAppManager.getInstance().getNetworkManagerInstance().getAddStarUrl(identifier, cardIdentifier, value),
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Response:" + response.toString());
                        try {
                            String result = response.getString("result");
                            String status = response.getString("status");
                            if(status.equals("0") && result.equals("1")){
                                addNewStar();
                            }else{
                                Log.e(TAG, "Error agregando estrella a la tarjeta.");
                                Toast.makeText(LoyaltyActivity.this, R.string.RequestFailed, Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoyaltyActivity.this, "Result: " + result + " / Status: " + status, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parseando el JSON.", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onVolleyError(error);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void onVolleyError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
    }

    private void addNewStar(){
        card.addStar();
        setUpStars();
    }
}
