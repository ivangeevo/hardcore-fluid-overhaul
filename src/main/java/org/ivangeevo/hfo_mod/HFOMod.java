package org.ivangeevo.hfo_mod;

import com.google.gson.Gson;
import org.ivangeevo.hfo_mod.event.ModPlayerUseEvents;
import net.fabricmc.api.ModInitializer;
import org.ivangeevo.hfo_mod.config.HFOModSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class HFOMod implements ModInitializer {

    public static final String MOD_ID = "hardcore_fluid_overhaul";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public HFOModSettings settings;
    private static final String CONFIG_FILE_PATH = "./config/btwr/hardcoreFluidOverhaulCommon.json";
    private static HFOMod instance;

    public static HFOMod getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Hardcore Fluid Overhaul.");
        loadSettings();
        instance = this;

        //ModPlayerUseEvents.register();
    }

    public void loadSettings() {
        File file = new File(CONFIG_FILE_PATH);
        Gson gson = new Gson();
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                settings = gson.fromJson(fileReader, HFOModSettings.class);
                fileReader.close();
            } catch (IOException e) {
                LOGGER.warn("Could not load Hardcore Fluid Overhaul settings: " + e.getLocalizedMessage());
            }
        } else {
            settings = new HFOModSettings();
        }
    }

    public void saveSettings() {
        Gson gson = new Gson();
        File file = new File(CONFIG_FILE_PATH);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(settings));
            fileWriter.close();
        } catch (IOException e) {
            LOGGER.warn("Could not save Hardcore Fluid Overhaul settings: " + e.getLocalizedMessage());
        }
    }

}
