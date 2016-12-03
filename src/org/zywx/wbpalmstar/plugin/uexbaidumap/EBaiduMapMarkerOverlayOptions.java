package org.zywx.wbpalmstar.plugin.uexbaidumap;

/**
 * Created with IntelliJ IDEA. User: liguangqiao Date: 14/11/19 Time: 下午7:19 To
 * change this template use File | Settings | File Templates.
 */
public class EBaiduMapMarkerOverlayOptions extends EBaiduMapOverlayOptions {
	private String lngStr;
	private String latStr;
	private String iconPath;
	private String bubbleTitle;
	private String bubbleSubTitle;
	private String bubbleBgImgPath;
    private String bottomBubbleCard;
	private int yOffset;
	private boolean iUseYOffset = false;

	// 上海贝尔定制版，增加字段 by_waka
	private String type;// 气泡类型，用来区分气泡风格
	private String width;// 气泡宽度
	private String bgColor;// 背景颜色
	private String title1;// 左上角文本
	private String title2;// 右上角文本
	private String title3;// 下方文本

	public EBaiduMapMarkerOverlayOptions() {

	}
	
	public String getLngStr() {
		return lngStr;
	}

	public void setLngStr(String lngStr) {
		this.lngStr = lngStr;
	}

	public String getLatStr() {
		return latStr;
	}

	public void setLatStr(String latStr) {
		this.latStr = latStr;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getBubbleTitle() {
		return bubbleTitle;
	}

	public void setBubbleTitle(String bubbleTitle) {
		this.bubbleTitle = bubbleTitle;
	}

	public String getBubbleSubTitle() {
		return bubbleSubTitle;
	}

	public void setBubbleSubTitle(String bubbleSubTitle) {
		this.bubbleSubTitle = bubbleSubTitle;
	}

	public String getBubbleBgImgPath() {
		return bubbleBgImgPath;
	}

	public void setBubbleBgImgPath(String bubbleBgImgPath) {
		this.bubbleBgImgPath = bubbleBgImgPath;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	public boolean isiUseYOffset() {
		return iUseYOffset;
	}

	public void setiUseYOffset(boolean iUseYOffset) {
		this.iUseYOffset = iUseYOffset;
	}

    public String getBottomBubbleCard() {
        return bottomBubbleCard;
    }

    public void setBottomBubbleCard(String bottomBubbleCard) {
        this.bottomBubbleCard = bottomBubbleCard;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getTitle3() {
		return title3;
	}

	public void setTitle3(String title3) {
		this.title3 = title3;
	}

}
