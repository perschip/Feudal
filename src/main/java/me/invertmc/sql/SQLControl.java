package me.invertmc.sql;


import me.invertmc.Feudal;

public class SQLControl {
	
	private SQLManager sqlConnector;
	private DataManager kingdomsSQL;
	private DataManager membersSQL;
	private DataManager bansSQL;//store kingdom bans
	private DataManager landSQL;
	private DataManager relationsSQL;
	private DataManager usersSQL;
	private DataManager offlineMessagesSQL;
	private DataManager kingdomLogSQL;
	private boolean use = false;
	
	private SQLThread sql_thread;
	
	public SQLControl(){
		try{
			this.sqlConnector = createSqlControler("data");

			if(use) {
				setupKingdomsTable();
				setupMembersTable();//used to store kingdom members (fighter or not fighter)
				setupRelationsTable();//used to store kingdom members
				setupBansTable();
				setupLandTable();
				setupUsersTable();
				setupOfflineMessagesTable();
				setupKingdomLogTable();
	            
				this.sql_thread = new SQLThread(this.sqlConnector, 300L);
				this.sql_thread.start();
			}
		}catch(Exception e){
			e.printStackTrace();
			//Feudal.getPlugin().error("SQL CONTROL ISSUES ON STARTUP", false);
		}
	}
	
	/*
	 * NOTE: When creating new setupTable methods make sure not to include spaces after commas 
	 * for columns.
	 */
	
	private void setupMembersTable() throws Exception {
		Table membersTable = new Table("kingdom_members", 
				"kingdom_uuid VARCHAR(45),player VARCHAR(256),fighter INT"
				);
		this.membersSQL = new SQLTable(sqlConnector);
		this.membersSQL.setTable(membersTable);
	}
	
	private void setupRelationsTable() throws Exception {
		Table relationsTable = new Table("kingdom_relations", 
				"kingdom_uuid VARCHAR(45),other_uuid VARCHAR(45),relation INT"
				);
		this.relationsSQL = new SQLTable(sqlConnector);
		this.relationsSQL.setTable(relationsTable);
	}
	
	private void setupLandTable() throws Exception {
		Table table = new Table("kingdom_land", 
				"kingdom_uuid VARCHAR(45),land VARCHAR(500)"
				);
		this.landSQL = new SQLTable(sqlConnector);
		this.landSQL.setTable(table);
	}
	
	private void setupBansTable() throws Exception {
		Table table = new Table("kingdom_bans", 
				"kingdom_uuid VARCHAR(45),ban VARCHAR(300)"
				);
		this.bansSQL = new SQLTable(sqlConnector);
		this.bansSQL.setTable(table);
	}
	
	private void setupKingdomsTable() throws Exception {
		Table kingdomsTable = new Table("kingdoms", 
				"uuid VARCHAR(45) NOT NULL UNIQUE,name VARCHAR(255)," +
				"treasury DOUBLE,createData BIGINT,lastOnline BIGINT," +
				"maxLand INT,taxRate DOUBLE,description VARCHAR(500)," + 
				"open INT,challengesWon INT,challengesLost INT,firstClaim BIGINT," +
				"cooldown BIGINT,first INT,saftyEnd BIGINT,shield VARCHAR(128)," +
				"incomeTax DOUBLE,landTaxPercent DOUBLE,homeActive INT," +
				"homeWorld VARCHAR(128),homeX DOUBLE,homeY DOUBLE,homeZ DOUBLE," +
				"homeYaw DOUBLE,homePitch DOUBLE,bannerBackground VARCHAR(64),banner VARCHAR(1000)"
				);
		this.kingdomsSQL = new SQLTable(sqlConnector);
		this.kingdomsSQL.setTable(kingdomsTable);
	}
	
	private void setupUsersTable() throws Exception {
		Table table = new Table("users", 
				"uuid VARCHAR(45) NOT NULL UNIQUE,name VARCHAR(32)," +
				"reputation INT,firstJoin BIGINT,lastJoin BIGINT,kingdomUUID VARCHAR(45)," +
				"locationWorld VARCHAR(128),locationX DOUBLE,locationY DOUBLE,locationZ DOUBLE," + 
				"locationYaw DOUBLE,locationPitch DOUBLE,cooldownShield BIGINT,currentProfession VARCHAR(30)," + 
				"pastProfessions VARCHAR(600),balance DOUBLE,strength_xp INT,strength_level INT," +
				"toughness_xp INT,toughness_level INT," +
				"speed_xp INT,speed_level INT," +
				"stamina_xp INT,stamina_level INT," +
				"luck_xp INT,luck_level INT," +
				"online VARCHAR(50)"
				);
		this.usersSQL = new SQLTable(sqlConnector);
		this.usersSQL.setTable(table);
	}
	
	private void setupOfflineMessagesTable() throws Exception {
		Table table = new Table("user_offlineMessages", 
				"uuid VARCHAR(45),message VARCHAR(500)"
				);
		this.offlineMessagesSQL = new SQLTable(sqlConnector);
		this.offlineMessagesSQL.setTable(table);
	}
	
	private void setupKingdomLogTable() throws Exception {
		Table table = new Table("user_kingdomlog", 
				"uuid VARCHAR(45),log VARCHAR(100)"
				);
		this.kingdomLogSQL = new SQLTable(sqlConnector);
		this.kingdomLogSQL.setTable(table);
	}
	
	private SQLManager createSqlControler(String fileName) {
		if(Feudal.getConfiguration().getConfig().getBoolean("sql.enable", false)) {
			use = true;
			if (!Feudal.getConfiguration().getConfig().getBoolean("sql.sqlLite", false)) {
				return new MySQL();
			}
			return new SQLite(Feudal.getPlugin(Feudal.class).getDataFolder().getPath(), fileName + ".db");
		}else {
			return null;
		}
	}

	public Thread getSqlThread() {
		return sql_thread;
	}

	public SQLManager getSqlControler() {
		return this.sqlConnector;
	}
	
	public DataManager getMembersSQL(){
		return membersSQL;
	}
	
	public DataManager getBansSQL(){
		return bansSQL;
	}
	
	public DataManager getLandSQL(){
		return landSQL;
	}
	
	public DataManager getRelationsSQL(){
		return relationsSQL;
	}
	
	public DataManager getOfflineMessagesSQL(){
		return offlineMessagesSQL;
	}
	
	public DataManager getKingdomLogSQL(){
		return kingdomLogSQL;
	}
	
	public DataManager getKingdomsSQL(){
		return kingdomsSQL;
	}

	public DataManager getUsersSQL() {
		return usersSQL;
	}
	
	public boolean doesUse() {
		return use;
	}
	
}