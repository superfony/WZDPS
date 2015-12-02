package com.epsmart.wzdp.activity.contract.fragment;

import java.io.IOException;
import java.util.List;

import Decoder.BASE64Decoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.bean.DailyViewBuildOnline.OnDelClickListener;

/**
 * 指标列表适配器
 */

public class DeliveryDeterAdapter extends BaseAdapter {
    private List<DeliveryDeter> list;// 数据集合
    private Context mContext;
    private LayoutInflater listContainer;// 视图容器
    private int itemViewResource;// 自定义项视图源
    private OnDelClickListener onDelClick;
    private String num;


    public void setOnDelClick(OnDelClickListener onDelClick) {
        this.onDelClick = onDelClick;
    }

    static class ListItemView { // 自定义控件集合
        public TextView jhhh;// 计划行号
        public TextView jhfhsj;// 计划发货时间
        public TextView jhdhsj;// 计划到货时间
        public TextView jhzcsl;// 计划装车数量
        public ImageButton delet;
        public ImageView ydelet;
        public ImageButton jjue;// 拒绝按钮
    }

    public DeliveryDeterAdapter(Context mContext, List<DeliveryDeter> list,
                                int resource) {
        this.mContext = mContext;
        this.list = list;
        this.listContainer = LayoutInflater.from(mContext); // 创建视图容器并设置上下文
        this.itemViewResource = resource;
    }

    public int getCount() {
        return this.list.size();

    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        // 自定义视图
        ListItemView listItemView = null;
        if (view == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(this.itemViewResource, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.jhhh = (TextView) view
                    .findViewById(R.id.jhhh);
            listItemView.jhfhsj = (TextView) view
                    .findViewById(R.id.jhfhsj);
            listItemView.jhdhsj = (TextView) view
                    .findViewById(R.id.jhdhsj);
            listItemView.jhzcsl = (TextView) view
                    .findViewById(R.id.jhzcsl);
            listItemView.delet = (ImageButton) view
                    .findViewById(R.id.delet_btn);
            listItemView.ydelet = (ImageView) view
                    .findViewById(R.id.ydelet_btn);
            listItemView.jjue=(ImageButton) view
                    .findViewById(R.id.jjue_btn);
            // 设置控件集到convertView
            view.setTag(listItemView);
        } else {
            listItemView = (ListItemView) view.getTag();
        }
        // 设置文字和图片
        DeliveryDeter delivery = list.get(position);
        listItemView.jhhh.setText(delivery.jhhh);// 计划行号
        listItemView.jhfhsj.setText(delivery.jhfhsj);// 计划发货时间
        listItemView.jhdhsj.setText(delivery.jhdhsj);// 计划到货时间
        listItemView.jhzcsl.setText(delivery.jhzcsl);// 计划装车数量
               if("11".equals(delivery.state)) {//未提交

        } else if("12".equals(delivery.state)){//已确认
            listItemView.ydelet.setVisibility(View.VISIBLE);
            listItemView.delet.setVisibility(View.GONE);
            listItemView.jjue.setVisibility(View.GONE);
        }else if("17".equals(delivery.state)){// 已提交
            listItemView.delet.setVisibility(View.VISIBLE);
            listItemView.jjue.setVisibility(View.VISIBLE);
        }else if("18".equals(delivery.state)){// 已拒绝
                   listItemView.ydelet.setVisibility(View.GONE);
                   listItemView.delet.setVisibility(View.GONE);
                   listItemView.jjue.setVisibility(View.GONE);
        }
        num = delivery.jhhh;
        listItemView.delet.setOnClickListener(new OnDelClickListener(num,"1"));// 确认
        listItemView.jjue.setOnClickListener(new OnDelClickListener(num,"0"));// 拒绝

        return view;
    }

    public class OnDelClickListener implements OnClickListener {
        private String num;
        private String type;

        public OnDelClickListener() {
        }

        public OnDelClickListener(String num,String type) {
            this.num = num;
            this.type=type;
        }

        @Override
        public void onClick(View v) {
            v.setTag(num);
            onDelClick.onClick(v);
        }
    }





}