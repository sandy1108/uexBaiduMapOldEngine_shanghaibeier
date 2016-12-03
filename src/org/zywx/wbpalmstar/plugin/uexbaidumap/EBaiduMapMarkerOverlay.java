package org.zywx.wbpalmstar.plugin.uexbaidumap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;


public class EBaiduMapMarkerOverlay extends EBaiduMapOverlay implements OnInfoWindowClickListener {

	private Marker mMarker = null;
	private View bubbleView = null;
    private View bottomBubbleCard = null;
    private boolean showBottomCard = false;
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
    @SuppressWarnings("deprecation")
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
                bubbleBtn.setBackgroundDrawable(EBaiduMapUtils.bgColorDrawableSelector(defaultImage,defaultImage));
            }

            titleString = bubbleTitleStr;
        } else { //使用默认的
            titleString = bubbleTitleStr;
            bubbleBtn.setBackgroundResource(EUExUtil.getResDrawableID("plugin_map_bubble_bg_default"));
        }

        bubbleBtn.setText(titleString);


        LatLng ll = mMarker.getPosition();

        try {
            if (isUseYOffset) { //重新生成对象，设置位置偏移
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(bubbleView), ll, yOffset, this);
            } else {
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(bubbleView), ll, bubbleDefaultYOffset, this);
            }
            showBottomCard = false;
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
            Log.i("uexBaiduMap", "titleLeftTop||titleRightTop||titleBottom=null");
            return;
        }

        // 宽度不传的话默认为200
        if (width != null) {
            try {
                width_new = (int) Float.parseFloat(width);
            } catch (NumberFormatException e) {
                Log.i("uexBaiduMap", "width NumberFormatException");
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
        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(width_new, LinearLayout.LayoutParams.WRAP_CONTENT);
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
            Log.i("uexBaiduMap", e.getMessage());
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
		
		EBaiduMapBaseActivity activity = (EBaiduMapBaseActivity)mContext;
		
		if (activity != null && activity instanceof EBaiduMapBaseActivity) {
			
			EUExBaiduMap uexBaiduMap = activity.getUexBaseObj();
			
			String js = EUExBaiduMap.SCRIPT_HEADER + "if(" + EBaiduMapUtils.MAP_FUN_ON_MAKER_BUBBLE_CLICK_LISTNER + "){" + EBaiduMapUtils.MAP_FUN_ON_MAKER_BUBBLE_CLICK_LISTNER + "('"
                    + mIDString
                    + "');}";
			uexBaiduMap.onCallback(js);
			
			String json = EUExBaiduMap.SCRIPT_HEADER + "if(" + EBaiduMapUtils.MAP_FUN_ON_MARKER_BUBBLE_CLICK_LISTENER + "){" + EBaiduMapUtils.MAP_FUN_ON_MARKER_BUBBLE_CLICK_LISTENER + "('"
					+ mIDString
					+ "');}";
			uexBaiduMap.onCallback(json);

		}
		
	}

    public boolean isShowBottomCard() {
        return showBottomCard;
    }

    public void setShowBottomCard(boolean showBottomCard) {
        this.showBottomCard = showBottomCard;
    }

    @SuppressWarnings("deprecation")
	public void setBottomBubbleCard(MapView mapView, String bottomBubbleCardTitle, String imgPath) {
        try {
            if (bottomBubbleCard == null) {
                int resId = EUExUtil
                        .getResLayoutID("plugin_uexbaidumap_marker_bottom_bubble_card");
                bottomBubbleCard = LayoutInflater.from(mContext).inflate(resId,
                        null);
                bottomBubbleCard.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            if (imgPath != null) {
                Bitmap defaultImage = EBaiduMapUtils.getBitMapFromImageUrl(
                        mContext, imgPath);
                if (defaultImage != null) {
                    bottomBubbleCard
                            .setBackgroundDrawable(EBaiduMapUtils
                                    .bgColorDrawableSelector(defaultImage,
                                            defaultImage));
                } else {
                    bottomBubbleCard.setBackgroundColor(Color.WHITE);
                }
            } else {
                bottomBubbleCard.setBackgroundColor(Color.WHITE);
            }

            JSONObject cardJSON = new JSONObject(bottomBubbleCardTitle);
            String fontSize = cardJSON.optString(
                    EBaiduMapUtils.MAP_PARAMS_JSON_KEY_FONTSIZE, "14");
            float size = Float.parseFloat(fontSize);
            String fontColor = cardJSON.optString(
                    EBaiduMapUtils.MAP_PARAMS_JSON_KEY_FONTCOLOR, "#000000");
            int color = BUtility.parseColor(fontColor);
            
            String cardTitle1 = cardJSON.optString(
                    EBaiduMapUtils.MAP_PARAMS_JSON_KEY_CARDTITLE1, "");
            TextView bottomCardText1 = (TextView) bottomBubbleCard
                    .findViewById(EUExUtil.getResIdID("bottom_card_text1"));
            bottomCardText1.setText(cardTitle1);
            bottomCardText1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
            bottomCardText1.setTextColor(color);

            String cardTitle2 = cardJSON.optString(
                    EBaiduMapUtils.MAP_PARAMS_JSON_KEY_CARDTITLE2, "");
            TextView bottomCardText2 = (TextView) bottomBubbleCard
                    .findViewById(EUExUtil.getResIdID("bottom_card_text2"));
            bottomCardText2.setText(cardTitle2);
            bottomCardText2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
            bottomCardText2.setTextColor(color);

            String cardTitle3 = cardJSON.optString(
                    EBaiduMapUtils.MAP_PARAMS_JSON_KEY_CARDTITLE3, "");
            TextView bottomCardText3 = (TextView) bottomBubbleCard
                    .findViewById(EUExUtil.getResIdID("bottom_card_text3"));
            bottomCardText3.setText(cardTitle3);
            bottomCardText3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
            bottomCardText3.setTextColor(color);
            
            String cardTitle4 = cardJSON.optString(
                    EBaiduMapUtils.MAP_PARAMS_JSON_KEY_CARDTITLE4, "");
            TextView bottomCardText4 = (TextView) bottomBubbleCard
                    .findViewById(EUExUtil.getResIdID("bottom_card_text4"));
            bottomCardText4.setText(cardTitle4);
            bottomCardText4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
            bottomCardText4.setTextColor(color);

            bottomBubbleCard.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInfoWindowClick();
                }
            });
            showBottomCard = true;
        } catch (JSONException e) {
        }
    }

    public void setBottomCardShow(MapView mapView, boolean isShow) {
        if (isShow == true) {
            if (mapView != null) {
                showOrAddBottomCard(mapView);
            }
        } else {
            removeBottomCard();
        }
    }

    private void showOrAddBottomCard(MapView mapView) {
        if (bottomBubbleCard != null) {
            if (bottomBubbleCard.getParent() == null) {
                EBaiduMapBaseActivity activity = (EBaiduMapBaseActivity) mContext;
                if (activity != null
                        && activity instanceof EBaiduMapBaseActivity) {
                    EUExBaiduMap uexBaiduMap = activity.getUexBaseObj();
                    uexBaiduMap.addCardView2Window(bottomBubbleCard);
                }
            } else {
                bottomBubbleCard.bringToFront();
                bottomBubbleCard.invalidate();
            }
        }
    }

    private void removeBottomCard() {
        if (bottomBubbleCard != null) {
            EBaiduMapBaseActivity activity = (EBaiduMapBaseActivity) mContext;
            if (activity != null && activity instanceof EBaiduMapBaseActivity) {
                EUExBaiduMap uexBaiduMap = activity.getUexBaseObj();
                uexBaiduMap.removeViewFromCurrentWindow(bottomBubbleCard);
            }
        }
    }

	@Override
	public void clearOverlay() {
		 mMarker.remove();
	     mMarker.getIcon().recycle();
	     bubbleView = null;
	     mInfoWindow = null;
         removeBottomCard();
         bottomBubbleCard = null;
	}


	
}
