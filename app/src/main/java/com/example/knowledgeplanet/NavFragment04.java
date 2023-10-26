package com.example.knowledgeplanet;

import static com.example.knowledgeplanet.utils.ConvertType.beanToJson;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.knowledgeplanet.Constants.constant;
import com.example.knowledgeplanet.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NavFragment04 extends Fragment {

    public static final String USERNAME = "usr";
    private static final String TAG = "method";

    private String userLoginNow;
    private Integer id;
    private EditText etUsername,etAge,etFavor,etBron;
    private Button btnUpdate,btnExit;

    public NavFragment04() {}

    //在创建fragment的时候就把username作为参数传进去，带用到的时候取出来
    public static NavFragment04 newInstance(String username) {
        NavFragment04 fragment = new NavFragment04();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userLoginNow = getArguments().getString(USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav04, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        etUsername = getActivity().findViewById(R.id.et_username);
        etBron = getActivity().findViewById(R.id.et_bron);
        etFavor = getActivity().findViewById(R.id.et_favor);
        etAge = getActivity().findViewById(R.id.et_age);

        btnUpdate = getActivity().findViewById(R.id.btn_update);
        btnExit = getActivity().findViewById(R.id.btn_exit);

        userLoginNow = getArguments().getString(USERNAME);
        etUsername.setText(userLoginNow);

        dataCallback();
        updateData();

        loginOut();

    }

    // 向主线程中回传数据
    JSONObject jsonObject;
    boolean isSame = false;
    // 数据更新
    public void updateData() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String strId = String.valueOf(id);
                            User user = initUser();

                            if(isSame) {
                                return;
                            }else{
                                user.setId(Integer.valueOf(strId));
                                String json = beanToJson(user);
                                // 创建一个user对象
                                // 创建HTTP客户端
                                OkHttpClient client = new OkHttpClient();
                                // 创建HTTP请求

                                Request request = new Request.Builder()
                                        .url("http://" + constant.IP_ADDRESS + "/user/updateByUsername")
                                        .post(RequestBody.create(MediaType.parse("application/json"),json))
                                        .build();
                                // 执行发送的指令
                                Response response = client.newCall(request).execute();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"发送成功",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dataCallback();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),"修改失败，网络波动或修改参数错误",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    // 数据回显
    public void dataCallback() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("http://" + constant.IP_ADDRESS + "/user/searchByUsername/" + userLoginNow)
                            .get()
                            .build();

                    Response response = client.newCall(request).execute();

                    String data = response.body().string();
                    jsonObject = new JSONObject(data);

                    Log.d(TAG, "return json: " + jsonObject);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                id = jsonObject.getInt("id");
                                etAge.setText(String.valueOf(jsonObject.getInt("age")));
                                etFavor.setText(jsonObject.getString("foodFavor"));
                                etBron.setText(jsonObject.getString("bronSeason"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getActivity(),"回显成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"网络连接失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    public User initUser() {
        User user = new User();

        String username = etUsername.getText().toString();
        String favor = etFavor.getText().toString();
        String bron = etBron.getText().toString();
        String age = etAge.getText().toString();

        if(username.equals(userLoginNow)) {
            isSame = true;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(),"昵称唯一不可变",Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
        user.setUsername(username);
        user.setAge(Integer.valueOf(age));
        user.setFoodFavor(favor);
        user.setBronSeason(bron);

        return user;
    }

    public void loginOut() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"已退出",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}