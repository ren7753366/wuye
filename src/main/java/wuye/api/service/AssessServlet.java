package wuye.api.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import wuye.api.bean.RetBean;
import wuye.bean.AssessDataBean;
import wuye.bean.CheckDayItem;
import wuye.bean.ListData;
import wuye.logic.AssessLogic;

/**
 * 
 * @author lujinfei
 *
 */
@Controller
@RequestMapping("/api/Assess")
public class AssessServlet {
	
	private static Logger logger = Logger.getLogger("assess");
	
	private AssessLogic assessLogic;

	public AssessLogic getAssessLogic() {
		return assessLogic;
	}

	public void setAssessLogic(AssessLogic assessLogic) {
		this.assessLogic = assessLogic;
	}

	@RequestMapping("submit")
	@ResponseBody
    public Object submit(HttpServletRequest request){
		String serailID = request.getParameter("serailid");
		String sdd = request.getParameter("data");  // 
		String pic1 = request.getParameter("pic1");      // 
		String pic2 = request.getParameter("pic2");      // 
		String pic3 = request.getParameter("pic3");      // 
		String pic4 = request.getParameter("pic4");
		String loc = request.getParameter("loc");        // 
		String msg = request.getParameter("msg");
		
		String uid = (String)request.getSession().getAttribute("userid");
		if(uid == null) {   //
			return new RetBean(2, "未登录");
		}
		
		String right = (String)request.getSession().getAttribute("right");
		if(right == null || ((Integer.parseInt(right)& 0x01) == 0)) {   // 
			return new RetBean(3, "没有权限");
		}
		
		SimpleDateFormat time=new SimpleDateFormat("yyyyMMddHHmmss"); 
		AssessDataBean data = new AssessDataBean();
		
		String[] temp = sdd.split("%");
		data.setAreaid(Integer.parseInt(temp[0].substring(1)));
		data.setStreetid(Integer.parseInt(temp[1].substring(1)));
		data.setPianquid(Integer.parseInt(temp[2].substring(1)));
		data.setHutongid(Integer.parseInt(temp[3].substring(1)));
		data.setWuyeid(Integer.parseInt(temp[4].substring(2)));
		data.setJibieid(Integer.parseInt(temp[5]));
		data.setYeneiid(Integer.parseInt(temp[6]));
		data.setAssessidtop(Integer.parseInt(temp[7].substring(2)));
		data.setAssessid(Integer.parseInt(temp[8].substring(2)));
		data.setScore(Integer.parseInt(temp[9]));
		data.setLoc(loc);
		
		data.setUserid(Integer.parseInt(uid));
		
		try {
			data.setTime(time.parse(temp[10]));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.setSerailID(serailID);
		data.setImg1(pic1);
		data.setImg2(pic2);
		data.setImg3(pic3);
		data.setImg4(pic4);
		data.setMsg(msg);
		
		int ret = assessLogic.submit(data);
		int aseid = data.getAssessid();
		
		logger.info("assess:" + ret + "," + JSON.toJSONString(data));
		
		if(ret == 0) {
			RetBean jsonRet = new RetBean(0, "");
			jsonRet.setValue(aseid);
			return jsonRet;
		}else {
			return new RetBean(99, "其他错误");
		}
    }
	
	@RequestMapping("point")
	@ResponseBody
    public Object point(HttpServletRequest request,@RequestParam(value = "street", required = true)Integer street
    		,@RequestParam(value = "pianqu", required = true)Integer pianqu
    		,@RequestParam(value = "time", required = true)String time) {
		
		System.out.println(street+"----"+pianqu+"----"+time);
		
		return assessLogic.getPoint(street,pianqu,time);
	}
	
	@RequestMapping("allsort")
	@ResponseBody
    public Object allsort(HttpServletRequest request) {
		
		String timetype = request.getParameter("timetype");  // 
		String time = request.getParameter("time");          //  时间
		String areaid = request.getParameter("areaid");      //  层次选
		String level = request.getParameter("level");        //  必选级别，这个选项目前没用上
		
		try{
			RetBean jsonRet = new RetBean(0, "");
			jsonRet.setValue(new ListData(assessLogic.getPianquSortData(timetype.charAt(0), time, Integer.parseInt(level), areaid)));
			return jsonRet;
		} catch(Exception e) {
			return new RetBean(2, "参数错误");
		}
	}
	
	@RequestMapping("itemsort")
	@ResponseBody
    public Object itemsort(HttpServletRequest request) {
		String timetype = request.getParameter("timetype");  // 
		String time = request.getParameter("time");          //  时间
		
		
		try{
			RetBean jsonRet = new RetBean(0, "");
			jsonRet.setValue(assessLogic.getBadCheck(timetype.charAt(0), time));
			return jsonRet;
		} catch(Exception e) {
			return new RetBean(2, "参数错误");
		}
	}
	
	@RequestMapping("pianqusort")
	@ResponseBody
    public Object pianqusort(HttpServletRequest request) {
		String timetype = request.getParameter("timetype");  // 
		String time = request.getParameter("time");          //  时间
		String areaid = request.getParameter("pianquid");    //  只选片区
		
		try{
			RetBean jsonRet = new RetBean(0, "");
			jsonRet.setValue(new ListData(assessLogic.getPianquSortList(timetype.charAt(0), time, areaid)));
			return jsonRet;
		} catch(Exception e) {
			e.printStackTrace();
			return new RetBean(2, "参数错误");
		}
	}
	
	@RequestMapping("wuyesort")
	@ResponseBody
    public Object wuyesort(HttpServletRequest request) {
		String timetype = request.getParameter("timetype");  // 
		String time = request.getParameter("time");          //  时间
		String wuyeid = request.getParameter("wuyeid");    //  只选片区
		
		try{
			RetBean jsonRet = new RetBean(0, "");
			jsonRet.setValue(new ListData(assessLogic.getWuyeSortList(timetype.charAt(0), time, wuyeid)));
			return jsonRet;
		} catch(Exception e) {
			e.printStackTrace();
			return new RetBean(2, "参数错误");
		}
	}
	
	@RequestMapping("checkday")
	@ResponseBody
    public Object checkday(HttpServletRequest request) {
		String start = request.getParameter("start");      // 
		String end = request.getParameter("end");      		// 
		String areaid = request.getParameter("pianquid");
		String checkyenei = request.getParameter("checkyenei");      // 这个是业内还是业外
		String page = request.getParameter("page");    // 当前页
		
		try{
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date dStart = df.parse(start);
			Date dEnd = df.parse(end);
			
			int iPage = Integer.parseInt(page);
			
			List<CheckDayItem> ret = assessLogic.getCheckDayList(dStart, dEnd, areaid, checkyenei, iPage);
			RetBean jsonRet = new RetBean(0, "");
			jsonRet.setValue(new ListData(ret));
			return jsonRet;
		} catch(Exception e) {
			return new RetBean(2, "参数错误");
		}
	}
	
	@RequestMapping("detailitem")
	@ResponseBody
    public Object detailitem(HttpServletRequest request) {
		String date = request.getParameter("date");
		String hutongid = request.getParameter("hutongid");
		String checkyenei = request.getParameter("checkyenei");      // 这个是业内还是业外
		String page = request.getParameter("page");    // 当前页
		
		try{
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date dStart = df.parse(date);
			
			int iPage = Integer.parseInt(page);
			
			List<AssessDataBean> ret = assessLogic.getDetailitem(dStart, hutongid, checkyenei, iPage);
			RetBean jsonRet = new RetBean(0, "");
			jsonRet.setValue(new ListData(ret));
			return jsonRet;
		} catch(Exception e) {
			return new RetBean(2, "参数错误");
		}
	}
	
	@RequestMapping("dumpfile")
	@ResponseBody
    public Object dumpfile(HttpServletRequest request) {
		String date = request.getParameter("date");
			
			int ret = assessLogic.getPianquWeekData(Integer.parseInt(date));
			RetBean jsonRet = new RetBean(ret, "");
			jsonRet.setValue(new ListData(ret));
			return jsonRet;
		
	}
	
	@RequestMapping("deleteitem")
	@ResponseBody
    public Object deleteitem(HttpServletRequest request) {
		String serailID = request.getParameter("serailid");  // 需要删除的id

		if(serailID == null) {
			RetBean jsonRet = new RetBean(1, "");
			return jsonRet;
		}
			int ret = assessLogic.delAssess(serailID);
			RetBean jsonRet = new RetBean(ret, "");
			return jsonRet;
		
	}
}
