package pl.asie.utilities.item;

import pl.asie.utilities.lib.Helper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemDyedEditableBook extends ItemEditableBook {
	private Icon iconGrayscale, iconNormal;
	
	public ItemDyedEditableBook(int par1, String name) {
		super(par1);
		this.setUnlocalizedName(name);
		this.setTextureName("asieutils:book_written_grayscale");
	}

	@Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
    
    public boolean hasColor(ItemStack stack) {
    	return ItemColorizer.hasColor(stack);
    }
    
    public int getColor(ItemStack stack) {
    	return Helper.minMaxDef(ItemColorizer.getColor(stack), 0, 16777215, 16777215);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        if (pass == 0) return 16777215;
        else return this.getColor(stack);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamageForRenderPass(int par1, int par2)
    {
        return par2 == 1 ? iconGrayscale : iconNormal;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir)
    {
        super.registerIcons(ir);

        this.iconNormal = ir.registerIcon("minecraft:book_written");
        this.iconGrayscale = ir.registerIcon("asieutils:book_written_grayscale");
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	// This is a hack that might ensure better future compatibility.
    	ItemStack targetStack = par1ItemStack.copy();
    	targetStack.itemID = Item.writtenBook.itemID;
        par3EntityPlayer.displayGUIBook(targetStack);
        return par1ItemStack;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack)
    {
        return false;
    }
}
