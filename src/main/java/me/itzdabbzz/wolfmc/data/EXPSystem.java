package me.itzdabbzz.wolfmc.data;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EXPSystem extends ListenerAdapter {

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

    public int getPlayerXP(Member member){
        return playerXP.get(member);
    }

    private void setPlayerXp(Member member, int numXp) {
        playerXP.put(member, numXp);
    }

    private int getPlayerTime(Member member) {
        return playerTimer.get(member);
    }

    public void setPlayerTime(Member member , int num) {
        playerTimer.put(member, num);
    }

    public void randXp(Member member) {
        Random r = new Random();
        if(!playerXP.containsKey(member))
            setPlayerXp(member, 0);
        setPlayerXp(member, getPlayerXP(member) + r.nextInt(25));
    }

    public boolean canGetXp(Member member) {
        return !playerTimer.containsKey(member);
    }

    public void startTimer(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for(Member member : playerTimer.keySet()) {
                    setPlayerTime(member, getPlayerTime(member) - 1);
                    if(getPlayerTime(member) == 0) {
                        playerTimer.remove(member);
                    }
                }
            }
        };
        timer.schedule(task, 1000,1000);
    }
}
