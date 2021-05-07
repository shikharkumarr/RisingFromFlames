package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.aliferous.risingflames.R;

import java.util.List;

import Model.Do_Dont;

public class Do_Dont_Adapter extends RecyclerView.Adapter<Do_Dont_Adapter.ViewHolder> {

    private Context mContext;
    private List<Do_Dont> mUsers;
    Linkrecycler mListener;
    String Details,Text,Bg;

    public Do_Dont_Adapter(Context mContext, List<Do_Dont> mUsers, boolean b,String Details, String Text,String Bg, Linkrecycler mListener){
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.mListener = mListener;
        this.Details = Details;
        this.Text = Text;
        this.Bg = Bg;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.do_dont_itemview,parent,false);
        return new Do_Dont_Adapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Do_Dont user = mUsers.get(position);


        holder.text.setText(user.getHeader());
        holder.number.setText(user.getNumber());

        if(user.getCirclebg().equals("G")){
            holder.Circlebg.setBackgroundResource(R.drawable.vector_do);
        }
        else if(user.getCirclebg().equals("R"))
        {
            holder.Circlebg.setBackgroundResource(R.drawable.vector_dont);
        }


        holder.do_dont_constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Text = user.getText();
                Details = user.getDetails();
                Bg = user.getCirclebg();
                mListener.linkingrecyler(user,Details,Text,Bg);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Linkrecycler mListener;
        ConstraintLayout do_dont_constraint;

        ImageView Itemviewbg,Circlebg;
        TextView text,number;

        public ViewHolder(@NonNull View itemView, Linkrecycler mListener) {
            super(itemView);
            this.mListener = mListener;

            Itemviewbg = itemView.findViewById(R.id.itemview_bg);
            Circlebg = itemView.findViewById(R.id.circle_bg);
            text = itemView.findViewById(R.id.Text);
            number = itemView.findViewById(R.id.Number);
            do_dont_constraint = itemView.findViewById(R.id.do_dont_constraint);


        }
    }

    public interface Linkrecycler{
        void linkingrecyler(Do_Dont do_dont, String detail, String text, String bg);
    }

}