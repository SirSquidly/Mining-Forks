package com.sirsquidly.mining_forks.init;

import com.sirsquidly.mining_forks.common.item.*;
import com.sirsquidly.mining_forks.miningForks;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Mod.EventBusSubscriber(modid = miningForks.MOD_ID)
public class MiningForkItems
{
    public static final List<Item> itemList = new ArrayList<Item>();

    public static LinkedHashMap<ItemMiningFork.Material, ItemMiningFork> MINING_FORKS = new LinkedHashMap<>();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        ItemMiningFork.Material.readFromConfig();
        ItemMiningFork.Material.getAll().forEach(material -> MINING_FORKS.put(material,
                (ItemMiningFork) itemReadyForRegister(new ItemMiningFork(material), material.getName() + "_mining_fork")));

        for (Item items : itemList) event.getRegistry().register(items);
    }

    public static Item itemReadyForRegister(Item item, String name)
    {
        if (name != null)
        {
            item.setTranslationKey(miningForks.MOD_ID + "." + name);
            item.setRegistryName(name);
        }

        itemList.add(item);

        return item;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    { for (Item items : itemList) miningForks.proxy.registerItemRenderer(items, 0, "inventory"); }
}