package fantastic.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fantastic.FantasticInfo;
import fantastic.Names;

public class ItemSharkFin extends Item
{


	public ItemSharkFin()
	{
		super();
		this.maxStackSize = 64;
		this.setCreativeTab(FantasticItems.tabFantastic);
		this.setUnlocalizedName("sharkfin");

	}



	

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister icon)
	{
		
		this.itemIcon = icon.registerIcon(FantasticInfo.ID.toLowerCase() + ":sharkfin");
		
	}

	
}
