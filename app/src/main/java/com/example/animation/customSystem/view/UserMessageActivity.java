package com.example.animation.customSystem.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.animation.R;
import com.example.animation.customSystem.bease.User;
import com.example.animation.customSystem.presenter.UserMessagePresent;
import com.example.animation.db.ComicMessageItem;
import com.example.animation.view.LineEditText;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserMessageActivity extends BaseActivity<UserMessageView,UserMessagePresent> implements UserMessageView,View.OnClickListener {

    private TextView tvTitle;
    private RelativeLayout rlUserPicture;
    private CircleImageView civUserPicture;
    private RelativeLayout rlUserName;
    private TextView tvUserName;
    private RelativeLayout rlUserSex;
    private TextView tvUserSex;
    private RelativeLayout rlUserBirthday;
    private TextView tvUserBirthday;
    private RelativeLayout rlUserChangePassword;
    private RelativeLayout rlUserLogout;
    private ProgressDialog dialog;

    private LineEditText newUsername;
    private Button changeUsernameAffirm;

    private Button dateSelectButton;
    private WheelView yearWheelView,mounthWheelView,dayWheelView;
    private String year = "1960",mounth = "1",day = "1";

    private LineEditText oldPassword,newPassword;
    private Button changePasswordAffirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        tvTitle = (TextView) findViewById(R.id.basic_texttitle);
        tvTitle.setText("个人信息");
        rlUserPicture = (RelativeLayout) findViewById(R.id.user_message_picture_layout);
        civUserPicture = (CircleImageView) findViewById(R.id.user_message_picture);
        rlUserName = (RelativeLayout) findViewById(R.id.user_message_name_layout);
        tvUserName = (TextView) findViewById(R.id.user_message_name);
        rlUserSex = (RelativeLayout) findViewById(R.id.user_message_sex_layout);
        tvUserSex = (TextView) findViewById(R.id.user_message_sex);
        rlUserBirthday = (RelativeLayout) findViewById(R.id.user_message_birthday_layout);
        tvUserBirthday = (TextView) findViewById(R.id.user_message_birthday);
        rlUserChangePassword = (RelativeLayout) findViewById(R.id.user_message_change_password_layout);
        rlUserLogout = (RelativeLayout) findViewById(R.id.user_message_logout_layout);

        rlUserPicture.setOnClickListener(this);
        rlUserName.setOnClickListener(this);
        rlUserSex.setOnClickListener(this);
        rlUserBirthday.setOnClickListener(this);
        rlUserChangePassword.setOnClickListener(this);
        rlUserLogout.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        User user = BmobUser.getCurrentUser(User.class);
        Glide.with(this).load(user.getUesrImage()).into(civUserPicture);
        tvUserName.setText(user.getName());
        tvUserSex.setText(user.getSex());
        tvUserBirthday.setText(user.getBirthday());
    }

    @Override
    public UserMessagePresent initPresenter() {
        return new UserMessagePresent();
    }

    @Override
    public void onStartChangeMessage() {
        dialog = ProgressDialog.show(this, null, "正在更新，请稍后…", true, false);
    }

    @Override
    public void onUserNameChangeSuccess(String name) {
        tvUserName.setText(name);
    }

    @Override
    public void onUserSexChangeSuccess(String sex) {
        tvUserSex.setText(sex);
    }

    @Override
    public void onUserBirthdayChangeSuccess(String birthday) {
        tvUserBirthday.setText(birthday);
    }

    @Override
    public void onUserChangePassword() {
        Toast.makeText(this,"密码修改成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailChangeMossage(BmobException e) {
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailChangeMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEndChangeMessgae() {
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_message_picture_layout:
                changeUserPicture();
                break;
            case R.id.user_message_name_layout:
                changeUserName();
                break;
            case R.id.user_message_sex_layout:
                changeUserSex();
                break;
            case R.id.user_message_birthday_layout:
                changeUserBirthday();
                break;
            case R.id.user_message_change_password_layout:
                changeUserPassword();
                break;
            case R.id.user_message_logout_layout:
                BmobUser.logOut();
                Intent logout = new Intent(this,LoginActivity.class);
                DataSupport.deleteAll(ComicMessageItem.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(logout);
                finish();
                break;
        }
    }

    private void changeUserPicture(){
        Intent intent = new Intent(this,ChangeUserPictureActivity.class);
        this.startActivity(intent);
    }

    private void changeUserName(){
        AlertDialog.Builder changeUsername = new AlertDialog.Builder(this, R.style.MyDialog);
        changeUsername.setCancelable(true);
        View changeUsernameView = View.inflate(this,R.layout.change_username_layout,null);
        changeUsername.setView(changeUsernameView);
        final AlertDialog dialog = changeUsername.create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        changeUsernameAffirm = (Button) changeUsernameView.findViewById(R.id.change_username_affirm_button);
        newUsername = (LineEditText) changeUsernameView .findViewById(R.id.change_username_newusername);
        changeUsernameAffirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.changeUserName(newUsername.getText().toString());
            }
        });
        dialog.show();
    }

    private void changeUserSex(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialog);
        final String[] sexs = {"男","女","未知"};
        builder.setItems(sexs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.changeUserSex(sexs[which]);
            }
        });
        builder.show();
    }

    private void changeUserBirthday(){
        AlertDialog.Builder birthdayBuider = new AlertDialog.Builder(this, R.style.MyDialog);
        birthdayBuider.setCancelable(true);
        View selectView = View.inflate(this,R.layout.select_date_wheel,null);
        birthdayBuider.setView(selectView);
        final AlertDialog dialog = birthdayBuider.create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dateSelectButton = (Button) selectView.findViewById(R.id.affirm_button);
        dateSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.changeUserBirthday(year+"-"+mounth+"-"+day);
            }
        });
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextSize = 16;
        style.textSize = 13;
        style.selectedTextColor = ContextCompat.getColor(this, R.color.colorAccent);

        yearWheelView = (WheelView) selectView.findViewById(R.id.year_wheelview);
        yearWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        yearWheelView.setStyle(style);
        yearWheelView.setExtraText("年", ContextCompat.getColor(this, R.color.colorAccent),40,100);
        yearWheelView.setSkin(WheelView.Skin.None);
        yearWheelView.setWheelData(creatYear());
        yearWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                year = creatYear().get(position);
                dayWheelView.setWheelData(creatDay(year,mounth));
            }
        });

        mounthWheelView = (WheelView) selectView.findViewById(R.id.mounth_wheelview);
        mounthWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        mounthWheelView.setStyle(style);
        mounthWheelView.setExtraText("月", ContextCompat.getColor(this, R.color.colorAccent),40,60);
        mounthWheelView.setSkin(WheelView.Skin.None);
        mounthWheelView.setLoop(true);
        mounthWheelView.setWheelData(creatMounth());
        mounthWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mounth = creatMounth().get(position);
                dayWheelView.setWheelData(creatDay(year,mounth));
            }
        });

        dayWheelView = (WheelView) selectView.findViewById(R.id.day_wheelview);
        dayWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        dayWheelView.setStyle(style);
        dayWheelView.setExtraText("日", ContextCompat.getColor(this, R.color.colorAccent),40,60);
        dayWheelView.setSkin(WheelView.Skin.None);
        dayWheelView.setLoop(true);
        dayWheelView.setWheelData(creatDay(year,mounth));
        dayWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                day = creatDay(year,mounth).get(position);
            }
        });
        dialog.show();
    }

    private void changeUserPassword(){
        AlertDialog.Builder changePassowrd = new AlertDialog.Builder(this, R.style.MyDialog);
        changePassowrd.setCancelable(true);
        View changePasswordView = View.inflate(this,R.layout.change_password_layout,null);
        changePassowrd.setView(changePasswordView);
        final AlertDialog dialog = changePassowrd.create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        changePasswordAffirm = (Button) changePasswordView.findViewById(R.id.change_password_affirm_button);
        oldPassword = (LineEditText) changePasswordView.findViewById(R.id.change_password_oldpassword);
        newPassword = (LineEditText) changePasswordView.findViewById(R.id.change_password_newpassword);
        changePasswordAffirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.changeUserPassword(oldPassword.getText().toString(),newPassword.getText().toString());
            }
        });
        dialog.show();
    }

    private List<String> creatYear(){
        List<String> year = new ArrayList<>();
        for(int i = 1960;i < 2051 ; i++){
            year.add(String.valueOf(i));
        }
        return year;
    }

    private List<String> creatMounth(){
        List<String> mounth = new ArrayList<>();
        for(int i = 1; i < 13;i++){
            mounth.add(String.valueOf(i));
        }
        return mounth;
    }

    private List<String> creatDay(String year,String mounth){
        int years = Integer.parseInt(year);
        int mounths = Integer.parseInt(mounth);
        List<String> day30 = new ArrayList<>();
        List<String> day31 = new ArrayList<>();
        List<String> day28 = new ArrayList<>();
        List<String> day29 = new ArrayList<>();

        for(int i = 1;i < 31; i++){
            day30.add(String.valueOf(i));
        }
        for(int i = 1;i < 32; i++){
            day31.add(String.valueOf(i));
        }

        for(int i = 1;i < 30; i++){
            day29.add(String.valueOf(i));
        }

        for(int i = 1;i < 29; i++){
            day28.add(String.valueOf(i));
        }

        if(years%4 == 0){
            switch (mounths){
                case 1:case 3:case 5:case 7:case 8:case 10:case 12:
                    return day31;
                case 2:
                    return day29;
                default:
                    return day30;
            }
        }else {
            switch (mounths){
                case 1:case 3:case 5:case 7:case 8:case 10:case 12:
                    return day31;
                case 2:
                    return day28;
                default:
                    return day30;
            }
        }
    }
}
