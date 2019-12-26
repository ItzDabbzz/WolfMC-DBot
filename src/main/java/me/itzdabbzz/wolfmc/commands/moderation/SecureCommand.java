package me.itzdabbzz.wolfmc.commands.moderation;

import me.vem.jdab.cmd.Command;

import java.util.List;

public abstract class SecureCommand extends Command {
    protected SecureCommand(String cmdname) {super(cmdname);}
    public abstract List<String> getValidKeySet();
}