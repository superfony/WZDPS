namespace java com.epsmart.wzdp.thrift

include "request.thrift"

service IMobileBusinessService{
	request.ResultBusinessData requestData(1:request.ReqParameter reqParameter);
	request.ResultBizPlugin validateTerminalUser(1:i32 bizSysID,2:string terminalCode,3:string userName,4:string password);
	
	
}


