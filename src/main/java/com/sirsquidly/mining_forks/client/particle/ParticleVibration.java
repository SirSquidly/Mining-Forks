package com.sirsquidly.mining_forks.client.particle;

import com.sirsquidly.mining_forks.init.MiningForkSounds;
import com.sirsquidly.mining_forks.miningForks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleVibration extends ParticleBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(miningForks.MOD_ID, "textures/particles/vibration.png");
    private static final ResourceLocation VIBRATION_RESPONSE_TEXTURE = new ResourceLocation(miningForks.MOD_ID, "textures/particles/response.png");
    private final EnumFacing facing;
    /** Makes the particle wait in place, before preforming any logic. Also adds to the max age. */
    private int waitTime;

    /* If a texture isn't passed by the factory, just use the normal one. */
    public ParticleVibration(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int facingIndexIn, int waitTimeIn)
    { this(textureManager, world, x, y, z, movementX, movementY, movementZ, facingIndexIn, waitTimeIn, TEXTURE); }

    public ParticleVibration(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int facingIndexIn, int waitTimeIn, ResourceLocation texture)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, texture, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.facing = EnumFacing.byIndex(facingIndexIn);
        this.waitTime = waitTimeIn;
        this.particleMaxAge = 5 + this.rand.nextInt(5);
        this.texSheetSeg = 2;
        this.renderYOffset = 0.001F;
        this.particleScale =  3.0F;
        this.setAlphaF(0.0F);
    }

    @Override
    public void onUpdate()
    {
        if (--this.waitTime <= 0)
        {
            super.onUpdate();

            world.playSound(null, this.posX, this.posY, this.posZ, MiningForkSounds.ITEM_MINING_FORK_RESPOND, SoundCategory.PLAYERS, 1.0F, 1.0F);
            this.texSpot = Math.min((int)((this.particleAge / (float)this.particleMaxAge) * 4), 3);

            float alpha;
            float half = this.particleMaxAge * 0.5F;

            if (this.particleAge <= half)
            {
                float progress = this.particleAge / half;
                alpha = 0.5F * progress;
            }
            else
            {
                float progress = (this.particleAge - half) / half;
                alpha = 0.5F * (1.0F - progress);
            }
            this.setAlphaF(alpha);
        }
    }

    /* Allow Water to always render OVER this particle, so the transparency doesn't conflict. */
    public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        GlStateManager.depthMask(false);
        super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        GlStateManager.depthMask(true);
    }

    /* StackOverflow has the WEIRDEST stuff like damn. */
    public Vec3d[] particleVertexRendering(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, float particleSize)
    {
        Vec3d[] basicQuad = new Vec3d[] {
                new Vec3d(-particleSize, -particleSize, 0),
                new Vec3d(-particleSize,  particleSize, 0),
                new Vec3d( particleSize,  particleSize, 0),
                new Vec3d( particleSize, -particleSize, 0)
        };

        Vec3d normal = new Vec3d(facing.getDirectionVec());
        Vec3d up = (facing == EnumFacing.UP || facing == EnumFacing.DOWN) ? new Vec3d(0, 0, 1) : new Vec3d(0, 1, 0);
        Vec3d xRotation = up.crossProduct(normal).normalize();
        up = normal.crossProduct(xRotation).normalize();

        /* Apply the stupid bad math onto the generic quad AAAAGHHHHH */
        for (int i = 0; i < 4; i++)
        {
            Vec3d v = basicQuad[i];
            basicQuad[i] = xRotation.scale(v.x).add(up.scale(v.y)).add(normal.scale(v.z));
        }

        return basicQuad;
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            /* If there is no given Wait Time, assume we want it NOW! */
            if (parameters.length == 1)
            { return new ParticleVibration(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0], 0); }
            else if (parameters.length == 2)
            {
                return new ParticleVibration(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0], parameters[1]);
            }
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ResponseFactory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            /* If there is no given Wait Time, assume we want it NOW! */
            if (parameters.length == 1)
            { return new ParticleVibration(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0], 0, VIBRATION_RESPONSE_TEXTURE); }
            else if (parameters.length == 2)
            {
                return new ParticleVibration(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0], parameters[1], VIBRATION_RESPONSE_TEXTURE);
            }
            return null;
        }
    }
}