package com.song.extUtils;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.song.DBModule.Boxes;
import com.song.DBModule.Cases;
import com.song.DBModule.Logistics;
import com.song.DBModule.Products;
import com.song.DBModule.Sales;
import com.song.DBUtils.JsonUtils;
import com.song.DBUtils.MyDBUtils;
import com.song.factory.DBFactory;

public class TestExtUtils extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public TestExtUtils() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		//设置远程访问报头，否则远程客户端访问该网站下的文件，将收不到回执消息
		response.setHeader("Access-Control-Allow-Origin", "*"); 
		String data=request.getParameter("data");  //扫码结果
		String setting = request.getParameter("setting");  //操作类型 
		String date = request.getParameter("date");  //时间
		String addr = request.getParameter("addr");  //地址
		if(data!=null&&setting!=null&&date!=null&&addr!=null){
			response.getWriter().write(ExtUtils(setting, data, date, addr));
		}else {
			System.out.println("receive null");
			response.getWriter().write(JsonUtils.statusResponse(0, "answer from server"));
		}
		return;
	} 

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	//处理函数
	public String ExtUtils(String setting,String data,String date,String addr){
		System.out.println("data :"+data+"   setting :"+setting+"   date :"+date+"  addr :"+addr);
		String prefix=data.substring(0, 1).toUpperCase();  //根据data前缀，判断数据类型
		if(prefix.equals("P")){//产品
			MyDBUtils<Products> dbUtils = DBFactory.getDBFactory(1);
			Products pro = new Products();
			pro = dbUtils.select(data);
			if(pro!=null){
				if(setting.equals("query")){//查询
					//增加物流记录
					addLogistics("查询", pro.getLogisticsId(), addr, date,pro.getId());
					return JsonUtils.statusResponse(1, pro);
				}
				else if(setting.equals("custom")){//消费
					if("已消费".equals(pro.getState())) return JsonUtils.statusResponse(3, "该产品已被消费");
					else{
						//增加物流记录
						addLogistics("消费", pro.getLogisticsId(), addr, date,pro.getId());
						pro.setState("已消费");
						pro.setConsumeAddr(addr);
						pro.setConsumeTime(date);
						if(dbUtils.update(pro)){
							/*MyDBUtils<Sales> dbUtils2 = DBFactory.getDBFactory(6);
							String time = date.split("-")[0]+"-"+date.split("-")[1];
							List<Sales> list = dbUtils2.query("select * from sales where region='"+pro.getTargetAddr()+"' and pName='"+pro.getpName()+"' and type='product' and time='"+time+"'");
							if(!list.isEmpty() && list!=null){
								Sales sales = list.get(0);
								sales.setNum(sales.getNum()+1);
								dbUtils2.update(sales);
							}else{
								Sales sales = new Sales();
								sales.setId(0);
								sales.setRegion(pro.getTargetAddr());
								sales.setpName(pro.getpName());
								sales.setTime(time);
								sales.setNum(1);
								dbUtils2.add(sales);
							}*/
							return JsonUtils.statusResponse(3, "消费成功");
						}else {
							return JsonUtils.statusResponse(3, "消费失败，请重新再试");
						}
					}
				}else if(setting.equals("logistic")){//物流
						String alert = pro.getState().equals("已消费")?"(异常)":"";
						if(addLogistics("运输"+alert, pro.getLogisticsId(), addr, date,pro.getId()))
							return JsonUtils.statusResponse(3, "上传物流信息成功");
						else return JsonUtils.statusResponse(3, "上传物流信息成功");
				}
			}
			else return JsonUtils.statusResponse(2, "没有查找到有效数据，你所查询的为虚假产品");
			
		}else if(prefix.equals("B")){  //Box
			MyDBUtils<Boxes> dbUtils = DBFactory.getDBFactory(3);
			Boxes boxes = new Boxes();
			boxes = dbUtils.select(data);
			if(boxes!=null){
				if(setting.equals("query")){//查询
					//增加物流记录
					addLogistics("查询", boxes.getLogisticsId(), addr, date,boxes.getBoxID());
					return JsonUtils.statusResponse(1, boxes);
				}
				else if(setting.equals("custom")){//消费
					if("已消费".equals(boxes.getState())) return JsonUtils.statusResponse(3, "该产品已被消费");
					else{
						//增加物流记录
						addLogistics("消费", boxes.getLogisticsId(), addr, date,boxes.getBoxID());
						boxes.setState("已消费");
						boxes.setConsumeAddr(addr);
						boxes.setConsumeTime(date);
						if(dbUtils.update(boxes)){
							MyDBUtils<Sales> dbUtils2 = DBFactory.getDBFactory(6);
							String time = date.split("-")[0]+"-"+date.split("-")[1];
							List<Sales> list = dbUtils2.query("select * from sales where region='"+boxes.getTargetAddr()+"' and pName='"+boxes.getpName()+"' and time='"+time+"'");
							if(!list.isEmpty() && list!=null){
								Sales sales = list.get(0);
								sales.setNum(sales.getNum()+1);
								dbUtils2.update(sales);
							}else{
								Sales sales = new Sales();
								sales.setId(0);
								sales.setRegion(boxes.getTargetAddr());
								sales.setpName(boxes.getpName());
								sales.setTime(time);
								sales.setNum(1);
								dbUtils2.add(sales);
							}
							return JsonUtils.statusResponse(3, "消费成功");
						}else {
							return JsonUtils.statusResponse(3, "消费失败，请重新再试");
						}
					}
				}else if(setting.equals("logistic")){//物流
					if(boxes.getLogisticsId().equals("L000")){//默认物流ID
						if(createLogisticsID(data)){  //新建物流
							addLogistics("出库", boxes.getLogisticsId(), addr, date,boxes.getBoxID());
							return JsonUtils.statusResponse(3, "上传物流信息成功");
						}else return JsonUtils.statusResponse(3, "上传物流信息失败");
					}else{
						String alert = boxes.getState().equals("已消费")?"(异常)":"";
						if(addLogistics("运输"+alert, boxes.getLogisticsId(), addr, date,boxes.getBoxID()))
							return JsonUtils.statusResponse(3, "上传物流信息成功");
						else return JsonUtils.statusResponse(3, "上传物流信息失败");
					}
				}
			}
			else return JsonUtils.statusResponse(2, "没有查找到有效数据，你所查询的为虚假产品");
		}else if(prefix.equals("C")){  //Case
			MyDBUtils<Cases> dbUtils = DBFactory.getDBFactory(2);
			Cases cases = new Cases();
			cases = dbUtils.select(data);
			if(cases!=null){
				if(setting.equals("query")){//查询
					//增加物流记录
					addLogistics("查询", cases.getLogisticsId(), addr, date,cases.getCaseID());
					return JsonUtils.statusResponse(1, cases);
				}
				else if(setting.equals("custom")){//消费
					if("已消费".equals(cases.getState())) return JsonUtils.statusResponse(3, "该产品已被消费");
					else{
						//增加物流记录
						addLogistics("消费", cases.getLogisticsId(), addr, date,cases.getBoxID());
						cases.setState("已消费");
						cases.setConsumeAddr(addr);
						cases.setConsumeTime(date);
						if(dbUtils.update(cases)){
							/*MyDBUtils<Sales> dbUtils2 = DBFactory.getDBFactory(6);
							String time = date.split("-")[0]+"-"+date.split("-")[1];
							List<Sales> list = dbUtils2.query("select * from sales where region='"+cases.getTargetAddr()+"' and pName='"+cases.getpName()+"' and type='case' and time='"+time+"'");
							if(!list.isEmpty() && list!=null){
								Sales sales = list.get(0);
								sales.setNum(sales.getNum()+1);
								dbUtils2.update(sales);
							}else{
								Sales sales = new Sales();
								sales.setId(0);
								sales.setRegion(cases.getTargetAddr());
								sales.setpName(cases.getpName());
								sales.setTime(time);
								sales.setNum(1);
								dbUtils2.add(sales);
							}*/
							return JsonUtils.statusResponse(3, "消费成功");
						}else {
							return JsonUtils.statusResponse(3, "消费失败，请重新再试");
						}
					}
				}else if(setting.equals("logistic")){//物流
						String alert = cases.getState().equals("已消费")?"(异常)":"";
						if(addLogistics("运输"+alert, cases.getLogisticsId(), addr, date,cases.getBoxID()))
							return JsonUtils.statusResponse(3, "上传物流信息成功");
						else return JsonUtils.statusResponse(3, "上传物流信息失败");
				}
			}
			else return JsonUtils.statusResponse(2, "没有查找到有效数据，你所查询的为虚假产品");
		}
		return JsonUtils.statusResponse(2, "没有查找到有效数据，你所查询的为虚假产品");
	}//end
	
	/*
	 * @param type:传入物流信息类型，包括产品注册，装盒，装箱，物流，查询，消费
	 * @param id:物流信息编号
	 * @param pid:产品id 用于物流信息备注是哪一个物件被操作，从而区分同一物流下的箱，盒以及产品
	 * @param addr:地点
	 * @param data:时间
	 * @remark：用于添加一条物流信息
	*/
	private boolean addLogistics(String type,String id,String addr,String date,String pid){
		Logistics logistics = new Logistics();
		logistics.setId(0); 
		logistics.setLogisticsID(id);  //根据物流信息编号，绑定到特定产品的物流信息上
		logistics.setRemark(pid);
		logistics.setState(type);
		logistics.setAddress(addr);
		logistics.setTime(date);
		MyDBUtils<Logistics> dbUtils2 = DBFactory.getDBFactory(5);
		return dbUtils2.add(logistics);
	}
	/** 
	 * 重新生成一条物流信息，返回新的物流信息ID
	 * 商品第一次出库产生物流信息时需生成物流信息ID
	 * @param id 出库时BoxID
	 * @return
	 */
	private boolean createLogisticsID(String id){
		MyDBUtils dbUtils = null;
		String lid = "L"+id.substring(1);
		//判断该ID对应的box是否存在
		dbUtils = DBFactory.getDBFactory(3);
		if(dbUtils.select(id)!=null){
			if (dbUtils.updateLogisticsId(id,lid)){
				dbUtils = DBFactory.getDBFactory(2);
				List<Cases> cases = dbUtils.query("select * from cases where boxID = '"+id+"'");
				for(int i=0;i<cases.size();i++){
					if(dbUtils.updateLogisticsId(cases.get(i).getCaseID(), lid)){
						MyDBUtils<Products> dbUtils2 = DBFactory.getDBFactory(1);
						List<Products> pros =dbUtils2.query("select * from products where caseID = '"+cases.get(i).getCaseID()+"'");
						for(int j=0;j<pros.size();j++){
							if(!dbUtils2.updateLogisticsId(pros.get(j).getId(), lid)){
								return false;
							}
						}
					}else return false;
				}
				return true;
			}
		}
		return false;
	}

}
