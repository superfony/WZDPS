package com.epsmart.wzdp.db.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.epsmart.wzdp.db.DatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

/**
 * DaoManager 类进行
 * 
 * @author fony
 * 
 * @param <E>
 */
public class DaoManager<E> {
	protected Dao<E, Integer> dao = null;
	public static DaoManager<?> daoManager;
	private DatabaseHelper<?> dbHelp;
	private Context context;
	private Map<String, Dao<E, Integer>> daos = new HashMap<String, Dao<E, Integer>>();

	public void setContext(Context context) {
		this.context = context;
	}
	public DaoManager() {
	}

	public DaoManager(Context context) {
		this.context = context;
	}

	
	public static DaoManager<?> getInstance() {
		if (daoManager == null) {
			daoManager = new DaoManager();
		}
		return daoManager;
	}

	/**
	 * 获取对应类的dao对象
	 * 
	 * @param dbHelp
	 * @param cls
	 */
	@SuppressWarnings("unchecked")
	public Dao<E, Integer> getDao(Class<?> clazz) {
		String className = clazz.getSimpleName();
		if (daos.containsKey(className)) {
			dao = daos.get(className);
		}else {
			try {
				dbHelp = DatabaseHelper.getHelper(context);
				dao = (Dao<E, Integer>) dbHelp.getDao(clazz);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			daos.put(className, dao);
		}
		return dao;
	}

	/**
	 * 查询本地所有表数据
	 * 
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<E> getAllData() {
		List<E> list = null;
		try {
			list = (List<E>) dao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 分页查询本地表数据
	 */
	public List<E> getPageData(String stargPage, String sum) {
		List<E> list = null;
		// QueryBuilder<E, Integer> queryBuilder = dao.queryBuilder();
		// PreparedQuery<E> preparedQuery;
		// queryBuilder
		return list;
	}

	/**
	 * 根据id单条记录数据
	 */
	public E getE(Integer id) {
		E e = null;
		try {
			e = dao.queryForId(id);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return e;
	}

	/**
	 * 根据where条件查询
	 */
	public List<E> getWhereList(String attributeName, String attributeValue) {
		List<E> list = null;
		QueryBuilder<E, Integer> queryBuilder = dao.queryBuilder();
		PreparedQuery<E> preparedQuery;
		try {
			queryBuilder.where().eq(attributeName, attributeValue);
			preparedQuery = queryBuilder.prepare();
			list = dao.query(preparedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 多个条件查询
	 */
	public List<E> getMoreWhereList(String[] attributeNames,
			String[] attributeValues) {
		List<E> list = null;
		QueryBuilder<E, Integer> queryBuilder = dao.queryBuilder();
		Where<E, Integer> wheres = queryBuilder.where();
		try {
		for (int i = 0; i < attributeNames.length; i++) {
			if(TextUtils.isEmpty(attributeValues[i]))
			   continue;
			if(i==attributeNames.length-1)
				wheres.eq(attributeNames[i], attributeValues[i]);
			else
				wheres.eq(attributeNames[i], attributeValues[i]).and();
		}
//		PreparedQuery<E> preparedQuery = queryBuilder.prepare();
//		list = dao.query(preparedQuery);
		list=queryBuilder.query();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 新增操作
	 */
	public int save(E e) throws SQLException {
		return dao.create(e);
	}

	/**
	 * 删除操作
	 */
	public int delete(E t) throws SQLException {
		return dao.delete(t);
	}

	/**
	 * 批量删除
	 */
	public int delete(List<E> lst) throws SQLException {
		return dao.delete(lst);
	}

	/**
	 * 更新操作
	 * */
	public int update(E e) throws SQLException {
		return dao.update(e);
	}

	/**
	 * 获取总记录数
	 */
	public long getCount() throws SQLException {
		return dao.countOf();
	}

	/**
	 * 释放资源
	 */
	public void closeDao() {

		for (String key : daos.keySet()) {
			Dao<E, Integer> dao = daos.get(key);
			dao = null;
		}
	}

}
