package com.threehalf.howfast;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.threehalf.howfast.animation.TitanicAnimation;
import com.threehalf.howfast.util.BaseDialog;
import com.threehalf.howfast.util.Typefaces;
import com.threehalf.howfast.view.TitanicTextView;

public class MainActivity extends Activity implements OnClickListener {
	private Button mBtnSatrt;
	private Button mBtnSeting;
	private TitanicTextView mTvAppName;
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AdManager.getInstance(this).init("6535121bb59b53a7",
				"1f72ec20ed543c11", false);
		initView();
		initEven();
	}

	private void initView() {
		mBtnSatrt = (Button) findViewById(R.id.btn_main_start);
		mBtnSeting = (Button) findViewById(R.id.btn_main_setting);
		mTvAppName = (TitanicTextView) findViewById(R.id.tv_app_name);
		mTvAppName
				.setTypeface(Typefaces.get(this, "fonts/Satisfy-Regular.ttf"));
		new TitanicAnimation().start(mTvAppName);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// ���ù����������λ��
		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // ����ʾ��Ϊ���½�
		// ʵ���������
		adView = new AdView(this, AdSize.FIT_SCREEN);
		// ����Activity��addContentView����
		this.addContentView(adView, layoutParams);
		//
		// ���ز岥��Դ
		SpotManager.getInstance(this).loadSpotAds();
		// ����չʾ��ʱʱ�䣬���س�ʱ��չʾ��棬Ĭ��0���������ó�ʱʱ��
		SpotManager.getInstance(this).setSpotTimeout(5000);// ����5��
		SpotManager.getInstance(this).setShowInterval(20);// ����20�����ʾʱ����
		// ����Ҫʹ���Զ��رղ������ܣ���ȡ��ע�����淽��
		SpotManager.getInstance(this).setAutoCloseSpot(true);// �����Զ��رղ�������
		SpotManager.getInstance(this).setCloseTime(3500); // ���ùرղ���ʱ��
	}

	@Override
	public void onBackPressed() {
		// �������Ҫ�����Ե�����˹رղ岥��档

		BaseDialog mBaseDialog = BaseDialog.getDialog(MainActivity.this,
				"Exit Game", "Really want to quit the game?", "Exit",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
						finish();
					}
				}, "Continue", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
		mBaseDialog.show();
		SpotManager.getInstance(MainActivity.this).showSpotAds(
				MainActivity.this, new SpotDialogListener() {
					@Override
					public void onShowSuccess() {
						Log.i("YoumiAdDemo", "չʾ�ɹ�");
					}

					@Override
					public void onShowFailed() {
						Log.i("YoumiAdDemo", "չʾʧ��");
					}

				}); // //

		/*
		 * if (!SpotManager.getInstance(MainActivity.this).disMiss(true)) { //
		 * super.onBackPressed(); }
		 */
	}

	@Override
	protected void onStop() {
		// ��������ô˷�������home����ʱ������ͼ���޷���ʾ�������
		SpotManager.getInstance(MainActivity.this).disMiss(false);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).unregisterSceenReceiver();
		super.onDestroy();
	}

	private void initEven() {
		mBtnSatrt.setOnClickListener(this);
		mBtnSeting.setOnClickListener(this);
		// ����������ӿ�
		adView.setAdListener(new AdViewListener() {

			@Override
			public void onSwitchedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "������л�");
			}

			@Override
			public void onReceivedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "������ɹ�");

			}

			@Override
			public void onFailedToReceivedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "������ʧ��");
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_main_start:
			intent.setClass(MainActivity.this, GameActity.class);
			startActivity(intent);
			break;
		case R.id.btn_main_setting:
			intent.setClass(MainActivity.this, SettingActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
