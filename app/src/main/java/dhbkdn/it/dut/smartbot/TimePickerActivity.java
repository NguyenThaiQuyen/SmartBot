package dhbkdn.it.dut.smartbot;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TimePickerActivity extends AppCompatActivity {

    TimePicker timePicker;
    String status;
    String position;
    Button btnSaveTime;
    Button btnOn;
    Button btnOff;

    FirebaseDatabase mData =  FirebaseDatabase.getInstance();
    final DatabaseReference dataRef =  mData.getReference();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        inits();
        setWidgets();
        getWidgets();
        addListener();
    }

    private void addListener() {
        btnSaveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hours = timePicker.getCurrentHour();
                int min = timePicker.getCurrentMinute();
                String str_min = String.valueOf(min);
                String str_hour = String.valueOf(hours);
                if(min < 10) {
                    str_min = "0" + min;
                }

                if(hours < 10) {
                    str_hour = "0" +  hours;
                }
                String alarm = status + "/" + str_hour + ":" + str_min;
                assert user != null;
                dataRef.child(user.getUid()).child("E" + position).child("alarm").setValue(alarm);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOn();
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOff();
            }
        });
    }

    private void getWidgets() {
        timePicker.setIs24HourView(true);
        if(status.equals("ON")) {
           setOn();
        } else {
            setOff();
        }
    }

    private void setWidgets() {
        timePicker = findViewById(R.id.timePicker);
        btnSaveTime = findViewById(R.id.btnSaveTime);
        btnOff = findViewById(R.id.btnOff);
        btnOn = findViewById(R.id.btnOn);

    }

    private void inits() {
        Intent intent = getIntent();
        status = intent.getStringExtra("status");

        position = intent.getStringExtra("position");

    }


    public void setOff() {
        status = "OFF";
        btnOff.setBackgroundColor(Color.RED);
        btnOff.setTextColor(Color.WHITE);
        btnOn.setBackgroundColor(Color.WHITE);
        btnOn.setTextColor(Color.BLACK);
    }

    public void setOn() {
        status = "ON";
        btnOn.setBackgroundColor(Color.RED);
        btnOn.setTextColor(Color.WHITE);
        btnOff.setBackgroundColor(Color.WHITE);
        btnOff.setTextColor(Color.BLACK);
    }
}
