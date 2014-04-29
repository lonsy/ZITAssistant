package com.zte.zita.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zte.zita.R;

@SuppressLint("ValidFragment")
public class ZDialogFragment extends DialogFragment {
	
	private String title;
	
	private String content;
	
	private OnClickListener okListener;
	
	private OnClickListener cancelListener;
	
	public ZDialogFragment(String title, String content, 
			OnClickListener okListener, OnClickListener cancelListener)
	{
		this.title = title;
		this.content = content;
		this.okListener = okListener;
		this.cancelListener = cancelListener;
	}
  
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    	
    	LayoutInflater inflater = getActivity().getLayoutInflater();
    	View v = inflater.inflate(R.layout.zdialog_layout, null);
    	((TextView)v.findViewById(R.id.zdialog_title)).setText(this.title);
    	((TextView)v.findViewById(R.id.zdialog_content)).setText(this.content);
    	builder.setView(v);
    	
    	builder.setPositiveButton("确定", this.okListener);
    	builder.setNegativeButton("取消", this.cancelListener);
    	
        return builder.create();
    }
}
