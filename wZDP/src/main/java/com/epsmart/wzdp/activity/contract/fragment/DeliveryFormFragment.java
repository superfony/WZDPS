package com.epsmart.wzdp.activity.contract.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.fragment.CommonFragment;
import com.epsmart.wzdp.activity.fragment.ContractFragment;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.bean.BasicResponseNew;
import com.epsmart.wzdp.activity.supply.fragment.parser.BasicResponseHandlerNew;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.common.Common;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;

/**
 * 发货计划（新建）维护表单   供应商
 */
public class DeliveryFormFragment extends ContractFragment {

    private Activity activitys;
    private View view;
    protected RequestAction requestAction = null;
    protected WorkOrder workOrder;
    private BasicResponse response;
    private BasicResponseNew responseNew;
    private ImageView title_image;
    private String TAG = DeliveryFormFragment.class.getName();
    private Button query_button;


    @Override
    public void onAttach(Activity activity) {
        this.activitys = activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scene_pro_check, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        text_lay = (LinearLayout) view.findViewById(R.id.show_text_lay);
        table_lay = (LinearLayout) view.findViewById(R.id.show_input_lay);
        btn_lay = (LinearLayout) view.findViewById(R.id.show_submit_lay);
        submit_btn = (Button) view.findViewById(R.id.submit_button);
        cancel_btn = (Button) view.findViewById(R.id.cancel_button);
        submit_btn.setOnClickListener(clickLis);
        cancel_btn.setOnClickListener(clickLis);
        present_lay.setVisibility(View.VISIBLE);
        present_btn.setOnClickListener(clickLis);
        query_tv.setOnClickListener(clickLis);

        if (response == null) {
            loadData(requestPram);
        } else {
            initTable(response.entity);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = activitys.getActionBar();
        View view = actionBar.getCustomView();
        title_image = (ImageView) view.findViewById(R.id.title_image);
        title_image.setBackgroundResource(R.drawable.delivery_title);//发货计划
        title_image.setVisibility(View.VISIBLE);

    }


    /**
     * 数据请求
     */
    public void loadData(RequestPram param) {
        showModuleProgressDialog("提示", "数据请求中请稍后...");
        requestAction = new RequestAction();
        requestAction.reset();
        requestPram.bizId = 1004;
        requestPram.password = "password";
        requestPram.pluginId = 119;
        requestPram.userName = appContext.user.getUid();

        requestPram.methodName = RequestParamConfig.DelplanDownload;
        requestPram.param = getArguments().getString("reqParam");
        requestAction.setReqPram(requestPram);

        httpModule.executeRequest(requestAction, new BasicResponseHandlerNew(),
                new ProcessResponse(), RequestType.THRIFT);
    }

    class ProcessResponse implements ModuleResponseProcessor {
        @Override
        public void processResponse(BaseHttpModule httpModule, Object parseObj) {
            if (parseObj instanceof BasicResponseNew) {
                mHandler.obtainMessage(0, parseObj).sendToTarget();
            } else if (parseObj instanceof BasicResponse) {
                mHandler.obtainMessage(1, parseObj).sendToTarget();
            }
        }
    }


    //设置请求数据
    protected void submitMethod(RequestPram reqPram) {
        requestPram.bizId = 1004;
        requestPram.password = "password12";
        requestPram.pluginId = 119;
        requestPram.userName = appContext.user.getUid();
        requestPram.methodName = RequestParamConfig.DelplanUpload;
        requestPram.param = fillHelpNew.getparams(getArguments().getString("reqP"));
        super.submitMethod(requestPram);

    }

    // 查询已拒绝的数据
    @Override
    protected void cancelMethod() {
        JuFormFragment dffragment = new JuFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(
                "req", fillHelpNew.getparams(getArguments().getString("reqP"))
        );
        dffragment.setArguments(bundle);
        activity.setSmGone();
        Common.replaceRightFragment(activity, dffragment,
                false, R.id.content);
    }

    private FlowHandler mHandler = new FlowHandler();

    private class FlowHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeDialog();
            if (msg.what == 0) {
                BasicResponseNew brsn=(BasicResponseNew) msg.obj;
                String message=  brsn.message;
                Log.i("","message="+message);
                Log.i("", "TextUtils.isEmpty(message)=" + TextUtils.isEmpty(message));
               if(!"null".equals(message)&&!TextUtils.isEmpty(message)){
                    search_lay.setVisibility(View.GONE);
                    query_tv.setVisibility(View.VISIBLE);
                    query_lay.setVisibility(View.VISIBLE);
                    query_tv.setText("拒绝数" + brsn.message);
                    query_tv.setTextSize(18);
                    query_tv.setTextColor(Color.WHITE);
               }
                onSuccessNew(brsn);
            } else if (msg.what == 1) {
                onSuccess((BasicResponse) msg.obj);
            }
        }
    }

    private void onSuccess(BasicResponse response) {
        this.response = response;
        if (null == response.entity || response.entity.rows.size() < 1)
            return;
        initTable(response.entity);
    }

    private void onSuccessNew(BasicResponseNew response) {
        this.responseNew = response;
        if (null == response.basicEntityList
                || response.basicEntityList.size() < 1)
            return;
        initTableNew(response.basicEntityList);
    }

    @Override
    public void onPause() {
        present_lay.setVisibility(View.GONE);
        query_lay.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
