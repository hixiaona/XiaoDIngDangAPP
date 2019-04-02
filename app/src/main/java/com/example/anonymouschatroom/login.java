package com.example.anonymouschatroom;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnTopPosCallback;
import zhy.com.highlight.shape.CircleLightShape;

public class login extends AppCompatActivity {
    ImageView btn_confirm;
    Button btn_unload;
    Button btn_rigister;
    EditText l_username ;
    EditText l_password ;
    private  String mFileName="chatting.txt";
    private HighLight mHightLight;
    private int Count=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        btn_confirm=findViewById(R.id.btn_start1);
        btn_unload=findViewById(R.id.btn_unload);
        btn_rigister=findViewById(R.id.btn_regist);
         l_username = findViewById(R.id.username);
        l_password = findViewById(R.id.password);
        int enter= Integer.parseInt(String.valueOf(read()));
        if(enter==0){
            showNextTipView();
        }


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent mIntent = new Intent(getBaseContext(), playing.class);
                // startActivity(mIntent);
                if (l_username.getText().toString().length() < 1||l_password.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(),
                            "No input!", Toast.LENGTH_LONG)
                            .show();
                } else {

                    mFileName=l_username.getText().toString()+".txt";
                    String allinfo=read1();
                    int na=allinfo.indexOf("name:");
                    int pw=allinfo.indexOf("password:");
                    int nu=allinfo.indexOf("num:");
                    String  myname=allinfo.substring(na+5,pw-1);
                    String  mypassword=allinfo.substring(pw+9,nu-1);
                    if(l_username.getText().toString().equals(myname)&&l_password.getText().toString().equals(mypassword)){

                        Toast.makeText(getApplicationContext(),
                                "Login scuuessful!", Toast.LENGTH_LONG)
                                .show();

                        Intent intent=new Intent(getApplicationContext(),seeking.class);
                        Bundle bundle =new Bundle();
                        bundle.putString("name",myname);
                        bundle.putInt("ident",1);
                        intent.putExtras(bundle);
                        startActivityForResult(intent,0);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),
                                "Password error!", Toast.LENGTH_LONG)
                                .show();
                    }


                    //Save("{start\tname:"+mEditText.getText().toString()+"\tnum:0\tend}");
                   // startActivityForResult(new Intent(getBaseContext(), seeking.class),0);
                }


            }
        });
        btn_unload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent mIntent = new Intent(getBaseContext(), playing.class);
                // startActivity(mIntent);
                read1();
                if(read1()==""){
                    save1("{start\timage:cat_01\tname:null\tpassword:null\tnum:0\tend}\t<Page:\tpage0:0\tpage1:0\tpage2:0\tpage3:0\tpage4:0\tPage>");
                }

                Intent intent=new Intent(getApplicationContext(),seeking.class);
                Bundle bundle =new Bundle();
                bundle.putInt("ident",0);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);





            }
        });
        btn_rigister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent mIntent = new Intent(getBaseContext(), playing.class);
                // startActivity(mIntent);

                startActivityForResult(new Intent(getBaseContext(), register.class),0);



            }
        });


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
    private boolean isBoolean=false;
    public  void showNextTipView(){
        mHightLight = new HighLight(login.this)//
                //.anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
                .autoRemove(false)
                .enableNext()
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        //界面布局完成添加tipview
                        if(!isBoolean){
                            isBoolean=true;
                            mHightLight.addHighLight(R.id.btn_start1,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());
                            mHightLight.addHighLight(R.id.btn_regist,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());
                            mHightLight.addHighLight(R.id.btn_unload,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());

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


                        if(Count==0){
                            Toast.makeText(login.this, "Please enter your username and password and click to login.", Toast.LENGTH_SHORT).show();
                            mHightLight.next();
                            Count++;
                        }else if(Count==1){
                            Toast.makeText(login.this, "Click here to register an account.", Toast.LENGTH_SHORT).show();
                            mHightLight.next();
                            Count++;
                        }else{
                            Toast.makeText(login.this, "Click here to enter the program without logging in.", Toast.LENGTH_SHORT).show();
                            mHightLight.next();
                        }


                    }


                });


    }
    private void save1(String content){
        FileOutputStream fileOutputStream = null;
        try {
            File  dir = new File(Environment.getExternalStorageDirectory(),"a_chatting");
            if(!dir.exists()){
                dir.mkdir();
            }
            File file = new File(dir,mFileName);
            if(!file.exists()){
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
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
    private String  read1(){
        FileInputStream fileInputStream = null;

        try {

            //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"skypan",mFileName);
            File dir = new File(Environment.getExternalStorageDirectory(),"a_chatting");
            if(!dir.exists()){
                dir.mkdir();
            }
            File file = new File(dir,mFileName);
            if(!file.exists()){
                file.createNewFile();
            }
            fileInputStream = new FileInputStream(file);
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
        String Content=read1();
        Content=Content+"\t"+content;
        save1(Content);
    }

}

