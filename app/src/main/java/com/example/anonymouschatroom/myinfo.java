package com.example.anonymouschatroom;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

public class myinfo extends AppCompatActivity {

    private static final int TAKE_PHOTO = 11;// 拍照
    private static final int CROP_PHOTO = 12;// 裁剪图片
    private static final int LOCAL_CROP = 13;// 本地图库
    private Uri imageUri;// 拍照时的图片uri
    private ImageView btn_choose_picture;
    private ImageView iv_show_picture;
    private int ident=0;
    private String name="UNLOAD";
    private  String mFileName="chatting.txt";
    private Bitmap bitmap;
    private HighLight mHightLight;
    private int Count11;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);


        btn_choose_picture = findViewById(R.id.btn_start1);
        iv_show_picture = findViewById(R.id.head);
        setListeners();// 设置监听
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        ident= getIntent().getIntExtra("ident",1);

        if(ident==1){
            name=getIntent().getStringExtra("name");
            mFileName=name+".txt";
        }
        String enterinfo=read1();
        int p1=enterinfo.indexOf("page1:");
        int p2=enterinfo.indexOf("page2:");
        String  enter=enterinfo.substring(p1+6,p2-1);
        int enter0= Integer.parseInt(String.valueOf(enter));
        if(enter0==0){
            showNextTipView();
            enter0=1;
            enterinfo=enterinfo.substring(0,p1+6)+String.valueOf(enter0)+enterinfo.substring(p2-1,enterinfo.length());
            save1(enterinfo);
        }


        btn_choose_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent mIntent = new Intent(getBaseContext(), playing.class);
                // startActivity(mIntent);
                String i=mFileName;

                String image=convertIconToString(bitmap);
                String allinfo=read1();
                int im=allinfo.indexOf("image:");
                int na=allinfo.indexOf("name:");
                allinfo=allinfo.substring(0,im+6)+image+allinfo.substring(na-1,allinfo.length());
                save1(allinfo);


                Intent intent=new Intent(getApplicationContext(),seeking.class);
                Bundle bundle =new Bundle();
                bundle.putString("name",name);
                bundle.putInt("ident",ident);
                bundle.putInt("image",1);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);


            }
        });



    }


    private void setListeners() {

        // 展示图片按钮点击事件
        iv_show_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takePhotoOrSelectPicture();// 拍照或者调用图库

            }
        });

    }

    /**
     *
     */
    private void takePhotoOrSelectPicture() {
        CharSequence[] items = {"Photograph","Album "};// 裁剪items选项

        // 弹出对话框提示用户拍照或者是通过本地图库选择图片
        new AlertDialog.Builder(myinfo.this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            // 选择了拍照
                            case 0:
                                // 创建文件保存拍照的图片
                                File takePhotoImage = new File(Environment.getExternalStorageDirectory(), "take_photo_image.jpg");
                                try {
                                    // 文件存在，删除文件
                                    if(takePhotoImage.exists()){
                                        takePhotoImage.delete();
                                    }
                                    // 根据路径名自动的创建一个新的空文件
                                    takePhotoImage.createNewFile();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                // 获取图片文件的uri对象
                                imageUri = Uri.fromFile(takePhotoImage);
                                // 创建Intent，用于启动手机的照相机拍照
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 指定输出到文件uri中
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                                // 启动intent开始拍照
                                startActivityForResult(intent,TAKE_PHOTO);
                                break;
                            // 调用系统图库
                            case 1:

                                // 创建Intent，用于打开手机本地图库选择图片
                                Intent intent1 = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                // 启动intent打开本地图库
                                startActivityForResult(intent1,LOCAL_CROP);
                                break;

                        }

                    }
                }).show();
    }


    /**
     * 调用startActivityForResult方法启动一个intent后，
     * 可以在该方法中拿到返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){

            case TAKE_PHOTO:// 拍照

                if(resultCode == RESULT_OK){
                    // 创建intent用于裁剪图片
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    // 设置数据为文件uri，类型为图片格式
                    intent.setDataAndType(imageUri,"image/*");
                    // 允许裁剪
                    intent.putExtra("scale",true);
                    // 指定输出到文件uri中
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    // 启动intent，开始裁剪
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case LOCAL_CROP:// 系统图库

                if(resultCode == RESULT_OK){
                    // 创建intent用于裁剪图片
                    Intent intent1 = new Intent("com.android.camera.action.CROP");
                    // 获取图库所选图片的uri
                    Uri uri = data.getData();
                    imageUri =uri;
                    intent1.setDataAndType(uri,"image/*");
                    //  设置裁剪图片的宽高
                    intent1.putExtra("outputX", 300);
                    intent1.putExtra("outputY", 300);
                    // 裁剪后返回数据
                    intent1.putExtra("return-data", true);
                    // 启动intent，开始裁剪
                    startActivityForResult(intent1, CROP_PHOTO);
                }

                break;
            case CROP_PHOTO:// 裁剪后展示图片
                if(resultCode == RESULT_OK){
                    try{
                        // 展示拍照后裁剪的图片
                        if(imageUri != null){
                            // 创建BitmapFactory.Options对象
                            BitmapFactory.Options option = new BitmapFactory.Options();
                            // 属性设置，用于压缩bitmap对象
                            option.inSampleSize = 2;
                            option.inPreferredConfig = Bitmap.Config.RGB_565;
                            // 根据文件流解析生成Bitmap对象
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, option);
                            // 展示图片
                            iv_show_picture.setImageBitmap(bitmap);
                        }

                        // 展示图库中选择裁剪后的图片
                        if(data != null){
                            // 根据返回的data，获取Bitmap对象
                            bitmap = data.getExtras().getParcelable("data");
                            // 展示图片
                            iv_show_picture.setImageBitmap(bitmap);

                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;

        }

    }

    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }



    private boolean isBoolean=false;
    public  void showNextTipView(){
        mHightLight = new HighLight(myinfo.this)//
                //.anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
                .autoRemove(false)
                .enableNext()
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        //界面布局完成添加tipview
                        if(!isBoolean){
                            isBoolean=true;
                            mHightLight.addHighLight(R.id.head,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());
                            mHightLight.addHighLight(R.id.btn_start1,R.layout.layout_01,new OnTopPosCallback(),new CircleLightShape());

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
                            Toast.makeText(myinfo.this, "Click here to change the avatar.", Toast.LENGTH_SHORT).show();
                            mHightLight.next();
                            Count11++;
                        }else{
                            Toast.makeText(myinfo.this, "Click here to save the modified information.", Toast.LENGTH_SHORT).show();
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
