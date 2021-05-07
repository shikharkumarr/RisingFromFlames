package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliferous.risingflames.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Model.Do_Dont;
import Model.Sensor;

public class Sensor_Adapter extends RecyclerView.Adapter<Sensor_Adapter.ViewHolder> {

    private Context mContext;
    private List<Sensor> mSensors;
    int last_sensor;


    public Sensor_Adapter(Context mContext, List<Sensor> mSensors, boolean b){
        this.mSensors = mSensors;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sensor_item,parent,false);
        return new Sensor_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Sensor sensor = mSensors.get(position);



        holder.sensor_name.setText(sensor.getName());

        last_sensor = getItemCount();
        if(last_sensor == sensor.getNumber())
        {
            holder.divide.setVisibility(View.GONE);
        }

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference referenceList = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Sensor List");

        holder.remove_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referenceList.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        int noORequests = (int) dataSnapshot.getChildrenCount();
                        String id = String.valueOf(sensor.getID());

                        for (int i=1;i<=500;i++){
                            if (dataSnapshot.hasChild(""+i)){
                                String checkID = dataSnapshot.child(""+i).child("ID").getValue().toString();
                                if (checkID.equals(id)){
                                    referenceList.child(""+i).child("ID").removeValue();
                                    referenceList.child(""+i).child("Name").removeValue();
                                    referenceList.child(""+i).child("Status").removeValue();
                                    referenceList.child(""+i).child("Date").removeValue();
                                    mSensors.clear();
                                }
                            }


                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return mSensors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView sensor_name;
        ImageView divide,remove_sensor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sensor_name = itemView.findViewById(R.id.sensor_name);
            divide = itemView.findViewById(R.id.divider);
            remove_sensor = itemView.findViewById(R.id.remove_sensor);



        }
    }

}