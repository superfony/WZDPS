package com.epsmart.wzdp.activity;

/**
 * webservice 接口定义
 * @author fony
 */
public class RequestParamConfig {

	public static boolean isDubeg = false;
	public static final int FTP_PORT = 21;
	public static final int pagesize = 5;
	// TF加密码访问
	public static String IP = "127.0.0.1";
	
	// 148机器 测试机用
//	public static int PORT = 8551;1、	特高压项目发货计划功能修改
//	public static String ServerUrl = "http://127.0.0.1:8551/wzdp/services/cmisService";
//    public static String loginUrl = "http://127.0.0.1:8552/lnptgl/webservices/IMobilePlatformsWebservices";
	// 153 服务器 148测试服务器
//	public static int PORT = 8553;
	public static String ServerUrl ="http://127.0.0.1:8551/wzdp/services/cmisService";
	public static String loginUrl = "http://127.0.0.1:8554/lnptgl/webservices/IMobilePlatformsWebservices";

	// 148 CMIS  153 中间件 测试模式
//	public static int PORT = 8553;
//	public static String ServerUrl ="http://127.0.0.1:8551/wzdp/services/cmisService";
//	public static String loginUrl = "http://127.0.0.1:28099/lnptgl/webservices/IMobilePlatformsWebservices";

	// 正式库 223
	 public static int PORT =28080;
//	 public static String ServerUrl ="http://127.0.0.1:28080/wzdp/services/cmisService";
//	 public static String loginUrl ="http://127.0.0.1:28099/lnptgl/webservices/IMobilePlatformsWebservices";

     // 153 服务器  模拟器用
//	  public static String ServerUrl ="http://10.192.29.153:8080/wzdp/services/cmisService";
//	 	public static String loginUrl = "http://10.192.29.153:8899/lnptgl/webservices/IMobilePlatformsWebservices";

   // 登录命名空间
	public static final String servicenamespacelogin = "http://webservices.lnptgl.aowei.com";
	// 登录接口名称
	public static final String loginname = "validateTerminalUser";
	// 命名空间
	public static final String serviceNameSpace = "http://service.webservice.lnsoft.com";
	// 接口名称
	public static final String selectNowWork = "selectNowWOrk";
	// 权限接口
	public static final String userLogin = "userLogin";
	// 供应商生成过程监控
	public static final String procedureReq = "procedureReq";
	// 关键点见证基础信息字段请求接口
	public static final String keyPotReq = "keyPotReq";
	// 项目现场管理基础信息(供应进度维护基础信息接收接口)
	public static final String projectlistReq = "projectlistReq";
	// 工作日志基础信息接口
	public static final String dairylistReq = "dairylistReq";
	// 生产过程信息字段请求接口
	public static final String procedureDownload = "procedureDownload";
	// 生产过程信息上传
	public static final String procedureUpload = "procedureUpload";
	// 质量问题清单列表请求接口
	public static final String issuelistDownload = "issuelistDownload";
	// 质量问题信息字段请求接口/质量问题处理信息字段获取接口/问题处理结果信息字段请求接口/质量问题信息查询
	public static final String qualityIssueDownload = "qualityIssueDownload";// qualityIssueDownload
	// 质量问题信息上传/质量问题处理结果上传/质量问题处理结果上传接口
	public static final String qualityIssueUpload = "qualityIssueUpload";
	// 关键点见证信息字段请求接口/新增关键点
	public static final String keyPotQueryDownload = "keyPotQueryDownload";
	// 关键点见证基础信息上传
	public static final String keyPotQueryUpload = "keyPotQueryUpload";
	// 项目施工节点信息请求接口
	public static final String projectQueryReq = "projectQueryReq";
	// 项目施工节点信息上传接口
	public static final String projectNodeUpload = "projectNodeUpload";
	// 供应进度维护二级列表
	public static final String materialslistReq = "materialslistReq";// materialslistReq
	// 供应进度维护信息请求接口
	public static final String materialsReceiveReq = "materialsReceiveReq";
	// 供应进度维护上传接口
	public static final String projectReceiveUpload = "projectReceiveUpload";
	// 工作日志维护字段请求接口
	public static final String dairyReceiveReq = "dairyReceiveReq";
	// 工作日志维护字段提交接口
	public static final String dairyReceiveUpload = "dairyReceiveUpload";
	// 现场服务的第一层列表
	public static final String materialsListReq2 = "materialslistReq2";
	// 现场服务申请单列表信息接口
	public static final String servicelistDownload = "servicelistDownload";
	// 现场服务问题反馈单上传
	public static final String newserviceslistupload = "newservieslistupload";// newserviceslistupload
	// 现场服务提问题单信息字段接口
	public static final String newServicelistDownload = "newServicelistDownload";
	// 现场服务联系单信息字段请求接口/现场服务联系单反馈信息字段请求接口/现场服务问题单信息字段接口/现场服务评价信息字段接口
	public static final String serviceDownload = "serviceDownload";
	// 现场服务联系单上传接口/现场服务联系单反馈上传接口/现场服务完成确认标识上传/现场服务评价单上传
	public static final String serviceRequireUpload = "serviceRequireUpload";
	// 合同结算信息分析数接收接口
	public static final String contructAnlysis = "contructAnlysis";
	// 合同签订情况分析数接收接口
	public static final String contractsign = "contractsign";
	// 物资供应综合分析数接收接口
	public static final String materialsupply = "materialsupply";
	//
	public static final String projectmilestone = "projectmilestone";
	// 项目施工节点信息二级列表请求接口
	public static final String linelistDownload = "linelistDownload";
	// 修改密码
	public static final String userUpload = "userUpload";
	// 特高压项目列表接口
	public static final String ehvProjectList = "ehvProjectList";
	// 铁塔排产计划基础信息请求接口
	public static final String ehvPlanDownload = "ehvPlanDownload";
	// 铁塔排产计划模、铁塔生产进度周报模板下载请求接口
	public static final String ehvTempDownload = "ehvTempDownload";
	// 塔排产计划、生产进度周报信息附件上传接口
	public static final String ehvtempUpload = "ehvtempUpload";
	// 铁塔生产进度周报信息请求接口
	public static final String ehvReportDownload = "ehvReportDownload";
	// 排产计划和生产进度周报筛选条件请求接口
	public static final String ehvplanrep = "ehvplanrep";
	// 铁塔排产计划统计分析数据接收接口
	public static final String ehvPlan = "ehvPlan";
	// 铁塔生产进度周报信息统计数据接收接口
	public static final String ehvReportReq = "ehvReportReq";
	// 在线交流--查询主题列表
	public static final String getSubList = "getSubList";
	// 在线交流--查询主题回复列表
	public static final String getReplayList = "getReplayList";
	// 回复主题接口
	public static final String submitReplay = "submitReplay";
	// 发布主题服务接口
	public static final String buildSub = "buildSub";
	// 查询人员列表
	public static final String getPersionList = "getPersionList";
	// 头像上传接口
	public static final String getHeadImage = "getHeadImage";
	// 获取用户头像接口
	public static final String getHeadImg = "getHeadImg";
	// 删除主题 
	public static final String delSubject = "delSubject";
	// 首页查看通知公告未读消息条数
	public static final String getNoRedNum = "getNoRedNum";
	// 通知公告消息列表	
	public static final String getNoticeList = "getNoticeList";
	// 通知公告消息详情	
	public static final String getNoticeDetail = "getNoticeDetail";
	// 位置定位	
	public static final String LocationUpdate = "locationUpdate";
	// 设备生产进度跟踪报表接口
	public static final String ehvMaterialprocedure = "ehvMaterialprocedure";
	// 物资月度供应计划信息列表接口
	public static final String uhvMateriallist = "uhvMateriallist";
	// 编制发货计划接口
	public static final String DelplanDownload = "delplanDownload";
	// 发货计划上传接口
	public static final String DelplanUpload = "delplanUpload";
	// 发货计划行列表接口
	public static final String Delplanlinelist = "delplanlinelist";
	// 编制起运通知接口
	public static final String DeldepartureDownload = "deldpartureDownload";
	// 起运通知上传接口
	public static final String Deldepartureupload = "deldpartureUpload";
	//计划列表接口
	public static final String delplanlist = "delplanlist";
	// 监控预警接口
	public static final String Monitorwar = "monitorwar";
	// 发货计划确认提交接口
	public static final String deldeterupload = "deldeterupload";
	// 到货确认请求接口
	public static final String delconfirmDownload = "delconfirmDownload";
	// 到货确认提交接口
	public static final String delconfirmupload = "delconfirmupload";

	//发货批次拒绝信息
	public static final String queryDenial = "queryDenial";
}
