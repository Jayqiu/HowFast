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
		// 设置广告条的悬浮位置
		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
		// 实例化广告条
		adView = new AdView(this, AdSize.FIT_SCREEN);
		// 调用Activity的addContentView函数
		this.addContentView(adView, layoutParams);
		//
		// 加载插播资源
		SpotManager.getInstance(this).loadSpotAds();
		// 设置展示超时时间，加载超时则不展示广告，默认0，代表不设置超时时间
		SpotManager.getInstance(this).setSpotTimeout(5000);// 设置5秒
		SpotManager.getInstance(this).setShowInterval(20);// 设置20秒的显示时间间隔
		// 如需要使用自动关闭插屏功能，请取消注释下面方法
		SpotManager.getInstance(this).setAutoCloseSpot(true);// 设置自动关闭插屏开关
		SpotManager.getInstance(this).setCloseTime(3500); // 设置关闭插屏时间
	}

	@Override
	public void onBackPressed() {
		// 如果有需要，可以点击后退关闭插播广告。

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
						Log.i("YoumiAdDemo", "展示成功");
					}

					@Override
					public void onShowFailed() {
						Log.i("YoumiAdDemo", "展示失败");
					}

				}); // //

		/*
		 * if (!SpotManager.getInstance(MainActivity.this).disMiss(true)) { //
		 * super.onBackPressed(); }
		 */
	}

	@Override
	protected void onStop() {
		// 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
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
		// 监听广告条接口
		adView.setAdListener(new AdViewListener() {

			@Override
			public void onSwitchedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "广告条切换");
			}

			@Override
			public void onReceivedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "请求广告成功");

			}

			@Override
			public void onFailedToReceivedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "请求广告失败");
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
