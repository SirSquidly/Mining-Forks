package com.sirsquidly.mining_forks.config;

import com.sirsquidly.mining_forks.miningForks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * 	This is to break part arrays in the config for use in other areas of the code.
 *
 *  I break it up in this class so that I don't have to break the config arrays every time I want to use them.
 */
public class ConfigParser
{
	/** Goes through the many Arrays in the config, to translate them into lists to be used elsewhere. */
	public static void breakupConfigArrays()
	{
		for (String string : Config.mineshaftLootInjection)
		{
			if (string == null || string.isEmpty()) continue;

			String[] split = string.split("-");

			ItemStack stack = getItemStackFromString(split[0].trim());
			float chance = 1.0F;
			float minDurability = 1.0F;
			float maxDurability = 1.0F;

			if (stack.isEmpty())
			{
				miningForks.LOGGER.error("Invalid item for mineshaft injection, and will be skipped: " + split[0]);
				continue;
			}
			if (split.length > 1 && !split[1].isEmpty())
			{
				try { chance = MathHelper.clamp(Float.parseFloat(split[1].trim()), 0.0F, 1.0F); }
				catch (Exception e)
				{ miningForks.LOGGER.error(split[1] + " is an invalid float for chance."); }
			}
			if (split.length > 2 && !split[2].isEmpty())
			{
				try { minDurability = MathHelper.clamp(Float.parseFloat(split[2].trim()), 0.0F, 1.0F); }
				catch (Exception e)
				{ miningForks.LOGGER.error(split[2] + " is an invalid float for chance."); }
			}
			if (split.length > 3 && !split[3].isEmpty())
			{
				try { maxDurability = MathHelper.clamp(Float.parseFloat(split[3].trim()), 0.0F, 1.0F); }
				catch (Exception e)
				{ miningForks.LOGGER.error(split[3] + " is an invalid float for chance."); }

				if (minDurability > maxDurability)
				{
					miningForks.LOGGER.error("A minimum durability was set higher than a maximum!");
					minDurability = maxDurability;
				}
			}

			ConfigCache.mineshaftLootEntries.add(new ForkLootInjectEntry(stack, chance, minDurability, maxDurability));
		}
	}

	public static ItemStack getItemStackFromString(String string)
	{
		String[] ripString = string.split(":");

		if (ripString.length < 2) return ItemStack.EMPTY;

		Item item = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(ripString[0], ripString[1]));
		if(item == null) return ItemStack.EMPTY;

		int meta = 0;
		if (ripString.length > 2) meta = Integer.parseInt(ripString[2]);

		return new ItemStack(item, 1, meta);
	}

	public static List<IBlockState> getBlockStatesFromString(String string)
	{
		List<IBlockState> states = new ArrayList<>();
		String[] ripString = string.split(":");

		if (ripString.length < 2)
		{ return states; }

		Block block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(ripString[0], ripString[1]));
		Integer meta;

		if(block == null || block == Blocks.AIR)
		{ return states; }
		if(ripString.length > 2)
		{
			if (ripString[2].equals("*")) meta = -1;
			else meta = Integer.parseInt(ripString[2]);

			if(meta == -1) states.addAll(block.getBlockState().getValidStates());
			else states.add(block.getStateFromMeta(meta));
		}
		else states.add(block.getDefaultState());

		return states;
	}

	public static class ForkLootInjectEntry
	{
		public final ItemStack stack;
		public final float weight;
		public final float minDamage;
		public final float maxDamage;

		public ForkLootInjectEntry(ItemStack stack, float weight, float minDamage, float maxDamage)
		{
			this.stack = stack;
			this.weight = weight;
			this.minDamage = minDamage;
			this.maxDamage = maxDamage;
		}
	}
}