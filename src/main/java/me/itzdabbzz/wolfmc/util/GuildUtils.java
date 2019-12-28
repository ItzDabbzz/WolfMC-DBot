package me.itzdabbzz.wolfmc.util;

import net.dv8tion.jda.api.entities.*;

public class GuildUtils {

    /**
     * Gets the number of users that the {@link Guild} has. Not including bots.
     *
     * @param guild The {@link Guild} to get the user count from
     * @return An int of the number of users
     */
    public static int getGuildUserCount(Guild guild) {
        int i = 0;
        for (Member member : guild.getMembers()) {
            if (!member.getUser().isBot()) {
                i++;
            }
        }
        return i;
    }

    /**
     *
     * @return
     */
    public static Role getRole(String string, Guild guild){
        return (Role) guild.getRolesByName(string, true);
    }

}
