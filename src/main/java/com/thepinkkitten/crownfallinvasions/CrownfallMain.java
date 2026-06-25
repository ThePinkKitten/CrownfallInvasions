package com.thepinkkitten.crownfallinvasions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(CrownfallMain.MODID)
public class CrownfallMain {

  public static final String MODID = "crownfallinvasions";
  public static final Logger LOGGER = LogManager.getLogger();

  public CrownfallMain() {
    // Crownfall Invasions uses only event-driven logic — no blocks, items, or config needed
    LOGGER.info("Crownfall Invasions initialized");
  }
}
