package com.zte.zita.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zte.zita.R;

public class CalendarDayLayout extends RelativeLayout {
	
	private TextView cardDayText;

    @SuppressLint("NewApi")
    public CalendarDayLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CalendarDayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarDayLayout(Context context) {
        super(context);
        init(context);
    }
    
    private void init(Context context) {
    	View.inflate(context, R.layout.card_cell, this); 
    	
    	this.cardDayText = (TextView)this.findViewById(R.id.cardDayText);
    }
    
    public void setText(String text)
    {
    	this.cardDayText.setText(text);
    }
    
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        return drawableState;
    }

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}
}
