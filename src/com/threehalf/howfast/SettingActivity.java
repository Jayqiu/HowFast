package com.threehalf.howfast;

import java.util.HashMap;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.threehalf.howfast.adapter.SimpleListDialogAdapter;
import com.threehalf.howfast.util.AppCache;
import com.threehalf.howfast.util.SimpleListDialog;
import com.threehalf.howfast.util.SimpleListDialog.onSimpleListItemClickListener;

public class SettingActivity extends SherlockFragmentActivity implements
		OnClickListener {
	private RelativeLayout mRlGrid;
	private RelativeLayout mRlSound;
	private RelativeLayout mRlHint;
	private RelativeLayout mRlHidden;
	private RelativeLayout mRlShake;
	private RelativeLayout mRlAbout;
	private RelativeLayout mRlGameMode;
	private TextView mTvShoudSwitch;
	private TextView mTvHintSwitch;
	private TextView mTvHiddenSwitch;
	private TextView mTvShakeSwitch;
	private TextView mTvGridNum;
	private TextView mTvGameMode;
	private HashMap<String, Object> dataMap;
	private int grid;
	private boolean sound;
	private boolean hint;
	private boolean hidden;
	private boolean shake;
	private int gameMode;
	private String open;
	private String close;
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		AdManager.getInstance(this).init("6535121bb59b53a7",
				"1f72ec20ed543c11", false);
		initView();
		initTitle();
		initEven();
		initData();

	}

	private void initTitle() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	private void initData() {
		dataMap = AppCache.getInstance(this).getSetting();
		grid = (Integer) dataMap.get(AppCache.GRID);
		sound = (Boolean) dataMap.get(AppCache.SOUND);
		hint = (Boolean) dataMap.get(AppCache.HINT);
		hidden = (Boolean) dataMap.get(AppCache.HIDDEN);
		shake = (Boolean) dataMap.get(AppCache.SHAKE);
		open = getString(R.string.setting_open_text);
		close = getString(R.string.setting_close_text);
		gameMode = (Integer) dataMap.get(AppCache.GAME_MODE);
		if (sound) {
			mTvShoudSwitch.setText(open);
		} else {
			mTvShoudSwitch.setText(close);
		}
		if (hint) {
			mTvHintSwitch.setText(open);
		} else {
			mTvHintSwitch.setText(close);
		}
		if (hidden) {
			mTvHiddenSwitch.setText(open);
		} else {
			mTvHiddenSwitch.setText(close);
		}
		if (shake) {
			mTvShakeSwitch.setText(open);
		} else {
			mTvShakeSwitch.setText(close);
		}
		if (gameMode == 0) {
			mTvGameMode.setText("Normal Mode");

		} else {
			mTvGameMode.setText("Challenge Mode");
		}
		mTvGridNum.setText(grid + " x " + grid);
	}

	private void initView() {
		mRlAbout = (RelativeLayout) findViewById(R.id.rl_about);
		mRlGrid = (RelativeLayout) findViewById(R.id.rl_grid);
		mRlHidden = (RelativeLayout) findViewById(R.id.rl_hidden);
		mRlHint = (RelativeLayout) findViewById(R.id.rl_hint);
		mRlShake = (RelativeLayout) findViewById(R.id.rl_shake);
		mRlSound = (RelativeLayout) findViewById(R.id.rl_sound);
		mRlGameMode = (RelativeLayout) findViewById(R.id.rl_game_mode);
		mTvHiddenSwitch = (TextView) findViewById(R.id.tv_hidden_switch);
		mTvHintSwitch = (TextView) findViewById(R.id.tv_hint_switch);
		mTvShakeSwitch = (TextView) findViewById(R.id.tv_shake_switch);
		mTvShoudSwitch = (TextView) findViewById(R.id.tv_sound_switch);
		mTvGridNum = (TextView) findViewById(R.id.tv_grid_num);
		mTvGameMode = (TextView) findViewById(R.id.tv_game_mode);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告条的悬浮位置
		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
		// 实例化广告条
		adView = new AdView(this, AdSize.FIT_SCREEN);
		// 调用Activity的addContentView函数
		this.addContentView(adView, layoutParams);
	}

	private void initEven() {
		mRlGrid.setOnClickListener(this);
		mRlSound.setOnClickListener(this);
		mRlAbout.setOnClickListener(this);
		mRlHidden.setOnClickListener(this);
		mRlHint.setOnClickListener(this);
		mRlShake.setOnClickListener(this);
		mRlGameMode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_about:
			break;
		case R.id.rl_grid:
			SimpleListDialog mDialog = new SimpleListDialog(this);
			String[] codes = getResources().getStringArray(R.array.grid_items);
			mDialog.setTitle("Grid");
			mDialog.setTitleLineVisibility(View.GONE);
			mDialog.setAdapter(new SimpleListDialogAdapter(this, codes));
			mDialog.setOnSimpleListItemClickListener(new OnReportDialogItemClickListener());
			mDialog.show();
			break;
		case R.id.rl_hidden:
			if (hidden) {
				hidden = false;

			} else {
				hidden = true;
			}
			AppCache.getInstance(this).saveHidden(hidden);
			break;
		case R.id.rl_hint:
			if (hint) {
				hint = false;

			} else {
				hint = true;
			}
			AppCache.getInstance(this).saveHint(hint);
			break;
		case R.id.rl_shake:
			if (shake) {
				shake = false;

			} else {
				shake = true;
			}
			AppCache.getInstance(this).saveShake(shake);
			break;
		case R.id.rl_sound:
			if (sound) {
				sound = false;

			} else {
				sound = true;
			}
			AppCache.getInstance(this).saveSound(sound);
			break;

		case R.id.rl_game_mode:
			SimpleListDialog mGameModeDialog = new SimpleListDialog(this);
			String[] gameMode = getResources()
					.getStringArray(R.array.game_mode);
			mGameModeDialog.setTitle("Game Mode");
			mGameModeDialog.setTitleLineVisibility(View.GONE);
			mGameModeDialog.setAdapter(new SimpleListDialogAdapter(this,
					gameMode));
			mGameModeDialog
					.setOnSimpleListItemClickListener(new OnGameModeDialogItemClickListener());
			mGameModeDialog.show();
			break;
		default:
			break;
		}
		initData();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		finish();
		return super.onOptionsItemSelected(item);
	}

	public class OnReportDialogItemClickListener implements
			onSimpleListItemClickListener {

		@Override
		public void onItemClick(int position, View view) {
			// TODO Auto-generated method stub
			switch (position) {
			case 0:
				AppCache.getInstance(SettingActivity.this).saveGrid(3);
				grid = 3;
				break;
			case 1:
				AppCache.getInstance(SettingActivity.this).saveGrid(4);
				grid = 4;
				break;
			case 2:
				AppCache.getInstance(SettingActivity.this).saveGrid(5);
				grid = 5;
				break;
			default:
				break;
			}
			initData();
		}

	}

	public class OnGameModeDialogItemClickListener implements
			onSimpleListItemClickListener {

		@Override
		public void onItemClick(int position, View view) {
			// TODO Auto-generated method stub
			switch (position) {
			case 0:
				AppCache.getInstance(SettingActivity.this).saveGameMode(
						position);
				break;
			case 1:
				AppCache.getInstance(SettingActivity.this).saveGameMode(
						position);
				break;
			default:
				break;
			}
			gameMode = position;
			initData();
		}

	}

}
