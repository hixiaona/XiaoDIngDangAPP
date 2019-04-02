package com.example.anonymouschatroom;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnTopPosCallback;
import zhy.com.highlight.shape.CircleLightShape;

public class seeking extends AppCompatActivity {
    ImageView create;
    ImageView head1;
    ListView  select;
    TextView  username;
    private  int Count=0;
    private  String mFileName="chatting.txt";
    private String[] arrayDemo ;
    private String[] arrayDate ;
    private String[] arrayMes ;
    private SimpleAdapter sa;
    private int ident=0;
    private String name="UNLOAD";
    private int url;
    private int image=0;
    private HighLight mHightLight;
    private int Count11=0;


    private int[] headimage = {R.drawable.cat01,R.drawable.cat02,R.drawable.cat01,R.drawable.cat03,R.drawable.cat04,R.drawable.cat05,R.drawable.cat06,R.drawable.cat07,R.drawable.cat08,R.drawable.cat09,R.drawable.cat10,R.drawable.cat11,R.drawable.cat12,R.drawable.cat13,R.drawable.cat14,R.drawable.cat15,R.drawable.cat16,R.drawable.cat17,R.drawable.cat18,R.drawable.cat19};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeking);

        create=findViewById(R.id.create);
        head1=findViewById(R.id.head1);
        select=findViewById(R.id.lv);
        select=findViewById(R.id.lv1);
        select=findViewById(R.id.lv2);
        username=findViewById(R.id.message111);
        ident= getIntent().getIntExtra("ident",1);


        if(ident==1){
            name=getIntent().getStringExtra("name");
            username.setText(name);
            mFileName=name+".txt";
        }
        //head1.setImageResource(R.drawable.cat10);
        //image= getIntent().getIntExtra("image",0);
        //if(image==1){
            String headinfo=read1();
            int im=headinfo.indexOf("image:");
            int na=headinfo.indexOf("name:");
            headinfo=headinfo.substring(im+6,na-1);
            Bitmap bitmap=convertStringToIcon(headinfo);
            head1.setImageBitmap(bitmap);
        //}






        String allinfo=read1();
        int nu=allinfo.indexOf("num:");
        int end=allinfo.indexOf("end}");
        String  count=allinfo.substring(nu+4,end-1);
        int mcount= Integer.parseInt(String.valueOf(count));
        arrayDemo = new String[mcount];
        arrayMes = new String[mcount];
        arrayDate = new String[mcount];
        Count=0;
       for(int i=0;i<mcount;i++){

           //for(int j=0;j<3;j++) {
               int theme = allinfo.indexOf("theme:", Count);
               int con = allinfo.indexOf("content:", Count);
               int time = allinfo.indexOf("time:", Count);
               int url = allinfo.indexOf("url:", Count);
               Count = url + 1;
               arrayDemo[i] = (allinfo.substring(theme + 6, con - 1));
               arrayMes[i] = (allinfo.substring(con + 8, time - 1));
               arrayDate[i] = (allinfo.substring(time + 5, url - 1));
           //}

       }





        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.item_layout,R.id.name_msg_item, arrayDemo);
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.item_layout, R.id.content_msg_item, arrayMes);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, R.layout.item_layout, R.id.time_msg_item, arrayDate);

        ListView listView = findViewById(R.id.lv);
        ListView listView1 = findViewById(R.id.lv1);
        ListView listView2 = findViewById(R.id.lv2);



        listView.setAdapter(arrayAdapter);
        listView1.setAdapter(arrayAdapter1);
        listView2.setAdapter(arrayAdapter2);

        if(mcount==1){
            String enterinfo=read1();
            int p3=enterinfo.indexOf("page3:");
            int p4=enterinfo.indexOf("page4:");
            String  enter=enterinfo.substring(p3+6,p4-1);
            int enter0= Integer.parseInt(String.valueOf(enter));
            if(enter0==0){
                showNextTipView1();
                enter0=1;
                enterinfo=enterinfo.substring(0,p3+6)+String.valueOf(enter0)+enterinfo.substring(p4-1,enterinfo.length());
                save1(enterinfo);
            }
        }

        String enterinfo=read1();
        int p0=enterinfo.indexOf("page0:");
        int p1=enterinfo.indexOf("page1:");
        String  enter=enterinfo.substring(p0+6,p1-1);
        int enter0= Integer.parseInt(String.valueOf(enter));
        if(enter0==0){
            showNextTipView();
            enter0=1;
            enterinfo=enterinfo.substring(0,p0+6)+String.valueOf(enter0)+enterinfo.substring(p1-1,enterinfo.length());
            save1(enterinfo);
        }


        /*
         *  Update the title on the action to show the total number of student list
         */



            select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(),
                        "Open scuuessful!", Toast.LENGTH_LONG)
                        .show();
                Intent intent=new Intent(getApplicationContext(),chatting.class);
                Bundle bundle =new Bundle();
                bundle.putInt("num",position);
                bundle.putString("name",name);
                bundle.putInt("ident",ident);

                intent.putExtras(bundle);
                startActivityForResult(intent,0);

                //startActivityForResult(new Intent(getBaseContext(), chatting.class),0);
                //Toast.makeText(seeking.this,"click pos:"+position,Toast.LENGTH_SHORT).show();
            }
        });
      select.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


               String allinfo=read1();
               int nu=allinfo.indexOf("num:");
               int end=allinfo.indexOf("end}");
               String  count=allinfo.substring(nu+4,end-1);
               int mcount= Integer.parseInt(String.valueOf(count));
               mcount=mcount-1;
               if(mcount<0){
                   mcount=0;
               }
               allinfo=allinfo.substring(0,nu+4)+String.valueOf(mcount)+allinfo.substring(end-1,allinfo.length());
               save1(allinfo);

               allinfo=read1();

              for(int i=0;i<=position;i++){
                  Count=end+1;
                   url=allinfo.indexOf("{Url",Count);
                   end=allinfo.indexOf("end}",Count);
              }
               allinfo=allinfo.substring(0,url)+"\t"+allinfo.substring(end+4,allinfo.length());
               save1(allinfo);


               Toast.makeText(getApplicationContext(),
                       "Delete scuuessful!", Toast.LENGTH_LONG)
                       .show();

               Intent intent=new Intent(getApplicationContext(),seeking.class);
               Bundle bundle =new Bundle();
               bundle.putString("name",name);
               bundle.putInt("ident",ident);

               intent.putExtras(bundle);
               startActivityForResult(intent,0);
               //Toast.makeText(seeking.this,"clicking pos:"+position,Toast.LENGTH_SHORT).show();
               return true;
           }
       });




        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent mIntent = new Intent(getBaseContext(), playing.class);
                // startActivity(mIntent);
                Intent intent=new Intent(getApplicationContext(),creating.class);
                Bundle bundle =new Bundle();
                bundle.putString("name",name);
                bundle.putInt("ident",ident);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });

        head1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent mIntent = new Intent(getBaseContext(), playing.class);
                // startActivity(mIntent);

                if(ident==1){
                    Intent intent=new Intent(getApplicationContext(),myinfo.class);
                    Bundle bundle =new Bundle();
                    bundle.putString("name",name);
                    bundle.putInt("ident",ident);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,0);
                }else{
                    Toast.makeText(getApplicationContext(),
                           "Unlogged cannot use this feature！", Toast.LENGTH_LONG)
                           .show();
                }

            }
        });




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
    private boolean isBoolean=false;
    public  void showNextTipView(){
        mHightLight = new HighLight(seeking.this)//
                //.anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
                .autoRemove(false)
                .enableNext()
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        //界面布局完成添加tipview
                        if(!isBoolean){
                            isBoolean=true;
                            mHightLight.addHighLight(R.id.head1,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());
                            mHightLight.addHighLight(R.id.create,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());
                            mHightLight.addHighLight(R.id.lv,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());


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
                            Toast.makeText(seeking.this, "Click here to change personal information.", Toast.LENGTH_SHORT).show();
                            mHightLight.next();
                            Count11++;
                        }else if(Count11==1){
                            Toast.makeText(seeking.this, "Click here to create a link.", Toast.LENGTH_SHORT).show();
                            mHightLight.next();
                            Count11++;
                        }else{
                            Toast.makeText(seeking.this, "Long press can delete the link.", Toast.LENGTH_SHORT).show();
                            mHightLight.next();
                        }


                    }


                });


    }
    public  void showNextTipView1(){
        mHightLight = new HighLight(seeking.this)//
                //.anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
                .autoRemove(false)
                .enableNext()
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        //界面布局完成添加tipview
                        if(!isBoolean){
                            isBoolean=true;
                            mHightLight.addHighLight(R.id.lv,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());

                            mHightLight.show();
                            Log.e("text","11111111");
                        }
                    }
                })
                .setClickCallback(new HighLight.OnClickCallback() {
                    @Override
                    public void onClick() {

                            Toast.makeText(seeking.this, "Long press can delete the link.", Toast.LENGTH_SHORT).show();
                            mHightLight.next();

                    }

                });

    }
    public static Bitmap convertStringToIcon(String st)
    {
        // OutputStream out;
        Bitmap bitmap = null;
        try
        {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
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
