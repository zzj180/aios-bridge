package com.aispeech.aios.bridge.vendor;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.aispeech.aios.bridge.oem.RomSystemSetting;
import com.aispeech.aios.bridge.utils.APPUtil;

/**
 * @desc 高德接口类
 * @auth zzj
 * @date 2016-03-17
 */
public class GDOperate {

	private Context context;
	private static GDOperate mInstance;

	public GDOperate(Context context) {
		this.context = context;
	}

	public static synchronized GDOperate getInstance(Context context) {
		if (null == mInstance) {
			mInstance = new GDOperate(context);
		}
		return mInstance;
	}

	public void startNavigation(double lat, double lon) {
		// this.checkGDReNavigation();//删除poi遗留
		if (APPUtil.getInstance().isInstalled(APPUtil.GD_CARJ_PKG)) {
			Intent intent = new Intent("AUTONAVI_STANDARD_BROADCAST_RECV");
			intent.putExtra("KEY_TYPE", 10038);
			intent.putExtra("LAT", lat);
			intent.putExtra("LON", lon);
			intent.putExtra("DEV", 0);
			intent.putExtra("STYLE", 2);
			context.sendBroadcast(intent);
		} else if (APPUtil.getInstance().isInstalled(APPUtil.GD_CAR_PKG)) {
			String cat = "android.intent.category.DEFAULT";
			String dat = "androidauto://navi?sourceApplication=TXZAssistant&lat="
					+ lat + "&lon=" + lon + "&dev=0&style=2";
			Intent i = new Intent();
			i.setData(android.net.Uri.parse(dat));
			i.setPackage(APPUtil.GD_CAR_PKG);
			i.addCategory(cat);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			context.startActivity(i);
		} else if (APPUtil.getInstance().isInstalled(APPUtil.GD_MAP_PKG)) {
			String cat = "android.intent.category.DEFAULT";
			String dat = "androidamap://navi?sourceApplication=TXZAssistant&lat="
					+ lat + "&lon=" + lon + "&dev=0&style=2";
			Intent i = new Intent();
			i.setData(android.net.Uri.parse(dat));
			i.setPackage(APPUtil.GD_MAP_PKG);
			i.addCategory(cat);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			context.startActivity(i);
		} else {
			Toast.makeText(context, "请先安装高德地图", Toast.LENGTH_LONG).show();
		}
	}

	public void closeMap() {// 强制结束掉高德APP，需要root权限
		if (APPUtil.getInstance().isInstalled(APPUtil.GD_CARJ_PKG)) {
			closeGDCARJMap();
		}
		if (APPUtil.getInstance().isInstalled(APPUtil.GD_CAR_PKG)) {
			closeCarMap();
		}
		if (APPUtil.getInstance().isInstalled(APPUtil.GD_MAP_PKG)) {
			closeGDMap();
		}

	}
	
/*	private void closeMapBy() {// 强制结束掉高德APP，需要root权限
		if (APPUtil.getInstance().isInstalled(APPUtil.GD_CAR_PKG)) {
			AdapterApplication.runOnUiGround(new Runnable() {

				@Override
				public void run() {
					try {
						RomSystemSetting.forceStopPackage(APPUtil.GD_CAR_PKG);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 1000);
		}
		if (APPUtil.getInstance().isInstalled(APPUtil.GD_MAP_PKG)) {
			closeGDMap();
		}

	}*/

	/**
	 * 退出高德
	 */
	private void closeGDMap() {// 强制结束掉高德APP，需要root权限

		context.sendBroadcast(new Intent("com.amap.stopnavi"));
		Intent mIntent = new Intent("com.autonavi.minimap.carmode.command");
		mIntent.putExtra("NAVI", "APP_EXIT");
		context.sendBroadcast(mIntent);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					RomSystemSetting.forceStopPackage(APPUtil.GD_MAP_PKG);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1000);

	}
	
	private void closeGDCARJMap() {// 强制结束掉高德APP，需要root权限

		Intent mIntent = new Intent("AUTONAVI_STANDARD_BROADCAST_RECV");
		mIntent.putExtra("KEY_TYPE",10010);
		context.sendBroadcast(mIntent);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					RomSystemSetting.forceStopPackage(APPUtil.GD_CARJ_PKG);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1000);

	}

	/**
	 * 当前位置，并在地图定位
	 */
	public void locate() {
		
		if (APPUtil.getInstance().isInstalled(APPUtil.GD_CARJ_PKG)) {
    		Intent i = new Intent("AUTONAVI_STANDARD_BROADCAST_RECV");
    		i.putExtra("KEY_TYPE", 10008);
    		context.sendBroadcast(i);
        } else if (APPUtil.getInstance().isInstalled(APPUtil.GD_CAR_PKG)) {
			String act = "android.intent.action.VIEW";
			String cat = "android.intent.category.DEFAULT";
			String data = "androidauto://myLocation?sourceApplication=TXZAssistant";
			Intent i = new Intent();
			i.setAction(act);
			i.addCategory(cat);
			i.setData(android.net.Uri.parse(data));
			i.setPackage(APPUtil.GD_CAR_PKG);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			context.startActivity(i);
		} else if (APPUtil.getInstance().isInstalled(APPUtil.GD_MAP_PKG)) {
			String act = "android.intent.action.VIEW";
			String cat = "android.intent.category.DEFAULT";
			String data = "androidamap://myLocation?sourceApplication=TXZAssistant";
			Intent i = new Intent();
			i.setAction(act);
			i.addCategory(cat);
			i.setData(android.net.Uri.parse(data));
			i.setPackage(APPUtil.GD_MAP_PKG);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			context.startActivity(i);
		} else {
			Toast.makeText(context, "请先安装高德地图", Toast.LENGTH_LONG).show();
		}

	}

	public void closeCarMap() {// 强制结束掉高德APP，需要root权限
		/*Intent intent = new Intent("android.intent.action.VIEW",
				android.net.Uri.parse("androidauto://naviExit?sourceApplication=TXZAssistant"));
		intent.setPackage("com.autonavi.amapauto");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		context.startActivity(intent);*/
		
		Intent intent = new Intent("AUTONAVI_STANDARD_BROADCAST_RECV");
		intent.putExtra("KEY_TYPE", 10021);
		context.sendBroadcast(intent);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					RomSystemSetting.forceStopPackage(APPUtil.GD_CAR_PKG);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1500);

	}

}
