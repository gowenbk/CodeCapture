package com.example.scmaster.codecapture;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by scmaster on 2018-01-08.
 */

public class Code {

/**
 * Created by SCMaster on 2018-01-08.
 */

    private int c_num;
    private String c_title;
    private String c_code;
    private String c_type;
    private String c_comment;
    private String c_date;
    private String c_ref;
    private int c_star;
    private int f_num;
    private String f_name;
    private ArrayList<String> tag;

    public Code() {
        // TODO Auto-generated constructor stub
    }

    public Code(int c_num, String c_title, String c_code, String c_type, String c_comment, String c_date, String c_ref,
                int c_star, int f_num, String f_name, ArrayList<String> tag) {
        super();
        this.c_num = c_num;
        this.c_title = c_title;
        this.c_code = c_code;
        this.c_type = c_type;
        this.c_comment = c_comment;
        this.c_date = c_date;
        this.c_ref = c_ref;
        this.c_star = c_star;
        this.f_num = f_num;
        this.f_name = f_name;
        this.tag = tag;
    }

    public int getC_num() {
        return c_num;
    }

    public void setC_num(int c_num) {
        this.c_num = c_num;
    }

    public String getC_title() {
        return c_title;
    }

    public void setC_title(String c_title) {
        this.c_title = c_title;
    }

    public String getC_code() {
        return c_code;
    }

    public void setC_code(String c_code) {
        this.c_code = c_code;
    }

    public String getC_type() {
        return c_type;
    }

    public void setC_type(String c_type) {
        this.c_type = c_type;
    }

    public String getC_comment() {
        return c_comment;
    }

    public void setC_comment(String c_comment) {
        this.c_comment = c_comment;
    }

    public String getC_date() {
        return c_date;
    }

    public void setC_date(String c_date) {
        this.c_date = c_date;
    }

    public String getC_ref() {
        return c_ref;
    }

    public void setC_ref(String c_ref) {
        this.c_ref = c_ref;
    }

    public int getC_star() {
        return c_star;
    }

    public void setC_star(int c_star) {
        this.c_star = c_star;
    }

    public int getF_num() {
        return f_num;
    }

    public void setF_num(int f_num) {
        this.f_num = f_num;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }


    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    @Override
    public String toString() {
        return "Code [c_num=" + c_num + ", c_title=" + c_title + ", c_code=" + c_code + ", c_type=" + c_type
                + ", c_comment=" + c_comment + ", c_date=" + c_date + ", c_ref=" + c_ref + ", c_star=" + c_star
                + ", f_num=" + f_num + ", f_name=" + f_name + ", tag=" + tag + "]";
    }
}
