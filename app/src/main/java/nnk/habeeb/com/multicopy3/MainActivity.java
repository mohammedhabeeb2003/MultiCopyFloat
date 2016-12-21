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
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    SqliteHelper mSqliteHelper;
    EditText mEdit_text;
    ArrayList<String> onlyText;
    ListView lv;
    ArrayList<Items> contact_data = new ArrayList<Items>();
    CustomAdapter mCustomAdapter;
    CheckBox serviceCheckBox;
    List<Items> contact_array_from_db;
    TinyDB mytydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdit_text =(EditText)findViewById(R.id.editText);
        lv = (ListView)findViewById(R.id.list_view);
        serviceCheckBox = (CheckBox)findViewById(R.id.ActivateCheckBox);
        mytydb = new TinyDB(this);
        contact_array_from_db = new ArrayList<>();
        startService(new Intent(this,copyService.class));
        mSqliteHelper = new SqliteHelper(this);
       reloadingDatabase();

        if (isMyServiceRunning(FloatingFaceBubbleService.class )){

            serviceCheckBox.setChecked(true);
        }else { serviceCheckBox.setChecked(false);}

    }
    //Inserting Data in Database
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
    public void reloadingDatabase() {
        contact_array_from_db = mSqliteHelper.getAllFriends();
        if (contact_array_from_db.size() == 0) {
            Toast.makeText(this, "No record found in database!", Toast.LENGTH_SHORT).show();

        }
       mCustomAdapter = new CustomAdapter(this, R.layout.custom_list_view, contact_array_from_db, mSqliteHelper);
        lv.setAdapter(mCustomAdapter);

    }
    public void CheckBox(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if (checked){
            requestPermmission();
            startService(new Intent(this,FloatingFaceBubbleService.class));
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }else{
            stopService(new Intent(this,FloatingFaceBubbleService.class));

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
        }
        catch (Exception e){

        }
        if (isMyServiceRunning(FloatingFaceBubbleService.class )){

            serviceCheckBox.setChecked(true);
        }else { serviceCheckBox.setChecked(false);}
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reloadingDatabase();

            mCustomAdapter.notifyDataSetChanged();
        if (isMyServiceRunning(FloatingFaceBubbleService.class )){

            serviceCheckBox.setChecked(true);
        }else { serviceCheckBox.setChecked(false);}
    }

    public void clearDatabase(View view) {

        if (!mSqliteHelper.GetListItem().isEmpty()) {
            new AlertDialog.Builder(this).setTitle("DeleteAll").setMessage("Are You Sure ? Data Cannot Be Retrived").setNegativeButton("Delete", new DialogInterface.OnClickListener() {
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

    public void requestPermmission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(this)){
                Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
                startActivity(i);
            }
        }

    }

}


