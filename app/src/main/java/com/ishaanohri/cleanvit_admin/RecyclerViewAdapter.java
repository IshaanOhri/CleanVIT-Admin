package com.ishaanohri.cleanvit_admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<Complaint> arrayList = new ArrayList<>();
    private ArrayList<String> regNoArrayList = new ArrayList<>();
    private ArrayList<String> nameArrayList = new ArrayList<>();
    private ArrayList<String> mobNoArrayList = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(ArrayList<Complaint> arrayList, ArrayList<String> regNoArrayList, ArrayList<String> nameArrayList, ArrayList<String> mobNoArrayList, Context context) {
        this.arrayList = arrayList;
        this.regNoArrayList = regNoArrayList;
        this.nameArrayList = nameArrayList;
        this.mobNoArrayList = mobNoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.individual_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final String landmark = arrayList.get(i).getLandmark();
        final String remarks = arrayList.get(i).getRemarks();
        final String dateTime = arrayList.get(i).getDateTime();
        final String status = arrayList.get(i).getStatus();
        final String image = arrayList.get(i).getImageURL();
        final String latitude = arrayList.get(i).getLatitude();
        final String longitude = arrayList.get(i).getLongitude();
        final String key = arrayList.get(i).getKey();
        final String name = nameArrayList.get(i);
        final String regNo = regNoArrayList.get(i);
        final String mobNo = mobNoArrayList.get(i);

        viewHolder.landmarkTextView.setText(landmark);
        viewHolder.dateTimeTextView.setText(dateTime);
        viewHolder.statusTextView.setText("Status: " + status);

        try
        {
            if(status.equals("Completed"))
            {
                viewHolder.statusTextView.setTextColor(Color.parseColor("#13CF26"));
            }
            else
            {
                viewHolder.statusTextView.setTextColor(Color.parseColor("#FF2626"));
            }
        }
        catch (Exception e)
        {

        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(image);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(viewHolder.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,MapsActivity.class);
                intent.putExtra("Landmark",landmark);
                intent.putExtra("Remarks",remarks);
                intent.putExtra("DateTime",dateTime);
                intent.putExtra("Status",status);
                intent.putExtra("Image",image);
                intent.putExtra("Longitude",longitude);
                intent.putExtra("Latitude",latitude);
                intent.putExtra("Key",key);
                intent.putExtra("Name",name);
                intent.putExtra("RegNo",regNo);
                intent.putExtra("MobNo",mobNo);

                context.startActivity(intent);
                Animatoo.animateSlideUp(context);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView landmarkTextView,dateTimeTextView,statusTextView;
        ImageView imageView;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            landmarkTextView = itemView.findViewById(R.id.landmarkTextView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            imageView = itemView.findViewById(R.id.imageView);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
