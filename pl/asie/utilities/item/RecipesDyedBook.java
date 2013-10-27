package pl.asie.utilities.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import pl.asie.utilities.AsieUtilities;

public class RecipesDyedBook extends RecipesColorizer {
	private static final List<Item> sourceItems;
	
	static {
		sourceItems = new ArrayList<Item>();
		sourceItems.add(Item.writtenBook);
	}
	public RecipesDyedBook() {
		super(AsieUtilities.instance.itemDyedBook, sourceItems);
	}
}
