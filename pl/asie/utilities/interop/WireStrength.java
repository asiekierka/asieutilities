package pl.asie.utilities.interop;

import mods.immibis.redlogic.api.wiring.IBundledWire;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class WireStrength {
	public static int getBundledPowerInput(World world, int i, int j, int k, int side) {
		System.out.println("Patched BundledPowerOutput check!");
		if(j >= 0 && j < world.getHeight()) {
			TileEntity te = world.getBlockTileEntity(i, j, k);
			if(te instanceof IBundledWire) {
				System.out.println("Found BundledWire!");
				byte[] data = null;
				int toDirection = Facing.oppositeSide[side];
				for(int l = -1; l < 6; l++) {
					if(l == side || !((IBundledWire)te).wireConnectsInDirection(l, toDirection)) continue;
					System.out.println("Checking " + l + " " + toDirection);
					data = ((IBundledWire)te).getBundledCableStrength(l, toDirection);
				}
				if(data != null) {
					int value = 0;
					for(int b = 0; b < 16; b++)
						value |= (data[b] > 0 ? 1<<b : 0);
					System.out.println("Outputting value " + value);
					return value;
				} else return 0;
			} else return -1;
		} else {
			return 0;
		}
	}
}
