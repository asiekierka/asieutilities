package pl.asie.utilities.text;

import net.minecraft.util.ResourceLocation;

public class TextRenderer {
	protected final ResourceLocation texture;
	protected final int width, height;
	
	public TextRenderer(ResourceLocation texture, int w, int h) {
		this.texture = texture;
		this.width = w;
		this.height = h;
	}
	
	public int[] calculateColor(int color, short[] tint) {
		int[] colors = new int[3];
		colors[0] = (color >> 10) & 31;
		colors[1] = (color >> 5) & 31;
		colors[2] = color & 31;
		int gray = (30*colors[0] + 60*colors[1] + 11*colors[2]) / 100; 
		// Mix with grayscale
		if(tint[3] > 0) {
			colors[0] = ((colors[0] * (32 - tint[3])) + (gray * tint[3])) >> 5;
			colors[1] = ((colors[1] * (32 - tint[3])) + (gray * tint[3])) >> 5;
			colors[2] = ((colors[2] * (32 - tint[3])) + (gray * tint[3])) >> 5;
		}
		colors[0] = colors[0] * tint[0] >> 5;
		colors[1] = colors[1] * tint[1] >> 5;
		colors[2] = colors[2] * tint[2] >> 5;
		return colors;
	}
}
