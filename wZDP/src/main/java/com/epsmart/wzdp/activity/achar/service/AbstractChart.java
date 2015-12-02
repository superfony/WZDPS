package com.epsmart.wzdp.activity.achar.service;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.XYChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.TextUtils;
import android.view.View;

import com.epsmart.wzdp.activity.achar.CharBean;
import com.epsmart.wzdp.activity.supply.bean.BasicEntity;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.bean.Field;

/**
 * 提取LineChar BarChar 公共的部分
 * 
 * @author fony 设置表属性
 */

public abstract class AbstractChart {

	protected XYMultipleSeriesRenderer renderer;
	protected XYMultipleSeriesDataset buildDataset;
	protected GraphicalView view;
	protected XYChart xyChar;
	protected int SERIES_NR = 0;
	protected CharBean charBean;

	public AbstractChart() {
	}

	protected void buildDataset(XYMultipleSeriesDataset dataset,
			CharBean charBean) {
	}

	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}

	protected XYMultipleSeriesRenderer buildRenderer(int[] colors) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors);
		return renderer;
	}

	/**
	 * 折线图 设置不经常变动的参数
	 */
	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
			PointStyle[] styles) {
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			r.setFillPoints(true);// 点是不是实心的
			r.setLineWidth(5);//折线的宽度
			renderer.addSeriesRenderer(r);
		}
	}
	/* 设置圆柱体颜色 */
	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors) {
		setRenderer(renderer);
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.TRANSPARENT);
		renderer.addSeriesRenderer(r);
	}

	protected void setRenderer(XYMultipleSeriesRenderer renderer) {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		
		renderer.setLegendTextSize(15);
		renderer.setApplyBackgroundColor(true);// 设置背景可自定义
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setMarginsColor(Color.parseColor("#444444"));// 设置刻度里面的背景
		renderer.setBackgroundColor(Color.parseColor("#444444"));// 设置表格外的背景
		renderer.setMargins(new int[] { 100, 50, 50, 50 });// 设置显示表格区域边距 依次 上下左右
	}

	/**
	 * 
	 * @param renderer
	 * @param title
	 *            顶部标题
	 * 
	 * @param xTitle
	 *            x轴标题
	 * @param yTitle
	 *            y轴标题
	 * @param xMin
	 *            x周
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @param axesColor
	 * @param labelsColor
	 */
	protected void setRenderer(XYMultipleSeriesRenderer renderer,
			CharBean charbean) {
		setRenderer(renderer);
		renderer.setPanLimits(charbean.getPanLimits());
		renderer.setZoomLimits(charbean.getPanLimits());
		renderer.setChartTitle(charbean.getTitle());
		renderer.setXTitle(charbean.getxTitle());
		renderer.setYTitle(charbean.getyTitle());
		renderer.setXAxisMin(charbean.getXyAxis()[0]);
		renderer.setXAxisMax(charbean.getXyAxis()[1]);
		renderer.setYAxisMin(charbean.getXyAxis()[2]);
		renderer.setYAxisMax(charbean.getXyAxis()[3]);
		renderer.setAxesColor(Color.WHITE);
		renderer.setLabelsColor(Color.WHITE);
		renderer.setXLabels(0);
		renderer.setYLabels(charbean.getyLeable());
	}

	protected GraphicalView getCubicLineChart(Context context, XYChart xyChar) {
		GraphicalView mView = new GraphicalView(context, xyChar);
		mView.setBackgroundColor(Color.GRAY);
		return mView;
	}

	protected void buildDataset(XYMultipleSeriesDataset dataset) {

	}

	/* 柱状图 */

	protected XYMultipleSeriesDataset buildDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		return dataset;
	}

	/* 圆柱属性设置 */
	protected XYMultipleSeriesRenderer buildRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		return renderer;
	}

	protected int[] stringToInt(String array[]) {
		if (array == null)
			return null;
		int arr[] = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			arr[i] = Integer.parseInt(array[i].trim());
		}
		return arr;
	}

	protected double[] stringToDouble(String array[]) {
		if (array == null)
			return null;
 
		double arr[] = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			
			String arrayValue=array[i].trim();
			if(TextUtils.isEmpty(arrayValue)){
				arrayValue="0";
			}
			arr[i] = new Double(arrayValue);
		}
		return arr;
	}

	protected List<double[]> getDoubleList(String str) {
		List<double[]> values = new ArrayList<double[]>();
		if (str == null)
			return null;

		String array[] = str.split("#");
		for (int i = 0; i < array.length; i++) {
			values.add(stringToDouble(array[i].split(",")));
		}
		return values;
	}
	protected List<String[]> getStringList(String str) {
		List<String[]> values = new ArrayList<String[]>();
		if (str == null)
			return null;

		String array[] = str.split("#");
		for (int i = 0; i < array.length; i++) {
			values.add(array[i].split(","));
		}
		return values;
	}

	protected List<double[]> getxDoubleList(String str, int n) {
		List<double[]> values = new ArrayList<double[]>();
		if (str == null)
			return null;
		for (int i = 0; i < n; i++) {
			values.add(stringToDouble(str.split(",")));
		}
		return values;
	}

	public void execute(BasicEntity entity) {
		charBean = new CharBean();
		Field field = entity.fields.get("title");
		charBean.setTitle(field.fieldContent);
		field = entity.fields.get("bTitles");
		String[] bTitles = field.fieldContent.split(",");
		charBean.setbTitles(bTitles);

		field = entity.fields.get("xTitle");
		charBean.setxTitle(field.fieldContent);

		field = entity.fields.get("yTitle");
		charBean.setyTitle(field.fieldContent);

		field = entity.fields.get("xLeable");

		charBean.setxLeable(Integer.parseInt(field.fieldContent));

		field = entity.fields.get("yLeable");
		charBean.setyLeable(Integer.parseInt(field.fieldContent));

		field = entity.fields.get("colors");
		charBean.setColors(stringToInt(field.fieldContent.split(",")));

		field = entity.fields.get("xyAxis");
		charBean.setXyAxis(stringToDouble(field.fieldContent.split(",")));

		field = entity.fields.get("panLimits");
		charBean.setPanLimits(stringToDouble(field.fieldContent.split(",")));

		field = entity.fields.get("xValue");
		charBean.setxValue(getxDoubleList(field.fieldContent,
				Integer.parseInt(field.fieldView)));

		field = entity.fields.get("yValue");// 报表用到
		 if(entity.fields.get("chartype").fieldContent.equals("table"))
			 charBean.setyTableValue(getStringList(field.fieldContent));
		 else
		charBean.setyValue(getDoubleList(field.fieldContent));
		 
		field = entity.fields.get("lineTitle");// 报表用到
		if(field!=null){
			boolean isShowtitle=field.fieldView.equals("0");
			charBean.setShowXtitle(isShowtitle);
			String[] lineTitle = field.fieldContent.split(",");
			charBean.setLineTitle(lineTitle);
		}
	}

	public View getview() {
		return null;
	}

}
