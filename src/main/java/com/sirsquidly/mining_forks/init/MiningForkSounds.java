package com.sirsquidly.mining_forks.init;

import com.sirsquidly.mining_forks.miningForks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class MiningForkSounds
{
    private static List<SoundEvent> soundList = new ArrayList<SoundEvent>();

    public static SoundEvent ITEM_MINING_FORK_USE = soundReadyForRegister("item.mining_fork.use");
    public static SoundEvent ITEM_MINING_FORK_RESPOND = soundReadyForRegister("item.mining_fork.respond");

    public static void registerSounds()
    { for (SoundEvent sounds : soundList) ForgeRegistries.SOUND_EVENTS.register(sounds); }

    private static SoundEvent soundReadyForRegister(String name)
    {
        ResourceLocation location = new ResourceLocation(miningForks.MOD_ID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        soundList.add(event);

        return event;
    }
}