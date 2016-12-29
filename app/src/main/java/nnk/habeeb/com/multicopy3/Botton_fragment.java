package nnk.habeeb.com.multicopy3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class Botton_fragment extends Fragment {

    Button btn_home,btn_setting;
    public Botton_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_botton_fragment, container, false);
        btn_home = (Button)v.findViewById(R.id.button_home);
        btn_setting = (Button)v.findViewById(R.id.btn_setting);

        // Inflate the layout for this fragment
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = new SettingFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft =fm.beginTransaction();
            /*    ft.remove();*/
                ft.replace(R.id.rl_list_view,f);
                ft.commit();
            }
        });
        return v;
    }

}
