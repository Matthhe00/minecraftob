package com.noirtrou.obtracker.input;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static KeyBinding OPEN_CONFIG;

    public static void register() {
        OPEN_CONFIG = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Open Mod",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            "ObTracker"
        ));
    }
}
