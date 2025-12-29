package com.sirsquidly.mining_forks;

import com.sirsquidly.mining_forks.common.CommonProxy;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
@Mod(modid = miningForks.MOD_ID, name = miningForks.NAME, version = miningForks.VERSION, dependencies = miningForks.DEPENDENCIES)
public class miningForks {
    public static final String MOD_ID = "mining_forks";
    public static final String NAME = "Mining Forks";
    public static final String CONFIG_NAME = "mining_forks";
    public static final String VERSION = "1.0.0";
    public static final String DEPENDENCIES = "";
    public static final String CLIENT_PROXY_CLASS = "com.sirsquidly.mining_forks.client.ClientProxy";
    public static final String COMMON_PROXY_CLASS = "com.sirsquidly.mining_forks.common.CommonProxy";
    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.Instance
    public static miningForks instance;

    @SidedProxy(clientSide = miningForks.CLIENT_PROXY_CLASS, serverSide = miningForks.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    { proxy.preInitRegisteries(event); }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {}

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {  proxy.postInitRegisteries(event);  }

    @SubscribeEvent
    public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {}
}
