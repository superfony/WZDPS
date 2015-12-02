package com.epsmart.wzdp.http;

import android.os.Looper;

import com.epsmart.wzdp.http.response.model.StatusEntity;

public class DefaultModuleResponseProcessor implements ModuleResponseProcessor {

	@Override
	public void processResponse(BaseHttpModule baseHttpModule,
			Object parseObj) {
		if (parseObj instanceof StatusEntity) {
			StatusEntity se = (StatusEntity) parseObj;
			Looper.prepare();
			//ToastTool.toast(baseHttpModule.getContext(), se.message);
			Looper.loop();
			Looper.myLooper().quit();
		}
	}

}
