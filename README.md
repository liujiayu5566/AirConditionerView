# 介绍 #
空调调机器(圆环双方向控制温度、时间)

**先一下UI提供的效果**<br>
![](https://i.imgur.com/MteZZ9w.jpg)<br>

**最终效果**<br>
![](https://i.imgur.com/Qp2RAH6.gif)


# 代码实现 #
# res/values/attr.xml<br>
自定义属性: #

	<?xml version="1.0" encoding="utf-8"?>
	<resources>

    <declare-styleable name="CirqueView">
        <attr name="temperature_min" format="integer">10</attr>
        <attr name="temperature_max" format="integer">30</attr>
        <attr name="time_left" format="integer">10</attr>
        <attr name="time_right" format="integer">30</attr>
        <attr name="time_direction" format="boolean">true</attr>
    </declare-styleable>

	</resources>

简单介绍一下:<br>
**temperature_min:**	温度范围最小值.<br>
**temperature_max:**	温度范围最大值.<br>
**time_left:**	时间范围左侧数值.<br>
**time_right:**	时间范围右侧数值.<br>
**time_direction:**	时间轴滑动方向.<br>

UI设置上,滑动方向都是从左到右的.个人感觉下方滑动改为从右向左会好看一些.所以添加了time_direction参数,来控制下方的滑动方向.


# 调用方法 #

	CirqueView mCv = (CirqueView) findViewById(R.id.cv);
	//        mCv.setTemperaturemin(-30, 30); //设置温度范围
	//        mCv.setTime(0, 60); //设置时间范围
	//        mCv.setTimeaspect(true);  //设置下方滑动方向 true从左到右  false从右到左
        mCv.setTxtFinishListener(new CirqueView.txtFinishListener() {
            @Override
            public void onFinish(String temperature, String time) {
                Util.showToast(MainActivity.this, temperature + "//" + time);
            }
        });
回调返回String类型数值.

# 总结 #

完成时间大概是3天时间,之前对自定义View这块很薄弱,相关API记忆不是很深,查了一些相关博客,对自己有很大帮助.还是多撸撸代码成长很大(撸代码上班时间过的特别快^_^!).


**文章地址:**<br>
[http://blog.csdn.net/u012401802/article/details/78543322](http://blog.csdn.net/u012401802/article/details/78543322)<br>
**博客地址:**<br>
[http://blog.csdn.net/u012401802/卡斯迪奥-北京](http://blog.csdn.net/u012401802 "卡斯迪奥-北京")<br>


