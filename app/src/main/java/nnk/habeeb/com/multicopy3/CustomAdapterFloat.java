package nnk.habeeb.com.multicopy3;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Habeeb on 12/14/2016.
 */

public class CustomAdapterFloat extends ArrayAdapter<Items> {

    private FloatingFaceBubbleService activity;
    private SqliteHelper databaseHelper;
    private List<Items> friendList;

    public CustomAdapterFloat(FloatingFaceBubbleService context, int resource, List<Items> objects, SqliteHelper helper) {
        super(context, resource, objects);
        this.activity = context;
        this.databaseHelper = helper;
        this.friendList = objects;
    }
 @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_listview_float, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
try {
    holder.name.setText(getItem(position).getNames());
}
catch (NullPointerException e){

}
        //Delete an item
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.deleteFriend(getItem(position));
                activity.onDismiss();//delete in db
                    new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        activity.reloadinglistview();
                    }
                }, 200);


                //reload the database to view



            }
        });

        //Edit/Update an item
      /*  holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Update a Friend");

                LinearLayout layout = new LinearLayout(activity);
                layout.setPadding(10, 10, 10, 10);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText nameBox = new EditText(activity);
                nameBox.setHint("Name");
                layout.addView(nameBox);

                final EditText jobBox = new EditText(activity);
                jobBox.setHint("job");
                layout.addView(jobBox);

                nameBox.setText(getItem(position).getNames());


                alertDialog.setView(layout);

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Items friend = new Items(nameBox.getText().toString());
                        friend.set_id(getItem(position).get_id());
                        databaseHelper.updateFriend(friend); //update to db
                        Toast.makeText(activity, "Updated!", Toast.LENGTH_SHORT).show();

                        //reload the database to view
                        activity.reloadinglistview();
                    }
                });

                alertDialog.setNegativeButton("Cancel", null);

                //show alert
                alertDialog.show();
            }
        });

        //show details when each row item clicked
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Friend ");

                LinearLayout layout = new LinearLayout(activity);
                layout.setPadding(10, 10, 10, 10);
                layout.setOrientation(LinearLayout.VERTICAL);

                TextView nameBox = new TextView(activity);
                layout.addView(nameBox);

                TextView jobBox = new TextView(activity);
                layout.addView(jobBox);

                nameBox.setText("Friend name: " + getItem(position).getNames());


                alertDialog.setView(layout);
                alertDialog.setNegativeButton("OK", null);

                //show alert
                alertDialog.show();
            }
        });*/

        return convertView;
    }

    private static class ViewHolder {
        private TextView name;
        private View btnDelete;
        /*private View btnEdit;*/

        public ViewHolder (View v) {
            name = (TextView)v.findViewById(R.id.tv_lv_float);
            btnDelete = v.findViewById(R.id.btn_delete_float);
           /* btnEdit = v.findViewById(R.id.btn_edit_float);*/
        }
    }

}
