package com.qlct.pttkht.quanlychitieu.adapter;

import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qlct.pttkht.quanlychitieu.R;
import com.qlct.pttkht.quanlychitieu.model.Chitieu;

import java.util.ArrayList;
import java.util.Collections;

public class ChitieuAdapter extends RecyclerView.Adapter<ChitieuAdapter.ChitieuViewHolder> {

    private ArrayList<Chitieu> arrChitieu;
    private Context mContext;

    public ChitieuAdapter(ArrayList<Chitieu> arrChitieu, Context mContext) {
        this.arrChitieu = arrChitieu;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChitieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chitieu, parent, false);
        return new ChitieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChitieuViewHolder holder, int position) {
        Chitieu chitieu = arrChitieu.get(position);
        if (chitieu != null){
            holder.txtNote.setText(chitieu.getNote());
            String money = chitieu.getAmount() + "";
            money = money.replace(".", ",");
            if (money.endsWith("0")){
                money = money.substring(0, money.length() - 2);
            }
            holder.txtAmount.setText( money + ".000 vnd");

            if (chitieu.getType() == 0)
                holder.txtType.setText(chitieu.getCategory());
            else if (chitieu.getType() == 1)
                holder.txtType.setText("Đột xuất");
            /*long timeNote = chitieu.getTimeStamp();
            long currentTime = System.currentTimeMillis();
            long diff = currentTime - timeNote;
            long diffMinutes = diff / (60 * 1000);
            if (diffMinutes < 60 )
                holder.txtTime.setText(diffMinutes + " minutes ago");
            else if (60 <= diffMinutes && diffMinutes <= 1440 ) {
                int hour = (int) (diffMinutes /60);
                holder.txtTime.setText(hour + " hours ago");
            } else if (1440 <= diffMinutes) {
                int day = (int) (diffMinutes /1440);
                holder.txtTime.setText(day + " days ago");
            }*/
            holder.txtTime.setText(chitieu.getDate());

            holder.imgEdit.setVisibility(View.GONE);


        }

    }

    @Override
    public int getItemCount() {
        return arrChitieu.size();
    }

    public void setArrChitieu(ArrayList<Chitieu> arrChitieu) {
        this.arrChitieu = arrChitieu;
    }

    public void updateList (ArrayList<Chitieu> list){
        Collections.sort(list, (c1, c2) -> {
            return Long.compare(c2.getTimeStamp(), c1.getTimeStamp());
        });
        setArrChitieu(list);
        notifyDataSetChanged();
    }

    public class ChitieuViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNote, txtType, txtAmount, txtTime, txtLine;
        private ImageView imgEdit;
        public ChitieuViewHolder(View itemView) {
            super(itemView);
            txtNote = itemView.findViewById(R.id.txtNote);
            txtType = itemView.findViewById(R.id.txtTypeCT);
            txtAmount = itemView.findViewById(R.id.txtAmountCT);
            txtTime = itemView.findViewById(R.id.txtTimeCT);
            imgEdit = itemView.findViewById(R.id.imgEditChitieu);
            txtLine = itemView.findViewById(R.id.txtLine);
        }
    }
}
