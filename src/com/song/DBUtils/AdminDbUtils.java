package com.song.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.song.DBModule.Administrators;
import com.song.factory.DBFactory;

public class AdminDbUtils implements MyDBUtils<Administrators> {
	private static final String TABLE_NAME = "Administrators";
	private static ComboPooledDataSource ds = null;
	private Connection conn = null;
	private PreparedStatement pre = null;
	private Administrators administrator = new Administrators();


	@Override
	public Administrators select(String userName) {
		try {
//			System.out.println(t.getClass().toString().equals("class com.song.DBModule.Administrators")); 
			String sql = "select * from "+TABLE_NAME+" where userName = '"+userName+"'";
			System.out.println(sql);
			conn = DBFactory.getConnection();
				pre = (PreparedStatement) conn.prepareStatement(sql);
				ResultSet resultSet = pre.executeQuery();
				if(resultSet.next()){
					administrator.setUserName(resultSet.getString(2));
					administrator.setPassword(resultSet.getString(3));
					administrator.setPermission(resultSet.getInt(4));
					administrator.setScopes(resultSet.getString(5));
					administrator.setName(resultSet.getString(6));
					administrator.setEmail(resultSet.getString(7));
					administrator.setPhoneNumber(resultSet.getString(8));
					administrator.setRemark(resultSet.getString(9));
				}	
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}finally{
			try {
				conn.close();
				pre.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				return null;
			}	
		}
		return administrator;
	}

	@Override
	public Boolean add(Administrators admin) {
		try {
			String sql = "insert into "+TABLE_NAME+"(userName,password,permission,scopes,name,email,phoneNumber,remark)"+" values(?,?,?,?,?,?,?,?)";
		    conn = DBFactory.getConnection();
			pre = (PreparedStatement) conn.prepareStatement(sql);
			pre.setString(1, admin.getUserName());
			pre.setString(2, admin.getPassword());
			pre.setInt(3, admin.getPermission());
			pre.setString(4, admin.getScopes());
			pre.setString(5, admin.getName());
			pre.setString(6, admin.getEmail());
			pre.setString(7, admin.getPhoneNumber());
			pre.setString(8, admin.getRemark());
			pre.execute();			
		} catch (Exception e) {
			return false;
		}finally{
			try {
				conn.close();
				pre.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				return false;
			}	
		}
		return true;
	}

	@Override
	public Boolean update(Administrators admin) {
		try {
			//String sql="update "+TABLE_NAME+" set userName=?,password=?... where id = ?";
			String sql = "update "+TABLE_NAME+" set userName=?,password=?,permission=?,scopes=?,name=?,email=?,phoneNumber=?,remark=? where id="+admin.getId();
			System.out.println(sql);
			conn = DBFactory.getConnection();
			pre = (PreparedStatement) conn.prepareStatement(sql);
			pre.setString(1, admin.getUserName());
			pre.setString(2, admin.getPassword());
			pre.setInt(3, admin.getPermission());
			pre.setString(4, admin.getScopes());
			pre.setString(5, admin.getName());
			pre.setString(6, admin.getEmail());
			pre.setString(7, admin.getPhoneNumber());
			pre.setString(8, admin.getRemark());
			pre.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				conn.close();
				pre.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}	
		}
		return true;
	}


	@Override
	public  <E> Boolean delete(E id) {
		try {
			String sql = "delete from "+TABLE_NAME+" where id = ?";
			conn = DBFactory.getConnection();
			pre = (PreparedStatement) conn.prepareStatement(sql);
			pre.setInt(1, (Integer) id);
			pre.execute();
		} catch (Exception e) {
			return false;
		}finally{
			try {
				conn.close();
				pre.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				return false;
			}	
		}
		return true;
	}

	@Override
	public List<Administrators> query(String sql) {
		try {
			System.out.println(sql);
			conn = DBFactory.getConnection();
			pre = (PreparedStatement) conn.prepareStatement(sql);
			List<Administrators> list = new ArrayList<Administrators>();
			ResultSet resultSet = pre.executeQuery();
			
			while(resultSet.next()){
				Administrators admin = new Administrators();
				admin.setId(resultSet.getInt(1));
				admin.setUserName(resultSet.getString(2));
				admin.setPassword(resultSet.getString(3));
				admin.setPermission(resultSet.getInt(4));
				admin.setScopes(resultSet.getString(5));
				admin.setName(resultSet.getString(6));
				admin.setEmail(resultSet.getString(7));
				admin.setPhoneNumber(resultSet.getString(8));
				admin.setRemark(resultSet.getString(9));
				list.add(admin);
			}
			return list;
		} catch (Exception e) {
			return null;
		}finally{
			try {
				conn.close();
				pre.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				return null;
			}	
		}
		
	}

	@Override
	public boolean updateLogisticsId(String id, String lid) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
