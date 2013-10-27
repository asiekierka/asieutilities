package pl.asie.utilities;

import java.io.*;
import java.util.*;

import pl.asie.utilities.skin.SkinHandler;
import net.minecraft.src.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.*;
import net.minecraft.network.*;
import net.minecraft.network.packet.*;
import net.minecraft.network.*;

public class NetworkHandler implements IPacketHandler {
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		DataInputStream packetData = new DataInputStream(new ByteArrayInputStream(packet.data));
		try {
			int commandType = packetData.readByte();
			switch(commandType) {
				case 1: { // Reload player skin
					String playerName = packetData.readUTF();
					System.out.println("Invalidating skin for player " + playerName);
					SkinHandler.invalidate(playerName, true);
				} break;
				default: break; // No command
			}
		} catch(Exception e) { e.printStackTrace(); }
	}
}