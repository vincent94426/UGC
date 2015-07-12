package cn.dezhisoft.cloud.mi.newugc.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

public final class JSONUtil {
	private JSONUtil(){}
	
	public static Map<String, Object> toMapFromJSONObject(JSONObject jDomain){
		Map<String, Object> domain = new HashMap<String, Object>();
		Iterator<String> keyIterator = jDomain.keys();
		while (keyIterator.hasNext()) {
			String property = (String) keyIterator.next();
			if(!TextUtils.isEmpty(property) && null != jDomain.opt(property)){
				Object val = jDomain.opt(property);
				if(val instanceof JSONObject){
					domain.put(property, toMapFromJSONObject((JSONObject)val));
				}else if(val instanceof JSONArray){
					domain.put(property, toListFromJSONArray((JSONArray)val));
				}else{
					domain.put(property, val);
				}
			}
		}
		
		return domain;
	}
	
	public static List<Object> toListFromJSONArray(JSONArray val){
		List<Object> valList = new ArrayList<Object>();
		
		for(int i=0, len = val.length();i < len; i++){
			Object element = val.opt(i);
			if(null != element){
				if(element instanceof JSONObject){
					valList.add(toMapFromJSONObject((JSONObject)element));
				}else if(element instanceof JSONArray){
					valList.add(toListFromJSONArray((JSONArray)element));
				}else{
					valList.add(element);
				}
			}
		}
		
		return valList;
	}
	
	public static String toJSONStringFromMap(Map<String, Object> domain){
		JSONObject jdomain = new JSONObject(domain);
		return jdomain.toString();
	}
}
