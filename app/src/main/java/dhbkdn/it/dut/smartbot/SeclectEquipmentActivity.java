package dhbkdn.it.dut.smartbot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dhbkdn.it.dut.smartbot.models.Equipment;


public class SeclectEquipmentActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnFan, btnTv, btnAir, btnLight, btnSave;
    private int[] ArrayImage = {
            R.drawable.fan,
            R.drawable.tv,
            R.drawable.air,
            R.drawable.light
    };

    private SharedPreferences sharedPreferences;
    private static final String myref = "numberDevice";
    private SharedPreferences.Editor editor;

    FirebaseDatabase mData =  FirebaseDatabase.getInstance();
    final DatabaseReference dataRef =  mData.getReference();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    final FirebaseUser user = mAuth.getCurrentUser();
    int count  = 0;
    int createNew = 1;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seclect_equipment);

        inits();
        setWidgets();
        getWidgets();
        addListener();
    }

    @SuppressLint("CommitPrefEdits")
    private void inits() {
        sharedPreferences = getSharedPreferences(myref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Intent intent = getIntent();
        createNew = intent.getIntExtra("new", 1);
        position = intent.getIntExtra("position", 0);

    }

    private void setWidgets() {
        btnFan = findViewById(R.id.btnFan);
        btnTv = findViewById(R.id.btnTv);
        btnAir = findViewById(R.id.btnAir);
        btnLight = findViewById(R.id.btnLight);
        btnSave = findViewById(R.id.btnSave);
    }

    private void getWidgets() {

    }

    private void addListener() {
        btnFan.setOnClickListener(this);
        btnTv.setOnClickListener(this);
        btnAir.setOnClickListener(this);
        btnLight.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private void saveDevice(String name, int device) {

        if(createNew == 1) {
            position = sharedPreferences.getInt("number", 1);

            position = position + 1;
            editor.putInt("number", position);

            editor.apply();

            Equipment equipment = new Equipment(name, "OFF", "OFF/23:0", String.valueOf(device));
            dataRef.child(user.getUid()).child("E" + position).setValue(equipment);

        } else {

            dataRef.child(user.getUid()).child("E" + position).child("image").setValue(String.valueOf(device));
            dataRef.child(user.getUid()).child("E" + position).child("name").setValue(name);
        }



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnFan: {
                count  = 1;
                btnFan.setBackgroundResource(R.drawable.fanselect);
                btnTv.setBackgroundResource(R.drawable.tv);
                btnAir.setBackgroundResource(R.drawable.air);
                btnLight.setBackgroundResource(R.drawable.light);
                break;
            }

            case R.id.btnTv:{
                count = 2;
                btnFan.setBackgroundResource(R.drawable.fan);
                btnTv.setBackgroundResource(R.drawable.tvselect);
                btnAir.setBackgroundResource(R.drawable.air);
                btnLight.setBackgroundResource(R.drawable.light);
                break;
            }

            case R.id.btnAir:{
                count = 3;
                btnFan.setBackgroundResource(R.drawable.fan);
                btnTv.setBackgroundResource(R.drawable.tv);
                btnAir.setBackgroundResource(R.drawable.airselect);
                btnLight.setBackgroundResource(R.drawable.light);
                break;
            }

            case R.id.btnLight:{
                count = 4;
                btnFan.setBackgroundResource(R.drawable.fan);
                btnTv.setBackgroundResource(R.drawable.tv);
                btnAir.setBackgroundResource(R.drawable.air);
                btnLight.setBackgroundResource(R.drawable.lightselect);
                break;
            }

            case R.id.btnSave:{
                switch(count) {
                    case 0: {
                        Toast.makeText(getApplicationContext(), "Please choose a device", Toast.LENGTH_SHORT).show();

                        break;
                    }

                    case 1: {
                        saveDevice("Fan", 0);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;

                    }

                    case 2: {
                        saveDevice("Television", 1);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    }

                    case 3: {
                        saveDevice("Air condition", 2);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    }

                    case 4: {
                        saveDevice("Light", 3);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    }
                }
                break;
            }
        }

    }
}
