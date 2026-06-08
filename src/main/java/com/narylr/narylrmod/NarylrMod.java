package com.narylr.narylrmod;

import com.narylr.narylrmod.block.ModBlocks;
import com.narylr.narylrmod.block.entity.ModBlockEntities;
import com.narylr.narylrmod.item.HeavySystemEvents;
import com.narylr.narylrmod.item.ModItemGroup;
import com.narylr.narylrmod.item.ModItems;
import com.narylr.narylrmod.recipe.ModRecipes;
import com.narylr.narylrmod.screen.ModMenus;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModRecipes.registerRecipes();
		ModMenus.registerMenus();
		HeavySystemEvents.registerEvents();
		ModItemGroup.registerItemGroups();
	}
}