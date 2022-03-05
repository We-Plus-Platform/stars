package com.aeo.test.bean;

import java.util.ArrayList;

//菜单信息返回数据实体
public class MenuInfoData extends Data {
    private ArrayList<MenuList> menulist = new ArrayList<>();

    public ArrayList<MenuList> getMenulist() {
        return menulist;
    }

    public void setMenulist(ArrayList<MenuList> menulist) {
        this.menulist = menulist;
    }
}
