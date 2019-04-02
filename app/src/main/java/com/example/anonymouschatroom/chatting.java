package com.example.anonymouschatroom;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
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

public class chatting extends AppCompatActivity {
   ImageView btn_return;
    private WebView webView;
    ImageView menu;
    private  String mFileName="chatting.txt";
    private  int Count;
    private    String URL;
    TextView    tv_url;
    private int ident=0;
    private String name="UNLOAD";
    private HighLight mHightLight;
    private int Count11=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting);
        //setContentView(R.layout.top_panel_return_doubletitle_ivmenu);
        final int Num= getIntent().getIntExtra("num",1);
        ident= getIntent().getIntExtra("ident",1);
        if(ident==1){
            name=getIntent().getStringExtra("name");
            mFileName=name+".txt";
        }

        btn_return=findViewById(R.id.ivreturn);
        webView = findViewById(R.id.view1);
        menu=findViewById(R.id.ivmenu);
        tv_url=findViewById(R.id.tv_url);

        String enterinfo=read1();
        int p4=enterinfo.indexOf("page4:");
        int page=enterinfo.indexOf("Page>");
        String  enter=enterinfo.substring(p4+6,page-1);
        int enter0= Integer.parseInt(String.valueOf(enter));
        if(enter0==0){
            showNextTipView();
            enter0=1;
            enterinfo=enterinfo.substring(0,p4+6)+String.valueOf(enter0)+enterinfo.substring(page-1,enterinfo.length());
            save1(enterinfo);
        }

        String allinfo=read1();
        Count=0;
        final int  Url = allinfo.indexOf("{Url", Count);
        Count=Url;
        for(int i=0;i<=Num;i++){

            int url = allinfo.indexOf("url:", Count);
            int end = allinfo.indexOf("end}", Count);
            Count = end + 1;
            URL=allinfo.substring(url + 4, end - 1);
        }

        tv_url.setText(URL);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setDomStorageEnabled(true);
        //webView.loadUrl("http://tianqi.moji.com/");

        //是否可以后退
        webView.canGoBack();
//后退网页
        webView.goBack();

//是否可以前进
        webView.canGoForward();
//前进网页
        webView.goForward();


        webView.loadUrl(URL);
//以当前的index为起始点前进或者后退到历史记录中指定的steps
//如果steps为负数则为后退，正数则为前进
        int intsteps=1;
        webView.goBackOrForward(intsteps);




        tv_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent mIntent = new Intent(getBaseContext(), playing.class);
                // startActivity(mIntent);
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); //"image/*"
                intent.putExtra(Intent.EXTRA_SUBJECT,"共享软件");
                intent.putExtra(Intent.EXTRA_TEXT, URL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "选择分享类型"));
            }
        });

        btn_return.setOnClickListener(new View.OnClickListener() {
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

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent mIntent = new Intent(getBaseContext(), playing.class);
                // startActivity(mIntent);
                Intent intent=new Intent(getApplicationContext(),creating.class);
                Bundle bundle =new Bundle();
                bundle.putString("name",name);
                bundle.putInt("ident",ident);
                bundle.putInt("position",Num);
                bundle.putInt("change",1);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);


            }
        });

    }

    private boolean isBoolean=false;
    public  void showNextTipView(){
        mHightLight = new HighLight(chatting.this)//
                //.anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
                .autoRemove(false)
                .enableNext()
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        //界面布局完成添加tipview
                        if(!isBoolean){
                            isBoolean=true;
                            mHightLight.addHighLight(R.id.ivreturn,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());
                            mHightLight.addHighLight(R.id.tv_url,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());
                            mHightLight.addHighLight(R.id.ivmenu,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());

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
                            Toast.makeText(chatting.this, "Click here to go back to the next level.", Toast.LENGTH_SHORT).show();
                            mHightLight.next();
                            Count11++;
                        }else if(Count11==1){
                            Toast.makeText(chatting.this, "Click here to share your link.", Toast.LENGTH_SHORT).show();
                            mHightLight.next();
                            Count11++;
                        }else{
                            Toast.makeText(chatting.this, "Click here to change the link information.", Toast.LENGTH_SHORT).show();
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

}
