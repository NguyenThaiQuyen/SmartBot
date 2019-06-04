package dhbkdn.it.dut.smartbot.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import dhbkdn.it.dut.smartbot.MainActivity;
import dhbkdn.it.dut.smartbot.R;
import dhbkdn.it.dut.smartbot.TimePickerActivity;
import dhbkdn.it.dut.smartbot.models.Equipment;

public class ListViewEquipment extends ArrayAdapter<Equipment> {

    private ArrayList<Equipment> mList;
    private int mLayoutId;
    private Context mContext;
    private MainActivity mMainActivity;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseDatabase mData =  FirebaseDatabase.getInstance();
    private final DatabaseReference dataRef =  mData.getReference();
    private int[] img = {R.drawable.fan,R.drawable.tv,R.drawable.air,R.drawable.light};
    private int[] imgOff = {R.drawable.fanoff,R.drawable.tvoff,R.drawable.airoff,R.drawable.lightoff};
    private final String onStatus = "ON - ON";
    private final String offStatus = "OFF - OFF";
    private final String waitingOn = "Waiting for turning on";
    private final String waitingOff = "Waiting for turning off";
    private final String alarmText = "will be ";



    public ListViewEquipment(Context context, int resource, ArrayList<Equipment> list, MainActivity mainActivity) {
        super(context, resource, list);
        mList = list;
        mLayoutId = resource;
        mContext = context;
        mMainActivity = mainActivity;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mLayoutId, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.txtName = convertView.findViewById(R.id.txtName);
            viewHolder.txtStatus = convertView.findViewById(R.id.txtStatus);
            viewHolder.txtTimeAlarm = convertView.findViewById(R.id.txtTimeAlarm);
            viewHolder.txtStatusAlarm = convertView.findViewById(R.id.txtStatusAlarm);
            viewHolder.imgView = convertView.findViewById(R.id.imgView);
            viewHolder.imgViewAlarm = convertView.findViewById(R.id.imgAlarm);
            viewHolder.imgViewDelete = convertView.findViewById(R.id.imgViewDelete);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final Equipment equipment = mList.get(position);

        viewHolder.txtName.setText("E" + equipment.getId() + ". " + equipment.getName());

        final String[] alarm = equipment.getAlarm().split("/");

        viewHolder.txtStatusAlarm.setText(alarmText + alarm[0]);
        viewHolder.txtTimeAlarm.setText(alarm[1]);

        String show_status = "";
        String app_status = equipment.getStatus();
        String device_status = equipment.getDevice_status();

        if(app_status.equals("ON") && device_status.equals("ON")) {
            show_status = onStatus;
            viewHolder.txtStatus.setTextSize(24);
        } else if(app_status.equals("ON") && device_status.equals("OFF")) {
            show_status = waitingOn;
            viewHolder.txtStatus.setTextSize(17);
        } else if(app_status.equals("OFF") && device_status.equals("ON")){
            show_status = waitingOff;
            viewHolder.txtStatus.setTextSize(17);

        } else {
            show_status = offStatus;
            viewHolder.txtStatus.setTextSize(24);
        }
        viewHolder.txtStatus.setText(show_status);

        if (show_status.equals(onStatus) || show_status.equals(waitingOff) ) {
            viewHolder.imgView.setImageResource(img[Integer.valueOf(equipment.getImage())]);
        } else {
            viewHolder.imgView.setImageResource(imgOff[Integer.valueOf(equipment.getImage())]);
        }

        if (alarm[0].equals("ON")) {
            viewHolder.imgViewAlarm.setImageResource(R.drawable.on);
        } else {
            viewHolder.imgViewAlarm.setImageResource(R.drawable.off);
        }


        final String finalShow_status = show_status;
        viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(finalShow_status.equals(onStatus) || finalShow_status.equals(waitingOn)) {

                    assert mUser != null;
                    dataRef.child(mUser.getUid()).child("E" + equipment.getId()).child("status").setValue("OFF");

                } else {

                    assert mUser != null;
                    dataRef.child(mUser.getUid()).child("E" + equipment.getId()).child("status").setValue("ON");

                }
            }
        });

        viewHolder.txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalShow_status.equals(onStatus) || finalShow_status.equals(waitingOn)) {

                    assert mUser != null;
                    dataRef.child(mUser.getUid()).child("E" + equipment.getId()).child("status").setValue("OFF");

                } else {

                    assert mUser != null;
                    dataRef.child(mUser.getUid()).child("E" + equipment.getId()).child("status").setValue("ON");

                }
            }
        });

        viewHolder.txtTimeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mMainActivity, TimePickerActivity.class);
                intent.putExtra("position", equipment.getId());
                if(finalShow_status.equals(onStatus) || finalShow_status.equals(waitingOn)) {

                    intent.putExtra("status", "OFF");
                } else {
                    intent.putExtra("status", "ON");
                }

                mMainActivity.startActivity(intent);
            }
        });
        viewHolder.imgViewAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(alarm[0].equals("ON")) {

                    assert mUser != null;
                    dataRef.child(mUser.getUid()).child("E" + equipment.getId()).child("alarm").setValue("OFF/" + alarm[1]);

                } else {

                    assert mUser != null;
                    dataRef.child(mUser.getUid()).child("E" + equipment.getId()).child("alarm").setValue("ON/" + alarm[1]);
                }
            }
        });

        viewHolder.imgViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert mUser != null;
                Equipment tmp;
                for(int i = position + 1; i < mList.size(); i++) {
                    tmp = mList.get(i);
                    tmp.setId(String.valueOf(i-1));
                    mList.set(i-1, tmp);

                    dataRef.child(mUser.getUid()).child("E" + (i - 1)).setValue(tmp);
                }

                dataRef.child(mUser.getUid()).child("E" + (mList.size()-1)).removeValue();
                mList.remove(mList.size()-1);


            }
        });

        return convertView;
    }

    private class ViewHolder {
        public TextView txtName;
        public TextView txtStatus;
        public TextView txtTimeAlarm;
        public TextView txtStatusAlarm;

        public ImageView imgView;
        public ImageView imgViewAlarm;
        public ImageView imgViewDelete;
    }

}