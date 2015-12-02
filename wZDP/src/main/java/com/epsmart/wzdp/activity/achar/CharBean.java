package com.epsmart.wzdp.activity.achar;

import java.util.List;

public class CharBean  {
	/* 标题 */
	private String title;
	/* x轴坐标名称 */
	private String xTitle;
	/* y轴坐标名称 */
	private String yTitle;
	/* x轴显示多少个坐标点 */
	private int xLeable;// 设置x轴显示?个点,根据最大值和最小值自动计算点的间隔
	/* y轴显示多少个坐标点 */
	private int yLeable;
	/* 线条颜色 */
	private int[] colors;
	/* 底部条线显示名称 */
	private String[] bTitles;
	/* 依次显示 x轴最小值 x轴最大值 y轴最小值 y轴最大值 */
	private double[] xyAxis;
	/* 设置缩放范围 */
	private double[] panLimits;
	/* 设置X轴显示刻度 */
	private List<double[]> xValue;
	/* 设置Y轴显示刻度 */
	private List<double[]> yValue;
	/* 表格显示设置Y轴显示刻度 */
	private List<String[]> yTableValue;
	public List<String[]> getyTableValue() {
		return yTableValue;
	}
	public void setyTableValue(List<String[]> yTableValue) {
		this.yTableValue = yTableValue;
	}
	/*行标题*/
	private String[] lineTitle;
	//列坐标
	private String[] columnTitle;
	
	private boolean isShowXtitle;
	
	public boolean isShowXtitle() {
		return isShowXtitle;
	}
	public void setShowXtitle(boolean isShowXtitle) {
		this.isShowXtitle = isShowXtitle;
	}
	public String[] getLineTitle() {
		return lineTitle;
	}
	public void setLineTitle(String[] lineTitle) {
		this.lineTitle = lineTitle;
	}
	
	public String[] getColumnTitle() {
		return columnTitle;
	}
	public void setColumnTitle(String[] columnTitle) {
		this.columnTitle = columnTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getxTitle() {
		return xTitle;
	}
	public void setxTitle(String xTitle) {
		this.xTitle = xTitle;
	}
	public String getyTitle() {
		return yTitle;
	}
	public void setyTitle(String yTitle) {
		this.yTitle = yTitle;
	}
	public int getxLeable() {
		return xLeable;
	}
	public void setxLeable(int xLeable) {
		this.xLeable = xLeable;
	}
	public int getyLeable() {
		return yLeable;
	}
	public void setyLeable(int yLeable) {
		this.yLeable = yLeable;
	}
	public int[] getColors() {
		return colors;
	}
	public void setColors(int[] colors) {
		this.colors = colors;
	}
	public String[] getbTitles() {
		return bTitles;
	}
	public void setbTitles(String[] bTitles) {
		this.bTitles = bTitles;
	}
	public double[] getXyAxis() {
		return xyAxis;
	}
	public void setXyAxis(double[] xyAxis) {
		this.xyAxis = xyAxis;
	}
	public double[] getPanLimits() {
		return panLimits;
	}
	public void setPanLimits(double[] panLimits) {
		this.panLimits = panLimits;
	}
	public List<double[]> getxValue() {
		return xValue;
	}
	public void setxValue(List<double[]> xValue) {
		this.xValue = xValue;
	}
	public List<double[]> getyValue() {
		return yValue;
	}
	public void setyValue(List<double[]> yValue) {
		this.yValue = yValue;
	}
	


}
