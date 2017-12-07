package com.liujiayu.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.liujiayu.view.cirque.CirqueView;
import com.liujiayu.view.utils.Util;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);

        CirqueView mCv = (CirqueView) findViewById(R.id.cv);
//        mCv.setTemperaturemin(-30, 30); //设置温度范围  默认10-30
//        mCv.setTime(0, 60); //设置时间范围  默认10-30
        mCv.setDefault(27, 22);  //添加默认数据--注:不能超出范围
        mCv.setTxtFinishListener(new CirqueView.txtFinishListener() {
            @Override
            public void onFinish(int temperature, int time) {
                Util.showToast(MainActivity.this, temperature + "//" + time);
            }
        });

    }

}
