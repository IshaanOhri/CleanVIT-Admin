package com.ishaanohri.cleanvit_admin;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wang.avi.AVLoadingIndicatorView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int CALL_CODE = 100;
    private TextView landmarkTextView, dateTimeTextView, remarksTextView, nameTextView, mobNoTextView;
    private ImageView imageView;
    private Double latitude, longitude;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Switch statusSwitch;
    private String status;
    private LinearLayout linearLayout;
    private AVLoadingIndicatorView progressBar;
    private MapView map;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED && requestCode == CALL_CODE) {
                callPhone();
            }
        }
    }

    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mobNoTextView.getText().toString()));
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        landmarkTextView = findViewById(R.id.landmarkTextView);
        remarksTextView = findViewById(R.id.remarksTextView);
        dateTimeTextView = findViewById(R.id.dateTimeTextView);
        imageView = findViewById(R.id.imageView);
        statusSwitch = findViewById(R.id.statusSwitch);
        nameTextView = findViewById(R.id.nameTextView);
        mobNoTextView = findViewById(R.id.mobileNoTextView);
        linearLayout = findViewById(R.id.linearLayout);
        progressBar = findViewById(R.id.progressBar);
//        map = findViewById(R.id.map);

        String landmark = getIntent().getStringExtra("Landmark");
        String remarks = getIntent().getStringExtra("Remarks");
        String dateTime = getIntent().getStringExtra("DateTime");
        status = getIntent().getStringExtra("Status");
        final String image = getIntent().getStringExtra("Image");
        final String key = getIntent().getStringExtra("Key");
        String name = getIntent().getStringExtra("Name");
        final String regNo = getIntent().getStringExtra("RegNo");
        String mobNo = getIntent().getStringExtra("MobNo");

        hideUI();

        storageReference = FirebaseStorage.getInstance().getReference().child(image);

        latitude = Double.parseDouble(getIntent().getStringExtra("Latitude"));
        longitude = Double.parseDouble(getIntent().getStringExtra("Longitude"));

        landmarkTextView.setText(landmark);
        remarksTextView.setText(remarks);
        dateTimeTextView.setText(dateTime);
        nameTextView.setText(name);
        mobNoTextView.setText(mobNo);

        statusSwitch.setText(status);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(status.equals("Completed"))
        {
            statusSwitch.setTextColor(Color.parseColor("#13CF26"));
            statusSwitch.setChecked(true);
            statusSwitch.setEnabled(false);
        }
        else
        {
            statusSwitch.setTextColor(Color.parseColor("#FF2626"));
        }

        mobNoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},CALL_CODE);
                }
                else
                {
                    callPhone();
                }
            }
        });

        statusSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check = statusSwitch.isChecked();

                if(check)
                {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setIcon(R.drawable.status)
                            .setTitle("Change status")
                            .setMessage("Are you sure you want to change the complaint status to Completed?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    statusSwitch.setText("Completed");
                                    statusSwitch.setTextColor(Color.parseColor("#13CF26"));
                                    statusSwitch.setEnabled(false);
                                    databaseReference.child(regNo).child(key).child("Status").setValue("Completed");
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    statusSwitch.setChecked(false);
                                }
                            })
                            .show();
                }
                else
                {
                    statusSwitch.setText("Pending");
                    statusSwitch.setTextColor(Color.parseColor("#FF2626"));
                }
            }
        });

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MapsActivity.this)
                        .load(uri)
                        .into(imageView);
                showUI();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showUI();
                Toast.makeText(MapsActivity.this, "Error getting image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hideUI()
    {
        Float alpha = 0.2f;
        linearLayout.setAlpha(alpha);
        statusSwitch.setEnabled(false);
//        map.setEnabled(false);
        mobNoTextView.setEnabled(false);
        linearLayout.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void showUI()
    {
        Float alpha = 1.0f;
        linearLayout.setAlpha(alpha);
        statusSwitch.setEnabled(true);
//        map.setEnabled(true);
        mobNoTextView.setEnabled(true);
        linearLayout.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng complaintLocation = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        mMap.addMarker(new MarkerOptions().position(complaintLocation).title("Complaint Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(complaintLocation,18F));
    }
}
