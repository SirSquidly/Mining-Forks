package com.sirsquidly.mining_forks.config;

import com.sirsquidly.mining_forks.miningForks;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@net.minecraftforge.common.config.Config(modid = miningForks.MOD_ID, name = miningForks.CONFIG_NAME)
@net.minecraftforge.common.config.Config.LangKey("config.mining_forks.title")
@Mod.EventBusSubscriber(modid = miningForks.MOD_ID)
public class Config
{
    @net.minecraftforge.common.config.Config.LangKey("config.mining_forks.configVersion")
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
    @net.minecraftforge.common.config.Config.LangKey("config.mining_forks.item.miningFork.miningForkTypes")
    @net.minecraftforge.common.config.Config.Comment("Registers Mining Forks. Formatted as \"[name]-[durability]-[blockID:meta];[blockID:meta]...\"")
    public static String[] miningForkTypes = {"iron-285-minecraft:iron_ore;minecraft:iron_block", "golden-1020-minecraft:gold_ore;minecraft:gold_block;nb:nether_gold_ore;nb:gilded_blackstone",
            "diamond-855-minecraft:diamond_ore;minecraft:diamond_block", "copper-261-deeperdepths:copper_ore;deeperdepths:copper_block:*;deeperdepths:chiseled_copper:*;deeperdepths:cut_copper:*",
            "netherite-1300-nb:netherite_ore"};


    @net.minecraftforge.common.config.Config.RequiresMcRestart
    @net.minecraftforge.common.config.Config.LangKey("config.mining_forks.item.miningFork.miningForkDepthCheck")
    @net.minecraftforge.common.config.Config.Comment("The depth (in blocks) that Mining Forks will check.")
    public static int miningForkDepthCheck = 10;

    @net.minecraftforge.common.config.Config.RequiresMcRestart
    @net.minecraftforge.common.config.Config.LangKey("config.mining_forks.item.miningFork.miningForkRadiusCheck")
    @net.minecraftforge.common.config.Config.Comment("The radius (in blocks) that Mining Forks will check.")
    public static int miningForkRadiusCheck = 1;

    @net.minecraftforge.common.config.Config.RequiresMcRestart
    @net.minecraftforge.common.config.Config.LangKey("config.mining_forks.item.miningFork.mineshaftLootInjection")
    @net.minecraftforge.common.config.Config.Comment({
            "Injects Mining Forks into the Abandoned Mineshaft Loot Table.",
            "Formatted as \"[Item]-[Chance]-[Min Durability]-[Max Durability]\"",
            "",
            "[Item] - The ID of the item (appended with '_lapis_rune').",
            "[Chance] - The chance for the item to be placed in the container. Defaults to 1.0.",
            "[Min Durability] - The minimum Durability the for will have when generated. Defaults to 1.0.",
            "[Max Durability] - The maximum Durability the for will have when generated. Defaults to 1.0."
    })
    public static String[] mineshaftLootInjection = {
            "mining_forks:iron_mining_fork-0.8-0.8",
            "mining_forks:golden_mining_fork-0.3",
            "mining_forks:diamond_mining_fork-0.2-0.25-0.9"};

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