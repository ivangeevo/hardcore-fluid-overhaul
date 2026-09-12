package org.ivangeevo.hfo_mod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import org.ivangeevo.hfo_mod.HFOMod;

import java.util.concurrent.CompletableFuture;

public class HFOModLangProvider extends FabricLanguageProvider {

    public HFOModLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder tb) {
        this.addConfigTranslations(tb);
    }

    private void addConfigTranslations(TranslationBuilder tb) {
        addConfigMenuDefaults(tb);
        addConfigMenuTitle("Hardcore Fluid Overhaul Configuration Menu", tb);
        addConfigCategory("general", "General Options", tb);
    }

    private void addConfigMenuDefaults(TranslationBuilder tb) {
        this.addSimpleText("clientSettingsText", "Client Settings:", tb);
        this.addSimpleText("emptyClientConfigText", "§eNote:§r There are currently no client config settings.", tb);
        this.addSimpleText("serverSettingsText", "Server Settings:", tb);
        this.addSimpleText("serverSettingsNoAccessText", "§eNote:§r Server settings are not accessible in menus." +
                "\nThey can only be changed by editing the config file manually and require a world reload to take effect.", tb
        );
    }

    private void addConfigMenuTitle(String translation, TranslationBuilder tb) {
        tb.add("title." + HFOMod.MOD_ID + ".config", translation);
    }

    private void addConfigCategory(String categoryPath, String translation, TranslationBuilder tb) {
        tb.add("config." + HFOMod.MOD_ID + ".category." + categoryPath, translation);
    }

    private void addConfig(String configPath, String translation, TranslationBuilder tb) {
        tb.add("config." + HFOMod.MOD_ID + "." + configPath, translation);
    }

    private void addConfigTooltip(String configPath, String translation, TranslationBuilder tb) {
        tb.add("config." + HFOMod.MOD_ID + ".tooltip." + configPath, translation);
    }

    private void addSimpleText(String path, String translation, TranslationBuilder tb) {
        tb.add(configBasePath() + "text." + path, translation);
    }

    private String configBasePath() {
        return "config." + HFOMod.MOD_ID + ".";
    }

}
