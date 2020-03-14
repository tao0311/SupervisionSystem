package com.chtc.util;

/**
 * JSON模型
 * 
 * @author 
 * 
 */
public class Json implements java.io.Serializable {
	private  String code="";
	private String msg = "";// 提示信息
	private Object obj = null;// 其他信息
	private boolean success = false;// 是否成功

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
