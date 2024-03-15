package nl.lelebees.betterslabs;

import net.fabricmc.api.ModInitializer;

import java.util.logging.Logger;

public class BetterSlabsMod implements ModInitializer {
    public static final String MOD_ID = "betterslabs";
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Running Lele's BetterSlabs v0.2.1!");
    }
}
