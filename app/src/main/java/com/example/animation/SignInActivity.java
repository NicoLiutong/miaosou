package com.example.animation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.animation.view.CalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.waps.AppConnect;

/**
 * Created by 刘通 on 2017/9/18.
 */

public class SignInActivity extends AppCompatActivity {

    private boolean isCurrentMonth = false;

    private int dateNumber = 0;

    private List<String> mOptionDays = new ArrayList<>();
    private List<String> mSelectDays = new ArrayList<>();

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private int nowCurrentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        //editor.putInt("currentMonth",0);
        //editor.putString("selectDays","");
        //editor.apply();

        LinearLayout advanceLayout = (LinearLayout) findViewById(R.id.ADLinearLayout);
        AppConnect.getInstance(this).showBannerAd(this,advanceLayout);

        final TextView signinDateNumber = (TextView) findViewById(R.id.signin_text);
        CalendarView calendarView = (CalendarView) findViewById(R.id.signin_calendar);

        mOptionDays = initOptionDays();
        mSelectDays = initSelectDays();
        dateNumber = setDateNumber(isCurrentMonth);

        signinDateNumber.setText("本月已签到" + dateNumber + "天");
        calendarView.setSelectedDates(mSelectDays);
        calendarView.setOptionalDate(mOptionDays);
        calendarView.setOnClickDate(new CalendarView.OnClickListener() {
            @Override
            public void onClickDateListener(String selectDate) {
                String selectDays = preferences.getString("selectDays","");
                if(isCurrentMonth){
                    selectDays = selectDays + selectDate + "+";
                }else {
                      selectDays = selectDate + "+";
                }
                editor.putString("selectDays",selectDays);
                editor.apply();
                dateNumber++;
                signinDateNumber.setText("本月已签到" + dateNumber + "天");
            }
        });
    }

    private List<String> initOptionDays(){
        List<String> optionDays = new ArrayList<>();
        nowCurrentMonth = preferences.getInt("currentMonth",0);

        //获取当天日期
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        //判断当天是否是正在签到的月份
        if(nowCurrentMonth == currentMonth){
            isCurrentMonth = true;
        }else {
            isCurrentMonth = false;
            //设置签到月份
            editor.putInt("currentMonth",currentMonth);
            editor.apply();
        }

        //设置可被点击的日期
        String currentYears = String.valueOf(currentYear);
        String currentMonths;
        String currentDays;
        //判断当月是否小于10，小于10在前边加0
        if(currentMonth < 10){
            currentMonths = "0" + currentMonth;
        }else {
            currentMonths = String.valueOf(currentMonth);
        }
        //判断当天是否小于10，小于10在前边加0
        if(currentDay < 10){
            currentDays = "0" + currentDay;
        }else {
            currentDays = String.valueOf(currentDay);
        }
        optionDays.add(currentYears + currentMonths + currentDays);

        return optionDays;
    }

    private List<String> initSelectDays(){
        String[] selectDays;
        List<String> selectDayss = new ArrayList<>();
        selectDays = preferences.getString("selectDays","").split("\\+");
        for (int i = 0;i < selectDays.length;i ++){
            selectDayss.add(selectDays[i]);
        }
        return selectDayss;
    }

    private int setDateNumber(boolean isCurrentMonth){
        if(isCurrentMonth){
            return mSelectDays.size();
        }else {
            return 0;
        }
    }
}
