package com.ayvytr.easydeveloper;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * Created by Doing on 2017/3/5.
 */

public class CenterTextView extends AppCompatTextView
{
    public CenterTextView(Context context)
    {
        this(context, null);
    }

    public CenterTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public CenterTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
    }
}
