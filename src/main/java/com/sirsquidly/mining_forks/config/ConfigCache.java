package com.sirsquidly.mining_forks.config;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 	This is simply a static form of the config, for reference throughout the mod.
 */
public class ConfigCache
{
    public static final int miningForkDepthCheck = Config.miningForkDepthCheck;
    public static final int miningForkRadiusCheck = Config.miningForkRadiusCheck;

    public static List<ConfigParser.ForkLootInjectEntry> mineshaftLootEntries = Lists.newArrayList();
}