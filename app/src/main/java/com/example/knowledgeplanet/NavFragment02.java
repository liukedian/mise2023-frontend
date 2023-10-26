package com.example.knowledgeplanet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.knowledgeplanet.Constants.constant;
import com.example.knowledgeplanet.bean.TuberBean;
import com.example.knowledgeplanet.utils.TuberListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NavFragment02 extends Fragment{


    List<HashMap<String,Object>> list = new ArrayList<>();

    public List<HashMap<String,Object>> putData(){
        HashMap<String,Object> map1 = new HashMap<>();
        map1.put("name", "张三");
        map1.put("id", "wh445306");
        map1.put("age", "28岁");
        map1.put("tel", "18322228898");
        HashMap<String,Object> map2 = new HashMap<>();
        map2.put("name", "李四");
        map2.put("id", "jyw8886");
        map2.put("age", "27岁");
        map2.put("tel", "13922278898");

        HashMap<String,Object> map3 = new HashMap<>();
        map3.put("name", "王五");
        map3.put("id", "bmw8899");
        map3.put("age", "27岁");
        map3.put("tel", "13319780706");

        HashMap<String,Object> map4 = new HashMap<>();
        map4.put("name", "听雨");
        map4.put("id", "wh445306");
        map4.put("age", "28岁");
        map4.put("tel", "17322228898");

        HashMap<String,Object> map5 = new HashMap<>();
        map5.put("name", "若兰");
        map5.put("id", "jyw8886");
        map5.put("age", "27岁");
        map5.put("tel", "13922278898");

        HashMap<String,Object> map6 = new HashMap<>();
        map6.put("name", "海子");
        map6.put("id", "bmw8899");
        map6.put("age", "27岁");
        map6.put("tel", "13319780706");

        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);
        list.add(map6);
        return list;
    }

    private final List<TuberBean> TuberBeanList = new ArrayList<>();
    private Intent intent;
    public NavFragment02() {
    }
    public static NavFragment02 newInstance() {
        return new NavFragment02();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.subscribe_main_frame, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        putData();

        ListView listView=getActivity().findViewById(R.id.subList);

        SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(),list,R.layout.sub_item_layout,
                new String[]{"name","id","age","tel"},
                new int[]{R.id.txtUserName,R.id.txtUserID,R.id.txtUserAge,R.id.txtUserTel});
        listView.setAdapter(simpleAdapter);



        //设置Item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }

}