namespace java com.epsmart.wzdp.thrift

//业务数据请求
struct ReqParameter{
  // 业务系统id
  1:i32 bizId;
  // 插件id
  2:i64 pluginId;
   //当前用户信息
  3:i64 userName;
  // 业务系统方法名
  4:string methodName;
  // 请求参数 xml 格式
  5:string reqParXml;
  6:i32 pageNo;
  7:i32 pageSize;
  8:string password;
}

//返回的业务数据结果包 
struct ResultBusinessData{
	//操作结果
	1:bool success;
	//失败信息
	2:string fault;
	//处理结果包
	3:string returnValue;
}


//用户信息
struct UserInfoTo{
	//用户真实姓名
	1:string name;
	//单位部门id
	2:i32 deptId;
	//单位部门名称
	3:string deptName;
	//代码
	4:string gdjdm;
	
}

//业务插件
struct BizPluginTo{
	//插件id
	1:i64 pluginId;
	//插件名称
	2:string pluginName;
	//插件版本
	3:i32 pluginVersion;
	//是否强制更新
	4:bool isForceUpdate;
	//包名
	5:string appPackage;
	//入口
	6:string appMainActivity;
}

struct ResultBizPlugin{
	//操作结果
	1:bool success;
	//失败信息
	2:string fault;
	//用户登录成功之后返回用户信息
	3:UserInfoTo userInfo;
	//返回用户可用插件清单
	4:list<BizPluginTo> result;

	
}