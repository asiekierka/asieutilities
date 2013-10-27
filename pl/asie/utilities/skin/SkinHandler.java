package pl.asie.utilities.skin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;

import pl.asie.utilities.AsieUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.TextureManager;

public class SkinHandler {
	public static HashSet<AbstractClientPlayer> players = new HashSet<AbstractClientPlayer>();
	
	public static void setup(String skinURL, String capeURL) {
		Class klazz = AbstractClientPlayer.class;
		try {
			klazz.getField("skinURL").set(null, skinURL);
			klazz.getField("capeURL").set(null, capeURL);
			AsieUtilities.instance.debug("Skin and Cape URLs configured");
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public static boolean onPreInit(AbstractClientPlayer player) {
		players.add(player);
		return true;
	}
	
	public static boolean onPostInit(AbstractClientPlayer player) {
		return true;
	}
	
	public static void invalidate(String nickname) {
		invalidate(nickname, false);
	}
	
	public static void invalidate(String nickname, boolean ignoreCase) {
		for(AbstractClientPlayer player: players) {
			if(player.username.equals(nickname) || (ignoreCase && player.username.equalsIgnoreCase(nickname))) {
				// Fiddle with TextureManager
				TextureManager tm = Minecraft.getMinecraft().getTextureManager();
				Field f = null;
				Object o = null;
				try { f = tm.getClass().getField("mapTextureObjects"); }
				catch(Exception e) {
					try { f = tm.getClass().getField("a"); }
					catch(Exception ee) { ee.printStackTrace(); return; }
				}
				f.setAccessible(true);
				try {
					o = f.get(tm);
				} catch(Exception e) { e.printStackTrace(); return; }
				if(o != null && o instanceof Map) {
					Map m = (Map)o;
					m.remove(player.getLocationSkin());
					m.remove(player.getLocationCape());
				}
				// Reload
				try { AbstractClientPlayer.class.getMethod("setupCustomSkin").invoke(player); }
				catch(Exception e) {
					try { AbstractClientPlayer.class.getMethod("l").invoke(player); }
					catch(Exception ee) { ee.printStackTrace(); }
				}
			}
		}
	}
}
