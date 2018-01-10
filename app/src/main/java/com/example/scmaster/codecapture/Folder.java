package com.example.scmaster.codecapture;

/**
 * Created by scmaster on 2018-01-08.
 */

public class Folder {
    private int f_num;
    private String f_name;
    private String f_email;

    public Folder() {
        // TODO Auto-generated constructor stub
    }

    public Folder(int f_num, String f_name, String f_email) {
        super();
        this.f_num = f_num;
        this.f_name = f_name;
        this.f_email = f_email;
    }

    public int getF_num() {
        return f_num;
    }

    public void setF_num(int f_num) {
        this.f_num = f_num;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_email() {
        return f_email;
    }

    public void setF_email(String f_email) {
        this.f_email = f_email;
    }

    @Override
    public String toString() {
        return "Folder [f_num=" + f_num + ", f_name=" + f_name + ", f_email=" + f_email + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Folder)) return false;
        Folder f = (Folder) obj;
        if(f_num == f.f_num && f_name.equals(f.f_name))
            return true;
        else
            return false;
    }

    @Override
    public int hashCode(){
        int result = 17;
        result = 31 * result + f_num;
        return result;
    }
}
