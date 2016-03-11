package com.example.netimageviewer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity {

	protected static final int SUCCESS = 1;
	protected static final int FAILED = 2;
	protected static final int ERROR = 3;
	private EditText ed_path;
	private ImageView iv;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case SUCCESS:
				Bitmap bitmap = (Bitmap) msg.obj;
				iv.setImageBitmap(bitmap);
				break;
			case FAILED:
				Toast.makeText(MainActivity.this, (String)msg.obj, 0).show();
				break;
			case ERROR:
				Toast.makeText(MainActivity.this, (String)msg.obj, 0).show();
				break;
			}
			
		};
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_path = (EditText) findViewById(R.id.ed_path);
        iv = (ImageView) findViewById(R.id.iv);
    }

    public void click(View v){
    	final String  path = ed_path.getText().toString().trim();
    	new Thread(){
    		public void run() {
    			 try {
    				 URL url = new URL(path);
    				 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    				 conn.setRequestMethod("GET");
    				 conn.setConnectTimeout(5000);
    				 int code = conn.getResponseCode();
    				 if(code == 200){
    					 InputStream is =conn.getInputStream();
    					 Bitmap bitmap = BitmapFactory.decodeStream(is);
    					 Message msg =  Message.obtain();
    					 msg.what = SUCCESS;
    					 msg.obj =bitmap;
    					 handler.sendMessage(msg);
    				 }
    				 else{
    					 Message msg =  Message.obtain();
    					 msg.what = FAILED;
    					 msg.obj= "请求失败";
    					 handler.sendMessage(msg);
    				 }
					
				} catch (Exception e) {
					e.printStackTrace();
					Message msg =  Message.obtain();
					msg.what = ERROR;
					 msg.obj= "出现了异常，请检查Logcat";
					 handler.sendMessage(msg);
				}
    		};
    	}.start();
		}
    
    
}
