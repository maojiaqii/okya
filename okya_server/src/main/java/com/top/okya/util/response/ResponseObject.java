package com.top.okya.util.response;

/**
 * 
 * 类名： ResponseObject <br>
 * 路径： com.tools <br>
 * 描述： 返回到页面的数据处理结果对象 <br>
 * 编写： maojiaqi <br>
 * 日期： 2018年11月6日 下午5:13:25
 *
 */
public class ResponseObject {
	private boolean success;
	private String message;
	private String extend;

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public String getExtend() {
		return extend;
	}

	/**
	 * 
	 * @param _success
	 *            操作是否成功
	 * @param _message
	 *            返回给页面的消息
	 * @param _extend
	 *            返回给页面附带消息
	 */
	public ResponseObject(boolean _success, String _message, String _extend) {
		this.success = _success;
		this.message = _message;
		this.extend = _extend;
	}
	
	public String invoke() {
		return ResponseJson.invoke(0, this);
	}

}
