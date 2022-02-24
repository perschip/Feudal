package me.invertmc.sql.async;


public class SQLWait {

	private static volatile long lastId = 0;
	
	private long id;
	private long createTime;
	private SQLFunction function;
	private boolean processed = false;
	private Code completeCode = Code.WORKING;
	
	public SQLWait(SQLFunction function){
		id = getNextId();
		
		createTime = System.currentTimeMillis();
		
		this.function = function;
	}
	
	public static synchronized long getNextId(){
		lastId++;
		return lastId;
	}
	
	public long getId(){
		return id;
	}
	
	public Code getCompleteCode(){
		return completeCode;
	}
	
	protected void setCompleteCode(Code code){
		this.completeCode = code;
	}
	
	public boolean isProcessed(){
		return processed;
	}
	
	protected void setProcessed(boolean processed){
		this.processed = processed;
	}
	
	public long getCreateTime(){
		return createTime;
	}
	
	public SQLFunction getFunction(){
		return function;
	}
	
	@Override
	public boolean equals(Object object){
		if(object instanceof SQLWait){
			SQLWait response = (SQLWait) object;
			if(response.id == this.id && response.function.equals(this.function)){
				return true;
			}
		}
		return false;
	}
	
	public enum SQLFunction{
		SET, GET, CONTAINS, UPDATE, REMOVE, EXECUTE_QUERY, EXECUTE_UPDATE;
	}
	
}
