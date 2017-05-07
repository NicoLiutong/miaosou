package com.example.animation.Class;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘通 on 2017/3/23.
 */

public class HistorySearch extends DataSupport {

    public String searchName;

    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
}
