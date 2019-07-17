package nycuro.commands.list;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import nycuro.API;
import nycuro.commands.ParentCommand;

/**
 * author: NycuRO
 * FactionsCore Project
 * API 1.0.0
 */
public class DropPartyMessageCommand extends ParentCommand {

    public DropPartyMessageCommand() {
        super("droppartymessage", "DropParty Message Command!");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof Player) return false;
        for (Player player : API.getMainAPI().getServer().getOnlinePlayers().values()) {
            API.getMechanicAPI().sendDropPartyMessageBroadcast(player);
            return true;
        }
        return false;
    }
}