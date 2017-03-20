package wuye.manager.norm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wuye.dao.DaoBasic;
import wuye.manager.norm.bean.AreaBean;
import wuye.manager.norm.bean.NormCategoryBean;
import wuye.manager.norm.dao.AreaDao;
import wuye.manager.utils.PageUtil;

@Component("areaDao")
public class AreaDaoImpl extends DaoBasic implements AreaDao{

	@Autowired
	private DataSource dataSource;
	
	@Override
	public boolean addState(AreaBean areaBean) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        boolean rs = false;
        try {
            String sql = "insert into t_state (statename) values (?)";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            pstmt.setString(1, areaBean.getName());
            rs = pstmt.execute();
        }catch(Exception e){
       	 	doCatchException("addState" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
        return rs;
	}

	@Override
	public boolean addStreet(AreaBean areaBean) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        boolean rs = false;
        try {
            String sql = "insert into t_street (street_name,area_id) values (?,?)";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            pstmt.setString(1, areaBean.getName());
            pstmt.setInt(2, areaBean.getParentId());
            rs = pstmt.execute();
        }catch(Exception e){
       	 	doCatchException("addStreet" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
        return rs;
	}

	@Override
	public boolean addPianqu(AreaBean areaBean) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        boolean rs = false;
        try {
            String sql = "insert into t_pianqu (pianqu_name,father_id) values (?,?)";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            pstmt.setString(1, areaBean.getName());
            pstmt.setInt(2, areaBean.getParentId());
            rs = pstmt.execute();
        }catch(Exception e){
       	 	doCatchException("addPianqu" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
        return rs;
	}

	@Override
	public boolean addHutong(AreaBean areaBean) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        boolean rs = false;
        try {
            String sql = "insert into t_hutong (hutong_name,father_id) values (?,?)";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            pstmt.setString(1, areaBean.getName());
            pstmt.setInt(2, areaBean.getParentId());
            rs = pstmt.execute();
        }catch(Exception e){
       	 	doCatchException("areaBeans" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
        return rs;
	}

	@Override
	public boolean addCompany(AreaBean areaBean) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        boolean rs = false;
        try {
            String sql = "insert into t_managecompany (company_name) values (?)";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            pstmt.setString(1, areaBean.getName());
            rs = pstmt.execute();
        }catch(Exception e){
       	 	doCatchException("areaBeans" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
        return rs;
	}

	@Override
	public List<AreaBean> queryStateList(PageUtil page) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from t_state limit "+page.getStartIndex()+","+page.getPageSize();
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            rs = pstmt.executeQuery();
            List<AreaBean> list = new ArrayList<AreaBean>();
            while(rs.next()){
            	AreaBean nb = new AreaBean();
            	nb.setId(rs.getInt("id"));
            	nb.setName(rs.getString("statename"));
            	list.add(nb);
            }
            return list;
        }catch(Exception e){
       	 	doCatchException("queryStateList" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
		return null;
	}

	@Override
	public int queryAreaTotal(String tableName) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select count(id) as con from "+tableName;
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            rs = pstmt.executeQuery();
            if(rs.next()){
            	return rs.getInt("con");
            }
        }catch(Exception e){
       	 	doCatchException("queryNormCategoryList" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
		return 0;
	}

	@Override
	public List<AreaBean> queryStreetList(PageUtil page) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select a.id,a.street_name,a.area_id,b.statename from t_street a left join t_state b on b.id=a.area_id order by a.id desc limit "+page.getStartIndex()+","+page.getPageSize();
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            rs = pstmt.executeQuery();
            List<AreaBean> list = new ArrayList<AreaBean>();
            while(rs.next()){
            	AreaBean nb = new AreaBean();
            	nb.setId(rs.getInt("id"));
            	nb.setName(rs.getString("street_name"));
            	nb.setParentId(rs.getInt("area_id"));
            	nb.setParentName(rs.getString("statename"));
            	list.add(nb);
            }
            return list;
        }catch(Exception e){
       	 	doCatchException("queryStreetList" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
		return null;
	}


	@Override
	public List<AreaBean> queryPianquList(PageUtil page) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select a.id,b.street_name,a.pianqu_name,a.father_id from t_pianqu a left join t_street b on b.id=a.father_id order by a.id desc limit "+page.getStartIndex()+","+page.getPageSize();
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            rs = pstmt.executeQuery();
            List<AreaBean> list = new ArrayList<AreaBean>();
            while(rs.next()){
            	AreaBean nb = new AreaBean();
            	nb.setId(rs.getInt("id"));
            	nb.setName(rs.getString("pianqu_name"));
            	nb.setParentId(rs.getInt("father_id"));
            	nb.setParentName(rs.getString("street_name"));
            	list.add(nb);
            }
            return list;
        }catch(Exception e){
       	 	doCatchException("queryPianquList" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
		return null;
	}


	@Override
	public List<AreaBean> queryHutongList(PageUtil page) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select a.id,a.hutong_name,b.pianqu_name,a.father_id from t_hutong a left join t_pianqu b on b.id=a.father_id order by a.id desc limit "+page.getStartIndex()+","+page.getPageSize();
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            rs = pstmt.executeQuery();
            List<AreaBean> list = new ArrayList<AreaBean>();
            while(rs.next()){
            	AreaBean nb = new AreaBean();
            	nb.setId(rs.getInt("id"));
            	nb.setName(rs.getString("hutong_name"));
            	nb.setParentId(rs.getInt("father_id"));
            	nb.setParentName(rs.getString("pianqu_name"));
            	list.add(nb);
            }
            return list;
        }catch(Exception e){
       	 	doCatchException("queryHutongList" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
		return null;
	}


	@Override
	public boolean updateState(AreaBean areaBean) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        boolean rs = false;
        try {
            String sql = "update t_state set statename=? where id =?";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            pstmt.setString(1, areaBean.getName());
            pstmt.setInt(2, areaBean.getId());
            rs = pstmt.execute();
        }catch(Exception e){
       	 	doCatchException("updateState" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
        return rs;
	}

	@Override
	public boolean updateStreet(AreaBean areaBean) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        boolean rs = false;
        try {
            String sql = "update t_street set street_name=?,area_id=? where id =?";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            pstmt.setString(1, areaBean.getName());
            pstmt.setInt(2, areaBean.getParentId());
            pstmt.setInt(3, areaBean.getId());
            rs = pstmt.execute();
        }catch(Exception e){
       	 	doCatchException("updateStreet" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
        return rs;
	}

	@Override
	public boolean updatePianqu(AreaBean areaBean) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        boolean rs = false;
        try {
            String sql = "update t_pianqu set pianqu_name=?,father_id=? where id =?";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            pstmt.setString(1, areaBean.getName());
            pstmt.setInt(2, areaBean.getParentId());
            pstmt.setInt(3, areaBean.getId());
            rs = pstmt.execute();
        }catch(Exception e){
       	 	doCatchException("updatePianqu" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
        return rs;
	}

	@Override
	public boolean updateHutong(AreaBean areaBean) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        boolean rs = false;
        try {
            String sql = "update t_hutong set hutong_name=?,father_id=? where id =?";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            pstmt.setString(1, areaBean.getName());
            pstmt.setInt(2, areaBean.getParentId());
            pstmt.setInt(3, areaBean.getId());
            rs = pstmt.execute();
        }catch(Exception e){
       	 	doCatchException("updateHutong" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
        return rs;
	}

	@Override
	public boolean updateCompany(AreaBean areaBean) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        boolean rs = false;
        try {
            String sql = "update t_managecompany set company_name=? where id =?";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            pstmt.setString(1, areaBean.getName());
            pstmt.setInt(2, areaBean.getId());
            rs = pstmt.execute();
        }catch(Exception e){
       	 	doCatchException("updateCompany" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
        return rs;
	}

	@Override
	public List<AreaBean> queryCompanyList(PageUtil page) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from t_managecompany limit "+page.getStartIndex()+","+page.getPageSize();
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            rs = pstmt.executeQuery();
            List<AreaBean> list = new ArrayList<AreaBean>();
            while(rs.next()){
            	AreaBean nb = new AreaBean();
            	nb.setId(rs.getInt("id"));
            	nb.setName(rs.getString("company_name"));
            	list.add(nb);
            }
            return list;
        }catch(Exception e){
       	 	doCatchException("queryCompanyList" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
		return null;
	}


	@Override
	public void delArea(String tableName,int id) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        boolean rs = false;
        try {
            String sql = "delete from "+tableName+" where id =?";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            pstmt.setInt(1, id);
            rs = pstmt.execute();
        }catch(Exception e){
       	 	doCatchException("delArea" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
	}
	
	/**
	 * 查询城区列表
	 * @param stateBean
	 * @return
	 */
	public List<AreaBean> queryAllState(){
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select id,statename from t_state";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            rs = pstmt.executeQuery();
            List<AreaBean> list = new ArrayList<AreaBean>();
            while(rs.next()){
            	AreaBean nb = new AreaBean();
            	nb.setId(rs.getInt("id"));
            	nb.setName(rs.getString("statename"));
            	list.add(nb);
            }
            return list;
        }catch(Exception e){
       	 	doCatchException("queryAllState" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
		return null;
	}
	
	
	/**
	 * 查询街道列表
	 * @return
	 */
	public List<AreaBean> queryAllStreet(){
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select id,street_name from t_street";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            rs = pstmt.executeQuery();
            List<AreaBean> list = new ArrayList<AreaBean>();
            while(rs.next()){
            	AreaBean nb = new AreaBean();
            	nb.setId(rs.getInt("id"));
            	nb.setName(rs.getString("street_name"));
            	list.add(nb);
            }
            return list;
        }catch(Exception e){
       	 	doCatchException("queryAllStreet" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
		return null;
	}
	
	/**
	 * 查询片区
	 * @param stateBean
	 * @return
	 */
	public List<AreaBean> queryAllPianqu(){
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select id,pianqu_name from t_pianqu";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            rs = pstmt.executeQuery();
            List<AreaBean> list = new ArrayList<AreaBean>();
            while(rs.next()){
            	AreaBean nb = new AreaBean();
            	nb.setId(rs.getInt("id"));
            	nb.setName(rs.getString("pianqu_name"));
            	list.add(nb);
            }
            return list;
        }catch(Exception e){
       	 	doCatchException("queryAllPianqu" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
		return null;
	}

	@Override
	public List<AreaBean> queryAllHutong() {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select id,hutong_name from t_hutong";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            rs = pstmt.executeQuery();
            List<AreaBean> list = new ArrayList<AreaBean>();
            while(rs.next()){
            	AreaBean nb = new AreaBean();
            	nb.setId(rs.getInt("id"));
            	nb.setName(rs.getString("hutong_name"));
            	list.add(nb);
            }
            return list;
        }catch(Exception e){
       	 	doCatchException("queryAllHutong" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
		return null;
	}

	@Override
	public List<AreaBean> queryAllCompany() {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
        	String sql = "select id,company_name from t_managecompany";
            conn = dataSource.getConnection();
            pstmt = prepareStatement(conn, sql);
            rs = pstmt.executeQuery();
            List<AreaBean> list = new ArrayList<AreaBean>();
            while(rs.next()){
            	AreaBean nb = new AreaBean();
            	nb.setId(rs.getInt("id"));
            	nb.setName(rs.getString("company_name"));
            	list.add(nb);
            }
            return list;
        }catch(Exception e){
       	 	doCatchException("queryAllCompany" ,e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
		return null;
	}
	
}
