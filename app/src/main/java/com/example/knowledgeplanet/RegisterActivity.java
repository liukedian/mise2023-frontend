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
import com.example.knowledgeplanet.bean.RegisterRequest;
import com.example.knowledgeplanet.utils.ConvertType;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    int senCaptchaFlag = 0;
    int flag=0;
    private TextView toLogin;
    private Button register, sendCaptcha;
    private EditText et_username, et_password, et_check, et_phone, et_captcha;
    private String userName, password, checkPassword, phoneNumber, captcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_username = findViewById(R.id.re_username);
        et_password = findViewById(R.id.re_password);
        et_check = findViewById(R.id.re_check);
        et_phone = findViewById(R.id.re_phone);
        et_captcha = findViewById(R.id.re_captcha);

        //处理去登陆界面
        toLogin = findViewById(R.id.to_login);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLoginActivity();
            }
        });

        //点击发送验证码按钮
        sendCaptcha = findViewById(R.id.send_captcha);
        sendCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber=et_phone.getText().toString().trim();
                //TODO 手机号验证
                sendCaptcha(phoneNumber);
                if(senCaptchaFlag==1){
                    Toast.makeText(RegisterActivity.this, "验证码已发送，请注意查收", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //处理注册
        register = findViewById(R.id.re_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //先获取ev的str形式
                getEditString();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegisterActivity.this, "请输入您的用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "请输入您的密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(checkPassword)) {
                    Toast.makeText(RegisterActivity.this, "请确认您的密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(captcha)) {
                    Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(checkPassword)) {
                    Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                //生成注册请求体
                RegisterRequest registerRequest = new RegisterRequest(userName, password, phoneNumber, captcha);
                registerToBackend(registerRequest);

                if (flag ==200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            //跳转到登录界面中
                            toLoginActivity();
                            RegisterActivity.this.finish();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //注册结果标志位

    public void registerToBackend(RegisterRequest registerRequest) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = ConvertType.beanToJson(registerRequest);
                    // 创建HTTP客户端
                    OkHttpClient client = new OkHttpClient()
                            .newBuilder()
                            .connectTimeout(60000, TimeUnit.MILLISECONDS)
                            .readTimeout(60000, TimeUnit.MILLISECONDS)
                            .build();
                    // 创建HTTP请求
                    Request request = new Request.Builder()
                            .url("http://" + constant.IP_ADDRESS + "/user/register")
                            .post(RequestBody.create(MediaType.parse("application/json"), json))
                            .build();
                    // 执行发送的指令
                    Response response = client.newCall(request).execute();
                    flag=response.code();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "网络问题，注册失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void sendCaptcha(String phoneNum) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建HTTP客户端
                    OkHttpClient client = new OkHttpClient()
                            .newBuilder()
                            .connectTimeout(60000, TimeUnit.MILLISECONDS)
                            .readTimeout(60000, TimeUnit.MILLISECONDS)
                            .build();
                    // 创建HTTP请求

                    Request request = new Request.Builder()
                            .url("http://" + constant.IP_ADDRESS + "/sms/send?phone=" + phoneNum)
                            .build();
                    // 执行发送的指令，获得返回结果
                    Response response = client.newCall(request).execute();

                    if (response.code() == 200) {
                        senCaptchaFlag=1;
                   } else {
                        senCaptchaFlag=0;
                    }

                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "网络或进程问题", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void getEditString() {
        userName = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();
        checkPassword = et_check.getText().toString().trim();
        phoneNumber = et_phone.getText().toString().trim();
        captcha = et_captcha.getText().toString().trim();
    }

    private void toLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}

