package nnk.habeeb.com.multicopy3;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

public class MainActivity extends AppCompatActivity {

    SqliteHelper mSqliteHelper;
    EditText mEdit_text;
    ArrayList<String> onlyText;
    ListView lv;
    ArrayList<Items> contact_data = new ArrayList<Items>();
    CustomAdapter mCustomAdapter;
    CheckBox serviceCheckBox;
    ArrayList<Items>  contact_array_from_db;
    TinyDB mytydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdit_text =(EditText)findViewById(R.id.editText);
        lv = (ListView)findViewById(R.id.list_view);
        serviceCheckBox = (CheckBox)findViewById(R.id.ActivateCheckBox);
        mytydb = new TinyDB(this);
        startService(new Intent(this,copyService.class));
        mSqliteHelper = new SqliteHelper(this);
        getItemList();

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
            mSqliteHelper.putList(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Lable",name);
        clipboard.setPrimaryClip(clip);*/
        getItemList();
        Toast.makeText(this, "ItemAdded", Toast.LENGTH_SHORT).show();
    }
    //getting Items from Database...
    public void getItemList() {

        contact_data.clear();
        mSqliteHelper = new SqliteHelper(this);

        contact_array_from_db = mSqliteHelper.Get_Contacts();

        for (int i = 0; i < contact_array_from_db.size(); i++) {


            String name = contact_array_from_db.get(i).getNames();

            Items item = new Items();

            item.setNames(name);


            contact_data.add(item);
            mSqliteHelper.close();
            mCustomAdapter = new CustomAdapter(this, R.layout.custom_list_view,
                    contact_data);
            lv.setAdapter(mCustomAdapter);
            mCustomAdapter.notifyDataSetChanged();
        }
    }
    public void CheckBox(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if (checked){

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
        getItemList();
        /*boolean emptyAdapter = mCustomAdapter.isEmpty();
        if(emptyAdapter==false){
             mCustomAdapter.notifyDataSetChanged();
        }*/
        try {
            mCustomAdapter.notifyDataSetChanged();
        }
        catch (Exception e){

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getItemList();

            mCustomAdapter.notifyDataSetChanged();

    }

    public void clearDatabase(View view) {


        mSqliteHelper.cleanDB();
        getItemList();

        mCustomAdapter.notifyDataSetChanged();
    }


}


