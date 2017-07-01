package com.market.application.javaclass;

/**
 * Created by Administrator on 30/3/2560.
 */
public class Status {
    public static int[] statusid = new int[]{1, 2};
    public static String[] status_name = new String[]{"กำลังดำเนินการ","ชำระเงินแล้ว" };

    private int status_id;
    private String status;

    public Status(int status_id) {
        this.status_id = status_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
