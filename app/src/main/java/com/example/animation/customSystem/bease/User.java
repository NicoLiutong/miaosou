package com.example.animation.customSystem.bease;

import cn.bmob.v3.BmobUser;

/**
 * Created by 刘通 on 2018/1/23.
 */

public class User extends BmobUser {

    private String name;

    private String sex;

    private String uesrImage;

    private String birthday;

    private String myFavority;

    private String animationFavority;

    public String getAnimationFavority() {
        return animationFavority;
    }

    public void setAnimationFavority(String animationFavority) {
        this.animationFavority = animationFavority;
    }

    public String getMyFavority() {
        return myFavority;
    }

    public void setMyFavority(String myFavority) {
        this.myFavority = myFavority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUesrImage() {
        return uesrImage;
    }

    public void setUesrImage(String uesrImage) {
        this.uesrImage = uesrImage;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
