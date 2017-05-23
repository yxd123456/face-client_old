package org.yanzi.activity;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.huashi.otg.sdk.HSInterface;
import com.huashi.otg.sdk.HsOtgService;
import com.kuaiyu.voiceprint.KyVoicePrint;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.yanzi.camera.preview.CameraSurfaceView;
import org.yanzi.mode.ApkVersion;
import org.yanzi.mode.FaceData;
import org.yanzi.mode.Response;
import org.yanzi.mode.Video;
import org.yanzi.playcamera.R;
import org.yanzi.ui.FaceView;
import org.yanzi.util.JniTool;
import org.yanzi.util.NetUtil;
import org.yanzi.util.SPUtil;
import org.yanzi.util.SoundUtil;
import org.yanzi.util.Util;
import org.yanzi.util.VersionUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;

public class CameraActivity extends FragmentActivity{
	private static final String DATA_URL = "http://192.168.111.111:8080/PictureUpdate/TestServlet";
	static CameraSurfaceView surfaceView = null;//显示摄像头画面
	SoundUtil soundUtil;//播放音效
	ImageView iv_face, iv_ic, iv_mid;//拍摄人脸照片，身份证人脸照片
	Util util;//位图工具
	Bitmap faceBitmap;//经过旋转处理的拍摄人脸位图
	//当前时间，身份证姓名、号码、比对结果，面板标题
	TextView tv_current_time, tv_name, tv_code, tv_result, tv_title;
	volatile Bitmap idFaceBitmap = null;//身份证人脸位图
	View testView, testView2;//两块面板
	ApkVersion version;

	MyConn conn;//读卡器相关类
	HSInterface HSinterface;//读卡器接口
	Intent service;//读卡器服务
	String filepath = "";
	GoogleApiClient client;

	DbUtils dbUtils;//数据库
	Bitmap cameraFaceBitmap;//初次拍摄人脸位图
	View ll_progressBar,ll_info;//进度条布局，信息布局
	SimpleDateFormat sdf;

	public static TextView tv_info;

	Paint paint;
	public static FaceView faceView;
	FaceData data;
	List<FaceData> datas = new ArrayList<>();

	Handler handler = new Handler(){
		@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 0x999:
					tv_title.setText("请放上身份证进行比对");
						tv_result.setText("");
						tv_name.setText("");
						tv_code.setText("");
						iv_ic.setImageResource(R.drawable.face);

//					tv_result.setText("");
//					tv_name.setText("");
//					tv_code.setText("");
//					iv_ic.setImageResource(R.drawable.face);
					iv_mid.setImageResource(R.drawable.face);
					flag_result = false;
					break;
				case 0x7777:
					testView.setVisibility(View.VISIBLE);
					testView2.setVisibility(View.VISIBLE);
					if(iv_ic.getVisibility() == View.INVISIBLE){
						iv_ic.setVisibility(View.VISIBLE);
						tv_title.setText("请放上身份证进行比对");
						ll_progressBar.setVisibility(View.GONE);
						ll_info.setVisibility(View.VISIBLE);
					}
					break;
				case 0x1122:
					data = (FaceData) msg.obj;
					if(NetUtil.isNetworkConnected(CameraActivity.this)){
						pushData(data, new PushDataEnd() {
							@Override
							public void success(String response) {
								Gson gson = new Gson();
								Response response1 = gson.fromJson(response, Response.class);
								if(response1.getCameraFaceImgsuccess().equals("true")&&
										response1.getDatabaseSaveMark().equals("true")&&
										response1.getIdCardFaceImgsuccess().equals("true")){
								}else {
									datas.add(data);
									SPUtil.save(CameraActivity.this, datas);
								}
								Log.d("Test", response1.getCameraFaceImgsuccess()+" "
										+response1.getDatabaseSaveMark()+" "
										+response1.getIdCardFaceImgsuccess());
							}

							@Override
							public void failure(Exception e) {
								datas.add(data);
								SPUtil.save(CameraActivity.this, datas);
							}
						});
					}else {
						Log.d("OFFLINE", data.getSimilar()+"=======");
						datas.add(data);
						SPUtil.save(CameraActivity.this, datas);
					}
					break;
				case 0x321:
					faceView.setRects(getRect(new Rect((faceRect[1])*3/2, (faceRect[2])*3/2, ((faceRect[1]+faceRect[3]))*3/2,((faceRect[2]+faceRect[4]))*3/2)));
					break;
				case 0x666:
					if(str != null){
						coors = Util.strToArr(str);
						tempBitmap = faceBitmap.copy(Bitmap.Config.RGB_565, true);
						canvas = new Canvas(tempBitmap);
						canvas.drawRect(coors[1], coors[2], coors[1]+coors[3], coors[2]+coors[4], paint);
						iv_mid.setImageBitmap(tempBitmap);
						flagB = true;
					}
					break;
				case 0x6661:
					if(faceBitmap != null)
						iv_mid.setImageBitmap((Bitmap) msg.obj);
					break;
				case 0x111:
					tv_title.setText("正在比对，请离手并稍等...");
					break;
				case 0x123:
//					if(HsOtgService.ic == null){
//						tv_name.setText("读卡失败！！！");
//						tv_title.setText("请放上身份证进行比对");
//					}else{
					if(msg.obj != null){
						String[] info = (String[]) msg.obj;
						tv_name.setText("姓名：" + info[0]);
						tv_code.setText("身份号码：" + info[1]);
					}else {
						tv_name.setText("读卡失败！！！");
						tv_title.setText("请放上身份证进行比对");
					}

//					}
					break;
				case 0x155:
					soundUtil.play(0);//成功
					break;
				case 0x88889:
					//iv_ic.setImageBitmap((Bitmap) msg.obj);
					if(idFaceBitmap != null){
						iv_ic.setImageBitmap(idFaceBitmap);
						tv_title.setText("正在读卡...");
					}
					break;
				case 0x124:
					//iv_ic.setImageBitmap((Bitmap) msg.obj);
					if(idFaceBitmap != null){
						iv_ic.setImageBitmap(idFaceBitmap);
						tv_title.setText("正在比对，请离手并稍等...");
					}
					break;
				case 0x125:
					tv_result.setVisibility(View.VISIBLE);

					tv_result.setTextColor(Color.GREEN);

					tv_result.setText("比对结果：比对成功");
					Log.d("TT", "a4");

					soundUtil.playLouder(1);
					Log.d("TT", "a5");

					tv_title.setText("请放上身份证进行比对");
					Log.d("TT", "a6");

					break;
				case 0x126:
					Log.d("TT", "b1");

					tv_result.setVisibility(View.VISIBLE);
					Log.d("TT", "b2");

					tv_result.setTextColor(Color.RED);
					Log.d("TT", "b3");

					tv_result.setText("比对结果：比对失败");
					Log.d("TT", "b4");

					soundUtil.playLouder(2);
					Log.d("TT", "b5");

					tv_title.setText("请放上身份证进行比对");
					Log.d("TT", "b6");

					break;
			}
		}
	};
	private int[] coors;//人脸坐标
	private Canvas canvas;
	private Bitmap tempBitmap;
	private String str;//反馈数据
	private FileInputStream fis;
	private FileOutputStream fos;
	private Matrix m;
	private static boolean flagA;
	private static boolean flagB;
	private Thread t1, t2;
	private float result;
	private Thread cameraThread;
	ImageView iv_rect;
	private static int mWidth;
	private int mHeight;
	private int[] faceRect;
	private String rectStr;
	private Bitmap newBitmap;
	private byte[] faceData;
	YuvImage image;
	ByteArrayOutputStream out;
	private Rect area;
	private Matrix matrix;
	private int ret1;
	private FaceData oneData;
	private boolean flag_test = false;
	private Video video;
	private long time1;
	private long time9;
	private ProgressDialog serviceDialog;
	private boolean flag_result = false;
	public static SurfaceView sv_movie;
	private static MediaPlayer mediaPlayer;
	private SurfaceHolder surfaceHolder;
	public static View ll_panel;
	private Button btn_enter;
	private Bundle bundle;
	private long time7, time8;
	private long time6;
	private long time10, time11;
	private long time_read1, time_read2;


	private static void play1(String path) {
		try {
			mediaPlayer.reset();
			mediaPlayer
					.setAudioStreamType(AudioManager.STREAM_MUSIC);
			//设置需要播放的视频
			mediaPlayer.setDataSource(path);
			//把视频画面输出到SurfaceView
			mediaPlayer.setDisplay(sv_movie.getHolder());
			mediaPlayer.prepare();
			mediaPlayer.setLooping(true);
			//播放
			mediaPlayer.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void showMovie(){
		try {
			//开始播放
			play1("/data/data/org.yanzi.playcamera/files/data/test.mp4");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void stopMovie(){
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);


		area = new Rect(0, 0, 800, 600);
		matrix = new Matrix();
		matrix.postRotate(270);
		initUI();
		initData();

		new Thread(new Runnable() {
			@Override
			public void run() {
				Thread.currentThread().setPriority(10);
				while (true) {
					try{
						doVeri();
					}catch (Exception e){
						Log.d("TT", "is exception!!!	"+e.getMessage());
					}
				}
			}
		}).start();


		testView.setVisibility(View.VISIBLE);
		testView2.setVisibility(View.VISIBLE);
		if (iv_ic.getVisibility() == View.INVISIBLE) {
			iv_ic.setVisibility(View.VISIBLE);
			tv_title.setText("请放上身份证进行比对");
			ll_progressBar.setVisibility(View.GONE);
			ll_info.setVisibility(View.VISIBLE);
		}


//		//todo 用来检测更新的线程
//		Thread updateThread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (true){
//					if(NetUtil.isNetworkConnected(CameraActivity.this)){
//						OkHttpUtils
//								.get()
//								.url("http://192.168.111.111:8080/PictureUpdate/VersionUpdataServlet")
//								.build()
//								.execute(new StringCallback() {
//									@Override
//									public void onError(Call call, Exception e, int id) {
//										Log.d("Update", "请求失败 "+e.getMessage());
//									}
//
//									@Override
//									public void onResponse(String response, int id) {
//										Log.d("Update", "成功"+response);
//										Gson gson = new Gson();
//										version = gson.fromJson(response, ApkVersion.class);
//										Log.d("TT", version.getAPKUrl()+"\n"
//												+version.getImportantLevel()+"\n"
//												+version.getVersionNumber());
//
//										new Thread(new Runnable() {
//											@Override
//											public void run() {
//												try {
//													if(!version.getVersionNumber().equals("1")){
//														URL url = new URL("http://"+version.getAPKUrl());
//														Log.d("Update", "the addr is "+"http://"+version.getAPKUrl());
//														HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//														Log.d("Update", "1"+(conn == null));
//														assert conn != null;
//														conn.connect();
//														Log.d("Update", "2");
//
//														InputStream is = conn.getInputStream();
//														Log.d("Update", "3");
//
//														if(conn.getContentLength() > 0){
//															Log.d("Update", "start download!!!!!");
//															FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory()+"/app_debug.apk");
//															byte[] bytes = new byte[1024];
//															int read = 0;
//															while ((read = is.read(bytes))!=-1){
//																fos.write(bytes, 0, read);
//															}
//															fos.flush();
//															fos.close();
//															Log.d("Update", "download apk success!!!");
//															Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/app_debug.apk"));
//															Intent localIntent = new Intent(Intent.ACTION_VIEW);
//															localIntent.setDataAndType(uri, "application/vnd.android.package-archive");
//															startActivity(localIntent);
//															Thread.sleep(20000);
//															Intent i = getBaseContext().getPackageManager()
//																	.getLaunchIntentForPackage(getBaseContext().getPackageName());
//															i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//															startActivity(i);
//														}
//
//													}
//												} catch (NullPointerException e) {
//													e.printStackTrace();
//													Log.d("Update", "1update is error "+e.getMessage());
//												} catch (FileNotFoundException e) {
//													e.printStackTrace();
//													Log.d("Update", "2update is error "+e.getMessage());
//
//												} catch (MalformedURLException e) {
//													e.printStackTrace();
//													Log.d("Update", "3update is error "+e.getMessage());
//
//												} catch (IOException e) {
//													e.printStackTrace();
//													Log.d("Update", "4update is error "+e.getMessage());
//
//												} catch (InterruptedException e) {
//													e.printStackTrace();
//												}
//											}
//										}).start();
//
//									}
//								});
//					}
//					try {
//						Thread.sleep(1000000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});

		//updateThread.start();




		/**
		 * 新建一个子线程来执行计时器任务
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setPriority(1);
				while (true) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv_current_time.setText(sdf.format(new Date()));
						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();


		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (iv_ic.getVisibility() == View.INVISIBLE && cameraFaceBitmap != null) {
								iv_ic.setVisibility(View.VISIBLE);
								tv_title.setText("请放上身份证进行比对");
								ll_progressBar.setVisibility(View.GONE);
								ll_info.setVisibility(View.VISIBLE);
							}
						}
					});
				}
			}
		}).start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		btn_enter.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onBackPressed() {
	}

	//人脸比对的逻辑
	private void doVeri() {
		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(HSinterface != null){
				if(HSinterface.Authenticate() == 1){
					break;
				}
			}else{
				service = new Intent(CameraActivity.this, HsOtgService.class);
				conn = new MyConn();
				bindService(service, conn, Service.BIND_AUTO_CREATE);
			}
		}
		handler.sendEmptyMessage(0x88889);
		time_read1 = System.currentTimeMillis();
		ret1 = HSinterface.ReadCard();
		handler.sendEmptyMessage(0x999);

		if (ret1 == 1){//成功
			try {
				ret1 = HSinterface.Unpack();// 照片解码
				handler.sendEmptyMessage(0x124);
				fis = new FileInputStream(filepath + "/zp.bmp");
				idFaceBitmap = BitmapFactory.decodeStream(fis);
				fis.close();
				faceData = surfaceView.saveScreenshot();

				try{
					if(idFaceBitmap != null){
						Log.d("Hope", "s2");

						Util.saveBitmap(idFaceBitmap, "idcard.bmp");
						str = JniTool.faceFeatureExtractCamera1(faceData);
						int i = Util.strToArr(str)[0];
						Log.d("Hope", "s3 "+i);

						int j = JniTool.faceFeatureExtractIDCard("idcard.bmp");

						Log.d("Hope", "s4 "+j);


						if(i != 0 && j != 0){
							result = JniTool.faceFeatureCompare();
							Log.d("Hope", "result's value is "+result);
						}

						try{
							if(i == 0 || j == 0){
								handler.sendEmptyMessage(0x126);
								Log.d("Hope", "step1");
							}else {
								Log.d("Hope", "step2");
								if(coors == null){
									Log.d("Hope", "step4");
									if(result > 0.29f){
										Log.d("Hope", "step5");

										handler.sendEmptyMessage(0x125);
										flag_result = true;
									} else {
										Log.d("Hope", "step6");

										handler.sendEmptyMessage(0x126);
										flag_result = true;
									}
								} else {
									Log.d("Hope", "step7");

									if(result > 0.29f && coors[0] != 0){
										Log.d("Hope", "step8");

										handler.sendEmptyMessage(0x125);
										flag_result = true;
									} else {
										Log.d("Hope", "step9");

										handler.sendEmptyMessage(0x126);
										flag_result = true;
									}
								}
							}
						}catch (Exception e){
							handler.sendEmptyMessage(0x126);
						}
					}
				}catch (Exception e){
				}

				if(faceData != null){
					image = new YuvImage(faceData, 17, 800, 600, null);
					out = new ByteArrayOutputStream();
					image.compressToJpeg(area, 100, out);
					cameraFaceBitmap = Bitmap.createBitmap(BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size()), 0, 0,800, 600, matrix, true);
				}

				if(cameraFaceBitmap != null){
					faceBitmap = util.rotateBitmap(cameraFaceBitmap, m);
					Message msg = Message.obtain();
					msg.what = 0x6661;
					msg.obj = faceBitmap;
					handler.sendMessage(msg);
				}
				//Util.saveBitmap(faceBitmap, "face.bmp");
				String[] info = {HsOtgService.ic.getPeopleName(),
						HsOtgService.ic.getIDCard()};
				Message msg = Message.obtain();
				msg.obj = info;
				msg.what = 0x123;
				handler.sendMessage(msg);
				handler.sendEmptyMessage(0x666);

//				FaceData faceData = new FaceData();
//				faceData.setCameraFaceImg(Util.convertIconToString(faceBitmap));
//				faceData.setIdCardFaceImg(Util.convertIconToString(idFaceBitmap));
//				faceData.setName(HsOtgService.ic.getPeopleName());
//				faceData.setSex(HsOtgService.ic.getSex());
//				faceData.setPeople(HsOtgService.ic.getPeople());
//				faceData.setDateOfBirth(HsOtgService.ic.getBirthDay().toLocaleString());
//				faceData.setAddr(HsOtgService.ic.getAddr());
//				faceData.setCode(HsOtgService.ic.getIDCard());
//				faceData.setDepartment(HsOtgService.ic.getDepartment());
//				faceData.setStartDate(HsOtgService.ic.getStrartDate());
//				faceData.setEndDate(HsOtgService.ic.getEndDate());
//				faceData.setSimilar(result);
//				if((faceData.getSimilar()+"").equals("NaN")){
//					faceData.setSimilar(0);
//				}
//				faceData.setCurrentTime(Util.getCurrTime());
//				faceData.setDeviceId(Util.getIMEI(CameraActivity.this));
//
//				try {
//					dbUtils.save(faceData);
//				} catch (DbException e) {
//					e.printStackTrace();
//				}


			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}catch (Exception e)
			{
			}finally {
				HsOtgService.ic = null;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0x999);
				return;
			}
		}



	}


	public void pushData(final FaceData data, final PushDataEnd end){
		OkHttpUtils
				.post()
				.url(DATA_URL)
				.addParams("name", data.getName())
				.addParams("similar", data.getSimilar()+"")
				.addParams("code", data.getCode())
				.addParams("CameraFaceImg", data.getCameraFaceImg())
				.addParams("IdCardFaceImg", data.getIdCardFaceImg())
				.addParams("sex", data.getSex())
				.addParams("people", data.getPeople())
				.addParams("dateOfBirth", data.getDateOfBirth())
				.addParams("addr", data.getAddr())
				.addParams("department", data.getDepartment())
				.addParams("startDate", data.getStartDate())
				.addParams("endDate", data.getEndDate())
				.addParams("deviceId", data.getDeviceId())
				.addParams("currentTime", data.getCurrentTime())
				.build().connTimeOut(20000)
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						Log.d("Test", "请求失败 ");
						end.failure(e);
					}

					@Override
					public void onResponse(String response, int id) {
						Log.d("Test", "成功"+response);
						end.success(response);
					}
				});
	}

	interface PushDataEnd{
		void success(String response);
		void failure(Exception e);
	}

	private void initUI(){
		tv_info = (TextView) findViewById(R.id.tv_face_info);
		btn_enter = (Button) findViewById(R.id.btn_enter);
		m = new Matrix();
		m.setRotate(90);
		m.postScale(0.4f, 0.4f);
		paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.STROKE);//不填充
		paint.setStrokeWidth(2);  //线的宽度
		ll_panel = findViewById(R.id.test);
		sv_movie = (SurfaceView) findViewById(R.id.sv_movie);
		sv_movie.setTranslationX(-1280f);
		iv_mid = (ImageView) findViewById(R.id.iv_mid);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setTextColor(Color.YELLOW);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_code = (TextView) findViewById(R.id.tv_code);
		tv_result = (TextView) findViewById(R.id.tv_result);
		iv_ic = (ImageView) findViewById(R.id.iv_ic);
		testView = findViewById(R.id.test);
		testView2 = findViewById(R.id.test2);
		ll_info = findViewById(R.id.ll_info);
		ll_progressBar = findViewById(R.id.ll_progressBar);
		surfaceView = (CameraSurfaceView)findViewById(R.id.camera_surfaceview);
		iv_face = (ImageView) findViewById(R.id.iv_face);
		tv_current_time = (TextView) findViewById(R.id.tv_current_time);

		WindowManager wm = (WindowManager)
				getSystemService(Context.WINDOW_SERVICE);

		mWidth = wm.getDefaultDisplay().getWidth();
		mHeight = wm.getDefaultDisplay().getHeight();

		faceView = (FaceView) findViewById(R.id.faceView);
	}

	public static Rect getRect(Rect rect){
		return new Rect(mWidth-rect.bottom, rect.left, mWidth-rect.top, rect.right);
	}

	private void initData() {
		util = new Util();
		sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
		soundUtil = new SoundUtil(this, R.raw.start, R.raw.success, R.raw.failure);
		dbUtils = DbUtils.create(this);
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
		filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wltlib";// 授权目录
		service = new Intent(CameraActivity.this, HsOtgService.class);
		conn = new MyConn();
		bindService(service, conn, Service.BIND_AUTO_CREATE);
	}
	@Override
	public void onStart() {
		super.onStart();
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Main Page", // TODO: Define a title for the content shown.
				Uri.parse("http://host/path"),
				Uri.parse("android-app://org.yanzi.playcamera/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}
	@Override
	public void onStop() {
		super.onStop();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Main Page", // TODO: Define a title for the content shown.
				Uri.parse("http://host/path"),
				Uri.parse("android-app://org.yanzi.playcamera/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("TT", "onDestroy");
		JniTool.free();
	}
	class MyConn implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			HSinterface = (HSInterface) service;
			int i = 2;
			while (i > 0) {
				i--;
				int ret = HSinterface.init();
				if (ret == 1) {
					i = 0;
				} else {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			conn = null;
			HSinterface = null;
		}
	}

}


