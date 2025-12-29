package com.sirsquidly.mining_forks.common;

import com.sirsquidly.mining_forks.config.ConfigParser;
import com.sirsquidly.mining_forks.init.MiningForkSounds;
import com.sirsquidly.mining_forks.network.MiningForkPacketHandler;
import com.sirsquidly.mining_forks.network.MiningForkPacketSpawnParticles;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy
{
    public void preInitRegisteries(FMLPreInitializationEvent event)
    {
        MiningForkSounds.registerSounds();
        MiningForkPacketHandler.registerMessages();
    }

    public void postInitRegisteries(FMLPostInitializationEvent event)
    { ConfigParser.breakupConfigArrays(); }

    @SideOnly(Side.CLIENT)
    public void registerItemRenderer(Item item, int meta, String id){}

    /**
     *  Specialized particle method that sends particles on servers
     * */
    public void spawnParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
    {
        if (world.isRemote)
        { spawnParticle(particleId, posX, posY, posZ, speedX, speedY, speedZ, parameters); }
        else
        { MiningForkPacketHandler.CHANNEL.sendToAllTracking( new MiningForkPacketSpawnParticles(particleId, posX, posY, posZ, speedX, speedY, speedZ, parameters), new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 0.0D)); }
    }

    public void spawnParticle(int particleId, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
    {}
}