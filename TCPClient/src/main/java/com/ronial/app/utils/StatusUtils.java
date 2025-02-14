package com.ronial.app.utils;

public class StatusUtils {
    private boolean status;
    public StatusUtils(boolean status) {
        this.status = status;
    }
    public StatusUtils(){
        this.status = false;
    }
    public void changeStatus(boolean status) {
        this.status = status;
    }
    public void changeStatus(){
        this.status = !this.status;
    }
    public boolean isTrue(){
        return status;
    }

}
