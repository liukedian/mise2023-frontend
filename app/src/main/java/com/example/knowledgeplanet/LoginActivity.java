package com.example.knowledgeplanet;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.knowledgeplanet.Constants.constant;
import com.example.knowledgeplanet.bean.LoginRequest;
import com.example.knowledgeplanet.utils.ConvertType;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextView tvRegister, tvFindPas;
    private EditText etUsername, etPwd;
    private String userName,password;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {

        etUsername = findViewById(R.id.username);
        etPwd = findViewById(R.id.password);
        tvFindPas = findViewById(R.id.tv_find_pas);
        login = findViewById(R.id.loginBtn);
        tvRegister = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getEditString();

                if (TextUtils.isEmpty(userName)) {
                    //用户名为空
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    //密码为空
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //调用后端的登录接口
                loginToBackend();

                if (flag==200) {
                    //如果登入成功了，要携带着个人信息返回，放进sharepre里
                    saveLoginStatus(true,userName);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else if(flag==999){
                    //登入失败
                    Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 跳转到找回密码
        tvFindPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,FindPasActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

        // 跳转到register页面。
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册界面
                Intent intent=new Intent( LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }

    //获取用户名和密码的String形式
    private void getEditString(){
        userName = etUsername.getText().toString().trim();
        password = etPwd.getText().toString().trim();
    }

    public void loginToBackend() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    // 创建HTTP客户端
                    OkHttpClient client = new OkHttpClient()
                            .newBuilder()
                            .connectTimeout(60000, TimeUnit.MILLISECONDS)
                            .readTimeout(60000, TimeUnit.MILLISECONDS)
                            .build();
                    // 创建HTTP请求

                    Request request = new Request.Builder()
                            .url("http://"+constant.IP_ADDRESS+"/user/login?userName="+userName+"&password="+password)
                            .build();
                    // 执行发送的指令，获得返回结果
                    Response response = client.newCall(request).execute();
                    Log.d("resp",response.toString());
                    flag=response.code();
                }catch (Exception e){
                    Log.e(TAG,Log.getStackTraceString(e));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"网络或进程问题",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void saveLoginStatus(boolean status,String userName){
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", status);
        editor.putString("loginUserName", userName);
        editor.commit();
    }

}