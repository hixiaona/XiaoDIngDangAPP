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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnTopPosCallback;
import zhy.com.highlight.shape.CircleLightShape;

public class creating extends AppCompatActivity {
    ImageView btn_confirmCreat;
    TextView  btn_finish;
    EditText e_theme;
    EditText e_content;
    EditText e_url;
    Spinner sunm;
    TextView tv_theme;
    private int ident=0;
    private String name="UNLOAD";
    private int f_enter=0;
    private HighLight mHightLight;
    private int position;
    private int change=0;
    private int Count11=0;
    private int Count;
    private int Url1;
    private int end2;


    private  String mFileName="chatting.txt";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creating);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        btn_confirmCreat=findViewById(R.id.ivreturn1);
        btn_finish=findViewById(R.id.finish);
        e_theme=findViewById(R.id.theme);
        e_content=findViewById(R.id.content111);
        e_url=findViewById(R.id.url);
        sunm=findViewById(R.id.snum);
        tv_theme=findViewById(R.id.tvtitleone1);

        ident= getIntent().getIntExtra("ident",1);
        if(ident==1){
            name=getIntent().getStringExtra("name");
            mFileName=name+".txt";
        }
        change= getIntent().getIntExtra("change",0);
        if(change==1){
            tv_theme.setText("Change");
            position= getIntent().getIntExtra("position",0);
            String Urlinfo=read1();
            Count=Urlinfo.indexOf("{Url");

            for(int i=0;i<=position;i++){
                int theme=Urlinfo.indexOf("theme:",Count);
                int content=Urlinfo.indexOf("content:",Count);
                int time=Urlinfo.indexOf("time:",Count);
                int url=Urlinfo.indexOf("url:",Count);
                int inn=Urlinfo.indexOf("://",Count);
                int end1=Urlinfo.indexOf("end}",Count);
                Count=end1+1;
                e_theme.setText(Urlinfo.substring(theme+6,content-1));
                e_content.setText(Urlinfo.substring(content+8,time-1));
                e_url.setText(Urlinfo.substring(inn+3,end1-1));
            }

        }





        String enterinfo=read1();
        int p2=enterinfo.indexOf("page2:");
        int p3=enterinfo.indexOf("page3:");
        String  enter=enterinfo.substring(p2+6,p3-1);
        int enter0= Integer.parseInt(String.valueOf(enter));
        if(enter0==0){
            showNextTipView();
            enter0=1;
            enterinfo=enterinfo.substring(0,p2+6)+String.valueOf(enter0)+enterinfo.substring(p3-1,enterinfo.length());
            save1(enterinfo);
        }


        btn_confirmCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent mIntent = new Intent(getBaseContext(), playing.class);
                // startActivity(mIntent);
                Intent intent=new Intent(getApplicationContext(),seeking.class);
                Bundle bundle =new Bundle();
                bundle.putString("name",name);
                bundle.putInt("ident",ident);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(change==1){
                    if(e_theme.getText().toString().length()<1||e_url.getText().toString().length()<1||e_content.getText().toString().length()<1){

                        Toast.makeText(getApplicationContext(),
                                "No changed!", Toast.LENGTH_LONG)
                                .show();
                    }else{
                        String Urlinfo=read1();
                        Count=Urlinfo.indexOf("{Url")-1;
                        for(int i=0;i<=position;i++){
                             Url1=Urlinfo.indexOf("{Url",Count);
                             end2=Urlinfo.indexOf("end}",Count);
                            Count=end2+1;
                        }
                        String time=getTime();
                        String changeinfo=Urlinfo.substring(0,Url1-1)+"{Url\ttheme:"+e_theme.getText().toString()+"\tcontent:"+e_content.getText().toString()+"\ttime:"+time+"\turl:"+sunm.getSelectedItem().toString()+"://"+e_url.getText().toString()+"\tend}\t"+Urlinfo.substring(end2+4,Urlinfo.length());
                        save1(changeinfo);


                        Toast.makeText(getApplicationContext(),
                                "Changed successful!", Toast.LENGTH_LONG)
                                .show();


                    }

                }else{
                    if(e_theme.getText().toString().length()<1||e_url.getText().toString().length()<1||e_content.getText().toString().length()<1){

                        Toast.makeText(getApplicationContext(),
                                "No appended!", Toast.LENGTH_LONG)
                                .show();
                    }else{
                        String allinfo=read1();
                        int nu=allinfo.indexOf("num:");
                        int end=allinfo.indexOf("end}");
                        String  count=allinfo.substring(nu+4,end-1);
                        int mcount= Integer.parseInt(String.valueOf(count));
                        mcount=mcount+1;
                        allinfo=allinfo.substring(0,nu+4)+String.valueOf(mcount)+allinfo.substring(end-1,allinfo.length());
                        save1(allinfo);

                        String time=getTime();
                        Save("{Url\ttheme:"+e_theme.getText().toString()+"\tcontent:"+e_content.getText().toString()+"\ttime:"+time+"\turl:"+sunm.getSelectedItem().toString()+"://"+e_url.getText().toString()+"\tend}");



                        Toast.makeText(getApplicationContext(),
                                "Appended successful!", Toast.LENGTH_LONG)
                                .show();


                    }

                }

                Intent intent=new Intent(getApplicationContext(),seeking.class);
                Bundle bundle =new Bundle();
                bundle.putString("name",name);
                bundle.putInt("ident",ident);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });


    }

    private boolean isBoolean=false;
    public  void showNextTipView(){
        mHightLight = new HighLight(creating.this)//
                //.anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
                .autoRemove(false)
                .enableNext()
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        //界面布局完成添加tipview
                        if(!isBoolean){
                            isBoolean=true;
                            mHightLight.addHighLight(R.id.ivreturn1,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());
                            mHightLight.addHighLight(R.id.finish,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());

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


                        if(Count11==0){
                            Toast.makeText(creating.this, "Click here to go back to the next level.", Toast.LENGTH_SHORT).show();
                            mHightLight.next();
                            Count11++;
                        }else{
                            Toast.makeText(creating.this, "Click here to complete the link creation.", Toast.LENGTH_SHORT).show();
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
    private String getTime() {
        final Calendar c = Calendar.getInstance();
        String year=String.valueOf(c.get(Calendar.YEAR));
        String month=String.valueOf(c.get(Calendar.MONTH));
        String date=String.valueOf(c.get(Calendar.DATE));
        String hour=String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String time=String.valueOf(c.get(Calendar.MINUTE));
        String Time=year+"/"+month+"/"+date+"\t"+hour+":"+time;
        return Time;
    }


}
