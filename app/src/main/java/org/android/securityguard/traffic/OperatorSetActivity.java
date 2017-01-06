package org.android.securityguard.traffic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.android.securityguard.R;
import org.w3c.dom.Text;

public class OperatorSetActivity extends AppCompatActivity {
    private Spinner mSelectSP;
    private String[] operators={"中国移动", "中国联通", "中国电信"};
    private ArrayAdapter mSelectAdapter;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_operator_set);

        mSharedPreferences=getSharedPreferences("config", MODE_PRIVATE);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_green));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.traffic_operator_info_setting);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
        mLeftImgV.setOnClickListener(listener);

        mSelectAdapter=new ArrayAdapter(OperatorSetActivity.this, R.layout.item_spinner_operatorset, R.id.tv_province, operators);
        mSelectSP= (Spinner) findViewById(R.id.spinner_operator_select);
        mSelectSP.setAdapter(mSelectAdapter);

        findViewById(R.id.btn_operator_finish).setOnClickListener(listener);
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences.Editor editor=mSharedPreferences.edit();
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    editor.putBoolean("isset_operator", false);
                    finish();
                    break;
                case R.id.btn_operator_finish:
                    editor.putInt("operator", mSelectSP.getSelectedItemPosition()+1);
                    editor.putBoolean("isset_operator", true);
                    editor.commit();
                    startActivity(new Intent(OperatorSetActivity.this, TrafficMonitoringActivity.class));
                    finish();
                    break;
            }
        }
    };
}
