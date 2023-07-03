package com.juaanp.perfectlybalanced;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerfectlyBalanced implements ModInitializer {
	public static final String MOD_ID = "perfectlybalanced";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		AllBlocks.registerAllBlocks();
		AllItems.registerAllItems();
	}
}