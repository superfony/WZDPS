package com.fony.menu.widget.slidingmenu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fony.menu.widget.slidingmenu.BaseSdingActivity;



/**
 * 
 * @author zhangdq
 * @version 1.0
 * 
 */
public class BaseTabFragment extends Fragment {
	public static final String DATA_KEY = "pagination";

	public static final String argumentKey = "key";

	protected BaseSdingActivity mActivity = null;
	protected FragmentObserver fragmentObserver;

	public interface FragmentObserver {
		/**
		 * 
		 * @param key
		 *            fragment的key
		 * @param savedInstanceState
		 */
		void onActivityCreated(String key, Bundle savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (BaseSdingActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (null == savedInstanceState)
			savedInstanceState = new Bundle();
		if (null != fragmentObserver) {
			fragmentObserver.onActivityCreated(
					getArguments().getString(argumentKey), savedInstanceState);
		}
		init(savedInstanceState);
		setUpAndBindEvents(savedInstanceState);
	}

	public void init(Bundle savedInstanceState) {

	}

	public void setUpAndBindEvents(Bundle savedInstanceState) {
	}

//	protected String key;
//	protected DataObserver dataObserver;
//
//	public void setDataObserver(String key, DataObserver dataObserver) {
//		this.key = key;
//		this.dataObserver = dataObserver;
//	}
//
//	public void setFragmentObserver(String key,
//			FragmentObserver fragmentObserver) {
//		this.key = key;
//		this.fragmentObserver = fragmentObserver;
//	}

}
