package dhbkdn.it.dut.smartbot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import dhbkdn.it.dut.smartbot.adapters.ListViewEquipment;
import dhbkdn.it.dut.smartbot.models.Equipment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected FirebaseAuth mAuth;
    protected FirebaseUser mUser;

    ArrayList<Equipment> mList;
    ListViewEquipment mAdapter;

    FirebaseDatabase mData =  FirebaseDatabase.getInstance();;
    DatabaseReference dataRef =  mData.getReference();

    ListView lvListEquipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inits();
        setWigets();
        getWigets();
        addListener();

    }

    private void addListener() {
        lvListEquipment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SeclectEquipmentActivity.class);
                intent.putExtra("new", 0);
                intent.putExtra("position", position + 1);
                startActivity(intent);
                return false;
            }
        });

    }



    private void inits() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mList = new ArrayList<>();
        mAdapter = new ListViewEquipment(this, R.layout.item__device, mList, MainActivity.this);
    }


    private void getWigets(){
        lvListEquipment.setAdapter(mAdapter);
        // get data from database
        dataRef.child(mUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Equipment equipment = dataSnapshot.getValue(Equipment.class);
                mList.add(equipment);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String strPos = dataSnapshot.getKey();
                assert strPos != null;
                strPos = strPos.substring(1);

                int intPos = Integer.valueOf(strPos);
                mList.set(intPos - 1, dataSnapshot.getValue(Equipment.class));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setWigets() {
        lvListEquipment = findViewById(R.id.lvListEquipment);

    }

    public void NewDevice(View view) {
        Intent intent = new Intent(getApplicationContext(), SeclectEquipmentActivity.class);
        intent.putExtra("new", 1);
        intent.putExtra("position", 0);

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

    public void Logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}


