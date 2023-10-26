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

public class FindPasActivity extends AppCompatActivity {

    public int senCaptchaFlag=0;
    int flag=0;
    private Button sendCaptcha,reset;
    private EditText et_phone,et_psw,et_pswConfirm,et_captcha;
    private String userName,phoneNumber,password,pswConfirm,captcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pas);

        et_phone=findViewById(R.id.finPass_phone);
        et_psw=findViewById(R.id.getPassword_newPsw);
        et_pswConfirm=findViewById(R.id.getPassword_newPswConfirm);
        et_captcha=findViewById(R.id.findPass_captcha);

        //发送验证码
        sendCaptcha=findViewById(R.id.send_captcha);
        sendCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber=et_phone.getText().toString().trim();
                sendCaptcha(phoneNumber);
                //TODO 手机号验证
                if(senCaptchaFlag==1){
                    Toast.makeText(FindPasActivity.this, "验证码已发送，请注意查收", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FindPasActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //重置密码
        reset=findViewById(R.id.resetPsw);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //先获取ev的str形式
                getEditString();

                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(FindPasActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(FindPasActivity.this, "请输入您的密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pswConfirm)){
                    Toast.makeText(FindPasActivity.this, "请确认您的密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(captcha)){
                    Toast.makeText(
                            FindPasActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(pswConfirm)){
                    Toast.makeText(FindPasActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                //不新建实体类了，和注册公用，只不过重新设置密码的时候不给username了，直接用电话号重设，反正手机号不能重复注册的
                RegisterRequest resetPswRequest=new RegisterRequest(null,password,phoneNumber,captcha);
                resetToBackend(resetPswRequest);

                if(flag ==200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FindPasActivity.this,"重置密码成功",Toast.LENGTH_SHORT).show();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            //跳转到登录界面中
                            toLoginActivity();
                            FindPasActivity.this.finish();
                        }
                    });
                }else{
                    Toast.makeText(FindPasActivity.this,"重置密码失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetToBackend(RegisterRequest registerRequest) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String json = ConvertType.beanToJson(registerRequest);
                    // 创建HTTP客户端
                    OkHttpClient client = new OkHttpClient()
                            .newBuilder()
                            .connectTimeout(60000, TimeUnit.MILLISECONDS)
                            .readTimeout(60000, TimeUnit.MILLISECONDS)
                            .build();
                    // 创建HTTP请求
                    Request request = new Request.Builder()
                            .url("http://"+ constant.IP_ADDRESS +"/user/resetPassword")
                            .post(RequestBody.create(MediaType.parse("application/json"),json))
                            .build();
                    // 执行发送的指令
                    Response response = client.newCall(request).execute();
                    flag =response.code();

                }catch (Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FindPasActivity.this,"网络问题，注册失败",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(FindPasActivity.this, "网络或进程问题", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
    private void getEditString(){
        phoneNumber = et_phone.getText().toString().trim();
        password = et_psw.getText().toString().trim();
        pswConfirm = et_pswConfirm.getText().toString().trim();
        captcha=et_captcha.getText().toString().trim();
    }

    private void toLoginActivity(){
        Intent intent = new Intent();
        intent.setClass(FindPasActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}