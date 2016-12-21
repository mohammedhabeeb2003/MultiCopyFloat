package nnk.habeeb.com.multicopy3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;

public class FloatingFaceBubbleService extends Service {
    private WindowManager windowManager;
    private ImageView floatingFaceBubble;
    ArrayList aa;
    Binder binder;
    SqliteHelper mydb;


    public void onCreate() {

      mydb = new SqliteHelper(this);
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        super.onCreate();
        floatingFaceBubble = new ImageView(this);

        //a face floating bubble as imageView
        floatingFaceBubble.setImageResource(R.drawable.logo_new);


        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        //here is all the science of params
        final LayoutParams myParams = new LayoutParams(

                LayoutParams.TYPE_PHONE,
                LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x=0;
        myParams.y=100;
        myParams.width = 150;
        myParams.height =150;
        // add a floatingfacebubble icon in window
        windowManager.addView(floatingFaceBubble, myParams);
        try{
            //for moving the picture on touch and slide
            floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {
                LayoutParams paramsT = myParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //remove face bubble on long press
                    if(System.currentTimeMillis()-touchStartTime>ViewConfiguration.getLongPressTimeout() && initialTouchX== event.getX()){
                        windowManager.removeView(floatingFaceBubble);
                        stopSelf();
                        return false;
                    }
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            touchStartTime = System.currentTimeMillis();
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, myParams);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

        floatingFaceBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               aa = mydb.GetListItem();
                initiatepopupwindow(floatingFaceBubble);

            }


        });
    }

    private void initiatepopupwindow(View anchor) {

        try{

            Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            final ListPopupWindow popup = new ListPopupWindow(this);
            popup.setAnchorView(anchor);
            popup.setWidth((int) (display.getWidth()));
            popup.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,aa));
            popup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String text = aa.get(i).toString();


                }
            });
             popup.show();

        }
        catch (Exception e){


        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       windowManager.removeViewImmediate(floatingFaceBubble);
    }

    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }

}