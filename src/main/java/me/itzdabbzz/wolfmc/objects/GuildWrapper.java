package me.itzdabbzz.wolfmc.objects;

import me.itzdabbzz.wolfmc.WolfBot;
import me.vem.jdab.utils.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class GuildWrapper {

    private String guildId;
    private String mutedRoleID = null;
    private Map<String, List<String>> warnings = new ConcurrentHashMap<>();

    /**
     * <b>Do not use</b>
     *
     * @param guildId Guild Id of the desired new GuildWrapper
     */
    public GuildWrapper(String guildId) {
        this.guildId = guildId;
    }

    public Guild getGuild() {
        return WolfBot.getClient().getJDA().getGuildById(guildId);
    }

    public String getGuildId() {
        return this.guildId;
    }

    public long getGuildIdLong() {
        return Long.parseLong(this.guildId);
    }

    @Nullable
    public Role getMutedRole() {
        if (mutedRoleID == null) {
            Role mutedRole = (Role) WolfBot.getClient().getJDA().getRolesByName("Muted", true);
            if (mutedRole == null) {
                if (!getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES, Permission.MANAGE_PERMISSIONS))
                    return null;
                try {
                    mutedRole = getGuild().createRole().setName("Muted").submit().get();
                    if (!getGuild().getSelfMember().getRoles().isEmpty())
                        getGuild().modifyRolePositions().selectPosition(mutedRole)
                                .moveTo(getGuild().getSelfMember().getRoles().get(0).getPosition() - 1).queue();
                    mutedRoleID = mutedRole.getId();
                    handleMuteChannels(mutedRole);
                    return mutedRole;
                } catch (InterruptedException | ExecutionException e) {
                    Logger.errf("Error creating role!", e);
                    return null;
                }
            } else {
                mutedRoleID = mutedRole.getId();
                handleMuteChannels(mutedRole);
                return mutedRole;
            }
        } else {
            Role mutedRole = getGuild().getRoleById(mutedRoleID);
            if (mutedRole == null) {
                mutedRoleID = null;
                return getMutedRole();
            } else {
                handleMuteChannels(mutedRole);
                return mutedRole;
            }
        }
    }

    /**
     * This will go through all the channels in a guild, if there is no permission override or it doesn't block message write then deny it.
     *
     * @param muteRole This is the muted role of the server, the role which will have MESSAGE_WRITE denied.
     */
    private void handleMuteChannels(Role muteRole) {
        getGuild().getTextChannels().forEach(channel -> {
            if (!getGuild().getSelfMember().hasPermission(channel, Permission.MANAGE_PERMISSIONS)) return;
            if (channel.getPermissionOverride(muteRole) != null &&
                    !channel.getPermissionOverride(muteRole).getDenied().contains(Permission.MESSAGE_WRITE))
                channel.getPermissionOverride(muteRole).getManager().deny(Permission.MESSAGE_WRITE).queue();
            else if (channel.getPermissionOverride(muteRole) == null)
                channel.createPermissionOverride(muteRole).setDeny(Permission.MESSAGE_WRITE).queue();
        });
    }


    public List<String> getUserWarnings(User user) {
        if (warnings == null) warnings = new ConcurrentHashMap<>();
        return warnings.getOrDefault(user.getId(), new ArrayList<>());
    }

    public void addWarning(User user, String reason) {
        List<String> warningsList = getUserWarnings(user);
        warningsList.add(reason);
        warnings.put(user.getId(), warningsList);
    }

    public Map<String, List<String>> getWarningsMap() {
        return warnings;
    }
}

