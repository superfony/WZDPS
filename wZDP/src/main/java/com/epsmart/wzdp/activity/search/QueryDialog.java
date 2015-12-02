package com.epsmart.wzdp.activity.search;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.common.RequestXmlHelp;

/**
 * 查询框
 * 
 */
public class QueryDialog {
	private static final String TAG  = QueryDialog.class.getName();
	private Activity mActivity;
	private LayoutInflater layoutInflater;
	private PopupWindow popupWindow = null;
	private View popView;
	private QueryDialogListener listener;
	public String req = null;
	private EditText et_one = null;
	private EditText et_two = null;
	private EditText et_three = null;
	private EditText et_four = null;
	private EditText et_five = null;
	private EditText et_six = null;
	private AppContext app_context;

	public QueryDialog() {
		super();
	}

	public QueryDialog(final Activity activity,
			final QueryDialogListener listener) {
		this.mActivity = activity;
		this.listener = listener;
		layoutInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = layoutInflater.inflate(R.layout.query, null);
		popView.setFocusableInTouchMode(true);
		popView.setFocusable(true);

		popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		ColorDrawable dw = new ColorDrawable(-00000);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.UpInAndOutAnimation);
		popupWindow.update();

		Resources res = activity.getResources();

		TextView ev_one = (TextView) popView
				.findViewById(R.id.search_title_one);
		TextView ev_two = (TextView) popView
				.findViewById(R.id.search_title_two);
		TextView ev_three = (TextView) popView
				.findViewById(R.id.search_title_three);
		TextView ev_four = (TextView) popView
				.findViewById(R.id.search_title_four);
		TextView ev_five = (TextView) popView
				.findViewById(R.id.search_title_five);
		TextView ev_six = (TextView) popView
				.findViewById(R.id.search_title_six);

		et_one = (EditText) popView.findViewById(R.id.search_et_one);// 条件一
		et_two = (EditText) popView.findViewById(R.id.search_et_two);// 条件二
		et_three = (EditText) popView.findViewById(R.id.search_et_three);// 条件三
		et_four = (EditText) popView.findViewById(R.id.search_et_four);
		et_five = (EditText) popView.findViewById(R.id.search_et_five);
		et_six = (EditText) popView.findViewById(R.id.search_et_six);

		app_context = (AppContext) activity.getApplication();
		switch (app_context.wzdpType) {
		case PRODUCTION:
			ev_one.setText("项目名称：");
			ev_two.setText("电压等级：");
			ev_three.setText("物料类型：");
			ev_four.setText("供应商名称：");
			ev_five.setVisibility(View.INVISIBLE);
			ev_six.setVisibility(View.INVISIBLE);
			et_five.setVisibility(View.GONE);
			et_six.setVisibility(View.GONE);
			break;
		case QUALITY:
			ev_one.setText("项目名称：");
			ev_two.setText("电压等级：");
			ev_three.setText("物料类型：");
			ev_four.setText("供应商名称：");
			ev_five.setVisibility(View.INVISIBLE);
			ev_six.setVisibility(View.INVISIBLE);
			et_five.setVisibility(View.GONE);
			et_six.setVisibility(View.GONE);

			break;
		case POINT:
			ev_one.setText("设备型号：");
			ev_two.setText("生产工号：");
			ev_three.setText("工程名称：");
			ev_four.setText("见证单序号：");
			ev_five.setVisibility(View.GONE);
			ev_six.setVisibility(View.GONE);
			et_five.setVisibility(View.GONE);
			et_six.setVisibility(View.GONE);
			break;
		case SCENEPRO:
			ev_one.setText("项目单位：");
			ev_two.setText("项目名称：");
			ev_three.setText("项目状态：");
			ev_four.setVisibility(View.GONE);
			ev_five.setVisibility(View.GONE);
			ev_six.setVisibility(View.GONE);
			et_four.setVisibility(View.GONE);
			et_five.setVisibility(View.GONE);
			et_six.setVisibility(View.GONE);

			break;
		case SCENEAFT:
			ev_one.setText("项目名称：");
			ev_two.setText("项目状态：");
			ev_three.setVisibility(View.GONE);
			ev_four.setVisibility(View.GONE);
			ev_five.setVisibility(View.GONE);
			ev_six.setVisibility(View.GONE);
			et_three.setVisibility(View.GONE);
			et_four.setVisibility(View.GONE);
			et_five.setVisibility(View.GONE);
			et_six.setVisibility(View.GONE);

			break;
		case SCENEAFTS:
			ev_one.setText("供应商名称：");
			ev_two.setText("物料名称  ：");
			ev_three.setText("采购订单  ：");
			ev_four.setText("订单行项目：");
			ev_five.setVisibility(View.GONE);
			ev_six.setVisibility(View.GONE);
			et_five.setVisibility(View.INVISIBLE);
			et_five.setText(((CommonActivity) activity).getPspid());
			et_six.setVisibility(View.GONE);
			break;
		case SCENSERVICE:
			ev_one.setText("项目描述：");
			ev_two.setText("供应商名称：");
			ev_three.setText("订单编号：");
			ev_four.setVisibility(View.GONE);
			ev_five.setVisibility(View.GONE);
			ev_six.setVisibility(View.GONE);
			et_four.setVisibility(View.GONE);
			et_five.setVisibility(View.GONE);
			et_six.setVisibility(View.GONE);
			break;
		case WORK:
			ev_one.setText("编号        ：");
			ev_four.setText("事件描述：");
			ev_three.setText("事件类型：");
			ev_two.setVisibility(View.INVISIBLE);
			ev_five.setVisibility(View.GONE);
			ev_six.setVisibility(View.GONE);
			et_two.setVisibility(View.INVISIBLE);
			et_five.setVisibility(View.GONE);
			et_six.setVisibility(View.GONE);
			break;
//		case TowerPlan:
//			ev_one.setText("项目名称 ：");
//			ev_two.setText("项目编码：");
//			ev_three.setVisibility(View.INVISIBLE);
//			ev_four.setVisibility(View.INVISIBLE);
//			ev_five.setVisibility(View.GONE);
//			ev_six.setVisibility(View.GONE);
//			et_three.setVisibility(View.INVISIBLE);
//			et_four.setVisibility(View.INVISIBLE);
//			et_five.setVisibility(View.GONE);
//			et_six.setVisibility(View.GONE);
//			break;
//		case TowerProgress:
//			ev_one.setText("项目名称 ：");
//			ev_two.setText("项目编码：");
//			ev_three.setVisibility(View.INVISIBLE);
//			ev_four.setVisibility(View.INVISIBLE);
//			ev_five.setVisibility(View.GONE);
//			ev_six.setVisibility(View.GONE);
//			et_three.setVisibility(View.INVISIBLE);
//			et_four.setVisibility(View.INVISIBLE);
//			et_five.setVisibility(View.GONE);
//			et_six.setVisibility(View.GONE);
//			break;
		default:
			break;
		}

		View cancel = popView.findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});

		View query = popView.findViewById(R.id.query);
		query.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 

				switch (app_context.wzdpType) {
				case PRODUCTION:
					req = RequestXmlHelp.getCommonXML(RequestXmlHelp
							.getReqXML("projectnamedim",
									et_one.getText().toString())
							.append(RequestXmlHelp.getReqXML(
									"voltagegradedim", et_two.getText()
											.toString()))
							.append(RequestXmlHelp.getReqXML("materialtypedim",
									et_three.getText().toString()))
							.append(RequestXmlHelp.getReqXML(
									"suppliernamedim", et_four.getText()
											.toString())));

					break;
				case QUALITY:

					req = RequestXmlHelp.getCommonXML(RequestXmlHelp
							.getReqXML("projectnamedim",
									et_one.getText().toString())
							.append(RequestXmlHelp.getReqXML(
									"voltagegradedim", et_two.getText()
											.toString()))
							.append(RequestXmlHelp.getReqXML("materialtypedim",
									et_three.getText().toString()))
							.append(RequestXmlHelp.getReqXML(
									"suppliernamedim", et_four.getText()
											.toString())));
					break;
				case POINT:

					req = RequestXmlHelp.getCommonXML(RequestXmlHelp
							.getReqXML("equipmentiddim",
									et_one.getText().toString())
							.append(RequestXmlHelp.getReqXML("productnodim",
									et_two.getText().toString()))
							.append(RequestXmlHelp.getReqXML(
									"engineeringnamedim", et_three.getText()
											.toString()))
							.append(RequestXmlHelp.getReqXML("iddim", et_four
									.getText().toString())));
					break;
				case SCENEPRO:

					req = RequestXmlHelp.getCommonXML(RequestXmlHelp
							.getReqXML("xmdwdim", et_one.getText().toString())
							.append(RequestXmlHelp.getReqXML("xmmcdim", et_two
									.getText().toString()))
							.append(RequestXmlHelp.getReqXML("xmztdim",
									et_three.getText().toString())));
					break;
				case SCENEAFT:

					req = RequestXmlHelp.getCommonXML(RequestXmlHelp.getReqXML(
							"xmmcdim", et_one.getText().toString()).append(
							RequestXmlHelp.getReqXML("xmztdim", et_two
									.getText().toString())));
					break;
				case SCENEAFTS:

					req = RequestXmlHelp
							.getCommonXML(RequestXmlHelp
									.getReqXML("zname1dim",
											et_one.getText().toString())
									.append(RequestXmlHelp.getReqXML(
											"ztxz01dim", et_two.getText()
													.toString()))
									.append(RequestXmlHelp.getReqXML(
											"zebelndim", et_three.getText()
													.toString()))
									.append(RequestXmlHelp.getReqXML(
											"zebelpdim", et_four.getText()
													.toString()))
									.append(RequestXmlHelp.getReqXML(
											"pspiddim", et_five.getText()
													.toString())));
					break;
				case SCENSERVICE:
					req = RequestXmlHelp.getCommonXML(RequestXmlHelp
							.getReqXML("post1dim", et_one.getText().toString())
							.append(RequestXmlHelp.getReqXML("zname1dim",
									et_two.getText().toString()))
							.append(RequestXmlHelp.getReqXML(
									"purchaseorderdim", et_three.getText()
											.toString())));
					break;
				case WORK:
					req = RequestXmlHelp.getCommonXML(RequestXmlHelp
							.getReqXML("zydid", et_one.getText().toString())
							.append(RequestXmlHelp.getReqXML("zsjlbdim",
									et_four.getText().toString()))
							.append(RequestXmlHelp.getReqXML("zsjmsdim",
									et_three.getText().toString())));
					break;
//				case TowerPlan:
//					req = RequestXmlHelp.getCommonXML(RequestXmlHelp
//							.getReqXML("post1", et_one.getText().toString())
//							.append(RequestXmlHelp.getReqXML("pspid",
//									et_two.getText().toString())));
//					break;
//				case TowerProgress:
//					req = RequestXmlHelp.getCommonXML(RequestXmlHelp
//							.getReqXML("post1", et_one.getText().toString())
//							.append(RequestXmlHelp.getReqXML("pspid",
//									et_two.getText().toString())));
//					break;
				default:
					break;
				}
				System.out.print("listener=" + listener);
				listener.doQuery(req);
				popupWindow.dismiss();
			}

		});
	}

	public void show(View anchor) {// TODO 设置显示
		popupWindow.setHeight(dipToPixels(400));
		Rect frame = new Rect();
		mActivity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(frame);

		popupWindow.showAtLocation(anchor, Gravity.CENTER_HORIZONTAL
				| Gravity.TOP, 0, frame.top);
	}

	public int dipToPixels(int dip) {
		Resources r = mActivity.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
				r.getDisplayMetrics());
		return (int) px;
	}
}