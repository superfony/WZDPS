package com.epsmart.wzdp.activity.dailyoffice.fragment;



import com.epsmart.wzdp.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;

public class FixedGridLayout extends ViewGroup {
	
	private int mCellWidth;
	private int mCellHeight;
	private Context context;

	public FixedGridLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 9500: 1920x1080
     * 9300: 1280x720
	 *<p>Description: </p>
	 * @param context
	 * @param attrs
	 */
	public FixedGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		
		
		//初始化单元格宽和高
//		mCellWidth = 60;
//		mCellHeight = 60;
		
		//通过xml文件配置
		//读取属性资源
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FixedGridLayout);
		//分别读取cellHeight和cellWidth属性  /90 180
		
		
		mCellHeight = a.getDimensionPixelSize(R.styleable.FixedGridLayout_cellHeight, dipToPixels(45));
		mCellWidth = a.getDimensionPixelSize(R.styleable.FixedGridLayout_cellWidth, dipToPixels(100));
		//释放资源
		a.recycle();
	}
	
	public int dipToPixels(int dip) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
				r.getDisplayMetrics());
		return (int) px;
	}

	
	//设置单元格宽度
	public void setCellWidth(int w) {
		mCellWidth = w;
		
		//Call this when something has changed which has invalidated the layout of this view. 
        //This will schedule a layout pass of the view tree
		requestLayout();
	}
	
	//设置单元格高度
	public void setCellHeight(int h) {
		mCellHeight = h;
		requestLayout();
	}
	
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		//获取布局控件宽高
		int width = getWidth();
		int height = getHeight();
		//创建画笔
		Paint mPaint = new Paint();
		//设置画笔的各个属性
		mPaint.setColor(Color.TRANSPARENT);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(10);
		mPaint.setAntiAlias(true);
		
		//创建矩形框
		Rect mRect = new Rect(0, 0, width, height);
		//绘制边框
		canvas.drawRect(mRect, mPaint);
		
		//最后必须调用父类的方法
		super.dispatchDraw(canvas);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		//创建测量参数
		int cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth, MeasureSpec.AT_MOST);
		int cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight, MeasureSpec.AT_MOST);
		
		//记录ViewGroup中Child的总个数
		int count = getChildCount();
		
		//设置子空间Child的宽高
		for (int i = 0; i < count; i++) {
			View childView = getChildAt(i);
			 /*
             * This is called to find out how big a view should be. 
             * The parent supplies constraint information in the width and height parameters. 
             * The actual mesurement work of a view is performed in onMeasure(int, int), 
             * called by this method. 
             * Therefore, only onMeasure(int, int) can and must be overriden by subclasses.
             */
			childView.measure(cellWidthSpec, cellHeightSpec);
		}
		
		//设置容器控件所占区域大小
		//注意setMeasuredDimension和resolveSize的用法
		setMeasuredDimension(resolveSize(mCellWidth * count, widthMeasureSpec), resolveSize(mCellHeight * count, heightMeasureSpec));
		//setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		
		//不需要调用父类的方法
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
		int cellWidth = mCellWidth;
		int cellHeight = mCellHeight;
		int columns = (r - l) / cellWidth;
		
		if(columns < 0) {
			columns = 1;
		}
		
		int x = 0;
		int y = 0;
		int i = 0;
		int count = getChildCount();
		
		for (int j = 0; j < count; j++) {
			final View childView = getChildAt(j);
			//获取子控件Child的宽高
			int w = childView.getMeasuredWidth();
			int h = childView.getMeasuredHeight();
			//计算子控件的顶点坐标
			int left = x + ((cellWidth - w)/2);
			int top = y + ((cellHeight - h)/2);
			//int left = x;
			//int top = y;
			//布局子控件
			childView.layout(left, top, left + w, top + h);
			
			if(i >= (columns - 1)) {
				i = 0;
				x = 0;
				y += cellHeight;
			} else {
				i++;
				x += cellWidth;
						
			}
			
		}

	}

}
