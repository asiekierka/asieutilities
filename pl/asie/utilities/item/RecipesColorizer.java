package pl.asie.utilities.item;

import java.util.List;
import java.util.ArrayList;

import pl.asie.utilities.AsieUtilities;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipesColorizer implements IRecipe
{
	private final Item targetItem;
	private List<Item> sourceItems;
	
	public RecipesColorizer(Item targetItem, List<Item> sourceItems) {
		this.targetItem = targetItem;
		this.sourceItems = sourceItems;
	}
	
    public boolean matches(InventoryCrafting crafting, World par2World)
    {
        ItemStack targetStack = null;
        ArrayList dyeList = new ArrayList();

        for (int i = 0; i < crafting.getSizeInventory(); i++) {
            ItemStack stack = crafting.getStackInSlot(i);
            if (stack != null) {
                if (sourceItems.contains(stack.getItem())
                		|| targetItem.getClass().isInstance(stack.getItem())) {
                    targetStack = stack; // We need to be more specific here.
                }
                else if (stack.itemID == Item.dyePowder.itemID)
                    dyeList.add(stack);
            }
        }
        
        return targetStack != null && !dyeList.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting crafting)
    {
    	// TODO: DON'T REPEAT YOURSELF
        ItemStack targetStack = null;
        int[] color = new int[3];
        int colorCount = 0;
        int maximum = 0;
        
        for (int i = 0; i < crafting.getSizeInventory(); i++) {
            ItemStack stack = crafting.getStackInSlot(i);
            
            if (stack != null) {
                if (sourceItems.contains(stack.getItem())
                		|| targetItem.getClass().isInstance(stack.getItem())) {
                    targetStack = stack.copy();
                    targetStack.stackSize = 1;
                } else {
                    if (stack.itemID != Item.dyePowder.itemID)
                       return null;

                    float[] itemColor = EntitySheep.fleeceColorTable[BlockColored.getBlockFromDye(stack.getItemDamage())];
                    int red = (int)(itemColor[0] * 255.0F);
                    int green = (int)(itemColor[1] * 255.0F);
                    int blue = (int)(itemColor[2] * 255.0F);
                    maximum += Math.max(red, Math.max(green, blue));
                    color[0] += red;
                    color[1] += green;
                    color[2] += blue;
                    ++colorCount;
                }
            }
        }
        
        if(targetStack == null) return null;
        
        if(targetItem.getClass().isInstance(targetStack.getItem())) {
        	Item foundItem = targetStack.getItem();
        	if(ItemColorizer.hasColor(targetStack)) {
	            int itemColor = ItemColorizer.getColor(targetStack);
	            float red = (float)(itemColor >> 16 & 255) / 255.0F;
	            float green = (float)(itemColor >> 8 & 255) / 255.0F;
	            float blue = (float)(itemColor & 255) / 255.0F;
	            maximum = (int)((float)maximum + Math.max(red, Math.max(green, blue)) * 255.0F);
	            color[0] = (int)((float)color[0] + red * 255.0F);
	            color[1] = (int)((float)color[1] + green * 255.0F);
	            color[2] = (int)((float)color[2] + blue * 255.0F);
	            colorCount++;
	        }
        } else if(sourceItems.contains(targetStack.getItem())) {
        	targetStack.itemID = targetItem.itemID; // OVERWRITE!
        }
        
        int red = color[0] / colorCount;
        int green = color[1] / colorCount;
        int blue = color[2] / colorCount;
        float max = (float)maximum / (float)colorCount;
        float div = (float)Math.max(red, Math.max(green, blue));
        red = (int)((float)red * max / div);
        green = (int)((float)green * max / div);
        blue = (int)((float)blue * max / div);
        ItemColorizer.setColor(targetStack, (red << 16) | (green << 8) | blue);
        return targetStack;
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return 10;
    }

    public ItemStack getRecipeOutput()
    {
        return null;
    }
}
