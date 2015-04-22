package com.poetry.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.poetry.db.annotation.Table;
import com.xiaomi.infra.galaxy.client.GalaxyAdmin;
import com.xiaomi.infra.galaxy.common.model.AttributeValue;
import com.xiaomi.infra.galaxy.common.model.DeleteRequest;
import com.xiaomi.infra.galaxy.common.model.DeleteResult;
import com.xiaomi.infra.galaxy.common.model.GetRequest;
import com.xiaomi.infra.galaxy.common.model.GetResult;
import com.xiaomi.infra.galaxy.common.model.JsonMarshaller;
import com.xiaomi.infra.galaxy.common.model.Record;
import com.xiaomi.infra.galaxy.common.model.Result;
import com.xiaomi.infra.galaxy.common.model.ScanRequest;
import com.xiaomi.infra.galaxy.common.model.ScanResult;
import com.xiaomi.infra.galaxy.common.model.SetRequest;
import com.xiaomi.infra.galaxy.common.model.SetResult;

public abstract class XmDao<T> {

	protected Class<? extends T> mClazz;
	protected String mTableName;
	protected String mTablePrefix = "";
	protected GalaxyAdmin mGalaxy = null;

	protected abstract T doItemFromRecord(Record record) ;
	protected abstract SetRequest doBindItemSetRequest(T item,SetRequest request) throws Exception;

	public XmDao(Class<? extends T> clazz,GalaxyAdmin galaxy,String prefix) {
		mTablePrefix = prefix;
		initize(clazz,galaxy);
	}
	
	public XmDao(Class<? extends T> clazz,GalaxyAdmin galaxy) {
		// TODO Auto-generated constructor stub
		initize(clazz,galaxy);
	}
	
	private void initize(Class<? extends T> clazz,GalaxyAdmin galaxy){
		mClazz = clazz;
		mGalaxy = galaxy;
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = clazz.getAnnotation(Table.class);
			mTableName = table.name();
		}
		if (mTableName == null || mTableName.length() == 0) {
			mTableName = mTablePrefix+mClazz.getSimpleName();
		}
	}

	public List<T> getAll() throws Exception {
		ScanRequest request = getScanRequest();
		ScanResult result = scan(request);
		return doListFormScanResult(result);
	}

	public void delAll() throws Exception {
		DeleteRequest request = getDeleteRequest();
		DeleteResult result = delete(request);
		result.toString();
	}
	
	protected ScanRequest getScanRequest(){
		return new ScanRequest().withTableName(mTableName);
	}

	protected DeleteRequest getDeleteRequest() {
		// TODO Auto-generated method stub
		return new DeleteRequest().withTableName(mTableName);
	}

	protected SetRequest getSetRequest() {
		// TODO Auto-generated method stub
		return new SetRequest().withTableName(mTableName);
	}

	protected GetRequest getGetRequest() {
		// TODO Auto-generated method stub
		return new GetRequest().withTableName(mTableName);
	}
	
	protected  ScanResult scan(ScanRequest request) throws Exception{
		ScanResult result = mGalaxy.scan(request);
		if (result.getCode() != ScanResult.SUCCESS) {
			throw new Exception(result.getCode()+":"+result.getMessage());
		}
		return result;
	}

	protected DeleteResult delete(DeleteRequest request) throws Exception {
		// TODO Auto-generated method stub
		DeleteResult result = mGalaxy.delete(request);
		if (result.getCode() != ScanResult.SUCCESS) {
			throw new Exception(result.getCode() + ":" + result.getMessage());
		}
		return result;
	}

	protected SetResult set(SetRequest request) throws Exception {
		// TODO Auto-generated method stub
		SetResult result = mGalaxy.set(request);
		if (result.getCode() != ScanResult.SUCCESS) {
			throw new Exception(result.getCode() + ":" + result.getMessage());
		}
		return result;
	}
	
	protected GetResult get(GetRequest request) throws Exception {
		// TODO Auto-generated method stub
		GetResult result = mGalaxy.get(request);
		if (result.getCode() != ScanResult.SUCCESS) {
			throw new Exception(result.getCode() + ":" + result.getMessage());
		}
		return result;
	}

	/**
	 * 从 GetResult 获取 Record
	 * @param result
	 * @return 如果不存在数据返回 null
	 */
	protected Record doRecordFromGetResult(GetResult result){
		Map<String, AttributeValue> attrib = result.getAttributes();
		if (attrib != null && !attrib.isEmpty()) {
			Record record = new Record();
			record.setAttributes(attrib);
			record.setKey(new HashMap<String, AttributeValue>());
			return record;
		}
		return null;
	}
	
	/**
	 * 从 GetResult 获取 Item
	 * @param result
	 * @return 如果不存在数据返回 null
	 */
	protected T doItemFromGetResult(GetResult result){
		Map<String, AttributeValue> attrib = result.getAttributes();
		if (attrib != null && !attrib.isEmpty()) {
			Record record = doRecordFromGetResult(result);
			return doItemFromRecord(record);
		}
		return null;
	}

	protected List<T> doListFormScanResult(ScanResult result) {
		// TODO Auto-generated method stub
		List<T> list = new ArrayList<T>();
		List<Record> item = result.getItems();
		for (Record record : item) {
			list.add(doItemFromRecord(record));
		}
		return list;
	}


	protected void doCheckResult(Result result) throws Exception {
		// TODO Auto-generated method stub
		if (result.getCode() != Result.SUCCESS) {
			throw new Exception(result.getMessage()+":"+
					JsonMarshaller.getInstance().marshall(result));
		}
	}

}
