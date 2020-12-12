package com.ishaanohri.cleanvit_admin;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    public static ArrayList<Complaint> arrayList = new ArrayList<>();
    public static ArrayList<String> regNoArrayList = new ArrayList<>();
    public static ArrayList<String> nameArrayList = new ArrayList<>();
    public static ArrayList<String> mobNoArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Log.i("INFO","Main Activity Created");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.i("INFO","Entering DataChange");

                arrayList = new ArrayList<>();
                regNoArrayList = new ArrayList<>();
                mobNoArrayList = new ArrayList<>();
                nameArrayList = new ArrayList<>();

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Log.i("INFO","Complaint Fetched");
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        if(dataSnapshot2.getKey().equals("Mobile Number") || dataSnapshot2.getKey().equals("Name"))
                        {

                        }
                        else
                        {
                            Complaint complaint = dataSnapshot2.getValue(Complaint.class);
                            arrayList.add(complaint);
                            regNoArrayList.add(dataSnapshot1.getKey());
                            mobNoArrayList.add(dataSnapshot1.child("Mobile Number").getValue().toString());
                            nameArrayList.add(dataSnapshot1.child("Name").getValue().toString());

//                            if(arrayList.size() != 0)
//                            {
//                                noItemTextView.setVisibility(View.GONE);
//                            }
                        }
                    }
                }
                recyclerViewAdapter = new RecyclerViewAdapter(arrayList, regNoArrayList, nameArrayList, mobNoArrayList,MainActivity.this);
                recyclerView.setAdapter(recyclerViewAdapter);

                Log.i("INFO","Adapter set");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
