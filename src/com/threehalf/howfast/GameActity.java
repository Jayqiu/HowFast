package com.threehalf.howfast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.diy.banner.DiyAdSize;
import net.youmi.android.diy.banner.DiyBanner;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threehalf.howfast.animation.MyAnimation;
import com.threehalf.howfast.util.AppCache;
import com.threehalf.howfast.util.BaseDialog;
import com.threehalf.howfast.util.Typefaces;
import com.threehalf.howfast.view.MyProgressView;
import com.threehalf.howfast.view.MyTextView;

public class GameActity extends Activity implements OnClickListener {
	private Vibrator mVibrator;
	private float mDensity;
	private int screenWidth;
	private int screenHeight;
	private int numsHeight;
	private MyTextView mBtns[][];
	private TextView mTvNum;
	private TextView mTvGameMode;
	private TextView mTvCount;
	private AbsoluteLayout btnsLayout;
	private SoundPool mSoundPool;
	private int soundId;
	private boolean flagDisappear = true;// 点击后消失
	private boolean flagVibrate = true;// 震动
	private boolean flagTick = true;
	private boolean flagSound = true;// 声音
	private boolean flagPromt = true;
	private View numsView = null;
	private int mScale = 4;
	private int numSeq[];
	private int prsNum;
	private long startTime;
	private MyProgressView progressView;
	private LinearLayout mTopLayout;
	private Button mBtnReStart;
	private TextView mTvBest;
	private TextView mTvScore;
	private boolean startFlag = false;
	private long mScore = 0;
	private int mCount = 1;
	private int tempNum[];
	private long gameTime = 10000; // z暂用50%
	private int gameMode = 0;// 游戏模式
	private BaseDialog mBaseDialog;
	private boolean activityIsRun = true;// 界面是否在运行
	private long best = 0;// 最高分
	// private int tipNum = 0;// 提示次数
	private Handler timerHandler = new Handler();
	private Runnable timerRunnable = new Runnable() {
		@Override
		public void run() {
			if (startFlag) {
				if (gameTime < 0) {// 游戏结束

					gameOver();
				} else {
					timerHandler.postDelayed(this, 100);
					gameTime = gameTime - (50 * mCount / 2);
					if (gameTime >= 20000) {
						gameTime = 20000;
						progressView.setCurrentCount(20000.0f);
					} else {
						progressView.setCurrentCount(gameTime);
					}
				}
			} else {
				progressView.setCurrentCount(gameTime);
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initView();
		initStartDialog();
	}

	private void init() {
		AdManager.getInstance(this).init("6535121bb59b53a7",
				"1f72ec20ed543c11", false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* set it to be full screen */
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		Resources resources = getBaseContext().getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		mDensity = dm.density;

		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();

		mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundId = mSoundPool.load(this, R.raw.effect_tick, 1);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告条的悬浮位置
		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 调用Activity的addContentView函数
		this.addContentView(adView, layoutParams);
	}

	private void initView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		numsView = inflater.inflate(R.layout.activity_game, null, false);
		setContentView(numsView);
		mTvNum = (TextView) findViewById(R.id.tv_num);
		progressView = (MyProgressView) findViewById(R.id.pv_time);
		mTopLayout = (LinearLayout) findViewById(R.id.ll_game_top);
		mBtnReStart = (Button) findViewById(R.id.btn_restart);
		mTvBest = (TextView) findViewById(R.id.tv_highest_score);
		mTvScore = (TextView) findViewById(R.id.tv_score);
		mTvGameMode = (TextView) findViewById(R.id.tv_game_mode);
		mTvCount = (TextView) findViewById(R.id.tv_game_count);
		btnsLayout = (AbsoluteLayout) findViewById(R.id.layout_nums);
		//
		progressView.setMaxCount(20000.0f);
		progressView.setCurrentCount(10000.0f);
		mTvNum.setTextSize((float) (screenWidth / 2 / mDensity));
		Typeface next_num_face = Typefaces.get(this, "fonts/Roboto-Medium.ttf");
		mTvNum.setTypeface(next_num_face);
		//

		btnsLayout.removeAllViews();
		mBtnReStart.setOnClickListener(this);
		initData();
	}

	private void initData() {
		HashMap<String, Object> dataMap = AppCache.getInstance(this)
				.getSetting();
		mScale = (Integer) dataMap.get(AppCache.GRID);

		flagSound = (Boolean) dataMap.get(AppCache.SOUND);
		flagPromt = (Boolean) dataMap.get(AppCache.HINT);
		flagDisappear = (Boolean) dataMap.get(AppCache.HIDDEN);
		flagVibrate = (Boolean) dataMap.get(AppCache.SHAKE);
		gameMode = (Integer) dataMap.get(AppCache.GAME_MODE);
		if (gameMode == 0) {
			mTvGameMode.setText("NormalMode:");
			best = (Long) dataMap.get(AppCache.BEST);
		} else if (gameMode == 1) {
			mTvGameMode.setText("ChallengeMode:");

			best = (Long) dataMap.get(AppCache.CHALLENGE_MODE_BEST);
		}

		mTvCount.setText("+" + String.valueOf(mCount));
		mTvBest.setText(String.valueOf(best));
		mTvScore.setText(String.valueOf(mScore));
	}

	private void initBtns() {
		if (mBtns != null) {
			mBtns = null;
		}
		mBtns = new MyTextView[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				mBtns[i][j] = new MyTextView(this);
				mBtns[i][j].setGravity(Gravity.CENTER);
			}
		}
	}

	private void initNumGrid() {
		fillNumView();
		numsHeight = screenHeight - mTopLayout.getBottom();
		float grid_width = 0f;
		if (numsHeight >= screenWidth) {
			grid_width = screenWidth / mScale;
		} else {
			grid_width = numsHeight / mScale;
		}
		float sy = (numsHeight - screenWidth) / 2;
		float rect_width = (float) (grid_width * 0.9);
		float text_size = (float) (screenWidth / mScale / 4 / mDensity * 1.5);
		Typeface face = Typefaces.get(this, "fonts/Roboto-Light.ttf");
		LayoutParams lp;
		for (int i = 0; i < mScale; i++) {
			for (int j = 0; j < mScale; j++) {
				mBtns[i][j].setTextSize(text_size);
				mBtns[i][j].setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_main));
				mBtns[i][j].setTextColor(getResources().getColor(
						R.color.gray_text));
				mBtns[i][j].setTypeface(face);
				lp = new LayoutParams((int) rect_width, (int) rect_width,
						(int) ((sy / mScale - 1) / 2 + j * grid_width),
						(int) (sy + i * grid_width));

				if (mBtns[i][j].getParent() != null) {
					ViewGroup vg = (ViewGroup) (mBtns[i][j].getParent());
					vg.removeView(mBtns[i][j]);
				}
				btnsLayout.addView(mBtns[i][j], lp);
				mBtns[i][j].setOnClickListener(this);
			}
		}

	}

	private void fillNumView() {
		genRanSeq();

		for (int i = 0; i < mScale; i++) {
			for (int j = 0; j < mScale; j++) {
				mBtns[i][j].setText(String.valueOf(numSeq[i * mScale + j]));
				mBtns[i][j].setVisibility(View.VISIBLE);
			}
		}
	}

	public void genRanSeq() {
		numSeq = new int[mScale * mScale];
		tempNum = new int[mScale * mScale];
		for (int i = 0; i < mScale * mScale; i++) {
			tempNum[i] = prsNum + i * mCount;
		}
		numSeq = genRandomData(tempNum);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		// if (view.getId() == R.id.btn_restart) {
		// if (startFlag) {
		// startFlag = false;
		// mBtnReStart.setText(getString(R.string.game_start));
		// } else {
		// prsNum = 1;
		// mCount = 1;
		// startFlag = true;
		// mBtnReStart.setText(getString(R.string.game_restart));
		// progressView.setCurrentCount(10000.0f);
		// initNumGrid();
		// startTime = System.currentTimeMillis();
		// timerHandler.postDelayed(timerRunnable, 0);
		// }
		//
		// return;
		// }
		if (flagVibrate) {
			mVibrator.vibrate(50);
		}
		if (flagSound) {
			mSoundPool.play(soundId, 1, 1, 0, 0, 1);
		}
		if (((MyTextView) view).getText().equals(String.valueOf(prsNum))) {

			if (flagDisappear) {
				((MyTextView) view).setVisibility(View.INVISIBLE);
			}
			MyAnimation.stopFlick(getPromptNumView());
			gameTime = gameTime + (500 * mCount / 3);
			mScore = mScore + prsNum;
			prsNum = prsNum + mCount;
			mTvScore.setText(String.valueOf(mScore));

			if (prsNum > tempNum[mScale * mScale - 1]) {// 一盘结束
				mCount++;
				if (gameMode == 0) {
					prsNum = 1;
				} else if (gameMode == 1) {
					prsNum = Integer.parseInt(((MyTextView) view).getText()
							.toString());
				}
				initData();
				initNumGrid();
			}

		} else {
			if (flagPromt) {
				// tipNum++;
				// if (tipNum > 3) {
				// gameOver();
				// }
				MyAnimation.startFlick(getPromptNumView());
			}
		}
	}

	public String getString(long num) {
		String res = String.valueOf(num);
		if (res.length() <= 3) {
			return res;
		} else {
			String tmp = res;
			int len = tmp.length();
			res = tmp.substring(0, len - 3) + "," + tmp.substring(len - 3);
			return res;
		}
	}

	private int[] genRandomData(int[] data) {

		int resultdata[] = new int[data.length];

		List<Integer> dataArray = new ArrayList<Integer>();

		for (int i = 0; i < data.length; i++) {

			dataArray.add(data[i]);
		}
		int i = 0;
		while (dataArray.size() > 0) {

			int index = (int) (Math.random() * dataArray.size());
			resultdata[i] = dataArray.get(index);
			i++;
			dataArray.remove(index);
		}
		return resultdata;
	}

	private View getPromptNumView() {
		int i = 0;
		int j = 0;
		for (i = 0; i < mScale; i++) {
			for (j = 0; j < mScale; j++) {
				if (mBtns[i][j].getText().equals(String.valueOf(prsNum))) {
					return mBtns[i][j];
				}
			}
		}
		return mBtns[i][j];
	}

	private void initStartDialog() {
		BaseDialog mBaseDialog = BaseDialog.getDialog(GameActity.this,
				"Start Game", "Whether to start the game", "Start",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
						if (startFlag) {
							startFlag = false;
						} else {
							initBtns();
							startGame();
						}

					}
				}, "Close", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
						finish();
					}
				});
		mBaseDialog.setCancelable(false);
		mBaseDialog.show();
	}

	private void startGame() {
		prsNum = 1;
		mCount = 1;
		// tipNum = 0;
		mScore = 0;
		startFlag = true;
		progressView.setCurrentCount(10000.0f);
		gameTime = 10000;

		btnVisible();
		initNumGrid();
		btnStopFlick();

		initData();
		// startTime = System.currentTimeMillis();
		timerHandler.postDelayed(timerRunnable, 0);
	}

	private void btnVisible() {
		for (int i = 0; i < mBtns.length; i++) {
			for (int j = 0; j < mBtns.length; j++) {
				mBtns[i][j].setVisibility(View.VISIBLE);
			}
		}
	}

	private void btnStopFlick() {
		for (int i = 0; i < mBtns.length; i++) {
			for (int j = 0; j < mBtns.length; j++) {
				MyAnimation.stopFlick(getPromptNumView());
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		activityIsRun = false;
	}

	private void gameOver() {
		timerHandler.removeCallbacks(timerRunnable);
		if (!activityIsRun) {
			return;
		}
		String strScore = "Score:";
		mScore = Integer.parseInt(mTvScore.getText().toString().trim());
		if (mScore > best) {
			mTvBest.setText(String.valueOf(best));
			strScore = "You created a new record:" + mScore;
			if (gameMode == 0) {
				AppCache.getInstance(GameActity.this).saveBest(mScore);
			} else if (gameMode == 1) {
				AppCache.getInstance(GameActity.this).saveChallengeModeBest(
						mScore);
			}

		} else {
			strScore = strScore + mScore;
		}
		mBaseDialog = BaseDialog.getDialog(GameActity.this, "Game Over",
				strScore, "OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
						finish();
					}
				}, "Restart", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
						startGame();
					}
				});
		mBaseDialog.setCancelable(false);
		mBaseDialog.show();
	}
}
