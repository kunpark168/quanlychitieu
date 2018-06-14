package com.qlct.pttkht.quanlychitieu.adapter;

import android.content.Context;
import android.os.Build;
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
import com.qlct.pttkht.quanlychitieu.model.Kehoach;
import com.qlct.pttkht.quanlychitieu.model.ThuNhap;

import java.util.ArrayList;
import java.util.Collections;

public class KehoachAdapter extends RecyclerView.Adapter<KehoachAdapter.ChitieuViewHolder> {

    private ArrayList<Kehoach> arrKeHoach;
    private Context mContext;
    private IClick mListener;

    public KehoachAdapter(ArrayList<Kehoach> arrKeHoach, Context mContext) {
        this.arrKeHoach = arrKeHoach;
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
        final Kehoach kehoach = arrKeHoach.get(position);
        if (kehoach != null){
            holder.txtNote.setText(kehoach.getNote());
            String money = kehoach.getAmount() + "";
            money = money.replace(".", ",");
            if (money.endsWith("0")){
                money = money.substring(0, money.length() - 2);
            }
            holder.txtAmount.setText( money + ".000 vnd");

            /*long timeNote = kehoach.getTimeStamp();
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
            holder.txtTime.setText(kehoach.getDate());
            holder.txtLine.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen));

            holder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(kehoach);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return arrKeHoach.size();
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

    public void setmListener(IClick mListener) {
        this.mListener = mListener;
    }

    public void setArrKeHoach(ArrayList<Kehoach> arrKeHoach) {
        this.arrKeHoach = arrKeHoach;
    }

    public void updateList (ArrayList<Kehoach> list){
        Collections.sort(list, (c1, c2) -> {
            return Long.compare(c2.getTimeStamp(), c1.getTimeStamp());
        });
        setArrKeHoach(list);
        notifyDataSetChanged();
    }

    public interface IClick {
        void onClick (Kehoach kehoach);
    }
}
