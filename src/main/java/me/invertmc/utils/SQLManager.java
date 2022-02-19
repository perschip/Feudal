package me.invertmc.utils;

import mc.obliviate.bloksqliteapi.SQLHandler;
import mc.obliviate.bloksqliteapi.sqlutils.DataType;
import mc.obliviate.bloksqliteapi.sqlutils.SQLTable;
import me.invertmc.Feudal;
import org.bukkit.Bukkit;

public class SQLManager extends SQLHandler {

    public SQLManager(Feudal plugin) {
        super(plugin.getDataFolder().getAbsolutePath());
        connect();
    }

    public void connect() {
        super.connect("feudal_data");
    }

    @Override
    public void onConnect() {
        Bukkit.getLogger().info("SQLite DB Connected Succesfully.");
    }

    public SQLTable createTable() {

        final SQLTable sqlTable = new SQLTable("players", "no")
                .addField("id", DataType.INTEGER, true, true, true)
                .addField("name", DataType.TEXT);

        return sqlTable.create();
    }
}
