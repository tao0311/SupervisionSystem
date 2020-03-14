package com.chtc.util;

import org.json.JSONObject;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * datatable工具类
 * 
 * @author 
 * 
 */
public class DataTableUtil {
	/**
	 *	iDisplayStart  起始值
	 *	iDisplayLength 每页的有多少行
	 *	iSortCol_0 从第几列开始使用排序
	 *  mDataProp_ 列名
	 * @param request
	 * @return
	 */
	public static DataRequest trans(HttpServletRequest request) {
		DataRequest dr=new DataRequest();
		Integer iDisplayStart=Integer.valueOf(request.getParameter("iDisplayStart"));
		Integer iDisplayLength=Integer.valueOf(request.getParameter("iDisplayLength"));
		String iSortCol=request.getParameter("iSortCol_0");
		if("0".equalsIgnoreCase(iSortCol) || StringUtils.isEmpty(iSortCol)){
			dr.setSidx("createDate");
		}else{
			String columnName=request.getParameter("mDataProp_"+iSortCol);
			dr.setSidx(columnName);
		}
		String sort=request.getParameter("sSortDir_0");
		String search=request.getParameter("sSearch");

//		try {
//			search = new String(search.getBytes("iso8859-1"),"UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}

		Integer page=iDisplayStart/iDisplayLength;
		dr.setPage(page+1);
		dr.setRows(iDisplayLength);
		dr.setSord(sort);
		dr.setSearch(search);
		String type=request.getParameter("type");
		dr.setType(type);
		return dr;
	}
	
	public static String trans(Class obj,List<Object> param,String search,String[] ingores) {
		Field[] fs = obj.getDeclaredFields();
		String str="";
		String[] valids=getValidPara(fs,ingores);
		int length = valids.length;
		for(int i=0;i<length;i++){
			String name=valids[i];
			if("createTime".equalsIgnoreCase(name)){
			}else{
				str+=name+" like ?";
				param.add("%"+search+"%");
			}
			if(i!=length-1){
				str+=" or ";
			}
		}
		return str;
	}
	
	private static String[] getValidPara(Field[] fs,String[] ingores){
		List<String> list=new ArrayList<String>();
		int length = fs.length;
		for(int i=0;i<length;i++){
			boolean isIngore=true;
			Field f = fs[i];
			String name = f.getName();
			for(String ingore:ingores){
				if(ingore.equalsIgnoreCase(name)){
					isIngore=false;
				}
			}
			if(isIngore){
				list.add(name);
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	public static String transToJsonStr(String sEcho,DataTableReturnObject dro){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("sEcho", sEcho);
		jsonObject.put("iTotalRecords", dro.getiTotalRecords());
		jsonObject.put("iTotalDisplayRecords", dro.getiTotalDisplayRecords());
		jsonObject.put("aaData", dro.getAaData());
		return jsonObject.toString();
	}
	
}
