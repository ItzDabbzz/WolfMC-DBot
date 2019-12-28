package me.itzdabbzz.wolfmc;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.entities.Member;

public class EXPSystem  {

    private static EXPSystem instance;
    public static EXPSystem getInstance() {
        initialize();
        return instance;
    }

    public static void initialize() {
        if(instance == null)
            instance = new EXPSystem();
    }
    
    private HashMap<Member, Integer> playerXP = new HashMap<>();
    private HashMap<Member, Integer> playerTimer = new HashMap<>();

    private EXPSystem() {
        playerXP = new HashMap<>();
        playerTimer = new HashMap<>();
        
        startTimer();
    }
    
    public int getPlayerXP(Member member)
    {
        return playerXP.get(member);
    }

    public void setPlayerXP(Member member, int xp){
        playerXP.put(member, xp);
    }

    public int getPlayerTime(Member member){
        return playerTimer.get(member);
    }

    public void setPlayerTime(Member member, int time){
        playerTimer.put(member, time);
    }

    public void randXP(Member member){
        Random r = new Random();
        setPlayerXP(member, getPlayerXP(member) + r.nextInt(10));
    }

    public boolean canMemberGetXP(Member member){
        if(playerTimer.containsKey(member))
            return true;
        else
            return false;
    }

    public void startTimer(){
        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for(Member member : playerTimer.keySet()){
                    setPlayerTime(member, getPlayerTime(member) - 1);
                }
            }
        };
        timer.schedule(task, 1000, 1000);
        System.out.println("exp timer started");
    }

}
