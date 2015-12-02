package com.epsmart.wzdp.activity.supply;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.view.CircleImageView;
import com.epsmart.wzdp.view.CircleLayoutTwo;

/**
 * @author fony 供应商现场管理
 */
public class SupplyMenuActivity extends Activity {
	private String TAG = SupplyMenuActivity.class.getName();
	private Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.actionbar_main);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		View view = actionBar.getCustomView();
		ImageView tview = (ImageView) view.findViewById(R.id.title_image);
		tview.setBackgroundResource(R.drawable.head_logo);

		setContentView(R.layout.supply_activity);
//		if (!PermissHelp.isPermiss("003")) {
//			PermissHelp.showToast(this);
//			return;
//		}
		CircleLayoutTwo circletwo = (CircleLayoutTwo) findViewById(R.id.supply_layout);

		CircleImageView product = (CircleImageView) circletwo
				.findViewById(R.id.product_image);
		product.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "生产过程监控",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(SupplyMenuActivity.this, SupplyActivity.class);
				intent.putExtra("tag", "0");
				startActivity(intent);
			}
		});

		CircleImageView quality = (CircleImageView) circletwo
				.findViewById(R.id.quality_image);
		quality.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(SupplyMenuActivity.this, SupplyActivity.class);
				intent.putExtra("tag", "1");
				startActivity(intent);
			}
		});

		CircleImageView point = (CircleImageView) circletwo
				.findViewById(R.id.point_image);
		point.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(SupplyMenuActivity.this, SupplyActivity.class);
				intent.putExtra("tag", "2");
				startActivity(intent);
			}
		});
		
		String EQUIP = getIntent().getStringExtra("tables");
		CircleImageView equipment = (CircleImageView) circletwo
				.findViewById(R.id.equipment_image);
		if ("tables".equals(EQUIP)) {
//			equipment.setBackgroundResource(R.drawable.icon_equipmentxz);
		}
		equipment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!PermissHelp.isPermiss("003")) {
					PermissHelp.showToast(activity);
					return;
				}
				Intent intent = new Intent();
				intent.setClass(SupplyMenuActivity.this, SupplyActivity.class);
				intent.putExtra("tag", "3");
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
