package pl.asie.utilities.lib;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import pl.asie.utilities.NetworkHandler;

public class PacketSender {
	public static final byte COMMAND_TILE_ENTITY = (byte)255;
	public static String prefix;
	public DataOutputStream stream;
	private ByteArrayOutputStream bos;
	
	public PacketSender() {
		this.bos = new ByteArrayOutputStream();
        stream = new DataOutputStream(this.bos);
	}
	
	public PacketSender(TileEntity te) {
		this();
		prefixTileEntity(te);
	}
	
	public PacketSender(int command) {
		this();
		try {
			stream.writeByte(command);
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public PacketSender(TileEntity te, int command) {
		this(te);
		try {
			stream.writeByte(command);
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public Packet250CustomPayload getPacket() {
		return new Packet250CustomPayload(prefix, bos.toByteArray());
	}
	
	public void prefixTileEntity(TileEntity entity) {
        try {
            stream.writeByte(COMMAND_TILE_ENTITY);
			stream.writeInt(entity.worldObj.provider.dimensionId);
            stream.writeInt(entity.xCoord);
            stream.writeInt(entity.yCoord);
            stream.writeInt(entity.zCoord);
        } catch(Exception e) { e.printStackTrace(); }
	}
	
	public void sendToPlayer(Player player) {
		PacketDispatcher.sendPacketToPlayer(this.getPacket(), player);
	}
	
	public void sendAround(TileEntity te) {
        PacketDispatcher.sendPacketToAllAround(te.xCoord, te.yCoord, te.zCoord, 24.0D,
        		te.worldObj.provider.dimensionId, this.getPacket());
	}
	
	public void sendServer() {
		PacketDispatcher.sendPacketToServer(this.getPacket());
	}
	
	public void sendAll() {
		PacketDispatcher.sendPacketToAllPlayers(this.getPacket());
	}
}
