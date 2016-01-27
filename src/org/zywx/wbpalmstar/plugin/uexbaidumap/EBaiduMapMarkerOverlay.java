package org.zywx.wbpalmstar.plugin.uexbaidumap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

public class EBaiduMapMarkerOverlay extends EBaiduMapOverlay implements OnInfoWindowClickListener {

	private Marker mMarker = null;
	private View bubbleView = null;
	private InfoWindow mInfoWindow = null;
	private String bubbleTitleStr = null;
	private final int bubbleDefaultYOffset = -75;

	public EBaiduMapMarkerOverlay(String id, Context context, BaiduMap baiduMap) {

		super(id, context, baiduMap);

	}

	public void setMarker(Marker maker) {
		mMarker = maker;
	}

	public Marker getMarker() {
		return mMarker;
	}

	/**
	 * 原来设置的气泡
	 * 
	 * @param title
	 * @param subTitlte
	 * @param imgPath
	 * @param yOffset
	 * @param isUseYOffset
	 */
	public void setBubbleViewData(String title, String subTitlte, String imgPath, int yOffset, boolean isUseYOffset) {

		Button bubbleBtn;

		if (title == null) {

			return;
		}

		setBubbleTitleStr(title);

		if (bubbleView == null) {

			int bubbleResId = EUExUtil.getResLayoutID("plugin_uexbaidumap_marker_bubble");
			bubbleView = LayoutInflater.from(mContext).inflate(bubbleResId, null);

		}

		bubbleBtn = (Button) bubbleView.findViewById(EUExUtil.getResIdID("imageView1"));

		String titleString;

		if (imgPath != null) { //

			Bitmap defaultImage = EBaiduMapUtils.getBitMapFromImageUrl(mContext, imgPath);

			if (defaultImage != null) {
				bubbleBtn.setBackgroundDrawable(EBaiduMapUtils.bgColorDrawableSelector(defaultImage, defaultImage));
			}

			titleString = bubbleTitleStr;
		} else { // 使用默认的
			titleString = bubbleTitleStr;
			bubbleBtn.setBackgroundResource(EUExUtil.getResDrawableID("plugin_map_bubble_bg_default"));
		}

		bubbleBtn.setText(titleString);

		LatLng ll = mMarker.getPosition();

		try {
			if (isUseYOffset) { // 重新生成对象，设置位置偏移
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(bubbleView), ll, yOffset, this);
			} else {
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(bubbleView), ll, bubbleDefaultYOffset,
						this);
			}
		} catch (Exception e) {
		}

	}

	/**
	 * 上海贝尔项目定制气泡 by waka 2016-01-25
	 * 
	 * @param titleLeftTop
	 * @param titleRightTop
	 * @param titleBottom
	 * @param yOffset
	 * @param isUseYOffset
	 */
	public void setBubbleViewDataShanghaiBeier(String width, String bgColor, String titleLeftTop, String titleRightTop,
			String titleBottom, int yOffset, boolean isUseYOffset) {

		LinearLayout layoutBubble;
		LinearLayout layoutTop;
		int width_new = 200;// 默认宽度
		String bgColor_new = "#000000";// 默认颜色
		TextView tvLeftTop, tvRightTop, tvBottom;// 三个title

		// 三个title必须传
		if (titleLeftTop == null || titleRightTop == null || titleBottom == null) {
			Log.i("uexBaiduMap setBubbleViewDataShanghaiBeier", "titleLeftTop||titleRightTop||titleBottom=null");
			return;
		}

		// 宽度不传的话默认为200
		if (width != null) {
			try {
				width_new = (int) Float.parseFloat(width);
			} catch (NumberFormatException e) {
				Log.i("uexBaiduMap setBubbleViewDataShanghaiBeier", "width NumberFormatException");
				e.printStackTrace();
			}
		}

		// 看传进来的颜色符不符合规则
		bgColor_new = EBaiduMapUtils.colorJudgment(bgColor);
		if (bgColor_new == null) {
			bgColor_new = "#000000";// 修正颜色，设置默认颜色为黑色
		}

		setBubbleTitleStr(titleBottom);

		if (bubbleView == null) {
			int bubbleResId = EUExUtil.getResLayoutID("plugin_uexbaidumap_marker_bubble_shanghai_beier");
			bubbleView = LayoutInflater.from(mContext).inflate(bubbleResId, null);
		}

		layoutBubble = (LinearLayout) bubbleView.findViewById(EUExUtil.getResIdID("layoutBubble"));
		layoutTop = (LinearLayout) bubbleView.findViewById(EUExUtil.getResIdID("layoutTop"));
		tvLeftTop = (TextView) bubbleView.findViewById(EUExUtil.getResIdID("tvLeftTop"));
		tvRightTop = (TextView) bubbleView.findViewById(EUExUtil.getResIdID("tvRightTop"));
		tvBottom = (TextView) bubbleView.findViewById(EUExUtil.getResIdID("tvBottom"));

		tvLeftTop.setText(titleLeftTop);
		tvRightTop.setText(titleRightTop);
		tvBottom.setText(titleBottom);

		width_new = EBaiduMapUtils.dip2px(mContext, width_new);

		// 设置宽度
		LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(width_new, LayoutParams.WRAP_CONTENT);
		layoutTop.setLayoutParams(topParams);

		// 设置气泡背景颜色
		layoutBubble.getBackground().setColorFilter(Color.parseColor(bgColor_new), PorterDuff.Mode.SRC_IN);

		LatLng ll = mMarker.getPosition();
		try {
			if (isUseYOffset) { // 重新生成对象，设置位置偏移
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(bubbleView), ll, yOffset, this);
			} else {
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(bubbleView), ll, bubbleDefaultYOffset,
						this);
			}
		} catch (Exception e) {
			Log.i("uexBaiduMap setBubbleViewDataShanghaiBeier ", e.getMessage());
			e.printStackTrace();
		}
	}

	public void setBubbleShow(boolean isShow) {

		if (isShow == true) {

			if (bubbleTitleStr != null) {

				if (mInfoWindow != null) {
					mBaiduMap.showInfoWindow(mInfoWindow);
				}

			}

		} else {
			mBaiduMap.hideInfoWindow();
		}

	}

	public String getBubbleTitleStr() {
		return bubbleTitleStr;
	}

	public void setBubbleTitleStr(String bubbleTitleStr) {
		this.bubbleTitleStr = bubbleTitleStr;
	}

	@Override
	public void onInfoWindowClick() {
		// TODO Auto-generated method stub

		EBaiduMapBaseActivity activity = (EBaiduMapBaseActivity) mContext;

		if (activity != null && activity instanceof EBaiduMapBaseActivity) {

			EUExBaiduMap uexBaiduMap = activity.getUexBaseObj();

			String js = EUExBaiduMap.SCRIPT_HEADER + "if(" + EBaiduMapUtils.MAP_FUN_ON_MAKER_BUBBLE_CLICK_LISTNER + "){"
					+ EBaiduMapUtils.MAP_FUN_ON_MAKER_BUBBLE_CLICK_LISTNER + "('" + mIDString + "');}";
			uexBaiduMap.onCallback(js);

			String json = EUExBaiduMap.SCRIPT_HEADER + "if(" + EBaiduMapUtils.MAP_FUN_ON_MARKER_BUBBLE_CLICK_LISTENER
					+ "){" + EBaiduMapUtils.MAP_FUN_ON_MARKER_BUBBLE_CLICK_LISTENER + "('" + mIDString + "');}";
			uexBaiduMap.onCallback(json);

		}

	}

	@Override
	public void clearOverlay() {
		mMarker.remove();
		mMarker.getIcon().recycle();
		bubbleView = null;
		mInfoWindow = null;
	}

}
