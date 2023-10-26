package com.example.knowledgeplanet;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout llHome,llFind,llMine,llsub;
    private ImageView ivHome,ivFind,ivMine,ivsub;
    private TextView tvHome,tvFind,tvMine,tvsub;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initEvent();
    }

    private void initEvent() {
        // 添加fragment
        fragmentTransaction();

        NavFragment04 navFragment04 = NavFragment04.newInstance(username);

        //找到首页的fragment并代替
        fragmentTransaction.replace(R.id.fcv_fragment, navFragment04).commit();
        tvHome.setTextColor(Color.BLUE);

        llHome.setOnClickListener(this);
        llFind.setOnClickListener(this);
        llMine.setOnClickListener(this);
        llsub.setOnClickListener(this);
    }

    private void initView() {
        llHome = findViewById(R.id.ll_home);
        llFind = findViewById(R.id.ll_find);
        llMine = findViewById(R.id.ll_mine);
        llsub = findViewById(R.id.ll_sub);

        ivHome = findViewById(R.id.iv_home);
        ivFind = findViewById(R.id.iv_find);
        ivMine = findViewById(R.id.iv_mine);
        ivsub = findViewById(R.id.iv_sub);

        tvHome = findViewById(R.id.tv_home);
        tvFind = findViewById(R.id.tv_find);
        tvMine = findViewById(R.id.tv_mine);
        tvsub = findViewById(R.id.tv_sub);
    }

    // 重置底部导航点击状态的字体颜色
    private void resetBottomState() {
        tvHome.setTextColor(Color.BLACK);
        tvFind.setTextColor(Color.BLACK);
        tvMine.setTextColor(Color.BLACK);
        tvsub.setTextColor(Color.BLACK);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        resetBottomState();
        int id=view.getId();
        if (id==R.id.ll_home){
            fragmentTransaction();
            NavFragment01 navFragment01 = NavFragment01.newInstance();
            fragmentTransaction.replace(R.id.fcv_fragment, navFragment01).commit();
            tvHome.setTextColor(Color.BLUE);
        }else if(id==R.id.ll_find){
            fragmentTransaction();
            NavFragment02 navFragment02 = NavFragment02.newInstance();
            fragmentTransaction.replace(R.id.fcv_fragment, navFragment02).commit();
            tvFind.setTextColor(Color.BLUE);
        }else if(id==R.id.ll_sub){
            fragmentTransaction();
            NavFragment03 navFragment03 = NavFragment03.newInstance();
            fragmentTransaction.replace(R.id.fcv_fragment, navFragment03).commit();
            tvsub.setTextColor(Color.BLUE);
        }else{
            fragmentTransaction();
            String username = getUsername();
            NavFragment04 navFragment04 = NavFragment04.newInstance(username);
            fragmentTransaction.replace(R.id.fcv_fragment, navFragment04).commit();
            tvMine.setTextColor(Color.BLUE);
        }
    }

    //可复用的注册fragment
    public void fragmentTransaction() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    public String getUsername() {
        SharedPreferences pref = getSharedPreferences("loginInfo", MODE_PRIVATE);
        username = pref.getString("loginUserName","");
        return username;
    }

}