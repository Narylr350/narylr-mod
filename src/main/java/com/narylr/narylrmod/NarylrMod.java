package com.narylr.narylrmod;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.narylr.narylrmod.item.ModBlocks.registerModBlocks;
import static com.narylr.narylrmod.item.ModItemGroup.registerItemGroups;
import static com.narylr.narylrmod.item.ModItems.registerModItems;

public class NarylrMod implements ModInitializer {
	public static final String MOD_ID = "narylr-mod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Narylr Mod loading");
		registerModItems();
		registerModBlocks();
		registerItemGroups();
	}
}