package pl.asie.utilities;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.asie.utilities.item.ItemDyedEditableBook;
import pl.asie.utilities.item.RecipesColorizer;
import pl.asie.utilities.item.RecipesDyedBook;
import pl.asie.utilities.lib.PacketSender;
import pl.asie.utilities.skin.CommandSkinReload;
import pl.asie.utilities.skin.SkinClassTransformer;
import pl.asie.utilities.skin.SkinHandler;
import pl.asie.utilities.tweaks.BoneMealClassTransformer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="asieutilities", name="Asie's Utilities", version="0.0.1")
@NetworkMod(channels={"AsieUtls"}, clientSideRequired=true, packetHandler=NetworkHandler.class)
public class AsieUtilities extends AsieMod {
	@Instance(value = "asieutilities")
	public static AsieUtilities instance;
	
	public static String skinURL;
	public static String capeURL;
	
	public static Item itemDyedBook;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	super.preInit(event);
    	PacketSender.prefix = "AsieUtls";
   
    	config.addCustomCategoryComment("skin", "Functions related to the skin changing functionality. NOTE: The URL parameters are intended for roleplay and/or NPC servers.");
    	skinURL = config.get("skin", "skinURL", "http://skins.minecraft.net/MinecraftSkins/%s.png").getString();
    	capeURL = config.get("skin", "capeURL", "http://skins.minecraft.net/MinecraftCloaks/%s.png").getString();
    	SkinHandler.setup(skinURL, capeURL);
    	
    	BoneMealClassTransformer.finish(config.get("misc", "bonemealTreeChance", 0.45D).getDouble(0.45D),
    			config.get("misc", "bonemealMushroomChance", 0.4D).getDouble(0.4D),
    			!config.get("misc", "requireTwoBonemealHits", true).getBoolean(true));
    	
    	if(config.get("books", "dyedWrittenBooks", true).getBoolean(true)) {
    		itemDyedBook = this.registerItem(ItemDyedEditableBook.class, "asieutils.dyedEditableBook", 17540);
    	}
    }
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
    	super.init(event);
		LanguageRegistry lr = LanguageRegistry.instance();
		lr.addStringLocalization("commands.skinreload.usage", "/skinreload [player]");
		
		if(config.get("books", "oldBookRecipe", false).getBoolean(false)) {
			GameRegistry.addShapedRecipe(new ItemStack(Item.book), "x", "x", "x", 'x', Item.paper);
		}
		if(itemDyedBook != null) {
			GameRegistry.addRecipe(new RecipesDyedBook());
		}
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
    	super.postInit(event);
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
	    event.registerServerCommand(new CommandSkinReload());
	}
}
