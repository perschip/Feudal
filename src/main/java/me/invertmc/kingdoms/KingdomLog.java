package me.invertmc.kingdoms;

public class KingdomLog {
    private String kingdomUUID;
    private long time;
    private boolean join;

    /**
     * Create new kingdom log from log string.
     * @param log
     * @throws Exception
     */
    public KingdomLog(String log) throws Exception{//Format uuid/time/<0|1>
        String split[] = log.split("/");
        if(split.length == 3){
            kingdomUUID = split[0];
            time = Long.parseLong(split[1]);
            if(split[2].equals("0")){
                join = false;
            }else if(split[2].equals("1")){
                join = true;
            }else{
                throw new Exception("INVALID LOG: Join boolean not '1' or '0'.");
            }
        }else{
            throw new Exception("INVALID LOG: Split length not 3 indexes.");
        }
    }

    /**
     * Create new kingdom log from kingdom uuid, time in ms, and if joining or leaving the kingdom.
     * @param uuid
     * @param time
     * @param join
     */
    public KingdomLog(String uuid, long time, boolean join){
        this.kingdomUUID = uuid;
        this.time = time;
        this.join = join;
    }

    /**
     * Get kingdom uuid
     * @return
     */
    public String getKingdomUUID() {
        return kingdomUUID;
    }

    /**
     * Set kingdom uuid
     * @param kingdomUUID
     */
    public void setKingdomUUID(String kingdomUUID) {
        this.kingdomUUID = kingdomUUID;
    }

    /**
     * Get time in ms
     * @return
     */
    public long getTime() {
        return time;
    }

    /**
     * Set time in ms.
     * @param time
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Check if this is a join log.
     * @return
     */
    public boolean isJoin() {
        return join;
    }

    /**
     * Set log as join or leave.
     * @param join
     */
    public void setJoin(boolean join) {
        this.join = join;
    }

    /**
     * Convert log to config string.
     */
    public String toString(){
        return kingdomUUID + "/" + time + "/" + ((join) ? "1" : "0");
    }
}