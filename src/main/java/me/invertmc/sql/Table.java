package me.invertmc.sql;


public class Table {
	private String name;
	private String usage;

	public Table(String name, String usage) {
		this.name = name;
		this.usage = usage;
	}

	public String getName() {
		return this.name;
	}

	public String getUsage() {
		return " (" + this.usage + ")";
	}

	public String getValues() {
		String v = "";
		String[] usageArray = this.usage.split(",");
		int i = 0;
		for (String column : usageArray) {
			i++;
			
			// exclude primary key information from columns
			if (!column.toUpperCase().startsWith("PRIMARY KEY")) {
				String[] c = column.split(" ");
				v = v + (c[0] == null ? "" : new StringBuilder().append(c[0]).append(i <= usageArray.length - 1 ? "," : "").toString());
			}	
		}

		return "(" + v + ")";
	}
	
	public String getValuesNonPrimary() {
		String v = "";
		String[] usageArray = this.usage.split(",");
		int i = 0;
		for (String column : usageArray) {
			i++;
			if(!column.toUpperCase().contains("PRIMARY KEY")) {
				
				// exclude primary key information from columns
				if (!column.toUpperCase().startsWith("PRIMARY KEY")) {
					String[] c = column.split(" ");
					v = v + (c[0] == null ? "" : new StringBuilder().append(c[0]).append(i <= usageArray.length - 1 ? "," : "").toString());
				}	
			}
		}

		return "(" + v + ")";
	}
}
