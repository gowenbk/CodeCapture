package com.example.scmaster.codecapture;

/**
 * Created by scmaster on 2018-01-10.
 */

public class Share {

    private int s_num;
    private String s_sender;
    private String s_receiver;
    private int s_context;
    private String s_date;
    private String c_title;

    public Share() {
        // TODO Auto-generated constructor stub
    }

    public Share(int s_num, String s_sender, String s_receiver, int s_context, String s_date, String c_title) {
        super();
        this.s_num = s_num;
        this.s_sender = s_sender;
        this.s_receiver = s_receiver;
        this.s_context = s_context;
        this.s_date = s_date;
        this.c_title = c_title;
    }

    public int getS_num() {
        return s_num;
    }

    public void setS_num(int s_num) {
        this.s_num = s_num;
    }

    public String getS_sender() {
        return s_sender;
    }

    public void setS_sender(String s_sender) {
        this.s_sender = s_sender;
    }

    public String getS_receiver() {
        return s_receiver;
    }

    public void setS_receiver(String s_receiver) {
        this.s_receiver = s_receiver;
    }

    public int getS_context() {
        return s_context;
    }

    public void setS_context(int s_context) {
        this.s_context = s_context;
    }

    public String getS_date() {
        return s_date;
    }

    public void setS_date(String s_date) {
        this.s_date = s_date;
    }

    public String getC_title() {
        return c_title;
    }

    public void setC_title(String c_title) {
        this.c_title = c_title;
    }

    @Override
    public String toString() {
        return "Share [s_num=" + s_num + ", s_sender=" + s_sender + ", s_receiver=" + s_receiver + ", s_context="
                + s_context + ", s_date=" + s_date + ", c_title=" + c_title + "]";
    }

}
