package com.qlct.pttkht.quanlychitieu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlct.pttkht.quanlychitieu.R;
import com.qlct.pttkht.quanlychitieu.model.Kehoach;
import com.qlct.pttkht.quanlychitieu.model.ThuNhap;

import java.util.ArrayList;
import java.util.Collections;

public class ThuNhapAdapter extends RecyclerView.Adapter<ThuNhapAdapter.ChitieuViewHolder> {

    private ArrayList<ThuNhap> arrThuNhap;
    private Context mContext;

    public ThuNhapAdapter(ArrayList<ThuNhap> arrThuNhap, Context mContext) {
        this.arrThuNhap = arrThuNhap;
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
        final ThuNhap thuNhap = arrThuNhap.get(position);
        if (thuNhap != null){
            holder.txtNote.setText(thuNhap.getNote());
            String money = thuNhap.getAmount() + "";
            money = money.replace(".", ",");
            if (money.endsWith("0")){
                money = money.substring(0, money.length() - 2);
            }
            holder.txtAmount.setText( money + ".000 vnd");

            /*long timeNote = thuNhap.getTimeStamp();
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
            holder.txtTime.setText(thuNhap.getDate());

            holder.txtLine.setBackgroundColor(mContext.getResources().getColor(R.color.colorRed));

            holder.imgEdit.setVisibility(View.INVISIBLE);
            holder.txtTypeCT.setVisibility(View.INVISIBLE);




        }

    }

    @Override
    public int getItemCount() {
        return arrThuNhap.size();
    }

    public void setArrThuNhap(ArrayList<ThuNhap> arrThuNhap) {
        this.arrThuNhap = arrThuNhap;
    }

    public void updateList (ArrayList<ThuNhap> list) {
        Collections.sort(list, (t1, t2) -> {
            return Long.compare(t2.getTimeStamp(), t1.getTimeStamp());
        });
        setArrThuNhap(list);
        notifyDataSetChanged();
    }


        public class ChitieuViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNote, txtAmount, txtTime, txtLine, txtTypeCT;
        private ImageView imgEdit;
        public ChitieuViewHolder(View itemView) {
            super(itemView);
            txtNote = itemView.findViewById(R.id.txtNote);
            txtAmount = itemView.findViewById(R.id.txtAmountCT);
            txtTime = itemView.findViewById(R.id.txtTimeCT);
            txtLine = itemView.findViewById(R.id.txtLine);
            imgEdit = itemView.findViewById(R.id.imgEditChitieu);
            txtTypeCT = itemView.findViewById(R.id.txtTypeCT);
        }
    }
}

