package me.invertmc.sql.async;

import java.sql.ResultSet;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SQLProcessEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	private SQLWait id;
	private Code responseCode;
	private Object get;
	private boolean contains;
	
	public SQLProcessEvent(SQLWait id, Code code) {
		this.id = id;
		this.responseCode = code;
	}
	
	public SQLProcessEvent(SQLWait id, Code code, boolean contains) {
		this.id = id;
		this.responseCode = code;
		this.contains = contains;
	}
	
	public SQLProcessEvent(SQLWait id, Code code, Object object) {
		this.id = id;
		this.responseCode = code;
		this.get = object;
	}

	public static HandlerList getHandlerList(){
		return handlers;
	}
	
	/**
	 * Get the id this command was run with.
	 * Use this to match with what you had before.
	 * @return
	 */
	public SQLWait getId(){
		return id;
	}
	
	/**
	 * Check if this query worked.
	 * @return
	 */
	public Code getResponseCode(){
		return responseCode;
	}
	
	/**
	 * Check if contains is true or false.
	 * DO NOT USE IF YOU DID NOT RUN THE CONTAINS COMMAND DIRECTLY
	 * @return
	 */
	public boolean contains(){
		return contains;
	}
	
	/**
	 * Check if update worked and made changes.
	 * DO NOT USE UNLESS YOU USED EXECUTE_UPDATE DIRECTLY
	 * @return
	 */
	public boolean didUpdate(){
		return contains;
	}
	
	/**
	 * Get the get response. This can be null.
	 * DO NOT USE UNLESS YOU USED THE get command directly.
	 * @return
	 */
	public Object getGet(){
		return get;
	}
	
	/**
	 * Get the result set from EXECUTE_QUERY
	 * DO NOT USE UNLESS YOU RAN EXECUTE_QUERY DIRECTLY.
	 * @return
	 */
	public ResultSet getResultSet(){
		if(get instanceof ResultSet){
			return (ResultSet) get;
		}else{
			return null;
		}
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public void set() {
		this.id.setCompleteCode(responseCode);
		this.id.setProcessed(true);
	}

}
