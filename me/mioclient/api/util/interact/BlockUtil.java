//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\luckyhvh\Desktop\ik\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockDeadBush
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockTallGrass
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.mioclient.api.util.interact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.mioclient.api.util.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BlockUtil
implements Wrapper {
    public static IBlockState getState(BlockPos pos) {
        return BlockUtil.mc.world.getBlockState(pos);
    }

    public static Block getBlock(BlockPos pos) {
        return BlockUtil.getState(pos).getBlock();
    }

    public static List<BlockPos> getSphere(float range, boolean sphere, boolean hollow) {
        ArrayList<BlockPos> out = new ArrayList<BlockPos>();
        int x = BlockUtil.mc.player.getPosition().getX() - (int)range;
        while ((float)x <= (float)BlockUtil.mc.player.getPosition().getX() + range) {
            int z = BlockUtil.mc.player.getPosition().getZ() - (int)range;
            while ((float)z <= (float)BlockUtil.mc.player.getPosition().getZ() + range) {
                int y;
                int n = y = sphere ? BlockUtil.mc.player.getPosition().getY() - (int)range : BlockUtil.mc.player.getPosition().getY();
                while ((float)y < (float)BlockUtil.mc.player.getPosition().getY() + range) {
                    double distance = (BlockUtil.mc.player.getPosition().getX() - x) * (BlockUtil.mc.player.getPosition().getX() - x) + (BlockUtil.mc.player.getPosition().getZ() - z) * (BlockUtil.mc.player.getPosition().getZ() - z) + (sphere ? (BlockUtil.mc.player.getPosition().getY() - y) * (BlockUtil.mc.player.getPosition().getY() - y) : 0);
                    if (distance < (double)(range * range) && (!hollow || distance >= ((double)range - Double.longBitsToDouble(Double.doubleToLongBits(638.4060856917202) ^ 0x7F73F33FA9DAEA7FL)) * ((double)range - Double.longBitsToDouble(Double.doubleToLongBits(13.015128470890444) ^ 0x7FDA07BEEB3F6D07L)))) {
                        out.add(new BlockPos(x, y, z));
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return out;
    }

    public static int getPlaceAbility(BlockPos pos, boolean raytrace) {
        return BlockUtil.getPlaceAbility(pos, raytrace, true);
    }

    public static int getPlaceAbility(BlockPos pos, boolean raytrace, boolean checkForEntities) {
        Block block = BlockUtil.getBlock(pos);
        if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow)) {
            return 0;
        }
        if (raytrace && !BlockUtil.raytraceCheck(pos, 0.0f)) {
            return -1;
        }
        if (checkForEntities && BlockUtil.checkForEntities(pos)) {
            return 1;
        }
        for (EnumFacing side : BlockUtil.getPossibleSides(pos)) {
            if (!BlockUtil.canBeClicked(pos.offset(side))) continue;
            return 3;
        }
        return 2;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(side);
            if (!BlockUtil.getBlock(neighbor).canCollideCheck(BlockUtil.getState(neighbor), false) || BlockUtil.canReplace(neighbor)) continue;
            facings.add(side);
        }
        return facings;
    }

    public static boolean canReplace(BlockPos pos) {
        return BlockUtil.getState(pos).getMaterial().isReplaceable();
    }

    public static boolean canPlaceCrystal(BlockPos pos) {
        BlockPos boost = pos.add(0, 1, 0);
        BlockPos boost2 = pos.add(0, 2, 0);
        try {
            return (BlockUtil.getBlock(pos) == Blocks.BEDROCK || BlockUtil.getBlock(pos) == Blocks.OBSIDIAN) && BlockUtil.getBlock(boost) == Blocks.AIR && BlockUtil.getBlock(boost2) == Blocks.AIR && BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean canBeClicked(BlockPos pos) {
        return BlockUtil.getBlock(pos).canCollideCheck(BlockUtil.getState(pos), false);
    }

    public static boolean checkForEntities(BlockPos blockPos) {
        for (Entity entity : BlockUtil.mc.world.loadedEntityList) {
            if (entity instanceof EntityItem || entity instanceof EntityEnderCrystal || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle || entity instanceof EntityArrow || !new AxisAlignedBB(blockPos).intersects(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    public static boolean raytraceCheck(BlockPos pos, float height) {
        return BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + (double)BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d((double)pos.getX(), (double)((float)pos.getY() + height), (double)pos.getZ()), false, true, false) == null;
    }

    public static boolean isUnsafe(Block block) {
        List<Block> unsafeBlocks = Arrays.asList(new Block[]{Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.ENDER_CHEST, Blocks.ANVIL});
        return unsafeBlocks.contains((Object)block);
    }
}

