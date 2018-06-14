package com.qlct.pttkht.quanlychitieu.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.qlct.pttkht.quanlychitieu.R;
import com.qlct.pttkht.quanlychitieu.adapter.KehoachAdapter;
import com.qlct.pttkht.quanlychitieu.adapter.ViewPageAdapter;
import com.qlct.pttkht.quanlychitieu.utils.Constan;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout layoutTaichinh, layoutChitieu, layoutKehoach, layoutTaikhoan;
    private TextView txtHeader, txtTaichinh, txtChitieu, txtKehoach, txtTaikhoan;
    private ImageView imgTaichinh, imgChitieu, imgKehoach, imgTaikhoan;
    private ViewPager mViewPage;
    private ViewPageAdapter mAdapter;
    private int type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView ();
        addEvents ();
    }

    private void addEvents() {

        //set event click for controls
        layoutTaichinh.setOnClickListener(this);
        layoutChitieu.setOnClickListener(this);
        layoutKehoach.setOnClickListener(this);
        layoutTaikhoan.setOnClickListener(this);

        //add event viewpager change
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onChangeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {

        //Ánh xạ view
        txtHeader = findViewById(R.id.txtHeader);

        layoutTaichinh = findViewById(R.id.layoutTaichinh);
        layoutChitieu = findViewById(R.id.layoutChitieu);
        layoutKehoach = findViewById(R.id.layoutKehoach);
        layoutTaikhoan = findViewById(R.id.layoutTaikhoan);

        imgTaichinh = findViewById(R.id.imgTaichinh);
        imgChitieu = findViewById(R.id.imgChitieu);
        imgKehoach = findViewById(R.id.imgKehoach);
        imgTaikhoan = findViewById(R.id.imgTaikhoan);

        txtTaichinh = findViewById(R.id.txtTaichinh);
        txtChitieu = findViewById(R.id.txtChitieu);
        txtKehoach = findViewById(R.id.txtKehoach);
        txtTaikhoan = findViewById(R.id.txtTaikhoan);

        //Khởi tạo và set adapter cho viewpage
        mViewPage = findViewById(R.id.mViewPager);
        mAdapter = new ViewPageAdapter(getSupportFragmentManager());
        mViewPage.setAdapter(mAdapter);
        mViewPage.setCurrentItem(0);
        onChangeTab(0);


    }

    @Override
    protected void onResume() {
        type = getIntent().getIntExtra(Constan.TYPEMAIN, 0);
        onChangeTab(type);
        mViewPage.setCurrentItem(type);
        super.onResume();
    }

    private void onChangeTab (int index){

        //Event click item tab to change tab of viewpager
        mViewPage.setCurrentItem(index);
        if (index == 0){
            layoutTaichinh.setBackgroundColor(getResources().getColor(R.color.colorActive));
            layoutKehoach.setBackgroundColor(getResources().getColor(R.color.colorNavigation));
            layoutChitieu.setBackgroundColor(getResources().getColor(R.color.colorNavigation));
            layoutTaikhoan.setBackgroundColor(getResources().getColor(R.color.colorNavigation));

            imgTaichinh.setImageResource(R.drawable.ic_money_active);
            imgChitieu.setImageResource(R.drawable.ic_spend_unactive);
            imgKehoach.setImageResource(R.drawable.ic_plan_unactive);
            imgTaikhoan.setImageResource(R.drawable.ic_acc_unactive);

            txtTaichinh.setTextColor(getResources().getColor(R.color.colorWhite));
            txtChitieu.setTextColor(getResources().getColor(R.color.colorUnactive));
            txtKehoach.setTextColor(getResources().getColor(R.color.colorUnactive));
            txtTaikhoan.setTextColor(getResources().getColor(R.color.colorUnactive));
            txtHeader.setText("Tài chính");

        } else if (index == 1 ){

            layoutTaichinh.setBackgroundColor(getResources().getColor(R.color.colorNavigation));
            layoutKehoach.setBackgroundColor(getResources().getColor(R.color.colorNavigation));
            layoutChitieu.setBackgroundColor(getResources().getColor(R.color.colorActive));
            layoutTaikhoan.setBackgroundColor(getResources().getColor(R.color.colorNavigation));

            imgTaichinh.setImageResource(R.drawable.ic_money_unacitive);
            imgChitieu.setImageResource(R.drawable.ic_spend_active);
            imgKehoach.setImageResource(R.drawable.ic_plan_unactive);
            imgTaikhoan.setImageResource(R.drawable.ic_acc_unactive);

            txtTaichinh.setTextColor(getResources().getColor(R.color.colorUnactive));
            txtChitieu.setTextColor(getResources().getColor(R.color.colorWhite));
            txtKehoach.setTextColor(getResources().getColor(R.color.colorUnactive));
            txtTaikhoan.setTextColor(getResources().getColor(R.color.colorUnactive));

            txtHeader.setText("Chi tiêu");

        } else if (index == 2 ){

            layoutTaichinh.setBackgroundColor(getResources().getColor(R.color.colorNavigation));
            layoutKehoach.setBackgroundColor(getResources().getColor(R.color.colorActive));
            layoutChitieu.setBackgroundColor(getResources().getColor(R.color.colorNavigation));
            layoutTaikhoan.setBackgroundColor(getResources().getColor(R.color.colorNavigation));

            imgTaichinh.setImageResource(R.drawable.ic_money_unacitive);
            imgChitieu.setImageResource(R.drawable.ic_spend_unactive);
            imgKehoach.setImageResource(R.drawable.ic_plan_active);
            imgTaikhoan.setImageResource(R.drawable.ic_acc_unactive);

            txtTaichinh.setTextColor(getResources().getColor(R.color.colorUnactive));
            txtChitieu.setTextColor(getResources().getColor(R.color.colorUnactive));
            txtKehoach.setTextColor(getResources().getColor(R.color.colorWhite));
            txtTaikhoan.setTextColor(getResources().getColor(R.color.colorUnactive));

            txtHeader.setText("Kế hoạch");

        } else if (index == 3 ){

            layoutTaichinh.setBackgroundColor(getResources().getColor(R.color.colorNavigation));
            layoutKehoach.setBackgroundColor(getResources().getColor(R.color.colorNavigation));
            layoutChitieu.setBackgroundColor(getResources().getColor(R.color.colorNavigation));
            layoutTaikhoan.setBackgroundColor(getResources().getColor(R.color.colorActive));

            imgTaichinh.setImageResource(R.drawable.ic_money_unacitive);
            imgChitieu.setImageResource(R.drawable.ic_spend_unactive);
            imgKehoach.setImageResource(R.drawable.ic_plan_unactive);
            imgTaikhoan.setImageResource(R.drawable.ic_acc_active);

            txtTaichinh.setTextColor(getResources().getColor(R.color.colorUnactive));
            txtChitieu.setTextColor(getResources().getColor(R.color.colorUnactive));
            txtKehoach.setTextColor(getResources().getColor(R.color.colorUnactive));
            txtTaikhoan.setTextColor(getResources().getColor(R.color.colorWhite));

            txtHeader.setText("Tài khoản");

        }
    }

    @Override
    public void onClick(View v) {
        //Events click of controls
        int id = v.getId();
        if (id == R.id.layoutTaichinh)
            onChangeTab(0);
        else if (id == R.id.layoutChitieu)
            onChangeTab(1);
        else if (id == R.id.layoutKehoach)
            onChangeTab(2);
        else if (id == R.id.layoutTaikhoan)
            onChangeTab(3);
    }


}
