package com.sirsquidly.mining_forks.common;

import com.sirsquidly.mining_forks.config.ConfigCache;
import com.sirsquidly.mining_forks.config.ConfigParser;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.Random;

@Mod.EventBusSubscriber
public class CommonEvents
{
    @SubscribeEvent
    public static void onLootLoad(LootTableLoadEvent event)
    {
        ResourceLocation tableName = event.getName();

        if (tableName.equals(LootTableList.CHESTS_ABANDONED_MINESHAFT))
        {
            for (ConfigParser.ForkLootInjectEntry entry : ConfigCache.mineshaftLootEntries)
            {
                ResourceLocation itemName = entry.stack.getItem().getRegistryName();
                injectPool(event.getTable(), entry.weight, "loottable:miningForkInjectTable_" + itemName, createLootEntry("loottable:miningForkInject_Entry_" + itemName, entry));
            }
        }
    }

    private static void injectPool(LootTable table, float chance, String poolName, LootEntry entry)
    {
        LootPool pool = new LootPool( new LootEntry[] {entry}, new LootCondition[] { new RandomChance(chance) }, new RandomValueRange(1), new RandomValueRange(0), poolName);
        table.addPool(pool);
    }

    private static LootEntry createLootEntry(String entryName, ConfigParser.ForkLootInjectEntry entry)
    {
        LootFunction damageFunction = new SetDamage(new LootCondition[0], new RandomValueRange(entry.minDamage, entry.maxDamage));

        return new LootEntryItem(entry.stack.getItem(), 1, 0, new LootFunction[] { damageFunction }, new LootCondition[0], entryName)
        {
            @Override
            public void addLoot(Collection<ItemStack> stacks, Random rand, LootContext context)
            {
                ItemStack stack = entry.stack.copy();
                damageFunction.apply(stack, rand, context);
                stacks.add(stack);
            }
        };
    }
}