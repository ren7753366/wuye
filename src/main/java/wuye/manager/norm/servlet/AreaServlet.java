package wuye.manager.norm.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import wuye.manager.norm.bean.AreaBean;
import wuye.manager.norm.bean.NormLevelBean;
import wuye.manager.norm.logic.AreaLogic;
import wuye.manager.norm.logic.NormLogic;
import wuye.manager.utils.ManagerRetBean;
import wuye.manager.utils.PageUtil;


@Controller
@RequestMapping("/manager")
public class AreaServlet {

	@Autowired
	private AreaLogic areaLogic;
	
	@Autowired
	private NormLogic normLogic;
	
	/**
	 * to 考核区域设置页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toAreaSet.aspx")
	public String toAreaSetting(HttpServletRequest request,HttpServletResponse response){
//		UserBean ub = (UserBean) request.getSession().getAttribute("user");
//		request.setAttribute("loginName", ub.getCn());
		
		return "qy_set";
	}
	
	
	/**
	 * to 考核区域设置页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toAreaSetChild.aspx")
	public String toAreaSetting(HttpServletRequest request,HttpServletResponse response
			,@RequestParam(value = "type", required = true)Integer type
			,@RequestParam(value = "pageNum", required = true)Integer pageNum){
//		UserBean ub = (UserBean) request.getSession().getAttribute("user");
//		request.setAttribute("loginName", ub.getCn());
		
		if(type!=1&&type!=5){//城区和物业没有上级
			List<AreaBean> areaList = areaLogic.queryAreaList(type-1);
			request.setAttribute("parentList", areaList);
		}
		
		PageUtil page = new PageUtil(pageNum);
		List<AreaBean> areaList = areaLogic.queryAreaList(type,page);
		
		if(type==3){//片区设置
			List<NormLevelBean> levelList = normLogic.queryNormLevelList();
			request.setAttribute("levelList", levelList);
			
			for(int i=0;i<areaList.size();i++){
				int levelId = areaList.get(i).getLevelId();
				String levelName = normLogic.getNormLevelById(levelId);
				areaList.get(i).setLevelName(levelName);
			}
		}

		
		request.setAttribute("list", areaList);
		request.setAttribute("page", page);
		String retPage="qy_set_"+type;
		return retPage;
	}
	
	
	/**
	 * 添加或修改考核级别
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addArea.aspx")
	public String addOrUpdateKhLevel(HttpServletRequest request,HttpServletResponse response
			,@Validated( { AreaBean.class }) AreaBean areaBean){
		System.out.println(areaBean.toString());
		String msg = "";
		if(areaBean.getId()!=0){//修改
			if(areaLogic.updateArea(areaBean)){
				msg = "修改成功";
			}else{
				msg = "修改失败，请重新尝试";
			};
			
		}else{//添加
			if(areaLogic.addArea(areaBean)){
				msg = "添加成功";
			};
		}
		return "redirect:/manager/toAreaSetChild.aspx?type="+areaBean.getType()+"&pageNum=1&msg="+msg;
	}
	
	
	/**
	 * 添加或修改考核级别
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteArea.aspx")
	@ResponseBody
	public Object addOrUpdateKhLevel(HttpServletRequest request,HttpServletResponse response
			,@RequestParam(value = "type", required = true)Integer type
			,@RequestParam(value = "id", required = true)Integer id){


		areaLogic.deleteArea(type,id);
		
		ManagerRetBean ret = new ManagerRetBean();
		ret.setRet(0);
		ret.setMsg("删除成功");
		return ret;
	}
	
}
