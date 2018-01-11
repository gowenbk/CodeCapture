package com.example.scmaster.codecapture;

import android.view.View;

import java.io.Serializable;

/**
 * Created by SCMaster on 2018-01-08.
 */

public class CodeData implements Serializable
{
    public String codeTitle;
    public String codeDate;
    public String codeComment;
    public String codeNum;

    public View.OnClickListener onClickListener;
}
