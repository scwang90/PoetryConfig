package com.poetry.model;

import java.io.Serializable;

import com.google.gson.Gson;
import com.poetry.util.AfVersion;

public class Config implements Serializable{
	private static final long serialVersionUID = -3444358992372197247L;
	public String Name = "";
	public int Verson = 0;
	public String Version = "";
	public boolean HideAd = false;
	public String Remark = "";
	public String Urls = "";
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
//		return Name+"- \tVerson:" + AfVersion.transformVersion(Verson)+"\tHideAd:"+HideAd+"\tRemark:"+Remark+"\tUrls:"+Urls;
		return Name+"-"+new Gson().toJson(this);
	}
}