package com.top.okya.util.response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 
 * 类名： ResponseJson
 * <br>
 * 路径： com.tools
 * <br>
 * 描述： 将要返回到页面的数据进行json格式化
 * <br>
 * 编写： maojiaqi
 * <br>
 * 日期： 2018年11月6日 下午5:02:07
 *
 */
public class ResponseJson {

	private ResponseJson() {};

	/**
	 * 
	 * @param objectOrArray 标识对象还是数组，0=对象，1=数组，其他值按对象处理
	 * @param data 将要json格式化的对象或数组
	 * @return
	 */
	public static String invoke(int objectOrArray, Object data) {
		if (objectOrArray == 1) {
			return JSONArray.toJSONString(data, SerializerFeature.WriteMapNullValue,
					SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullBooleanAsFalse,
					SerializerFeature.WriteNullListAsEmpty);
		} else {
			return JSONObject.toJSONString(data, SerializerFeature.WriteMapNullValue,
					SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullBooleanAsFalse,
					SerializerFeature.WriteNullListAsEmpty);
		}
	}
}
