//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\luckyhvh\Desktop\ik\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 */
package me.mioclient.api.util.math;

import me.mioclient.api.util.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class InterpolationUtil
implements Wrapper {
    public static Vec3d getInterpolatedPos(Entity entity, float partialTicks, boolean wrap) {
        Vec3d amount = new Vec3d((entity.posX - entity.lastTickPosX) * (double)partialTicks, (entity.posY - entity.lastTickPosY) * (double)partialTicks, (entity.posZ - entity.lastTickPosZ) * (double)partialTicks);
        Vec3d vec = new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(amount);
        if (wrap) {
            return vec.subtract(InterpolationUtil.mc.getRenderManager().renderPosX, InterpolationUtil.mc.getRenderManager().renderPosY, InterpolationUtil.mc.getRenderManager().renderPosZ);
        }
        return vec;
    }

    public static AxisAlignedBB getInterpolatedAxis(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.minX - InterpolationUtil.mc.getRenderManager().viewerPosX, bb.minY - InterpolationUtil.mc.getRenderManager().viewerPosY, bb.minZ - InterpolationUtil.mc.getRenderManager().viewerPosZ, bb.maxX - InterpolationUtil.mc.getRenderManager().viewerPosX, bb.maxY - InterpolationUtil.mc.getRenderManager().viewerPosY, bb.maxZ - InterpolationUtil.mc.getRenderManager().viewerPosZ);
    }

    public static double getInterpolatedDouble(double pre, double current, float partialTicks) {
        return pre + (current - pre) * (double)partialTicks;
    }

    public static float getInterpolatedFloat(float pre, float current, float partialTicks) {
        return pre + (current - pre) * partialTicks;
    }
}

