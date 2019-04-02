package com.example.anonymouschatroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnBottomPosCallback;
import zhy.com.highlight.position.OnLeftPosCallback;
import zhy.com.highlight.position.OnRightPosCallback;
import zhy.com.highlight.position.OnTopPosCallback;
import zhy.com.highlight.shape.BaseLightShape;
import zhy.com.highlight.shape.CircleLightShape;
import zhy.com.highlight.shape.OvalLightShape;
import zhy.com.highlight.shape.RectLightShape;
import zhy.com.highlight.view.HightLightView;



public class MainActivity extends AppCompatActivity {

    ImageView btn_seek;
    private HighLight mHightLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_seek=findViewById(R.id.btn_start);

       btn_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("text","加油");
                startActivityForResult(new Intent(getBaseContext(), login.class),1);
            }
        });
       if(read()==null)
       {
           save("0");
       }

        //Save("0");
        int enter= Integer.parseInt(String.valueOf(read()));
        if(enter==0){
            showNextTipView();
        }


    }
    private void save(String content){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput("Enter11", MODE_PRIVATE);
            fileOutputStream.write(content.getBytes());

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(fileOutputStream !=null){
                try{
                    fileOutputStream.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    private String  read(){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream=openFileInput("Enter11");
            byte[]buff=new byte[1024];
            StringBuilder sb=new StringBuilder("");
            int len=0;
            while ((len=fileInputStream.read(buff))>0){
                sb.append(new String(buff,0,len));
            }
            return sb.toString();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    private void  Save(String content){
        String Content=read();
        Content=Content+content;
        save(Content);
    }
    private boolean isBoolean=false;
    public  void showNextTipView(){
        mHightLight = new HighLight(MainActivity.this)//
                //.anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
                .autoRemove(false)
                .enableNext()
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        //界面布局完成添加tipview
                        if(!isBoolean){
                            isBoolean=true;
                            mHightLight.addHighLight(R.id.btn_start,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());
                            //mHightLight.addHighLight(R.id.iv_02,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());
                            //mHightLight.addHighLight(R.id.iv_03,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());

                            //      .addHighLight(R.id.iv_02,R.layout.layout_02,new OnTopLeftPosCallBack(0),new CircleLightShape())
                            //    .addHighLight(R.id.iv_03,R.layout.layout_03,new OnTopCenterLeftPosCallBack(40),new CircleLightShape());
                            //然后显示高亮布局
                            //这里OnTopLeftPosCallBack和OnTopCenterLeftPosCallBack是自定义继承与OnBaseCallback
                            //有需要的朋友可以自定义，这个主要用于用户放置layout的位置

                            mHightLight.show();
                            Log.e("text","11111111");
                        }
                    }
                })
                .setClickCallback(new HighLight.OnClickCallback() {
                    @Override
                    public void onClick() {


                            Toast.makeText(MainActivity.this, "Click here to enter the program！", Toast.LENGTH_SHORT).show();
                            mHightLight.next();



                    }


                });


    }

}
