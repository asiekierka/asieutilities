package pl.asie.utilities;

import java.util.logging.Logger;

import pl.asie.utilities.lib.ITileEntityOwner;
import pl.asie.utilities.lib.PacketSender;
import pl.asie.utilities.skin.SkinHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;

public class AsieMod {
	public boolean DEBUG = true;
	
	public Configuration config;
	public Logger logger;

    public void debug(String string) {
        if(DEBUG) logger.info(string);
    }
    
    public Block registerBlock(Class<? extends Block> blockClass, String name, int defaultID) {
    	int id = config.getBlock(name, defaultID).getInt();
    	try {
    		Block block = blockClass.getConstructor(Integer.TYPE, String.class).newInstance(id, name);
    		GameRegistry.registerBlock(block, name);
    		if(block instanceof ITileEntityOwner) {
    			ITileEntityOwner teOwner = (ITileEntityOwner)block;
    			GameRegistry.registerTileEntity(teOwner.getTileEntityClass(), name);
    		}
    		return block;
    	} catch(Exception e) { e.printStackTrace(); return null; }
    }
    
    public Item registerItem(Class<? extends Item> itemClass, String name, int defaultID) {
    	try {
    		Item item = itemClass.getConstructor(Integer.TYPE, String.class).newInstance(config.getItem(name, defaultID).getInt(), name);
    		GameRegistry.registerItem(item, name);
    		return item;
    	} catch(Exception e) { e.printStackTrace(); return null; }
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	logger = Logger.getLogger("asieutilities");
    	logger.setParent(FMLLog.getLogger());
    	
    	config = new Configuration(event.getSuggestedConfigurationFile());
    	config.load();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	config.save();
    }
}
