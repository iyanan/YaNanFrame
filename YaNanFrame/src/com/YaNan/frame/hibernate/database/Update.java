package com.YaNan.frame.hibernate.database;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.YaNan.frame.hibernate.database.DBInterface.OperateImplement;
import com.YaNan.frame.logging.Log;
import com.YaNan.frame.plugin.PlugsFactory;

/**
 * 该类用于提供给DATab的query一个查询的SQL语句的生成方法 提过一个构造器，传入一个DBTab型的表对象，应为他需要使用DBTab context
 * 
 * @author Administrator
 *
 */
public class Update extends OperateImplement{
	private Map<String, String> map = new HashMap<String, String>();
	private List<String> condition = new ArrayList<String>();
	private Map<String,Object> updateList = new LinkedHashMap<String,Object>();
	private final Log log = PlugsFactory.getPlugsInstance(Log.class,Query.class);
	public Update(DBTab dbTab, Object object) {
		this.setDbTab(dbTab);
	}
	/**
	 * 更新所有域(不包括null字段)
	 * @param object
	 */
	public Update(Object object) {
		this.setDbTab(new DBTab(object));
		Iterator<Field> fI = this.getDbTab().getFieldMap().keySet().iterator();
		while(fI.hasNext()){
			Field field = fI.next();
			try {
				if(this.dbTab.getLoader().get(field.getName())!=null)
					this.updateList.put(this.getDbTab().getDBColumn(field).getName(),this.dbTab.getLoader().get(field.getName()).toString());
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				log.error(e);
			}
		}
	}
	/**
	 * 
	 * @param object
	 */
	public Update(Object object,boolean updateNull) {
		this.setDbTab(new DBTab(object));
		Iterator<Field> fI = this.getDbTab().getFieldMap().keySet().iterator();
		while(fI.hasNext()){
			Field field = fI.next();
			try {
				if(this.dbTab.getLoader().get(field.getName())!=null)
					this.updateList.put(this.getDbTab().getDBColumn(field).getName(),this.dbTab.getLoader().get(field.getName()).toString());
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				log.error(e);
			}
		}
	}	/**
	 * 更新数据库，后面接要更新的域
	 * @param object
	 * @param fields
	 */
	public Update(Object object,String...fields){
		try{
			this.setDbTab(new DBTab(object));
			for(String strField : fields){
				Field field =this.getDbTab().getCls().getDeclaredField(strField);
				if(this.dbTab.getLoader().get(field.getName())!=null)
					this.updateList.put(this.getDbTab().getDBColumn(field).getName(),this.dbTab.getLoader().get(field.getName()).toString());
			}
		}catch(Exception e){
			log.error(e);
		}
	}
	/**
	 * 更新数据库,此类必须使用addField方法，对更新的数据设置；
	 * @param cls
	 */
	public Update(Class<?> cls) {
		this.setDbTab(new DBTab(cls));
	}
	public Update(Class<?> cls,String...fields){
		try{
			this.setDbTab(new DBTab(cls));
			for(String strField : fields){
				Field field =this.getDbTab().getCls().getDeclaredField(strField);
				if(this.dbTab.getLoader().get(field.getName())!=null)
					this.updateList.put(this.getDbTab().getDBColumn(field).getName(),"'"+this.dbTab.getLoader().get(field.getName()).toString().replace("'", "\\'")+"'");
			}
		}catch(Exception e){
			log.error(e);
		}
	}
	/**
	 * 设置要更新的数据库字段
	 * @param updateList
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public void setFields(Map<String,Object> updateList) throws NoSuchFieldException, SecurityException {
		if(!this.updateList.isEmpty())this.updateList.clear();
		Iterator<String> iterator= updateList.keySet().iterator();
		while(iterator.hasNext()){
			String field = iterator.next();
			String column = this.getDbTab().getDBColumn(field).getName();
			this.updateList.put(column,updateList.get(field));
			this.updateList = updateList;
		}
	}
	/**
	 * 要更新的数据库的字段
	 * @param field
	 * @param value
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public void setField(String field,Object value) throws NoSuchFieldException, SecurityException{
		String column = this.getDbTab().getDBColumn(field).getName();
		this.updateList.put(column,value.toString());
	}
	/**
	 * 添加要更新的域
	 * @param field
	 * @param value
	 */
	public void setColumn(String field,Object value){
		this.updateList.put(field,value.toString());
	}
	/**
	 * 设置要更新的域
	 * @param field
	 * @param condition
	 */
	public void setColumns(Map<String,Object> updateList) throws NoSuchFieldException, SecurityException {
		this.updateList = updateList;
	}
	public void addCondition(Field field, String condition) {
		this.map.put(getDbTab().getDBColumn(field).getName(), condition);
	}

	public void addCondition(Field field, Object condition) {
		this.map.put(getDbTab().getDBColumn(field).getName(), condition.toString());
	}

	public void addColumnCondition(String column, Object condition) {
		this.map.put(column, condition.toString());
	}
	public void addCondition(String field,Object condition) {
		try {
			this.map.put(getDbTab().getDBColumn(field).getName(), condition.toString());
		} catch (NoSuchFieldException | SecurityException e) {
			log.error(e);
		}
	}

	public void addConditionCommand(String condition) {
		this.condition.add(condition);
	}

	public String create() {
		StringBuilder sb = new StringBuilder("UPDATE ").append(this.getDbTab().getName()).append(" SET ");
		Iterator<String> iterator = this.updateList.keySet().iterator();
		while (iterator.hasNext()) {
			String column = iterator.next();
				sb.append(column).append( "='").append(updateList.get(column)).append(iterator.hasNext() ? "'," : "'");
		}
		if (this.map.size() != 0) {
			sb.append(" WHERE ");
			Iterator<String> i = this.map.keySet().iterator();
			while (i.hasNext()) {
				String s = i.next();
				sb.append(s).append("='").append(this.map.get(s)).append(i.hasNext() ? "' AND " : "'");
			}
		} 
		if (this.condition.size() != 0) {
			sb.append(this.map.size()==0?" WHERE ":" AND ");
			Iterator<String> i = this.condition.iterator();
			while (i.hasNext()) 
				sb.append(i.next()).append(i.hasNext() ? " AND " : "");
		}
		return sb.toString();
	}

	public int update() {
		return this.getDbTab().update(this);
	}
	/**
	 * 移除要更新的域
	 * @param field
	 */
	public void removeFields(String... fields) {
		try {
			for(String field :fields){
				String column = this.getDbTab().getDBColumn(field).getName();
				this.updateList.remove(column);
				}
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 移除要更新的域
	 * @param field
	 */
	public void removeField(String field){
		try{
			String column = this.getDbTab().getDBColumn(field).getName();
			this.updateList.remove(column);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 移除要更新的域
	 * @param field
	 */
	public void removeColumns(String... columns){
		for(String column :columns){
			this.updateList.remove(column);
			}
	}
	/**
	 * 移除要更新的列
	 * @param field
	 */
	public void removeColumn(String column){
		this.updateList.remove(column);
	}
	/**
	 * 设置域的表达式
	 * @param field
	 * @param value
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public void setFieldExpression(String field,Object value){
		try {
			String column = this.getDbTab().getDBColumn(field).getName();
			this.updateList.put(column, value);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 设置列的表达式
	 * @param field
	 * @param value
	 */
	public void setColumnExpression(String field, Object value) {
		this.updateList.put(field,value);
	}
	/**
	 * 设置列的Case表达式
	 * @param field
	 * @param value
	 */
	public void setColumnExpression(Case cases) {
		this.updateList.put(cases.getColumn(),cases.create());
	}
}
