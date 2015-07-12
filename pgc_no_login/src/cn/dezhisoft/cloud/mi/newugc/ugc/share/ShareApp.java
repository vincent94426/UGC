package cn.dezhisoft.cloud.mi.newugc.ugc.share;
import java.io.Serializable;

import android.content.pm.ResolveInfo;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class ShareApp implements Serializable {

	private String name;
	
	private ResolveInfo resolveInfo;
	
	public ShareApp(){
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ResolveInfo getResolveInfo() {
		return resolveInfo;
	}

	public void setResolveInfo(ResolveInfo resolveInfo) {
		this.resolveInfo = resolveInfo;
	}
	
}
