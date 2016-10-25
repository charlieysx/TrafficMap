package com.elims.trafficmap.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elims.trafficmap.R;

/**
 * Created by elims on 16/10/23.
 */

public class AddDialog extends AlertDialog implements View.OnClickListener {

    private Activity activity;
    private EditText et_location;
    private EditText et_add_info;
    private TextView tv_add;

    public AddDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_add_route);

        et_location = (EditText) findViewById(R.id.et_location);
        et_add_info = (EditText) findViewById(R.id.et_add_info);
        tv_add = (TextView) findViewById(R.id.tv_add);
        et_location.setEnabled(false);

        tv_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_add:
                String info = et_add_info.getText().toString();
                if(info.equals("")) {
                    Toast.makeText(activity, "请输入记录", Toast.LENGTH_SHORT).show();
                } else {
                    dismiss();
                    Toast.makeText(activity, "提交完成", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
