package thaumicbees.storage;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import forestry.api.storage.IBackpackDefinition;

/**
 * Backpack definition for Forestry.
 * @author MysteriousAges
 *
 */
public class BackpackDefinition implements IBackpackDefinition
{
	private ArrayList<ItemStack> items;
	private String key;
	private String name;
	private int colour;

	public BackpackDefinition(String backpackKey, String backpackName, int backpackColour)
	{
		items = new ArrayList<ItemStack>();
		this.name = backpackName;
		this.key = backpackKey;
		this.colour = backpackColour;
	}

	@Override
	public String getKey()
	{
		return this.key;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public int getPrimaryColour()
	{
		return this.colour;
	}

	@Override
	public int getSecondaryColour()
	{
		return 0xFFFFFF;
	}

	@Override
	public void addValidItem(ItemStack validItem)
	{
		if (!this.items.contains(validItem))
		{
			this.items.add(validItem);
		}
	}

	@Override
	public Collection<ItemStack> getValidItems(EntityPlayer player)
	{
		return this.items;
	}

	@Override
	public boolean isValidItem(EntityPlayer player, ItemStack itemStack)
	{
		boolean flag = false;
		
		ItemStack stack;
		for (int i = 0; i < this.items.size() && !flag; i++)
		{
			stack = this.items.get(i);
			// If comparison stack has meta of -1
			if (stack.itemID == itemStack.itemID)
			{
				flag = stack.getItemDamage() == -1 || stack.getItemDamage() == itemStack.getItemDamage();
			}
		}
		
		return flag;
	}

}
