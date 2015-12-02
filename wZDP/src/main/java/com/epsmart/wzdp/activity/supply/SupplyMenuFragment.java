package com.epsmart.wzdp.activity.supply;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.supply.fragment.EquipmentFragment;
import com.epsmart.wzdp.activity.supply.fragment.ItemFragment;
import com.epsmart.wzdp.activity.supply.fragment.PointFragment;
import com.epsmart.wzdp.activity.supply.fragment.ProductionFragment;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

/**
 * 
 * @author fony sdingmenu菜单 切换的实现
 * 
 */

public class SupplyMenuFragment extends SlidingMenuFragment {
	private View contextView = null;
	private LayoutInflater inflater = null;
	public ImageView point_imag;
	public ImageView product_imag;
	public ImageView zl_imag;
	public ImageView equip_imag;
	private Fragment newContent = null;

	public void setNewContent(Fragment newContent) {
		this.newContent = newContent;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
		contextView = inflater.inflate(R.layout.slide_menu_support, container,
				false);
		point_imag = (ImageView) contextView.findViewById(R.id.point_imag);
		product_imag = (ImageView) contextView.findViewById(R.id.product_imag);
		zl_imag = (ImageView) contextView.findViewById(R.id.zl_imag);
		equip_imag = (ImageView) contextView.findViewById(R.id.equip_imag);

		if (newContent instanceof ProductionFragment) {
			product_imag.setBackgroundResource(R.drawable.product_down);
		} else if (newContent instanceof ItemFragment) {
			zl_imag.setBackgroundResource(R.drawable.zl_down);
		} else if (newContent instanceof PointFragment) {
			point_imag.setBackgroundResource(R.drawable.point_down);
		} else if (newContent instanceof EquipmentFragment) {
			equip_imag.setBackgroundResource(R.drawable.equip_down);
		}

		point_imag.setOnClickListener(click_image);
		product_imag.setOnClickListener(click_image);
		zl_imag.setOnClickListener(click_image);
		equip_imag.setOnClickListener(click_image);
		return contextView;
	}

	OnClickListener click_image = new OnClickListener() {
		@Override
		public void onClick(View v) {

			int id = v.getId();
			if (id == R.id.product_imag) {
				newContent = new ProductionFragment();
				product_imag.setBackgroundResource(R.drawable.product_down);
				zl_imag.setBackgroundResource(R.drawable.zl_up);
				point_imag.setBackgroundResource(R.drawable.point_up);
				equip_imag.setBackgroundResource(R.drawable.equip_up);
			} else if (id == R.id.zl_imag) {
				newContent = new ItemFragment();
				zl_imag.setBackgroundResource(R.drawable.zl_down);
				point_imag.setBackgroundResource(R.drawable.point_up);
				product_imag.setBackgroundResource(R.drawable.product_up);
				equip_imag.setBackgroundResource(R.drawable.equip_up);
			} else if (id == R.id.point_imag) {
				newContent = new PointFragment();
				point_imag.setBackgroundResource(R.drawable.point_down);
				product_imag.setBackgroundResource(R.drawable.product_up);
				zl_imag.setBackgroundResource(R.drawable.zl_up);
				equip_imag.setBackgroundResource(R.drawable.equip_up);
			} else if (id == R.id.equip_imag) {
				newContent = new EquipmentFragment();
				equip_imag.setBackgroundResource(R.drawable.equip_down);
				point_imag.setBackgroundResource(R.drawable.point_up);
				product_imag.setBackgroundResource(R.drawable.product_up);
				zl_imag.setBackgroundResource(R.drawable.zl_up);
				mActivity.setSmGone();//TODO
			}

			if (newContent != null)
				switchFragment(newContent);

		}
	};

	public void setActivity(SupplyActivity mActivity) {
		this.mActivity = mActivity;
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof SupplyActivity) {
			SupplyActivity fca = (SupplyActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

}
