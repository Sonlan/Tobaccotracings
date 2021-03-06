package com.song.DBModule;
/**
 * 销售信息数据库模型
 * @author songsong
 *根据region 和 pName以及type和Date唯一确定一条记录，关键信息为num销售额
 */
public class Sales {
	private int id;  //主键
	private String region;  //地域
	private String pName;  //产品名称
	private String time; //销售时间年月
	private int num;  //销售额
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
}
