package com.song.DBUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.song.DBModule.Boxes;
import com.song.factory.DBFactory;

public class BoxesDbUtils implements MyDBUtils<Boxes> {
	private static final String TABLE_NAME = "boxes";
	private Connection conn = null;
	private PreparedStatement pre = null;
	private Boxes boxes = new Boxes();
	/**
	 * 更新物流ID
	 * @param id 产品ID
	 * @param lid 物流ID
	 * @return
	 */
	@Override
	public boolean updateLogisticsId(String id, String lid){
		try {
			conn = DBFactory.getConnection();
			System.out.println("update "+TABLE_NAME+" set logisticsId='"+lid+"'where boxID='"+id+"'");
			pre = (PreparedStatement) conn.prepareStatement("update "+TABLE_NAME+" set logisticsId='"+lid+"'where boxID='"+id+"'");
			pre.executeUpdate();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}
	@Override
	public Boxes select(String boxID) {
		try {
//			System.out.println(t.getClass().toString().equals("class com.song.DBModule.Administrators")); 
			String sql = "select * from "+TABLE_NAME+" where boxID = '"+boxID+"'";
			System.out.println(sql);
			conn = DBFactory.getConnection();
				pre = (PreparedStatement) conn.prepareStatement(sql);
				ResultSet resultSet = pre.executeQuery();
				if(resultSet.next()){
					boxes.setBoxID(resultSet.getString(1));
					boxes.setLogisticsId(resultSet.getString(2));
					boxes.setPd(resultSet.getString(3));
					boxes.setGp(resultSet.getString(4));
					boxes.setManufacturer(resultSet.getString(5));
					boxes.setPb(resultSet.getString(6));
					boxes.setStoreID(resultSet.getString(7));
					boxes.setpName(resultSet.getString(8));
					boxes.setAmount(resultSet.getInt(9));
					boxes.setPrice(resultSet.getFloat(10));
					boxes.setState(resultSet.getString(11));
					boxes.setTargetAddr(resultSet.getString(12));
					boxes.setRemark(resultSet.getString(13));
					boxes.setConsumeAddr(resultSet.getString(14));
					boxes.setConsumeTime(resultSet.getString(15));
					return boxes;
				}	
				return null;
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
	}

	@Override
	public Boolean add(Boxes boxes) {
		try {
			String sql = "insert into "+TABLE_NAME+"(boxID,logisticsId,pd,gp,manufacturer,pb,storeID,pName, amount, price,state, targetAddr,remark,consumeAddr,consumeTime)"+" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		    conn = DBFactory.getConnection();
			pre = (PreparedStatement) conn.prepareStatement(sql);
			pre.setString(1, boxes.getBoxID());
			pre.setString(2, boxes.getLogisticsId());
			pre.setString(3, boxes.getPd());
			pre.setString(4, boxes.getGp());
			pre.setString(5, boxes.getManufacturer());
			pre.setString(6, boxes.getPb());
			pre.setString(7, boxes.getStoreID());
			pre.setString(8, boxes.getpName());
			pre.setInt(9, boxes.getAmount());
			pre.setFloat(10, boxes.getPrice());
			pre.setString(11, boxes.getState());
			pre.setString(12, boxes.getTargetAddr());
			pre.setString(13, boxes.getRemark());
			pre.setString(14, boxes.getConsumeAddr());
			pre.setString(15, boxes.getConsumeTime());
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
	public Boolean update(Boxes boxes) {
		try {
			//String sql="update "+TABLE_NAME+" set userName=?,password=?... where id = ?";
			String sql = "update "+TABLE_NAME+" set logisticsId=?,pd=?,gp=?,manufacturer=?,pb=?,storeID=?, pName=?, amount=?, price=?,state=?,targetAddr=?,remark=?,consumeAddr=?,consumeTime=? where boxID='"+boxes.getBoxID()+"'";
			System.out.println(sql);
			conn = DBFactory.getConnection();
			pre = (PreparedStatement) conn.prepareStatement(sql);
			pre.setString(1, boxes.getLogisticsId());
			pre.setString(2,boxes.getPd());
			pre.setString(3,boxes.getGp());
			pre.setString(4, boxes.getManufacturer());
			pre.setString(5, boxes.getPb());
			pre.setString(6, boxes.getStoreID());
			pre.setString(7, boxes.getpName());
			pre.setInt(8, boxes.getAmount());
			pre.setFloat(9, boxes.getPrice());
			pre.setString(10, boxes.getState());
			pre.setString(11, boxes.getTargetAddr());
			pre.setString(12, boxes.getRemark());
			pre.setString(13, boxes.getConsumeAddr());
			pre.setString(14, boxes.getConsumeTime());
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
	public <E> Boolean delete(E id) {
		try {
			String sql = "delete from "+TABLE_NAME+" where boxID = ?";
			conn = DBFactory.getConnection();
			pre = (PreparedStatement) conn.prepareStatement(sql);
			pre.setString(1, (String) id);
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
	public List<Boxes> query(String sql) {
		try {
			System.out.println(sql);
			conn = DBFactory.getConnection();
			pre = (PreparedStatement) conn.prepareStatement(sql);
			List<Boxes> list = new ArrayList<Boxes>();
			ResultSet resultSet = pre.executeQuery();
			
			while(resultSet.next()){
				Boxes boxes = new Boxes();
				boxes.setBoxID(resultSet.getString(1));
				boxes.setLogisticsId(resultSet.getString(2));
				boxes.setPd(resultSet.getString(3));
				boxes.setGp(resultSet.getString(4));
				boxes.setManufacturer(resultSet.getString(5));
				boxes.setPb(resultSet.getString(6));
				boxes.setStoreID(resultSet.getString(7));
				boxes.setpName(resultSet.getString(8));
				boxes.setAmount(resultSet.getInt(9));
				boxes.setPrice(resultSet.getFloat(10));
				boxes.setState(resultSet.getString(11));
				boxes.setTargetAddr(resultSet.getString(12));
				boxes.setRemark(resultSet.getString(13));
				boxes.setConsumeAddr(resultSet.getString(14));
				boxes.setConsumeTime(resultSet.getString(15));
				list.add(boxes);
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


}

