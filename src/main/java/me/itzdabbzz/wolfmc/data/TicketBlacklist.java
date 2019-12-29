package me.itzdabbzz.wolfmc.data;

import net.dv8tion.jda.api.entities.Member;

import java.util.*;

public class TicketBlacklist {

    public static List<Member> blackList = new ArrayList<>();
    public static List<Member> getBlackList() { return blackList; }

    public static void addPlayer(Member member){
        blackList.add(member);
    }

    public static void removePlayer(Member member){ blackList.remove(member); }

    public static boolean doesPlayerExist(Member member) {return blackList.contains(member);}

}
