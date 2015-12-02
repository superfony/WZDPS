package com.epsmart.wzdp.activity.achar.service;

import org.achartengine.GraphicalView;
import org.achartengine.chart.CubicLineChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import org.achartengine.renderer.XYSeriesRenderer;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import com.epsmart.wzdp.activity.achar.CharBean;
import com.epsmart.wzdp.activity.supply.bean.BasicEntity;

/**
 * 折线图显示
 * 
 * @author fony
 * 
 */

@SuppressLint("UseValueOf")
public class LineCharShow extends AbstractChart {
	private Context context;
	private String title;

	public LineCharShow() {
	}

	public LineCharShow(Context context) {

	}

	public LineCharShow(Context context, String title) {
		this.context = context;
		this.title = title;
	}

	@Override
	public void execute(BasicEntity entity) {
		super.execute(entity);
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE,
				PointStyle.DIAMOND, PointStyle.TRIANGLE, PointStyle.SQUARE };
		
		renderer = buildRenderer(charBean.getColors(), styles);
		buildDataset = buildDataset();
		setRenderer(renderer, charBean);
		buildDataset(buildDataset, charBean);
		xyChar = new LineChart(buildDataset, renderer);//
	}

	public GraphicalView getview() {
		return getCubicLineChart(context, xyChar);
	}

	@Override
	protected void setRenderer(XYMultipleSeriesRenderer renderer) {
		super.setRenderer(renderer);
		renderer.setFitLegend(true);
		renderer.setInScroll(true);
		renderer.setZoomEnabled(true);
		renderer.setOrientation(Orientation.HORIZONTAL);
		renderer.setPointSize(6f);
		renderer.setZoomButtonsVisible(false);
		renderer.setShowGrid(true);
		renderer.setXLabelsColor(Color.WHITE);//设置X轴刻度颜色
		renderer.setYLabelsColor(0, Color.WHITE);//设置Y轴刻度颜色
		renderer.setGridColor(Color.WHITE);//网格
		renderer.setDisplayChartValues(true) ; //显示折线上点的数值
		renderer.setChartValuesTextSize(15);//折线上点的数字的显示大小
	//	renderer.setDisplayValues(true);//
		
		String xTextLables[]=charBean.getLineTitle();
		if(charBean.isShowXtitle()&&xTextLables!=null){
			for(int i=0;i<xTextLables.length;i++){
				renderer.addXTextLabel(i+1, xTextLables[i]);
			}
		}
		
	}

	@Override
	protected void buildDataset(XYMultipleSeriesDataset dataset,
			CharBean charBean) {
		super.buildDataset(dataset, charBean);
		int length = charBean.getbTitles().length;
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(charBean.getbTitles()[i], 0);
			double[] xV = charBean.getxValue().get(i);
			double[] yV = charBean.getyValue().get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
				
			}
			dataset.addSeries(series);
		}
	}
}
