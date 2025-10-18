package org.ivangeevo.hfo_mod.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.ivangeevo.hfo_mod.HFOMod;

public class SettingsGUI
{
    static HFOModSettings settingsCommon = HFOMod.getInstance().settings;

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent).setTitle(Text.translatable("title.hardcore_fluid_overhaul.config"));
        builder.setSavingRunnable(() -> HFOMod.getInstance().saveSettings());

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config.hardcore_fluid_overhaul.category.general"));

        /** General Category**/
        general.addEntry(entryBuilder
                        .startBooleanToggle(Text.translatable("config.hardcore_fluid_overhaul.waterloggingEnabled"), settingsCommon.waterloggingEnabled)
                        .setDefaultValue(true)
                        .setSaveConsumer(newValue -> settingsCommon.waterloggingEnabled = newValue)
                        .build()
        );

        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.hardcore_fluid_overhaul.persistentEndWater"), settingsCommon.persistentEndWater)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.persistentEndWater = newValue)
                .build()
        );

        return builder.build();
    }

}
