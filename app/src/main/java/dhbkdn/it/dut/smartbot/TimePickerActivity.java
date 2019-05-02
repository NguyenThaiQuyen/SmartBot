package dhbkdn.it.dut.smartbot;

import android.content.Intent;
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
    int position;
    Button btnSaveTime;

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
                String alarm = status + "/" + hours + ":" + min;
                dataRef.child(user.getUid()).child("E" + String.valueOf(position + 1)).child("alarm").setValue(alarm);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void getWidgets() {
        timePicker.setIs24HourView(true);
    }

    private void setWidgets() {
        timePicker = findViewById(R.id.timePicker);
        btnSaveTime = findViewById(R.id.btnSaveTime);

    }

    private void inits() {
        Intent intent = getIntent();
        status = intent.getStringExtra("status");
        if (status.equals("ON")) {

            status = "OFF";
        } else {
            status = "ON";
        }
        position = intent.getIntExtra("position", 0);

    }



}
