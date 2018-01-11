package com.example.scmaster.codecapture;

import android.view.View;

import java.io.Serializable;

/**
 * Created by scmaster on 2018-01-10.
 */

public class ShareData implements Serializable{
    public String shareTitle;
    public String shareDate;
    public String shareSender;
    public String shareNum;

    public View.OnClickListener onClickListener;
}
