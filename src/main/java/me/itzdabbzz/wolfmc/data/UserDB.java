package me.itzdabbzz.wolfmc.data;

import me.itzdabbzz.wolfmc.WolfBot;
import me.vem.jdab.sqlite.SqliteDatabase;
import me.vem.jdab.sqlite.SqliteQuery;
import me.vem.jdab.utils.Logger;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.io.IOException;
import java.sql.SQLException;

public class UserDB {

    private WolfBot bot;
    private static SqliteDatabase db;

    private Long userID;
    private String userName;
    private Long groupID;
    private Integer xp;
    private Integer level;
    private boolean muted;

    public static void getUser(Member member){
        String sqlQuery = "INSERT INTO wb_users ('id', 'name', 'group', 'xp', 'level', 'muted') VALUES (value-1, value-2, value-3, value-4, value-5, value-6)";
        db = SqliteDatabase.create();


        try(SqliteQuery command = db.getQuery(sqlQuery)){
            command.addParameter("value-1", member.getId());
            command.addParameter("value-2", member.getEffectiveName());
            command.addParameter("value-3", 0);
            command.addParameter("value-4", 0);
            command.addParameter("value-5", 1);
            command.addParameter("value-6", false);
            command.execNonQuery();
            Logger.info("Inserted User " + member.getEffectiveName());
        } catch(IOException | SQLException e) {
            e.printStackTrace();
            Logger.err("Cannot Insert User " + member.getEffectiveName()) ;
        }
    }

    public void getName(Long userID){

    }

    public void getRoles(Long userID){

    }

    public Integer addXP(Long userID, Integer xpGain){
        return 0;
    }

    public Integer setXP(Long userID, Integer xpValue){
        return 0;
    }

    public Integer getXP(Long userId){
        return 0;
    }

    public Integer addLevel(Long userID, Integer levelGain){
        return 0;
    }

    public Integer setLevel(Long userID, Integer levelValue){
        return 0;
    }

    public Integer getLevel(Long userID) {
        return 0;
    }

    public boolean isMuted(Long userID){
        return false;
    }

    public void setMuted(Long userID, boolean isMuted){

    }
}
