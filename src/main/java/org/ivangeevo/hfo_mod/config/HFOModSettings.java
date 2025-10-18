package org.ivangeevo.hfo_mod.config;

public class HFOModSettings
{
        public boolean lavaPickupDisabled = true;
        public boolean waterloggingEnabled = true;
        public boolean waterloggedBlocksDissipate = true;
        public boolean waterFromIceDissipating = true;
        public boolean waterPersistentInEnd = true;

        public boolean isLavaPickupDisabled() { return lavaPickupDisabled; }
        public boolean isWaterloggingEnabled() { return waterloggingEnabled; }
        public boolean isWaterloggedBlocksDissipate() { return waterloggedBlocksDissipate; }
        public boolean isWaterFromIceDissipating() { return waterFromIceDissipating; }
        public boolean isWaterPersistentInEnd() { return waterPersistentInEnd; }

}
