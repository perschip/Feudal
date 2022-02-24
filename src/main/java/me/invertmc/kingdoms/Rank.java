package me.invertmc.kingdoms;

public enum Rank {
    /**
     * Leader has all privilages.
     * Executive can do everything except kick leader and can not kick other executatives.  They can not promote others to executive.
     *
     * Members can invite and kick guests.  They can not claim land or unclaim land. They can start challenges.  They can not take from treasury.
     * Guests can not start challenges.
     *
     * Everyone can give to treasury.
     *
     * Higher rank you are, the less reputation subtraction there is for leaving.
     */
    LEADER(3, "Leader"), EXECUTIVE(2, "Executive"), MEMBER(1, "Member"), GUEST(0, "Guest");

    public final int ID;
    public final String NAME;

    private Rank(int id, String name) {
        ID = id;
        NAME = name;
    }

    public static Rank fromString(String string) {
        if(string.equalsIgnoreCase("LEADER")){
            return Rank.LEADER;
        }else if(string.equalsIgnoreCase("EXECUTIVE")){
            return Rank.EXECUTIVE;
        }else if(string.equalsIgnoreCase("MEMBER")){
            return Rank.MEMBER;
        }else{
            return Rank.GUEST;
        }
    }

    public int getValue(){
        if(this.equals(Rank.LEADER)){
            return 4;
        }else if(this.equals(Rank.EXECUTIVE)){
            return 3;
        }else if(this.equals(Rank.MEMBER)){
            return 2;
        }else{
            return 1;
        }
    }
}