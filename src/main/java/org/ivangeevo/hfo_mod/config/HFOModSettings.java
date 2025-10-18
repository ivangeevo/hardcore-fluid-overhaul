package org.ivangeevo.hfo_mod.config;

public class HFOModSettings
{
        public boolean waterloggingEnabled = true;
        public boolean waterloggedBlockWaterSource;
        public boolean persistentWaterloggedBlocks;
        public boolean persistentEndWater = true;

        public boolean isWaterloggingEnabled() { return waterloggingEnabled; }
        public boolean isWaterloggedBlockWaterSource() { return waterloggedBlockWaterSource; }
        public boolean isPersistentWaterloggedBlocks() { return persistentWaterloggedBlocks; }
        public boolean isPersistentEndWater() { return persistentEndWater; }

}
