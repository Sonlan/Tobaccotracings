package com.song.factory;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.sql.SQLException;

import com.song.DBUtils.AdminDbUtils;
import com.song.DBUtils.BoxesDbUtils;
import com.song.DBUtils.CasesDbUtils;
import com.song.DBUtils.LogisticsDbUtils;
import com.song.DBUtils.MyDBUtils;
import com.song.DBUtils.ProductDbUtils;
import com.song.DBUtils.SalesDbUtils;
import com.song.DBUtils.StoreInfoDbUtils;

public class DBFactory {
	private static ComboPooledDataSource ds = null;
	/**
	 * @param type:0:AdminDbUtils 
	 *type:1:ProductDbUtils
	 *type:2:CasesDbUtils
	 *type:3:BoxesDbUtils
	 *type:4:StoreInfoDbUtils
	 *type:5:LogisticsDbUtils
	 *type:6:SalesDbUtils
	 * @return 数据库操作实例
	 */
	public static MyDBUtils getDBFactory(int type){
		switch (type) {
		case 0:return new AdminDbUtils();
		case 1:return new ProductDbUtils();
		case 2:return new CasesDbUtils();
		case 3:return new BoxesDbUtils();
		case 4:return new StoreInfoDbUtils();
		case 5:return new LogisticsDbUtils();
		case 6:return new SalesDbUtils();
		default: return null;
		}
	}
	public static boolean DbPoolInit(){
		try {
			//连接池初始化
			ds=new ComboPooledDataSource("song");  //传入参数为c3p0配置文件中configname
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			ds = null;
			return false;
		}
	}
	
	public static Connection getConnection(){
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void close(){
		if(null != ds)
			ds.close();
		ds = null;
	}
}
