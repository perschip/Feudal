package me.invertmc.sql.async;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;

import me.invertmc.Feudal;
import me.invertmc.utils.StackUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;



public class AsyncSQLThread extends Thread {

	private static volatile CopyOnWriteArrayList<SQLCommand> todo = new CopyOnWriteArrayList<SQLCommand>();
	
	static{
		new AsyncSQLThread().start();
	}
	
	public void run(){
		try{
		while(true){
			if(todo.size() > 0){
				SQLCommand command = todo.remove(0);
				execute(command);
			}else{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void execute(SQLCommand command) {
		if(command.getId().getFunction().equals(SQLWait.SQLFunction.SET)){
			try {
				command.getPs().executeUpdate();
				executeEvent(new SQLProcessEvent(command.getId(), Code.SUCCESS));
			} catch (SQLException e) {
				if(!e.getMessage().contains("No operations allowed after statement closed.")) {
					e.printStackTrace();
					StackUtil.dumpStack(e);
					executeEvent(new SQLProcessEvent(command.getId(), Code.FAIL));
				}
			}
		}else if(command.getId().getFunction().equals(SQLWait.SQLFunction.UPDATE)){
			try{
				command.getPs().executeUpdate();
				executeEvent(new SQLProcessEvent(command.getId(), Code.SUCCESS));
			}catch(SQLException e){
				e.printStackTrace();
				StackUtil.dumpStack(e);
				executeEvent(new SQLProcessEvent(command.getId(), Code.FAIL));
			}
		}else if(command.getId().getFunction().equals(SQLWait.SQLFunction.GET)){
			try {
				ResultSet result = command.getPs().executeQuery();
				Object object = null;
				if (result.next()) {
					object = result.getObject(command.getData());
				}
				executeEvent(new SQLProcessEvent(command.getId(), Code.SUCCESS, object));
			} catch (SQLException e) {
				StackUtil.dumpStack(e);
				executeEvent(new SQLProcessEvent(command.getId(), Code.FAIL));
				e.printStackTrace();
			}
		}else if(command.getId().getFunction().equals(SQLWait.SQLFunction.CONTAINS)){
			try {
				ResultSet result = command.getPs().executeQuery();
				executeEvent(new SQLProcessEvent(command.getId(), Code.SUCCESS, result.next()));
			} catch (SQLException e) {
				StackUtil.dumpStack(e);
				executeEvent(new SQLProcessEvent(command.getId(), Code.FAIL));
				e.printStackTrace();
			}
		}else if(command.getId().getFunction().equals(SQLWait.SQLFunction.EXECUTE_QUERY)){
			try {
				ResultSet set = Feudal.getPlugin(Feudal.class).getSql().getSqlControler().executeQuery(command.getData(), command.isNext());
				executeEvent(new SQLProcessEvent(command.getId(), Code.SUCCESS, set));
			} catch (Exception e) {
				e.printStackTrace();
				StackUtil.dumpStack(e);
				executeEvent(new SQLProcessEvent(command.getId(), Code.FAIL));
			}
		}else if(command.getId().getFunction().equals(SQLWait.SQLFunction.EXECUTE_UPDATE)){
			try {
				boolean didUpdate = Feudal.getPlugin(Feudal.class).getSql().getSqlControler().executeUpdate(command.getData());
				executeEvent(new SQLProcessEvent(command.getId(), Code.SUCCESS, didUpdate));
			} catch (Exception e) {
				StackUtil.dumpStack(e);
				executeEvent(new SQLProcessEvent(command.getId(), Code.FAIL));
				e.printStackTrace();
			}
		}
	}

	/**
	 * Used to resync the event because this is running on a async thread.
	 * @param event
	 */
	private void executeEvent(final SQLProcessEvent event) {
		try{
			new BukkitRunnable() {
				@Override
				public void run() {
					try{
						event.set();
						Bukkit.getServer().getPluginManager().callEvent(event);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}.runTask(Feudal.getPlugin(Feudal.class));
		}catch(Exception e){
			if(!e.getLocalizedMessage().equalsIgnoreCase("Plugin attempted to register task while disabled")){
				e.printStackTrace();
			}
		}
	}

	public static synchronized void set(SQLWait id, PreparedStatement ps) {
		todo.add(new SQLCommand(id, ps));
	}

	public static synchronized void get(SQLWait id, PreparedStatement ps, String toGet) {
		todo.add(new SQLCommand(id, ps, toGet));
	}

	public static synchronized void contains(SQLWait id, PreparedStatement ps) {
		todo.add(new SQLCommand(id, ps));
	}

	public static synchronized void executeQuery(SQLWait id, String statement, boolean next) {
		todo.add(new SQLCommand(id, statement, next));
	}

	public static synchronized void executeUpdate(SQLWait id, String statement) {
		todo.add(new SQLCommand(id, statement));
	}

}
