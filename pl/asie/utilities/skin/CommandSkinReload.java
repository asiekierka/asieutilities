package pl.asie.utilities.skin;

import pl.asie.utilities.lib.PacketSender;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;

public class CommandSkinReload extends CommandBase
{
    public String getCommandName()
    {
        return "skinreload";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.skinreload.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
    	if(args.length > 0) {
    		PacketSender packet = new PacketSender(1);
    		try {
    			packet.stream.writeUTF(args[0]);
    			packet.sendAll();
    		} catch(Exception e) { e.printStackTrace(); }
    	} else throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
    }
}
