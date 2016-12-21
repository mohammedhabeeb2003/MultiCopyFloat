package nnk.habeeb.com.multicopy3;

import android.widget.Button;

/**
 * Created by Habeeb on 12/19/2016.
 */

public class Items {
    public int _id;
    String names;
    Button btn_edit;
    Button btn_delete;

    public Button getBtn_edit() {
        return btn_edit;
    }

    public void setBtn_edit(Button btn_edit) {
        this.btn_edit = btn_edit;
    }

    public Button getBtn_delete() {
        return btn_delete;
    }

    public void setBtn_delete(Button btn_delete) {
        this.btn_delete = btn_delete;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Items() {
    }

    public Items(String names) {
        this.names = names;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }
}
