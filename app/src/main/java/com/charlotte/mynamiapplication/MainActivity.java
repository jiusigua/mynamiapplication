package com.charlotte.mynamiapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static Toast mToast=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitle("我的应用作品集");
        setSupportActionBar(mToolbar);
    }

    public void btnClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                show(R.string.toast_button2);
                Intent movieIntent =  new Intent(MainActivity.this, MovieMainActivity.class);
                startActivity(movieIntent);
                break;
            case R.id.button3:
                show(R.string.toast_button3);
                break;
            case R.id.button4:
                show(R.string.toast_button4);
                break;
            case R.id.button5:
                show(R.string.toast_button5);
                break;
            case R.id.button6:
                show(R.string.toast_button6);
                break;
        }
    }

    private void show(int resId){
        if(mToast == null){
            mToast = Toast.makeText(this,resId,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(resId);
        }
        mToast.show();
    }
}
