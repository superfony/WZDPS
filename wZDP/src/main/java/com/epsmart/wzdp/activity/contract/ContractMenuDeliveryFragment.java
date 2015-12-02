package com.epsmart.wzdp.activity.contract;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.contract.fragment.ConfirmListFragment;
import com.epsmart.wzdp.activity.contract.fragment.DeliveryListFragment;
import com.epsmart.wzdp.activity.contract.fragment.DepartureListFragment;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

public class ContractMenuDeliveryFragment extends SlidingMenuFragment {
	private View contextView = null;
	private LayoutInflater inflater = null;
	public  ImageView d_form,s_form,c_form;
	private Fragment newContent = null;
	private String types;

	public void setNewContent(Fragment newContent) {
		this.newContent = newContent;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceSta) {

		this.inflater = inflater;
		contextView = inflater.inflate(R.layout.slide_menu_contractds, container,false);
		d_form = (ImageView) contextView.findViewById(R.id.d_left_form);
		s_form = (ImageView) contextView.findViewById(R.id.s_left_form);
		c_form = (ImageView) contextView.findViewById(R.id.c_left_form);

		if (newContent instanceof DeliveryListFragment) {
			d_form.setBackgroundResource(R.drawable.delivery_down);
		}else if (newContent instanceof DepartureListFragment) {
			s_form.setBackgroundResource(R.drawable.departure_down);
		}else if (newContent instanceof ConfirmListFragment) {
			c_form.setBackgroundResource(R.drawable.confirm_down);
		}

		d_form.setOnClickListener(click_image);
		s_form.setOnClickListener(click_image);
		c_form.setOnClickListener(click_image);
		return contextView;
	}

	OnClickListener click_image = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bundle bundle = new Bundle();
			int id = v.getId();
			if (id == R.id.d_left_form) {
				newContent = new DeliveryListFragment();
				d_form.setBackgroundResource(R.drawable.delivery_down);
				s_form.setBackgroundResource(R.drawable.departure_up);
				c_form.setBackgroundResource(R.drawable.confirm_up);
			}else if (id == R.id.s_left_form) {
				newContent = new DepartureListFragment();
				s_form.setBackgroundResource(R.drawable.departure_down);
				d_form.setBackgroundResource(R.drawable.delivery_up);
				c_form.setBackgroundResource(R.drawable.confirm_up);
			}else if (id == R.id.c_left_form) {
				newContent = new ConfirmListFragment();
				c_form.setBackgroundResource(R.drawable.confirm_down);
				s_form.setBackgroundResource(R.drawable.departure_up);
				d_form.setBackgroundResource(R.drawable.delivery_up);
			}
			if (newContent != null)
				switchFragment(newContent);
		}
	};

	public void setActivity(ContractDelDepActivity delDepActivity) {
		this.mActivity = delDepActivity;
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof ContractDelDepActivity) {
			ContractDelDepActivity fca = (ContractDelDepActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

}
