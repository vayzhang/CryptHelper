package com.vay.crypthelper.event;

public class WaterMarkerStyle {

	private String text;
	
	private int fontSize = 48;
	
	private float opacity = 0.5f;
	
	private int xSize = 3;
	
	private int ySize = 4;
	
	
	public WaterMarkerStyle() {
		super();
	}

	public WaterMarkerStyle(String text, int fontSize, float opacity, int xSize, int ySize) {
		super();
		this.text = text;
		this.fontSize = fontSize;
		this.opacity = opacity;
		this.xSize = xSize;
		this.ySize = ySize;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	public int getXSize() {
		return xSize;
	}

	public void setXSize(int xSize) {
		this.xSize = xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public void setYSize(int ySize) {
		this.ySize = ySize;
	}
	
	
	
	
}
