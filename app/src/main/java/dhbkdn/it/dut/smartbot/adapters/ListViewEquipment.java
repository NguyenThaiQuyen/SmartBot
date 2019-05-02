package dhbkdn.it.dut.smartbot.adapters;

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
    MainActivity mMainActivity;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseUser mUser = mAuth.getCurrentUser();
    FirebaseDatabase mData =  FirebaseDatabase.getInstance();
    final DatabaseReference dataRef =  mData.getReference();
    int[] img = {R.drawable.fan,R.drawable.tv,R.drawable.air,R.drawable.light};
    int[] imgOff = {R.drawable.fanoff,R.drawable.tvoff,R.drawable.airoff,R.drawable.lightoff};



    public ListViewEquipment(Context context, int resource, ArrayList<Equipment> list, MainActivity mainActivity) {
        super(context, resource, list);
        mList = list;
        mLayoutId = resource;
        mContext = context;
        mMainActivity = mainActivity;
    }

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

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final Equipment equipment = mList.get(position);

        viewHolder.txtName.setText(equipment.getName());
        viewHolder.txtStatus.setText(equipment.getStatus());
        String alarm[] = equipment.getAlarm().split("/");

        viewHolder.txtStatusAlarm.setText(alarm[0]);
        viewHolder.txtTimeAlarm.setText(alarm[1]);

        if (equipment.getStatus().equals("ON")) {
            viewHolder.imgView.setImageResource(img[Integer.valueOf(equipment.getImage())]);
        } else {
            viewHolder.imgView.setImageResource(imgOff[Integer.valueOf(equipment.getImage())]);
        }

        if (alarm[0].equals("ON")) {
            viewHolder.imgViewAlarm.setImageResource(R.drawable.on);
        } else {
            viewHolder.imgViewAlarm.setImageResource(R.drawable.off);
        }


        viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = viewHolder.txtStatus.getText().toString();
                if(status.equals("ON")) {
                    //viewHolder.txtStatus.setText("OFF");
                    dataRef.child(mUser.getUid()).child("E" + String.valueOf(position + 1)).child("status").setValue("OFF");
                    viewHolder.imgView.setImageResource(imgOff[Integer.valueOf(equipment.getImage())]);
                } else {
                    //viewHolder.txtStatus.setText("ON");
                    dataRef.child(mUser.getUid()).child("E" + String.valueOf(position + 1)).child("status").setValue("ON");
                    viewHolder.imgView.setImageResource(img[Integer.valueOf(equipment.getImage())]);
                }
            }
        });

        viewHolder.imgViewAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mMainActivity, TimePickerActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("status", viewHolder.txtStatus.getText());
                mMainActivity.startActivity(intent);
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
    }

}