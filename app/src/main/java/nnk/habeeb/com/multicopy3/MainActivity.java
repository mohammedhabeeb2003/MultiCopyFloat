package nnk.habeeb.com.multicopy3;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

public class MainActivity extends AppCompatActivity{

    SqliteHelper mSqliteHelper;
    EditText mEdit_text;

    ListView lv;
    ArrayList<Items> contact_data = new ArrayList<Items>();
    CustomAdapter mCustomAdapter;

    List<Items> contact_array_from_db;
    ToggleButton tg_on_off;
    TinyDB mytydb;

    int count=0;
    Button bt_clear;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.list_view);
        bt_clear = (Button)findViewById(R.id.clear);
        tg_on_off = (ToggleButton) findViewById(R.id.power_button);
        mytydb = new TinyDB(this);
        contact_array_from_db = new ArrayList<>();
        startService(new Intent(this, copyService.class));
        mSqliteHelper = new SqliteHelper(this);


        reloadingDatabase();

        if (isMyServiceRunning(FloatingFaceBubbleService.class)) {


            tg_on_off.setChecked(true);
        } else {
            tg_on_off.setChecked(false);
        }

        tg_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

    }

    //Inserting Data in Database
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void copy(View view) {

        String name = mEdit_text.getText().toString().trim();

        Items item = new Items(name);
        mEdit_text.setText("");

        try {
            mSqliteHelper.addItems(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Lable",name);
        clipboard.setPrimaryClip(clip);*/
        reloadingDatabase();
        Toast.makeText(this, "ItemAdded", Toast.LENGTH_SHORT).show();
    }

    //getting Items from Database...
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void reloadingDatabase() {
        contact_array_from_db = mSqliteHelper.getAllFriends();
        if (contact_array_from_db.size() == 0) {
            Toast.makeText(this, "No record found in database!", Toast.LENGTH_SHORT).show();

        }


        mCustomAdapter = new CustomAdapter(this, R.layout.custom_list_view, contact_array_from_db, mSqliteHelper);
        lv.setAdapter(mCustomAdapter);
        count = mCustomAdapter.getCount();
         bt_clear.setText(String.valueOf(count));

        bt_clear.setTextSize(20);
        bt_clear.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
          if(mCustomAdapter.getCount()==0){
              count = mCustomAdapter.getCount();
              Log.e("Count","="+String.valueOf(count));}


    }

    public void CheckBox(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            requestPermmission();
            startService(new Intent(this, FloatingFaceBubbleService.class));
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else {
            stopService(new Intent(this, FloatingFaceBubbleService.class));

        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onResume() {
        super.onResume();
        reloadingDatabase();
        /*boolean emptyAdapter = mCustomAdapter.isEmpty();
        if(emptyAdapter==false){
             mCustomAdapter.notifyDataSetChanged();
        }*/
        try {
            mCustomAdapter.notifyDataSetChanged();
        } catch (Exception e) {
           Log.e("Adapter","Error="+e);
        }
        if (isMyServiceRunning(FloatingFaceBubbleService.class)) {

          tg_on_off.setChecked(true);
        } else {
            tg_on_off.setChecked(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onRestart() {
        super.onRestart();
        reloadingDatabase();

        mCustomAdapter.notifyDataSetChanged();
        if (isMyServiceRunning(FloatingFaceBubbleService.class)) {


            tg_on_off.setChecked(true);
        } else {
            tg_on_off.setChecked(false);
        }
    }

    public void clearDatabase(View view) {

        if (!mSqliteHelper.GetListItem().isEmpty()) {
            new AlertDialog.Builder(this).setTitle("DeleteAll").setMessage("Are You Sure ? Data Cannot Be Retrived").setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mSqliteHelper.cleanDB();
                    reloadingDatabase();
                }
            }).setPositiveButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();


            mCustomAdapter.notifyDataSetChanged();
        }
    }

    public void requestPermmission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivity(i);
            }
        }

    }

    public void ToggleCheckBox(View view) {
        boolean checked = ((ToggleButton) view).isChecked();
        if (checked) {
            requestPermmission();
            startService(new Intent(this, FloatingFaceBubbleService.class));

        } else {
            stopService(new Intent(this, FloatingFaceBubbleService.class));
        }
    }
}


