package me.invertmc.kingdoms;

public class Shield {
    private byte type;//Types: 0 = vacation, 1 = 1 week, 2 = 2 week, 3 = 3 week
    private long start;
    private long end;

    /**
     * Create shield
     * @param type
     * @param start
     * @param end
     */
    public Shield(byte type, long start, long end){
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public long getEnd(){
        return end;
    }

    /**
     * Create shield from string.
     * @param string
     */
    public Shield(String string) {
        String split[] = string.split("/");
        if(split.length == 3){
            type = (byte) Integer.parseInt(split[0]);
            start = Long.parseLong(split[1]);
            end = Long.parseLong(split[2]);
        }
    }

    /**
     * Get save string.
     * @return
     */
    public String getSaveString() {
        return type + "/" + start + "/" + end;
    }

    /**
     * Get type of shield.
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     * Get shield display string.
     * @return
     */
    public String getDisplayString() {
        String type = "";
        if(this.type == 0){
            type = "Vacation";
        }else if(this.type == 1){
            type = "One Week";
        }else if(this.type == 2){
            type = "Two Week";
        }else if(this.type == 3){
            type = "Three Week";
        }else{
            type = "ERROR";
        }
        if(end - System.currentTimeMillis() <= 3600000){
            return "Type: " + type + " - Ends within the hour";
        }
        return "Type: " + type + " - Ends in " + Kingdom.getAge(end - System.currentTimeMillis());
    }

}
