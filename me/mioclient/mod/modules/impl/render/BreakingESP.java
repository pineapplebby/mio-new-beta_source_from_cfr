//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\luckyhvh\Desktop\ik\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.server.SPacketBlockChange
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.mioclient.mod.modules.impl.render;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.util.Map;
import me.mioclient.api.events.impl.DamageBlockEvent;
import me.mioclient.api.events.impl.PacketEvent;
import me.mioclient.api.events.impl.Render3DEvent;
import me.mioclient.api.util.interact.BlockUtil;
import me.mioclient.api.util.math.InterpolationUtil;
import me.mioclient.api.util.render.RenderUtil;
import me.mioclient.mod.modules.Category;
import me.mioclient.mod.modules.Module;
import me.mioclient.mod.modules.settings.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BreakingESP
extends Module {
    private final Setting<Mode> mode = this.add(new Setting<Mode>("Mode", Mode.OUT));
    private final Setting<Boolean> box = this.add(new Setting<Boolean>("Box", true));
    private final Setting<Boolean> line = this.add(new Setting<Boolean>("Line", true));
    private final Setting<Float> lineWidth = this.add(new Setting<Float>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(3.0f)));
    private final Setting<Double> range = this.add(new Setting<Double>("Range", 20.0, 1.0, 50.0));
    private final Setting<ColorMode> colorMode = this.add(new Setting<ColorMode>("ColorMode", ColorMode.PROGRESS));
    private final Setting<Color> color = this.add(new Setting<Color>("Color", new Color(-2005041707, true), v -> this.colorMode.getValue() != ColorMode.PROGRESS));
    private final Map<BlockPos, Integer> blocks = Maps.newHashMap();

    public BreakingESP() {
        super("BreakingESP", "Highlights the blocks being broken around you.", Category.RENDER, true);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        this.blocks.forEach((pos, progress) -> {
            if (pos != null && progress != null) {
                if (BlockUtil.getBlock(pos) == Blocks.AIR) {
                    return;
                }
                if (pos.getDistance((int)BreakingESP.mc.player.posX, (int)BreakingESP.mc.player.posY, (int)BreakingESP.mc.player.posZ) <= this.range.getValue()) {
                    float preDamage = BreakingESP.mc.playerController.curBlockDamageMP;
                    float damage = InterpolationUtil.getInterpolatedFloat(preDamage, BreakingESP.mc.playerController.curBlockDamageMP, event.getPartialTicks());
                    AxisAlignedBB bb = BreakingESP.mc.world.getBlockState(pos).getSelectedBoundingBox((World)BreakingESP.mc.world, pos);
                    double x = bb.minX + (bb.maxX - bb.minX) / 2.0;
                    double y = bb.minY + (bb.maxY - bb.minY) / 2.0;
                    double z = bb.minZ + (bb.maxZ - bb.minZ) / 2.0;
                    double sizeX = (double)damage * (bb.maxX - x);
                    double sizeY = (double)damage * (bb.maxY - y);
                    double sizeZ = (double)damage * (bb.maxZ - z);
                    Color color = this.colorMode.getValue() == ColorMode.PROGRESS ? new Color(damage <= 0.75f ? 200 : 0, damage >= 0.751f ? 200 : 0, 0, this.color.getValue().getAlpha()) : this.color.getValue();
                    AxisAlignedBB inBB = bb.shrink((double)damage * bb.getAverageEdgeLength() * 0.5);
                    AxisAlignedBB outBB = new AxisAlignedBB(x - sizeX, y - sizeY, z - sizeZ, x + sizeX, y + sizeY, z + sizeZ);
                    RenderUtil.drawBoxESP(this.mode.getValue() == Mode.IN ? inBB : outBB, color, false, new Color(-1), this.lineWidth.getValue().floatValue(), this.line.getValue(), this.box.getValue(), color.getAlpha(), false);
                }
            }
        });
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketBlockChange && this.blocks.containsKey((Object)((SPacketBlockChange)event.getPacket()).getBlockPosition()) && ((SPacketBlockChange)event.getPacket()).getBlockState().getBlock() != Blocks.AIR) {
            this.blocks.remove((Object)((SPacketBlockChange)event.getPacket()).getBlockPosition());
        }
    }

    @SubscribeEvent
    public void onDamageBlock(DamageBlockEvent event) {
        if (BreakingESP.fullNullCheck() || BreakingESP.mc.player.getDistanceSq(event.getPosition()) > this.range.getValue()) {
            return;
        }
        if (event.getProgress() > 0 && event.getProgress() < 9) {
            this.blocks.putIfAbsent(event.getPosition(), event.getProgress());
        } else {
            this.blocks.remove((Object)event.getPosition(), event.getProgress());
        }
    }

    private static enum ColorMode {
        PROGRESS,
        CUSTOM;

    }

    private static enum Mode {
        IN,
        OUT;

    }
}

