package org.ivangeevo.hfo_mod.config;

import org.btwr.shared_library.api.config.ConfigBuilder;
import org.btwr.shared_library.api.config.ConfigGroup;
import org.btwr.shared_library.api.config.ConfigSetting;
import org.btwr.shared_library.api.config.TomlConfigManager;
import org.ivangeevo.hfo_mod.HFOMod;

public class HFOModConfig
{
        /** Replace with your MOD_ID for easy adaptation **/
        private static final String MOD_ID = HFOMod.MOD_ID;

        public static final ConfigGroup CONFIG;

        /** Call this method in your mod initializer so the class can initialize **/
        public static void register() {}

        public static final ConfigSetting<Boolean> lavaPickupDisabled =
                ConfigBuilder.booleanSetting("lavaPickupDisabled")
                        .defaultValue(true)
                        .comment("Toggles whether picking up lava is disabled")
                        .build();

        public static final ConfigSetting<Boolean> sourceFluidPickupDisabled =
                ConfigBuilder.booleanSetting("sourceFluidPickupDisabled")
                        .defaultValue(true)
                        .comment("Toggles whether source fluid blocks pickup is disabled")
                        .build();

        public static final ConfigSetting<Boolean> waterloggingEnabled =
                ConfigBuilder.booleanSetting("waterloggingEnabled")
                        .defaultValue(true)
                        .comment("Toggles whether blocks can be waterlogged")
                        .build();

        public static final ConfigSetting<Boolean> waterloggedBlocksDissipate =
                ConfigBuilder.booleanSetting("waterloggedBlocksDissipate")
                        .defaultValue(true)
                        .comment("Toggles whether waterlogged blocks dissipate when broken")
                        .build();

        public static final ConfigSetting<Boolean> waterFromIceDissipating =
                ConfigBuilder.booleanSetting("waterFromIceDissipating")
                        .defaultValue(true)
                        .comment("Toggles whether ice blocks break to dissipating water")
                        .build();

        public static final ConfigSetting<Boolean> waterPersistentInOverworld =
                ConfigBuilder.booleanSetting("waterPersistentInOverworld")
                        .defaultValue(false)
                        .comment("Toggles whether water in The Overworld will persist when placed")
                        .build();

        public static final ConfigSetting<Boolean> waterPersistentInEnd =
                ConfigBuilder.booleanSetting("waterPersistentInEnd")
                        .defaultValue(true)
                        .comment("Toggles whether water in The End will persist when placed")
                        .build();

        static {
                CONFIG = new ConfigGroup(String.format("%s/%s_common.toml", MOD_ID, MOD_ID));

                CONFIG.add(lavaPickupDisabled);
                CONFIG.add(sourceFluidPickupDisabled);
                CONFIG.add(waterloggingEnabled);
                CONFIG.add(waterloggedBlocksDissipate);
                CONFIG.add(waterFromIceDissipating);
                CONFIG.add(waterPersistentInOverworld);
                CONFIG.add(waterPersistentInEnd);

                TomlConfigManager.registerGroup(CONFIG); // auto init/load/save
        }
}