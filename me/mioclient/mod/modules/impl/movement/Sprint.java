//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\luckyhvh\Desktop\ik\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 */
package me.mioclient.mod.modules.impl.movement;

import me.mioclient.api.managers.Managers;
import me.mioclient.mod.modules.Category;
import me.mioclient.mod.modules.Module;
import me.mioclient.mod.modules.settings.Setting;

public class Sprint
extends Module {
    private final Setting<Mode> mode = this.add(new Setting<Mode>("Mode", Mode.RAGE));

    public Sprint() {
        super("Sprint", "Sprints.", Category.MOVEMENT);
    }

    @Override
    public String getInfo() {
        return Managers.TEXT.normalizeCases((Object)this.mode.getValue());
    }

    @Override
    public void onTick() {
        if (this.mode.getValue() == Mode.RAGE && Sprint.isMoving()) {
            Sprint.mc.player.setSprinting(true);
        } else if (this.mode.getValue() == Mode.LEGIT && Sprint.mc.gameSettings.keyBindForward.isKeyDown() && !Sprint.mc.player.collidedHorizontally && !Sprint.mc.player.isSneaking()) {
            Sprint.mc.player.setSprinting(true);
        }
    }

    public static boolean isMoving() {
        return Sprint.mc.gameSettings.keyBindForward.isKeyDown() || Sprint.mc.gameSettings.keyBindBack.isKeyDown() || Sprint.mc.gameSettings.keyBindLeft.isKeyDown() || Sprint.mc.gameSettings.keyBindRight.isKeyDown();
    }

    private static enum Mode {
        RAGE,
        LEGIT;

    }
}

