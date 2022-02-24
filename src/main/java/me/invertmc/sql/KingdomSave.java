package me.invertmc.sql;

import me.invertmc.Feudal;
import me.invertmc.kingdoms.Kingdom;
import me.invertmc.kingdoms.Rank;
import me.invertmc.utils.ErrorManager;

import java.sql.ResultSet;


public class KingdomSave {

	public static void loadKingdoms() {
		//load kingdoms like in on enable except from sql
		SQLControl sql = Feudal.getPlugin(Feudal.class).getSql();

		try {
			ResultSet set = sql.getKingdomsSQL().executeQuery("SELECT * FROM " + sql.getKingdomsSQL().getTable().getName(), false);
			while(set!= null && set.next()) {
				Kingdom king = null;
				try {
					king = new Kingdom(set);
				} catch (Exception e) {
					ErrorManager.error(7, e);
					try {
						System.out.println("[Feudal] Failed to load kingdom from config: "
										+ set.getString("name"));
					}catch(Exception ee) {}
					continue;
				}
	
				// Does not load kingdom if the leader is missing. Sorry..
				boolean leader = false;
				for (String s : king.getMembers().keySet()) {
					if (king.getMembers().get(s).equals(Rank.LEADER)) {
						leader = true;
					}
				}
				if (leader) {
					Feudal.getKingdoms().add(king);
					king.updateLand();
				}
				//
			}
		}catch(Exception e) {
			Feudal.error("Failed to load kingdoms from SQL!");
			e.printStackTrace();
		}
	}

	public static void save(final Kingdom kingdom, boolean async) {	

		final SQLControl sql = Feudal.getPlugin(Feudal.class).getSql();
		
		final String sqlString = "UPDATE " + sql.getKingdomsSQL().getTable().getName() + " SET" + 
				" name='" + SQLUtil.filterBasic(kingdom.getName(), 255) + "'," +
				" treasury='" + kingdom.getTreasury() + "'," +
				" createData='" + kingdom.getCreateData() + "'," +
				" lastOnline='" + kingdom.getLastOnline() + "'," +
				" maxLand='" + kingdom.getMaxLand() + "'," +
				" taxRate='" + kingdom.getTaxRate() + "'," +
				" description='" + SQLUtil.filterBasic(kingdom.getDescription(), 500) + "'," +
				" open='" + (kingdom.isOpen() ? 1 : 0) + "'," +
				" challengesWon='" + kingdom.getChallengesWon() + "'," +
				" challengesLost='" + kingdom.getChallengesLost() + "'," +
				" firstClaim='" + kingdom.getFirstClaim() + "'," +
				" cooldown='" + kingdom.getCooldownEnd() + "'," +
				" first='" + (kingdom.isFirst() ? 1 : 0) + "'," +
				" saftyEnd='" + kingdom.getSaftyEnd() + "'," +
				" shield='" + (kingdom.getShield() != null ? kingdom.getShield().toString() : "") + "'," +
				" incomeTax='" + kingdom.getCurrentIncomeTax() + "'," +
				" landTaxPercent='" + kingdom.getLandTaxPercent() + "'," +
				" homeActive='" + (kingdom.getHome()!=null ? 1 : 0) + "'," +
				" homeWorld='" + (kingdom.getHome() != null ? SQLUtil.filterBasic(kingdom.getHome().getWorld().getName(), 128) : "world") + "'," +
				" homeX='" + (kingdom.getHome() != null ? kingdom.getHome().getX() : 0) + "'," +
				" homeY='" + (kingdom.getHome() != null ? kingdom.getHome().getY() : 0) + "'," +
				" homeZ='" + (kingdom.getHome() != null ? kingdom.getHome().getZ() : 0) + "'," +
				" homeYaw='" + (kingdom.getHome() != null ? kingdom.getHome().getYaw() : 0) + "'," +
				" homePitch='" + (kingdom.getHome() != null ? kingdom.getHome().getPitch() : 0) + "'," +
				" WHERE uuid='" + kingdom.getUUID() + "'";
		
		final int members = kingdom.getMembers().size();
		String memberValues = "";
		for(String member : kingdom.getMembers().keySet()) {
			member = member + "/" + kingdom.getMembers().get(member).toString();
			if(memberValues.isEmpty()) {
				memberValues += "('"+kingdom.getUUID()+"','"+member+"',"+(kingdom.getFighters().contains(member)?1:0)+")";
			}else {
				memberValues += ",('"+kingdom.getUUID()+"','"+member+"',"+(kingdom.getFighters().contains(member)?1:0)+")";
			}
		}
		final String sqlMembersSet = "INSERT INTO `"+sql.getMembersSQL().getTable().getName()+"` VALUES " + memberValues + ";";
		
		final int relations = kingdom.getAlliesList().size() + kingdom.getEnemyList().size();
		String relationValues = "";
		for(String ally : kingdom.getAlliesList()) {
			if(relationValues.isEmpty()) {
				relationValues += "('"+kingdom.getUUID()+"','"+ally+"',1)";
			}else {
				relationValues += ",('"+kingdom.getUUID()+"','"+ally+"',1)";
			}
		}
		for(String enemy : kingdom.getEnemyList()) {
			if(relationValues.isEmpty()) {
				relationValues += "('"+kingdom.getUUID()+"','"+enemy+"',2)";
			}else {
				relationValues += ",('"+kingdom.getUUID()+"','"+enemy+"',2)";
			}
		}
		final String sqlRelationSet = "INSERT INTO `"+sql.getRelationsSQL().getTable().getName()+"` VALUES " + relationValues + ";";
		
		final int landAmt = kingdom.getLand().size();
		String landValues = "";
		for(Land land : kingdom.getLand()) {
			String l = land.getX() + "~" + land.getZ() + "~" + land.getWorldName().replace("%", "%0").replace("~", "%1").replace("/", "%2");
			l = SQLUtil.filterBasic(l, 500);
			if(landValues.isEmpty()) {
				landValues += "('"+kingdom.getUUID()+"','"+l+"')";
			}else {
				landValues += ",('"+kingdom.getUUID()+"','"+l+"')";
			}
		}
		final String sqlLandSet = "INSERT INTO `"+sql.getLandSQL().getTable().getName()+"` VALUES " + landValues + ";";
		
		final int bans = kingdom.getBans().size();
		String banValues = "";
		for(String ban : kingdom.getBans().keySet()) {
			ban = ban + "/" + kingdom.getBans().get(ban);
			ban = SQLUtil.filterBasic(ban, 300);
			if(banValues.isEmpty()) {
				banValues += "('"+kingdom.getUUID()+"','"+ban+"')";
			}else {
				banValues += ",('"+kingdom.getUUID()+"','"+ban+"')";
			}
		}
		final String sqlBansSet = "INSERT INTO `"+sql.getBansSQL().getTable().getName()+"` VALUES " + banValues + ";";
		
		if(kingdom.isChange() || kingdom.isChangeBans() || kingdom.isChangeLand() || kingdom.isChangeMembers() || kingdom.isChangeRelations()) {
			Runnable runnable = new Runnable() {
				public void run() {
					try {
												
						if(kingdom.isChange()) {
							kingdom.setChange(false);
							sql.getKingdomsSQL().executeUpdate(sqlString);
						}
						
						if(kingdom.isChangeMembers()) {
							kingdom.setChangeMembers(false);
							sql.getMembersSQL().remove("kingdom_uuid", kingdom.getUUID());
							if(members > 0) {
								sql.getMembersSQL().executeUpdate(sqlMembersSet);
							}
						}
						
						if(kingdom.isChangeBans()) {
							kingdom.setChangeBans(false);
							sql.getBansSQL().remove("kingdom_uuid", kingdom.getUUID());
							if(bans > 0) {
								sql.getBansSQL().executeUpdate(sqlBansSet);
							}
						}
						
						if(kingdom.isChangeLand()) {
							kingdom.setChangeLand(false);
							sql.getLandSQL().remove("kingdom_uuid", kingdom.getUUID());
							if(landAmt > 0) {
								sql.getLandSQL().executeUpdate(sqlLandSet);
							}
						}
						
						if(kingdom.isChangeRelations()) {
							kingdom.setChangeRelations(false);
							sql.getRelationsSQL().remove("kingdom_uuid", kingdom.getUUID());
							if(relations > 0) {
								sql.getRelationsSQL().executeUpdate(sqlRelationSet);
							}
						}
						
						/*sql.getMembersSQL().remove("kingdom_uuid", kingdom.getUUID());
						for(String member : kingdom.getMembers().keySet()) {
							String mem = member + "/" + kingdom.getMembers().get(member).toString();
							sql.getMembersSQL().set(kingdom.getUUID(), mem, Integer.valueOf((kingdom.getFighters().contains(member)?1:0)));
						}
						
						sql.getLandSQL().remove("kingdom_uuid", kingdom.getUUID());
						for(Land land : kingdom.getLand()) {
							if(land != null) {
								String str = land.getX() + "~" + land.getZ() + "~" + land.getWorldName().replace("%", "%0").replace("~", "%1").replace("/", "%2");
										sql.getLandSQL().set(kingdom.getUUID(),
												str);
							}
						}
						
						sql.getBansSQL().remove("kingdom_uuid", kingdom.getUUID());
						for(String str : kingdom.getBans().keySet()) {
							sql.getBansSQL().set(kingdom.getUUID(),
									(str + "/" + kingdom.getBans().get(str)));
						}
						
						sql.getRelationsSQL().remove("kingdom_uuid", kingdom.getUUID());
						for(String str : kingdom.getAlliesList()) {
							sql.getRelationsSQL().set(kingdom.getUUID(), str, new Integer(1));
						}
						for(String str : kingdom.getEnemyList()) {
							sql.getRelationsSQL().set(kingdom.getUUID(), str, new Integer(2));
						}*/
					}catch(Exception e) {
						ErrorManager.error(1105322218, e);
					}
				}
			};
			
			if(async) {
				new Thread(runnable).start();
			}else {
				runnable.run();
			}
		}
	}
	
	public static void saveDefault(Kingdom kingdom) {
		new Thread(new Runnable() {
			public void run() {
				
				SQLControl sql = Feudal.getPlugin(Feudal.class).getSql();
				
				// set rather than update
				sql.getKingdomsSQL().set(
						kingdom.getUUID(),
						SQLUtil.filterBasic(kingdom.getName(), 255),
						Double.valueOf(kingdom.getTreasury()),
						Long.valueOf(kingdom.getCreateData()),
						Long.valueOf(kingdom.getLastOnline()),
						Integer.valueOf(kingdom.getMaxLand()),
						Double.valueOf(kingdom.getTaxRate()),
						SQLUtil.filterBasic(kingdom.getDescription(), 500),
						(kingdom.isOpen() ? Integer.valueOf(1) : Integer.valueOf(0)),
						Integer.valueOf(kingdom.getChallengesWon()),
						Integer.valueOf(kingdom.getChallengesLost()),
						Long.valueOf(kingdom.getFirstClaim()),
						Long.valueOf(kingdom.getCooldownEnd()),
						(kingdom.isFirst() ? Integer.valueOf(1) : Integer.valueOf(0)),
						Long.valueOf(kingdom.getSaftyEnd()),
						(kingdom.getShield() != null ? kingdom.getShield().toString() : ""),
						Double.valueOf(kingdom.getCurrentIncomeTax()),
						Double.valueOf(kingdom.getLandTaxPercent()),
						Integer.valueOf((kingdom.getHome() != null ? 1 : 0)),
						(kingdom.getHome() != null ? SQLUtil.filterBasic(kingdom.getHome().getWorld().getName(), 128) : "world"),
						Double.valueOf((kingdom.getHome() != null ? kingdom.getHome().getX() : 0)),
						Double.valueOf((kingdom.getHome() != null ? kingdom.getHome().getY() : 0)),
						Double.valueOf((kingdom.getHome() != null ? kingdom.getHome().getZ() : 0)),
						Double.valueOf((kingdom.getHome() != null ? kingdom.getHome().getYaw() : 0)),
						Double.valueOf((kingdom.getHome() != null ? kingdom.getHome().getPitch() : 0)),
						);
				
				sql.getMembersSQL().remove("kingdom_uuid", kingdom.getUUID());
				for(String member : kingdom.getMembers().keySet()) {
					String mem = member + "/" + kingdom.getMembers().get(member).toString();
					sql.getMembersSQL().set(kingdom.getUUID(), mem, Integer.valueOf((kingdom.getFighters().contains(member)?1:0)));
				}
				
				sql.getLandSQL().remove("kingdom_uuid", kingdom.getUUID());
				for(Land land : kingdom.getLand()) {
					if(land != null) {
						String str = land.getX() + "~" + land.getZ() + "~" + land.getWorldName().replace("%", "%0").replace("~", "%1").replace("/", "%2");
								sql.getLandSQL().set(kingdom.getUUID(),
										str);
					}
				}
				
				sql.getBansSQL().remove("kingdom_uuid", kingdom.getUUID());
				for(String str : kingdom.getBans().keySet()) {
					sql.getBansSQL().set(kingdom.getUUID(),
							(str + "/" + kingdom.getBans().get(str)));
				}
			}
		}).start();
	}

	public static void delete(Kingdom kingdom) {
		SQLControl sql = Feudal.getPlugin(Feudal.class).getSql();
		sql.getKingdomsSQL().remove("uuid", kingdom.getUUID());
		sql.getLandSQL().remove("kingdom_uuid", kingdom.getUUID());
		sql.getMembersSQL().remove("kingdom_uuid", kingdom.getUUID());
		sql.getBansSQL().remove("kingdom_uuid", kingdom.getUUID());
		sql.getRelationsSQL().remove("kingdom_uuid", kingdom.getUUID());
	}
}
