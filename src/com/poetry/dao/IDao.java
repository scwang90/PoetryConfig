package com.poetry.dao;

import java.util.List;

import com.poetry.model.Config;

public interface IDao<T extends Config> {
	
	public T newModel();

	public void add(Config item) throws Exception;

	public T getByName(String name) throws Exception;
	
	public void set(Config item) throws Exception;
	
	public void del(Config item) throws Exception;

	public List<T> getListByName(String name) throws Exception ;

	public List<T> getAll() throws Exception ;
}
