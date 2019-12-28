package me.itzdabbzz.wolfmc;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EXPSystem extends ListenerAdapter {

    HashMap<Member, Integer> playerXP = new HashMap<>();
    HashMap<Member, Integer> playerTimer = new HashMap<>();

    private static EXPSystem instance;
    public static EXPSystem getInstance() {
        return instance;
    }

    public static void initialize() {
        if(instance == null)
            instance = new EXPSystem();
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if(canMemberGetXP(event.getMember())){
            randXP(event.getMember());
            setPlayerTime(event.getMember(), 60);
        }
    }

    public int getPlayerXP(Member member)
    {
        return playerXP.get(member);
    }

    private void setPlayerXP(Member member, int xp){
        playerXP.put(member, xp);
    }

    private int getPlayerTime(Member member){
        return playerTimer.get(member);
    }

    private void setPlayerTime(Member member, int time){
        playerTimer.put(member, time);
    }

    private void randXP(Member member){
        Random r = new Random();
        setPlayerXP(member, getPlayerXP(member) + r.nextInt(10));
    }

    private boolean canMemberGetXP(Member member){
        if(playerTimer.containsKey(member))
            return true;
        else
            return false;
    }

    public void startTimer(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for(Member member : playerTimer.keySet()){
                    setPlayerTime(member, getPlayerTime(member) - 1);
                }
            }
        };
        timer.schedule(task, 0, 1000);
        //System.out.println("timer started");
    }

}
