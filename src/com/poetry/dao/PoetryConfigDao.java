package com.poetry.dao;

import java.util.List;

import com.google.gson.Gson;
import com.poetry.model.Config;
import com.poetry.model.PoetryConfig;
import com.poetry.util.AfMD5;
import com.poetry.util.DesUtils;
import com.xiaomi.infra.galaxy.client.GalaxyAdmin;
import com.xiaomi.infra.galaxy.common.model.DeleteRequest;
import com.xiaomi.infra.galaxy.common.model.DeleteResult;
import com.xiaomi.infra.galaxy.common.model.GetRequest;
import com.xiaomi.infra.galaxy.common.model.Record;
import com.xiaomi.infra.galaxy.common.model.RowKey;
import com.xiaomi.infra.galaxy.common.model.ScanRequest;
import com.xiaomi.infra.galaxy.common.model.ScanResult;
import com.xiaomi.infra.galaxy.common.model.SetRequest;

public class PoetryConfigDao extends XmDao<Config>  implements IDao<Config>{

	private String key = AfMD5.getMD5("");
	private DesUtils des = new DesUtils(key);
	
	public PoetryConfigDao(GalaxyAdmin galaxy) {
		// TODO Auto-generated constructor stub
		super(PoetryConfig.class, galaxy);
	}

	public void add(Config item) throws Exception{
		SetRequest request = getSetRequest();
		set(doBindItemSetRequest(item,request));
	}

	public Config getByName(String name) throws Exception {
		// TODO Auto-generated method stub
		GetRequest get = new GetRequest();
		get.setTableName(mTableName);
//		AttributeValue value = new AttributeValue();
//		value.setValue(name);
//		value.setType("STRING");
		get.addKey("Name", name);// 根据配置的Key确定要进行查询的数据
//		GetResult getRes = gac.get(getReq)
//		GetRequest get = getGetRequest();
//		get.addKey("Name", name);
//		Map<String, AttributeValue> keys = new HashMap<String, AttributeValue>();
//		AttributeValue value = new AttributeValue();
//		value.setValue(name);
//		value.setType("STRING");
//		keys.put("Name", value );
//		get.withKey(keys );
//		//		get.addKey("Name", name);
////		get.addAttribute("Value");
//		List<String> values = new ArrayList<String>();
//		values.add("Value");
//		get.withAttributes(values);
		return doItemFromGetResult(get(get));
	}
	
	public void set(Config item) throws Exception{
		SetRequest request = getSetRequest();
		set(doBindItemSetRequest(item,request));
	}
	
	public void del(Config item) throws Exception{
		DeleteRequest request = getDeleteRequest();
		request.addKey("Name", des.encrypt(item.Name));
		DeleteResult result = delete(request);
		doCheckResult (result);
	}

	protected PoetryConfig doItemFromRecord(Record record) {
		// TODO Auto-generated method stub
		String json = record.getAttributes().get("Value").getValue();
		json = des.decryptNoException(json);
		PoetryConfig item = new Gson().fromJson(json, PoetryConfig.class);
		item.Name = record.getKey().get("Name").getValue();
		item.Name = des.decryptNoException(item.Name);
		return item;
	}

	protected SetRequest doBindItemSetRequest(Config item,
			SetRequest request) throws Exception {
		// TODO Auto-generated method stub
		request.addKey("Name", des.encrypt(item.Name));
		request.addAttributeValue("Value", des.encrypt(new Gson().toJson(item)));
		return request;
	}

	public List<Config> getListByName(String name) throws Exception {
		// TODO Auto-generated method stub
		ScanRequest request = getScanRequest();
		RowKey startKey = new RowKey();
		startKey.addKey("Name", des.encrypt(name));
		RowKey stopKey = new RowKey();
		stopKey.addKey("Name", des.encrypt(name));
		request.withLimit(10).withStartKey(startKey).withStopKey(stopKey);
		ScanResult result = scan(request);
		return doListFormScanResult(result);
	}

	@Override
	public Config newModel() {
		// TODO Auto-generated method stub
		return new PoetryConfig();
	}
}
