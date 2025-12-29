package com.sirsquidly.mining_forks.config;

import com.sirsquidly.mining_forks.miningForks;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@net.minecraftforge.common.config.Config(modid = miningForks.MOD_ID, name = miningForks.CONFIG_NAME)
@net.minecraftforge.common.config.Config.LangKey("config.veilings.title")
@Mod.EventBusSubscriber(modid = miningForks.MOD_ID)
public class Config
{
    @net.minecraftforge.common.config.Config.LangKey("config.veilings.configVersion")
    @net.minecraftforge.common.config.Config.Comment({
            "Config Versions help inform modpack makers/config users if changes have been made to the config between updates. These differ from main versioning, since the config file is static.",
            "Basically, you compare the current default of this value, to the default of when you generated it.",
            "",
            "The versioning follows:",
            "0.0.x - Default values have been slightly adjusted.",
            "0.x.0 - Config options have been added.",
            "x.0.0 - Previous Config Options have been completely overhauled and/or removed. Creating a fresh file is recommended."
    })
    public static String configVersion = "1.0.0";



    @net.minecraftforge.common.config.Config.RequiresMcRestart
    @net.minecraftforge.common.config.Config.LangKey("oe.config.item.nautilusArmor.nautilusArmourMaterials")
    @net.minecraftforge.common.config.Config.Comment("Materials of Nautilus Armor that are registered. Formatted as \"name-protection\"")
    public static String[] miningForkTypes = {"iron-83-minecraft:iron_ore;minecraft:iron_block", "golden-39-minecraft:gold_ore;minecraft:gold_block;nb:nether_gold_ore;nb:gilded_blackstone",
            "diamond-182-minecraft:diamond_ore;minecraft:diamond_block", "copper-61-deeperdepths:copper_ore;deeperdepths:copper_block:*;deeperdepths:chiseled_copper:*;deeperdepths:cut_copper:*",
            "netherite-204-nb:netherite_ore"};



    @Mod.EventBusSubscriber(modid = miningForks.MOD_ID)
    public static class ConfigSyncHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if(event.getModID().equals(miningForks.MOD_ID))
            { ConfigManager.sync(miningForks.MOD_ID, net.minecraftforge.common.config.Config.Type.INSTANCE); }
        }
    }
}