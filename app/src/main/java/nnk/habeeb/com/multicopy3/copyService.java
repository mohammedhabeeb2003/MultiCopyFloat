package nnk.habeeb.com.multicopy3;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Sai Kiran on 8/7/2016.
 */
public class copyService extends Service {

     String serviceclipText = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipboardManager.OnPrimaryClipChangedListener onPrimaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener(){

            @Override
            public void onPrimaryClipChanged() {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData cData = clipboard.getPrimaryClip();
                ClipData.Item item;
                serviceclipText = "";
                if(cData!=null&&cData.getItemCount() > 0)
                {
                    item = cData.getItemAt(0);
                    serviceclipText = (String) item.getText();

                    startService(new Intent(copyService.this,FloatingFaceBubbleService.class));
                }
                else
                {
                    serviceclipText = null;

                }


                SqliteHelper mydb = new SqliteHelper(getApplicationContext());

                if(serviceclipText!=null&&serviceclipText.length()>0){

                    try {
                        mydb.putString(serviceclipText);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mClipboardManager.addPrimaryClipChangedListener(onPrimaryClipChangedListener);


        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}