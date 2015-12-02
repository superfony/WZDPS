package com.epsmart.wzdp.activity.achar.service;

import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import android.content.Context;

import com.epsmart.wzdp.activity.achar.CharBean;
import com.epsmart.wzdp.activity.supply.bean.BasicEntity;

/**
 * 柱状图显示
 * 
 * @author fony
 * 
 */
public class BarCharShow extends AbstractChart {
	
	private Context context;
	private String title;

	public BarCharShow() {

	}

	public BarCharShow(Context context, String title) {
		this.context = context;
		this.title = title;
	}

	public void execute(BasicEntity entity) {
		super.execute(entity);
		
	
		renderer = buildRenderer(charBean.getColors());
		buildDataset = buildDataset();
		buildDataset(buildDataset,charBean);
		setRenderer(renderer, charBean);//
		xyChar = new BarChart(buildDataset, renderer, Type.DEFAULT);
	}

	public GraphicalView getview() {
		return getCubicLineChart(context, xyChar);
	}

	@Override
	public void setRenderer(XYMultipleSeriesRenderer renderer) {
		super.setRenderer(renderer);

		renderer.setApplyBackgroundColor(true);// 设置背景可自定义
		renderer.setMargins(new int[] { 100, 50, 50, 50 });// 设置显示表格区域边距 依次 上下左右
		renderer.setAxisTitleTextSize(16);//设置坐标标题文本大小
		renderer.setChartTitleTextSize(20);//图表标题字体大小：20
		renderer.setLabelsTextSize(15);//轴标签字体大小：15
		renderer.setLegendTextSize(15);// 图例字体大小：15
		renderer.setXLabelsPadding(20);//设置文字和轴的距离
		renderer.setBarWidth(15);//柱状图柱子的宽度
		renderer.setXLabelsAngle(-30.0f);// 设置X轴标签倾斜角度(clockwise degree)
		renderer.setDisplayChartValues(true);//柱顶显示数值
		renderer.setChartValuesTextSize(15);//柱顶显示数值的显示大小
//		renderer.setXLabelsColor(0);//设置文字的颜色
		
		// 柱状图X坐标需要显示汉字的
		String xTextLables[]=charBean.getLineTitle();
		if(charBean.isShowXtitle()&&xTextLables!=null){
			for(int i=0;i<xTextLables.length;i++){
				renderer.addXTextLabel(i+1, xTextLables[i]);
			}
		}
	}

	@Override
	protected XYMultipleSeriesDataset buildDataset() {
		return super.buildDataset();

	}

	@Override
	protected void buildDataset(XYMultipleSeriesDataset dataset,CharBean charBean) {
		super.buildDataset(dataset);
		
		String titles[]=charBean.getbTitles();
		List<double[]> list=charBean.getyValue();
		for (int i = 0; i < titles.length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);// 添加柱图的列数
			double[] point=list.get(i);
			for (int k = 0; k < point.length; k++) {
				series.add(point[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}
		dataset.addSeries(new CategorySeries("").toXYSeries());
	}

}
