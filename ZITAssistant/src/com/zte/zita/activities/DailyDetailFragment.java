package com.zte.zita.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zte.zita.R;
import com.zte.zita.db.DailyDAO;
import com.zte.zita.db.SQLiteHelper;
import com.zte.zita.entity.DailyEntity;
import com.zte.zita.entity.DayValueEntity;
import com.zte.zita.utils.PreferenceUtil;

public class DailyDetailFragment extends Fragment {
	private static final String LOG_TAG = "DailyDetailFragment";
	
	/**
	 * ��ǰ����
	 */
	private Date currentDate;
	
	/**
	 * ѡ�����ʱ����
	 */
	private Date tempDate;
	
	/**
	 * ���ڱ༭��
	 */
	private EditText dateText;
	
	/**
	 * ���ݱ༭��
	 */
	private EditText contentText;
	
	/**
	 * �Ƿ��ڱ���ʱ��ʾ��ѡ������
	 */
	private boolean isSavePicker;
	
	/**
	 * ���ݿ�����
	 */
	private SQLiteDatabase database;
	
	/**
	 * ��ǰ�û�����
	 */
	private String userNumber;
	
	/**
	 * �ռ�״̬
	 */
	private int dailyStatus;
	
	/**
	 * ����������ַ���
	 */
	private String inDateString = null;
	
	/**
	 * �Ƿ�༭״̬
	 */
	private boolean isEdit = true;
	
	/**
	 * �ռ�ID
	 */
	private int dailyId = -1;
	
	//�����գ����ڷ�ֹ����ѡ���ĸı��¼���������
	private int tmpYear=-1;
	private int tmpDay=-1;
	private int tmpMonth=-1;
	
	public static DailyDetailFragment newInstance(String currentDateString) {
		DailyDetailFragment f = new DailyDetailFragment();
		
		Bundle args = new Bundle();
		args.putString("currentDateString", currentDateString);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater infalter, ViewGroup container, 
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		View v = infalter.inflate(R.layout.fragment_daily_detail, container, false);
		
		//����������
		ActionBar bar = this.getActivity().getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayShowCustomEnabled(true);
		bar.setCustomView(R.layout.actionbar_layout_child);
		if (DayValueEntity.STATUS_DONE==this.dailyStatus || DayValueEntity.STATUS_SAVE==this.dailyStatus)
		{
			((TextView)this.getActivity().findViewById(R.id.actionbar_title)).setText("�ռ�����");
		}
		else
		{
			((TextView)this.getActivity().findViewById(R.id.actionbar_title)).setText("�½��ռ�");
		}
		// ���ذ�ť
		this.getActivity().findViewById(R.id.actionbar_menu_back).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isEdit)
						{
							//���ؼ���
							InputMethodManager imm= (InputMethodManager)getActivity().getSystemService(DailyDetailActivity.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(getActivity()
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
							
							if (contentText.getText().length()!=0)
							{						
								ThreeButtonShadowFragment shadow = new ThreeButtonShadowFragment(new String[] {"����ݸ�", "����", "���ر༭"}, cancelLinstener);
								FragmentManager fragManager = getFragmentManager();
						        FragmentTransaction tran = fragManager.beginTransaction();
						        tran.replace(R.id.daily_editor_shadow, shadow);
						        tran.addToBackStack(null);
						        tran.commit();
							}
							else
							{
								finishActivity();
							}
						}
						else
						{
							finishActivity();
						}
					}
				});
		
		this.dateText = (EditText)v.findViewById(R.id.daily_editor_day_choose);
		this.contentText = (EditText)v.findViewById(R.id.daily_editor_content);
		
		if (null!=this.inDateString)
		{
			this.dateText.setText(this.inDateString.toString());
			try 
			{
				this.currentDate = new SimpleDateFormat("yyyy-MM-dd").parse(this.inDateString);		
			} catch (Exception e)
			{
				Log.e(LOG_TAG, "���ڲ�����ʽ����" + this.inDateString);
			}		

			if (DayValueEntity.STATUS_SAVE == this.dailyStatus || DayValueEntity.STATUS_DONE == dailyStatus)
			{
				//�ݸ�״̬ ���� ���ύ
				String contentTextString = this.getArguments().getString("dailyContent");
				this.contentText.setText(contentTextString);
				this.contentText.setEnabled(false);
				
				this.isEdit = false;
			}
		}
		else
		{
			this.currentDate = new Date();
			v.findViewById(R.id.daily_editor_day_choose_layout).setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					pickDate();
				}
			});
			this.dateText.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					pickDate();
				}
			});
		}
		
		//���Ϊ�ݸ�״̬����������ʾ�ύ��ť
		if (DayValueEntity.STATUS_SAVE == this.dailyStatus)
		{
			((Button)v.findViewById(R.id.daily_editor_btn_submit)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					submit();
				}
			});
		}
		else
		{
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 0);
			((RelativeLayout)v.findViewById(R.id.daily_editor_bottom_layout)).setLayoutParams(p);
		}
		
		this.isSavePicker = false;
		this.userNumber = PreferenceUtil.getStringPre(this.getActivity(), PreferenceUtil.USER_NUMBER, null);;
		
		return v;		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//���ô�Fragmentӵ�в˵���������ղ�������¼�
        setHasOptionsMenu(true);   
        
		Object dailyStatusStr = this.getArguments().get("dailyStatus");
		if (dailyStatusStr!=null && !"".equals(dailyStatusStr.toString()))
		{
			this.dailyStatus = Integer.parseInt(dailyStatusStr.toString());
		}
		Object inDate = this.getArguments().get("currentDateString");
		if (inDate!=null && !"".equals(inDate.toString()))
		{
			this.inDateString = inDate.toString();
		}
		Object dailyId = this.getArguments().get("dailyId");
		if (dailyId!=null && !"".equals(dailyId.toString()))
		{
			this.dailyId = Integer.parseInt(dailyId.toString());
		}
	}
	
	public void pickDate()
	{		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.currentDate);
		DatePickerDialog datePicker = new DatePickerDialog(this.getActivity(),  
                dateListener,  
                calendar.get(Calendar.YEAR),  
                calendar.get(Calendar.MONTH),  
                calendar.get(Calendar.DAY_OF_MONTH)); 
		datePicker.show();
	}

	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {  
        @SuppressLint("SimpleDateFormat")
		@Override  
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {  
        	Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);          
            
            if (!isDayChanged(year, month, dayOfMonth))
            {
            	return;
            }
            tmpYear = year;
            tmpMonth = month;
            tmpDay = dayOfMonth;
            
            tempDate = calendar.getTime();
            
            //�жϸ������Ƿ������ռ� 
            DailyEntity daily = getDaily(tempDate);
            if (daily!=null)
            {
    			Toast toast = Toast.makeText(getActivity(), "�������Ѵ����ռ�", Toast.LENGTH_SHORT);
    			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    			toast.show();
    			
    			pickDate();
            }
            else
            {
                SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd��");
                currentDate = tempDate;
                dateText.setText(format.format(currentDate));
                
	            //����Ǳ���ݸ�֮��Ĳ������򱣴�
	            if (isSavePicker)
	            {    			
	            	saveDialy(false);
	            }
            }
        }
    }; 
	
	//ȡ����ť����
	private ThanksShadowLinstener cancelLinstener = new ThanksShadowLinstener() {
		//ȡ�� -- ����ݸ�
		@Override
		public void mainAction() {
			if (dateText.getText().length()==0)
			{
				isSavePicker = true;

    			Toast toast = Toast.makeText(getActivity(), "��ѡ������", Toast.LENGTH_SHORT);
    			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    			toast.show();
    			pickDate();
			}
			else
			{
				saveDialy(false);
			}
		}

		//ȡ�� -- ����
		@Override
		public void subAction() {
			//���ٵ�ǰ��Activity
			finishActivity();
		}

		//ȡ�� -- ����
		@Override
		public void cancelAction() {
			
		}
	};
	
	/**
	 * ����ݸ� �� �ύ
	 */
	private void saveDialy(boolean isSubmit)
	{
		if (database==null)
		{
			database = new SQLiteHelper(this.getActivity()).getWritableDatabase();
		}
		
		DailyEntity daily = DailyDAO.getDaily(database, this.userNumber, new SimpleDateFormat("yyyy-MM-dd").format(this.currentDate));
		if (daily!=null)
		{
			daily.setDailyContent(this.contentText.getText().toString());
			daily.setDailyStatus(isSubmit ? DayValueEntity.STATUS_DONE : DayValueEntity.STATUS_SAVE);
			DailyDAO.updateDaily(database, daily);
		}
		else
		{		
			daily = new DailyEntity();
			daily.setCreatedBy(this.userNumber);
			daily.setDailyContent(this.contentText.getText().toString());
			daily.setDailyDate(new SimpleDateFormat("yyyy-MM-dd").format(this.currentDate));
			daily.setDailyStatus(isSubmit ? DayValueEntity.STATUS_DONE : DayValueEntity.STATUS_SAVE);
			DailyDAO.addDaily(database, daily);
		}

		finishActivity();
	}
	
	/**
	 * ɾ���ռ�
	 */
	private void deleteDialy()
	{

		if (database==null)
		{
			database = new SQLiteHelper(this.getActivity()).getWritableDatabase();
		}
		
		DailyDAO.disableDaily(database, this.dailyId);

		finishActivity();
	}
	
	/**
	 * �˵���Ⱦ
	 */
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.daily_detail, menu);
    }

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem save = menu.findItem(R.id.ic_save);
		MenuItem delete = menu.findItem(R.id.ic_delete); 
		MenuItem edit = menu.findItem(R.id.ic_edit); 
		MenuItem submit = menu.findItem(R.id.ic_submit);   
		
		if (DayValueEntity.STATUS_SAVE == this.dailyStatus)
		{
			//�ݸ�״̬ ֻ��ʾ�༭��ɾ�� 
			if (this.isEdit)
			{
				edit.setVisible(false);
				submit.setVisible(false);
			}
			else
			{
				save.setVisible(false);
				submit.setVisible(false);
			}
		}
		else if (DayValueEntity.STATUS_DONE == this.dailyStatus)
		{
			//���ύ ֻ��ʾ�༭��ɾ��
			if (this.isEdit)
			{
				edit.setVisible(false);
			}
			else
			{
				save.setVisible(false);
				submit.setVisible(false);
			}
		}
		else
		{
			//�½�  ֻ��ʾ���桢�ύ
			delete.setVisible(false);
			edit.setVisible(false);
		}
    }  
	
	/**
	 * �ύ
	 */
	private void submit()
	{
		if (!validate())
		{
			return;
		}
		
		OnClickListener okListener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveDialy(true);
			}        			
		};
		ZDialogFragment mdf = new ZDialogFragment("��ʾ", "ȷ��Ҫ�ύ���ռ���", okListener, null);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		mdf.show(ft, "df");		
	}
    
    @Override  
    public boolean onOptionsItemSelected(MenuItem menuItem) {  
        switch (menuItem.getItemId()) {  
        	case R.id.ic_submit:  
        		//�ύ
        		submit();
        		break;
        	case R.id.ic_save:
        		//����
        		if (!validate())
        		{
        			break;
        		}
        		saveDialy(false);
        		
        		break;
        	case R.id.ic_edit:
        		//�༭
        		this.isEdit = true;
        		this.contentText.setEnabled(true);
    			((TextView)this.getActivity().findViewById(R.id.actionbar_title)).setText("�༭�ռ�");
        		this.getActivity().getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);  
        		
        		break;
        	case R.id.ic_delete:
        		//ɾ��
        		OnClickListener delListener = new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteDialy();
					}        			
        		};
        		ZDialogFragment mdf1 = new ZDialogFragment("��ʾ", "ȷ��Ҫɾ�����ռ���", delListener, null);
        		FragmentTransaction ft1 = getFragmentManager().beginTransaction();
        		ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        		mdf1.show(ft1, "df1");
        		break;
        	default:  
        		break;  
        }  
        return true;  
    }  
    
    private boolean validate()
    {
		if (dateText.getText().length()==0)
		{
			Toast toast = Toast.makeText(getActivity(), "��ѡ������", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			return false;  
		}
		else if (contentText.getText().length()==0)
		{
			Toast toast = Toast.makeText(getActivity(), "�������ռ�����", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			return false;  
		}
		
		return true;
    }
	
	/**
	 * ����ʱ�ر����ݿ�����
	 */
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		if (this.database != null)
		{
			this.database.close();
		}
	}
	
	/**
	 * ��ȡ�ռǣ������ڱ仯ʱִ��
	 */
	private DailyEntity getDaily(Date date)
	{
        if (database==null)
        {
        	database = new SQLiteHelper(getActivity()).getWritableDatabase();
        }
        DailyEntity daily = DailyDAO.getDaily(database, userNumber, new SimpleDateFormat("yyyy-MM-dd").format(date));
        return daily;
	}
	
	private boolean isDayChanged(int year, int month, int day)
	{
		if (this.tmpYear==year && this.tmpMonth==month && this.tmpDay==day)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * ������ǰActivity������
	 */
	private void finishActivity()
	{
		Intent intent = this.getActivity().getIntent();
		if (this.inDateString==null)
		{
			this.inDateString = new SimpleDateFormat("yyyy-MM-dd").format(this.currentDate);
		}
		intent.putExtra("currentDateString", this.inDateString);
		
		getActivity().setResult(Activity.RESULT_OK, intent);
		getActivity().finish();
	}
	
	@Override 
	public void onPause()
	{
		super.onPause();
	}
}
