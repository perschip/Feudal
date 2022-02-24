package me.invertmc.sql.async;

import java.sql.PreparedStatement;

public class SQLCommand {
	
	private SQLWait id;
	private PreparedStatement ps;
	private String data;
	private boolean next;
	
	public SQLCommand(SQLWait id, PreparedStatement ps){
		this.id = id;
		this.ps = ps;
	}
	
	public SQLCommand(SQLWait id, PreparedStatement ps, String toGet){
		this.id = id;
		this.ps = ps;
		this.data = toGet;
	}
	
	public SQLCommand(SQLWait id, String statement, boolean next){
		this.id = id;
		this.data = statement;
		this.next = next;
	}
	
	public SQLCommand(SQLWait id, String statement){
		this.id = id;
		this.data = statement;
	}

	public SQLWait getId() {
		return id;
	}

	public PreparedStatement getPs() {
		return ps;
	}

	public String getData() {
		return data;
	}

	public boolean isNext() {
		return next;
	}
	
}
