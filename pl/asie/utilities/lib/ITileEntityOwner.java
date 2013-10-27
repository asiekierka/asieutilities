package pl.asie.utilities.lib;

import net.minecraft.tileentity.TileEntity;

public interface ITileEntityOwner {
	public Class<? extends TileEntity> getTileEntityClass();
}
