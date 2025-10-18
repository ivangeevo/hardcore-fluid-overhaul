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
        addConfigMenuTitle("Hardcore Fluid Overhaul Configuration Menu", tb);
        addConfigCategory("general", "General Options", tb);
        addConfig("waterloggingEnabled", "Enable Waterlogging", tb);
        addConfigTooltip("waterloggingEnabled", "Toggles whether blocks can be waterlogged", tb);
        addConfig("waterloggedBlocksDissipate", "Waterlogged Blocks Dissipate", tb);
        addConfigTooltip("waterloggedBlocksDissipate", "Toggles whether waterlogged blocks dissipate when broken", tb);
        addConfig("waterFromIceDissipating", "Dissipating Ice Blocks", tb);
        addConfigTooltip("waterFromIceDissipating", "Toggles whether ice blocks break to dissipating water", tb);
        addConfig("waterPersistentInEnd", "Persistent End Water", tb);
        addConfigTooltip("waterPersistentInEnd", "Toggles whether water in the end will persist when placed", tb);
        addConfig("lavaPickupDisabled", "Disable Lava Pickup", tb);
        addConfigTooltip("lavaPickupDisabled", "Toggles whether picking up lava is disabled", tb);

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
}
