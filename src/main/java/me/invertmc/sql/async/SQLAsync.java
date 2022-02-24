package me.invertmc.sql.async;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class SQLAsync {
	
	private static Set<SQLAsync> handlers = new HashSet<SQLAsync>();

	public interface SQLEventHandler {
		public void onProcess(SQLProcessEvent event);
	}
	
	private boolean destroy = true;
	private SQLWait wait;
	private SQLEventHandler handler;
	
	public SQLAsync(SQLWait wait, boolean destroy, SQLEventHandler handler){
		if(wait != null){
			this.destroy = destroy;
			this.wait = wait;
			handlers.add(this);
			this.handler = handler;
		}
	}
	
	public static void destroy(SQLWait wait){
		SQLAsync send = null;
		for(SQLAsync sender : handlers){
			if(sender.wait.equals(wait)){
				send = sender;
				break;
			}
		}
		handlers.remove(send);
	}
	
	public static void destroy(SQLAsync sender){
		handlers.remove(sender);
	}
	
	public static class SQLProcessListener implements Listener {
		
		@EventHandler
		public void onProcess(SQLProcessEvent event){
			try{
				//Debug.send("Process sql");
				SQLAsync destroy = null;
				for(SQLAsync async : new HashSet<SQLAsync>(handlers)){
					if(async.wait.equals(event.getId())){
						//Debug.send("SAME");
						async.handler.onProcess(event);
						if(async.destroy){
							destroy = async;
						}
						break;
					}
				}
				if(destroy != null){
					handlers.remove(destroy);
				}
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}
		
	}
	
}
