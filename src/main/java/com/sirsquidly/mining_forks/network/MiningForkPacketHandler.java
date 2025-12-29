package com.sirsquidly.mining_forks.network;

import com.sirsquidly.mining_forks.miningForks;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class MiningForkPacketHandler
{
	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(miningForks.MOD_ID);
	
	public static void registerMessages()
	{
		int messageId = 0;

		CHANNEL.registerMessage(MiningForkPacketSpawnParticles.Handler.class, MiningForkPacketSpawnParticles.class, messageId++, Side.CLIENT);
	}
}
