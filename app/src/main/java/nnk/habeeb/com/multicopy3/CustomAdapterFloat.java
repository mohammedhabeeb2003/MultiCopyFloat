package nnk.habeeb.com.multicopy3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Habeeb on 12/14/2016.
 */

public class CustomAdapterFloat extends ArrayAdapter {

    ArrayList<Items> addContact;
    LayoutInflater li;
    Context context;
    int resource;
    ViewHolder holder;

    public CustomAdapterFloat(Context context, int resource, ArrayList<Items> addContact) {
        super(context, resource,addContact);

        this.context = context;
        this.resource = resource;
        this.addContact = addContact;
        li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v  = convertView;
        holder = new ViewHolder();

        if(v==null){
            v = li.inflate(resource,null);

            holder.tv_name  = (TextView)v.findViewById(R.id.tv_lv);
            holder.btn_delte = (Button)v.findViewById(R.id.btn_delete_float);
            holder.btn_delte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button btn_delete = (Button)view;

                }
            });



            holder.tv_name.setText(addContact.get(position).getNames());
            holder.btn_delte.setId(addContact.get(position).get_id());
            v.setTag(holder);
        }
        else{
            holder = (ViewHolder)v.getTag();
        }

        return v;


    }

    static class ViewHolder{

        TextView tv_name;

        Button btn_delte;

    }

}
