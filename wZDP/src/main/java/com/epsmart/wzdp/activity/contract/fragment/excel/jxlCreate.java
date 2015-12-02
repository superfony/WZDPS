package com.epsmart.wzdp.activity.contract.fragment.excel;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.epsmart.wzdp.activity.supply.bean.BasicEntity;
import com.epsmart.wzdp.activity.supply.bean.Field;

import android.os.Environment;
import android.os.Handler;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class jxlCreate {
	Handler handler;

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	 * @param entiry
	 * @param list1
	 * @param args
	 */

	public void excelCreate(BasicEntity entiry, List<String[]> list1) {
		// 准备设置excel工作表的标题

		Field field = null;
		String[] title = null;
		String sheettitle = entiry.fields.get("title").fieldContent;
		field = entiry.fields.get("lineTitle");
		if (field != null) {
			title = field.fieldContent.split(",");
		}else{
			return;
		}
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat
				.getDateTimeInstance();
		sdf.applyPattern("yyyyMMddhhmmss");

		try {
			// 获得开始时间
			//long start = System.currentTimeMillis();
			// 输出的excel的路径
			String filePath = Environment.getExternalStorageDirectory()
					+ ("/"+sheettitle+sdf.format(calendar.getTime())+".xls");
			// 创建Excel工作薄
			WritableWorkbook wwb;
			// 新建立一个jxl文件,即在SDcard盘下生成test.xls
			OutputStream os = new FileOutputStream(filePath);
			wwb = Workbook.createWorkbook(os);
			// 添加第一个工作表并设置第一个Sheet的名字
			WritableSheet sheet = wwb.createSheet(sheettitle, 0);
			Label label;
			for (int x = 0; x < title.length; x++) {
				// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是y
				// 在Label对象的子对象中指明单元格的位置和内容
				label = new Label(x, 0, title[x]);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label);
			}
			for (int j = 1; j < list1.size() + 1; j++) {// 行
				String args[] = list1.get(j-1);
				for (int i = 0; i < args.length; i++) {// 列
					label = new Label(i, j, args[i]);
					sheet.addCell(label);
				}
			}

			// 写入数据
			wwb.write();
			// 关闭文件
			wwb.close();
			handler.obtainMessage(0, "导出成功！ 文件存放路径："+filePath).sendToTarget();
		} catch (Exception e) {
			e.printStackTrace();
			handler.obtainMessage(1, "导出失败！").sendToTarget();
		}

	}

}
