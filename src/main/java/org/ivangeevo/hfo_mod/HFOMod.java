package org.ivangeevo.hfo_mod;

import net.fabricmc.api.ModInitializer;
import org.ivangeevo.hfo_mod.config.HFOModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HFOMod implements ModInitializer {

    public static final String MOD_ID = "hardcore_fluid_overhaul";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Hardcore Fluid Overhaul.");

        HFOModConfig.register();

        //ModPlayerUseEvents.register();
    }

}
