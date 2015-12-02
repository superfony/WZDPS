package com;



public interface ICMIS {
	
	/**1.移动终端的账户管理接口
	 * @param name
	 * @param password
	 * @return
	 */
	public String userLogin(String name, String password);

	/**2.获取终端用户列表
	 * @param gdjdm
	 * @param dept_id
	 * @param account
	 * @param beginRownum
	 * @param endRownum
	 * @return
	 */
	public String getUserList(String gdjdm,String dept_id,String account,String beginRownum,String endRownum);

	/**3.生产过程监控基础信息接收接口
	 * @param userid
	 * @param start
	 * @param limit
	 * @return
	 */
	public String procedureReq(String userid, String start, String limit);
	
	// 4.获取终端用户列表
//	public abstract String getUserList(String gdjdm, String dept_id, String account, String beginRownum, String endRownum);
	
	// 5.项目现场管理基础信息
	public abstract String projectQueryReq(String userId, String pageNo, String pageSize);

	// 6.生产过程信息上传
	public abstract String procedureUpload(String userId, String req);

	// 7.质量问题处理反馈接收
	public abstract String qualityConduct(String userId, String pageNo, String pageSize);

	// 8.质量问题处理结果上传
	public abstract String qualityResultUpload(String userId, String req);

	// 9.关键点见证基础信息上传
	public abstract String keyPotQueryUpload(String userId, String req);

	// 10.现场服务申请单上传
	public abstract String serviceRequireUpload(String userId, String req);

	// 11.现场服务申请单信息回传
	public abstract String serviceReq(String userId, String req);

	// 12.现场服务评价单上传
	public abstract String serviceEvaluationUpload(String userId, String req);

}
