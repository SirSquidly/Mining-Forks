package com.sirsquidly.mining_forks.common.item;

import com.google.common.collect.Maps;
import com.sirsquidly.mining_forks.config.Config;
import com.sirsquidly.mining_forks.config.ConfigParser;
import com.sirsquidly.mining_forks.init.MiningForkSounds;
import com.sirsquidly.mining_forks.miningForks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class ItemMiningFork extends Item
{
    private final Material material;

    public ItemMiningFork(Material material)
    {
        this.material = material;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setMaxDamage(this.material.getDurability());
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        int distanceCheck = 8;
        float volume = 0.5F;
        float pitch = (worldIn.rand.nextFloat() * 0.25F) + 0.875F;

        player.swingArm(hand);
        player.getHeldItem(hand).damageItem(1, player);



        if (!worldIn.isRemote)
        {
            double offset = 0.01D;

            double partX = pos.getX() + hitX + facing.getXOffset() * offset;
            double partY = pos.getY() + hitY + facing.getYOffset() * offset;
            double partZ = pos.getZ() + hitZ + facing.getZOffset() * offset;

            miningForks.proxy.spawnParticle(0, worldIn, partX, partY, partZ, 0, 0, 0, facing.getIndex());
        }



        for (int i = 0; i < distanceCheck; i++)
        {
            BlockPos checkPos = pos.offset(facing.getOpposite(), i);

            if (this.material.isStateDetectable(worldIn.getBlockState(checkPos)))
            {
                worldIn.playSound(null, checkPos, MiningForkSounds.ITEM_MINING_FORK_RESPOND, SoundCategory.PLAYERS, 1.0F, pitch);
                volume = 1.0F;



                if (!worldIn.isRemote)
                {
                    double offset = 0.01D;

                    double partX = pos.getX() + hitX + facing.getXOffset() * offset;
                    double partY = pos.getY() + hitY + facing.getYOffset() * offset;
                    double partZ = pos.getZ() + hitZ + facing.getZOffset() * offset;

                    miningForks.proxy.spawnParticle(1, worldIn, partX, partY, partZ, 0, 0, 0, facing.getIndex(), (i * 2) + 5);
                }
                break;
            }

        }

        worldIn.playSound(null, pos, MiningForkSounds.ITEM_MINING_FORK_USE, SoundCategory.PLAYERS, volume, pitch);

        return EnumActionResult.PASS;
    }




    public String getName() { return material.getName(); }

    public Material getMaterial() { return material; }

    public static class Material
    {
        private static final LinkedHashMap<String, Material> MATERIALS = Maps.newLinkedHashMap();

        public static void readFromConfig()
        {
            for (String string : Config.miningForkTypes)
            {
                String[] split = string.split("-");
                if (split.length < 2) continue;

                String name = split[0];

                int durability = 1;
                try
                { durability = Integer.parseInt(split[1]); }
                catch (Exception ignored) {}

                Set<IBlockState> validStates = new HashSet<>();

                if (split.length == 3)
                {
                    for (String blockString : split[2].split(";"))
                    { validStates.addAll(ConfigParser.getBlockStatesFromString(blockString)); }
                }

                register(name, durability, validStates);
            }
        }

        public static Material register(String name, int durability, Set<IBlockState> states)
        {
            Material material = new Material(name, durability, states);
            MATERIALS.put(name, material);
            return material;
        }

        public static Material get(String name) {
            return MATERIALS.get(name);
        }

        public static Collection<Material> getAll() {
            return MATERIALS.values();
        }

        private final String name;
        private final int durability;
        private final Set<IBlockState> detectedStates;

        private Material(String name, int durabilityIn, Set<IBlockState> detectedStatesIn)
        {
            this.name = name;
            this.durability = durabilityIn;
            this.detectedStates = detectedStatesIn;
        }

        public String getName() {
            return this.name;
        }

        public int getDurability() {
            return this.durability;
        }

        public boolean isStateDetectable(IBlockState state)
        { return this.detectedStates.contains(state); }
    }
}