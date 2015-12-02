package com.epsmart.wzdp.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import android.util.Log;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.bean.RequestPram;

public class Client {
	public static final int clientTimeout = 60000 * 5;
	public TSocket ts;
	public TTransport transport;
	public IMobileBusinessService.Client client;
	public TProtocol protocol;
	public static final int  frameSize= 20 * 1024 * 1024;

	public Client() {
		ts = new TSocket(RequestParamConfig.IP, RequestParamConfig.PORT, clientTimeout);
		transport = new TFramedTransport(ts,frameSize);
		// 协议要和服务端一致
		protocol = new TCompactProtocol(transport);
		client = new IMobileBusinessService.Client(protocol);
	}

	/**
	 * 得到用户可用系统
	 * 
	 * @param terminalCode
	 *            设备码
	 * @param handler
	 * @return
	 */
	public ResultBusinessData getTerminalCode(String terminalCode) {
		ResultBusinessData rb = null;
		try {
			transport.open();
			ReqParameter req=new ReqParameter();
			req.setBizId(1);
			req.setMethodName("1");
			req.setPluginId(2);
			req.setUserName(13455544);
			req.setReqParXml(terminalCode);
			rb = client.requestData(req);
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			if(transport!=null)
			transport.close();
		}
		return rb;
	}
	
	public ResultBusinessData getThriftResult(RequestPram reqPram) {
		ResultBusinessData rb = null;
		try {
			transport.open();
			ReqParameter req=new ReqParameter();
			req.setBizId(reqPram.bizId);
			req.setPluginId(reqPram.pluginId);
			req.setMethodName(reqPram.methodName);
			req.setUserName(reqPram.userName);
			req.setReqParXml(reqPram.param);
			req.setPassword(reqPram.password);
			req.setPageNo(reqPram.pageNo);
			req.setPageSize(reqPram.pageSize);
			rb = client.requestData(req);
			Log.i("Client", ".....................rb...>>"+rb);
		} catch (TTransportException e) {
			e.printStackTrace();
			
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			if(transport!=null)
			transport.close();
		}
		return rb;
	}
	
	public ResultBizPlugin getLoginInfo(String userName,String password, String deviceID, int systemID) {
		ResultBizPlugin rbp=null;
		try {
			transport.open();
			
			rbp=client.validateTerminalUser(systemID, deviceID, userName, password);
			
		} catch (TTransportException e) {
			e.printStackTrace();
			
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			if(transport!=null)
			transport.close();
		}
		return rbp;
	}
	

}
