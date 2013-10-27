package pl.asie.utilities.text;

import org.lwjgl.opengl.GL11;

import pl.asie.moducomp.ModularComputing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class TextRendererFixedWidth extends TextRenderer {
	public TextRendererFixedWidth(ResourceLocation texture, int w, int h) {
		super(texture, w, h);
	}

	public void drawLetter(Gui gui, TextureManager tm, TextWindow window, int x, int y, int color, short chr, short[] tint) {
		int[] colors;
		// BG
		colors = calculateColor(window.getColor((byte)(color >> 8)), tint);
        GL11.glColor4f(colors[0]/31.0f, colors[1]/31.0f, colors[2]/31.0f, 1.0f);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
		gui.drawTexturedModalRect(x, y, 0, 0, width, height); // Cheat!
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		// FG
		colors = calculateColor(window.getColor((byte)(color & 0xFF)), tint);
        GL11.glColor4f(colors[0]/31.0f, colors[1]/31.0f, colors[2]/31.0f, 1.0f);
		tm.bindTexture(this.texture);
		gui.drawTexturedModalRect(x, y, (chr & 31)*width, (chr >> 5)*height, width, height);
	}
	
	public void renderWindow(Gui gui, TextureManager tm, TextWindow window, int xp, int yp, short[] tint) {
		short[] display = window.getCharArray();
		for(int y = 0; y < window.height; y++) {
			for(int x = 0; x < window.width; x++) {
				drawLetter(gui, tm, window, xp + (x*width), yp + (y*height), display[x + ((y+window.height)*window.width)], display[x + (y*window.width)], tint);
			}
		}
	}
}
