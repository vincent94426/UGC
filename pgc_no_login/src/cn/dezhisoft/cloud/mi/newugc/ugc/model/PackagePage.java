package cn.dezhisoft.cloud.mi.newugc.ugc.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 素材包
 * 
 * @author Rosson Chen
 *
 */
public class PackagePage extends Page implements Serializable {

	/** 素材列表*/
	private final ArrayList<PackageType> packageTypeList = new ArrayList<PackageType>() ;

	public PackagePage(){
		packageTypeList.clear() ;
	}
	
	public ArrayList<PackageType> getPackageTypeList() {
		return packageTypeList;
	}
	
	public void addPackageType(PackageType contentType){
		packageTypeList.add(contentType);
	}
	
	public void clear(){
		super.clear() ;
		
		packageTypeList.clear() ;
	}
}
