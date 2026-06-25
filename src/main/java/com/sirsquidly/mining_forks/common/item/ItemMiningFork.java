package com.sirsquidly.mining_forks.common.item;

import com.google.common.collect.Maps;
import com.sirsquidly.mining_forks.config.Config;
import com.sirsquidly.mining_forks.config.ConfigCache;
import com.sirsquidly.mining_forks.config.ConfigParser;
import com.sirsquidly.mining_forks.init.MiningForkSounds;
import com.sirsquidly.mining_forks.miningForks;
import net.minecraft.block.Block;
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
        int distanceCheck = ConfigCache.miningForkDepthCheck;
        int radiusCheck = ConfigCache.miningForkRadiusCheck;
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

        EnumFacing axisA = facing.getAxis() == EnumFacing.Axis.Y ? EnumFacing.NORTH : EnumFacing.UP;
        EnumFacing axisB = facing.getAxis() == EnumFacing.Axis.X ? EnumFacing.NORTH : EnumFacing.EAST;

        forkRayResult forkResult = null;


        for (int taxiRadius = 0; taxiRadius <= radiusCheck && forkResult == null; taxiRadius++)
        {
            for (int i = -taxiRadius; i <= taxiRadius && forkResult == null; i++)
            {
                int j = taxiRadius - Math.abs(i);

                forkResult = checkRay(worldIn, pos, facing, axisA, axisB, i, j, distanceCheck);
                if (forkResult != null) break;

                if (j != 0)
                {
                    forkResult = checkRay(worldIn, pos, facing, axisA, axisB, i, -j, distanceCheck);
                    if (forkResult != null) break;
                }
            }
        }

        if (forkResult != null)
        {
            int encodedPitch = (int)(pitch * 1000.0F);
            volume = 1.0F;

            if (!worldIn.isRemote)
            {
                int dirA = Integer.compare((forkResult.pos.getX() - pos.getX()) * axisA.getXOffset() + (forkResult.pos.getY() - pos.getY()) * axisA.getYOffset() + (forkResult.pos.getZ() - pos.getZ()) * axisA.getZOffset(), 0);
                int dirB = Integer.compare((forkResult.pos.getX() - pos.getX()) * axisB.getXOffset() + (forkResult.pos.getY() - pos.getY()) * axisB.getYOffset() + (forkResult.pos.getZ() - pos.getZ()) * axisB.getZOffset(),0);

                double shift = 0.4D;

                double partX = pos.getX() + 0.5D + facing.getXOffset() * 0.501D + (axisA.getXOffset() * dirA + axisB.getXOffset() * dirB) * shift;
                double partY = pos.getY() + 0.5D + facing.getYOffset() * 0.501D + (axisA.getYOffset() * dirA + axisB.getYOffset() * dirB) * shift;
                double partZ = pos.getZ() + 0.5D + facing.getZOffset() * 0.501D + (axisA.getZOffset() * dirA + axisB.getZOffset() * dirB) * shift;

                miningForks.proxy.spawnParticle(1, worldIn, partX, partY, partZ, 0, 0, 0, facing.getIndex(), (forkResult.depth * 2) + 5, encodedPitch);
            }
        }

        worldIn.playSound(null, pos, MiningForkSounds.ITEM_MINING_FORK_USE, SoundCategory.PLAYERS, volume, pitch);

        return EnumActionResult.PASS;
    }

    private forkRayResult checkRay(World worldIn, BlockPos pos, EnumFacing facing, EnumFacing axisA, EnumFacing axisB, int taxiX, int taxiY, int distanceCheck)
    {
        BlockPos taxiDistance = pos.offset(axisA, taxiX).offset(axisB, taxiY);

        for (int k = 0; k < distanceCheck; k++)
        {
            BlockPos rayCheckPos = taxiDistance.offset(facing.getOpposite(), k);
            IBlockState rayState = worldIn.getBlockState(rayCheckPos);

            if (!rayState.isFullBlock()) return null;

            if (this.material.isStateDetectable(rayState)) return new forkRayResult(rayCheckPos, k);
        }

        return null;
    }

    /**
     * The container, it's so baby!
     * Also easily allows me to send all the ray result data to be uses, since Depth would be stupid to re-calculate when I can just grab it
     * */
    private static class forkRayResult
    {
        final BlockPos pos;
        final int depth;

        forkRayResult(BlockPos pos, int depth)
        {
            this.pos = pos;
            this.depth = depth;
        }
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