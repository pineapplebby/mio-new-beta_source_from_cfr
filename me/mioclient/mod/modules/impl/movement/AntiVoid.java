//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\luckyhvh\Desktop\ik\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package me.mioclient.mod.modules.impl.movement;

import me.mioclient.api.util.interact.BlockUtil;
import me.mioclient.mod.modules.Category;
import me.mioclient.mod.modules.Module;
import me.mioclient.mod.modules.settings.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class AntiVoid
extends Module {
    private final Setting<Integer> height = this.add(new Setting<Integer>("Height", 100, 0, 256));

    public AntiVoid() {
        super("AntiVoid", "Allows you to fly over void blocks.", Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (AntiVoid.fullNullCheck()) {
            return;
        }
        boolean isVoid = true;
        for (int i = (int)AntiVoid.mc.player.posY; i > -1; --i) {
            if (BlockUtil.getBlock(new BlockPos(AntiVoid.mc.player.posX, (double)i, AntiVoid.mc.player.posZ)) == Blocks.AIR) continue;
            isVoid = false;
            break;
        }
        if (AntiVoid.mc.player.posY < (double)this.height.getValue().intValue() && isVoid) {
            AntiVoid.mc.player.motionY = 0.0;
        }
    }
}

