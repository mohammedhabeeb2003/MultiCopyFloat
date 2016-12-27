package nnk.habeeb.com.multicopy3;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.List;


public class FloatingFaceBubbleService extends Service {
    private WindowManager windowManager;
    private ImageView floatingFaceBubble;
    List<Items> contact_array_from_db;
    Binder binder;
    SqliteHelper mydb;
    CustomAdapterFloat mCustomAdapter;
    PopupWindow mPopupWindow;
    ListPopupWindow popup;
    LayoutParams myParams;

    public void onCreate() {

        mydb = new SqliteHelper(this);
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        super.onCreate();
        floatingFaceBubble = new ImageView(this);

        //a face floating bubble as imageView
        floatingFaceBubble.setImageResource(R.drawable.logo);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //here is all the science of params
        myParams = new LayoutParams(

                LayoutParams.TYPE_PHONE,
                LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x = 0;
        myParams.y = 100;
        myParams.width = 150;
        myParams.height = 150;
        // add a floatingfacebubble icon in window
        try {
            windowManager.addView(floatingFaceBubble, myParams);
        } catch (Exception e) {

            Log.e("Windows Manager", "Error", e);
        }

        try {
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
                    if (System.currentTimeMillis() - touchStartTime > ViewConfiguration.getLongPressTimeout() && initialTouchX == event.getX()) {
                        windowManager.removeView(floatingFaceBubble);
                        stopSelf();
                        return false;
                    }
                    switch (event.getAction()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        floatingFaceBubble.setOnClickListener(new View.OnClickListener() {
            boolean toggle = true;
            @Override
            public void onClick(View view) {
                setFloatView();
                if(toggle){
                    reloadinglistview();
                    initiatepopupwindow(floatingFaceBubble);
                    toggle = false;
                }
                else{
                    if(popup != null && popup.isShowing())
                        popup.dismiss();
                    toggle = true;
                }




            }


        });
    }

    private void initiatepopupwindow(View anchor) {


        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        popup = new ListPopupWindow(this);

        popup.setAnchorView(anchor);

        popup.setWidth((int) (display.getWidth()));
        mCustomAdapter = new CustomAdapterFloat(this, R.layout.custom_listview_float, contact_array_from_db, mydb);
        popup.setAdapter(mCustomAdapter);
        popup.setAnimationStyle(R.style.Animation);
        popup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = contact_array_from_db.get(i).toString();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData item = ClipData.newPlainText("Copy", text);
                clipboardManager.setPrimaryClip(item);
                Toast.makeText(FloatingFaceBubbleService.this, "Text Copied", Toast.LENGTH_SHORT).show();

            }
        });
        popup.show();


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            windowManager.removeViewImmediate(floatingFaceBubble);
        } catch (Exception e) {
            Log.e("RemoveView","WindowsManager(FloatingBubble)",e);
        }
    }



    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }
    public void reloadinglistview() {
        contact_array_from_db = mydb.getAllFriends();
        if (contact_array_from_db.size() == 0) {
            Toast.makeText(this, "No record found in database!", Toast.LENGTH_SHORT).show();

        }
        if(mCustomAdapter!=null) {


            initiatepopupwindow(floatingFaceBubble);
            mCustomAdapter.notifyDataSetChanged();
        }

    }
    public void setFloatView(){


        LayoutParams paramsT = myParams;
        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x=0;
        myParams.y=100;
        windowManager.updateViewLayout(floatingFaceBubble, paramsT);

    }

    public boolean onDismiss(){

        if(popup.isShowing()) {
            popup.dismiss();
            mCustomAdapter.notifyDataSetChanged();
            return true;
        }
        else {
            return false;
        }
    }
    public void onDismising(){
        popup.dismiss();
        mCustomAdapter.clear();
    }

}