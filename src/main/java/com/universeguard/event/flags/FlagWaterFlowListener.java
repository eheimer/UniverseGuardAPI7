/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import java.util.Optional;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.block.MatterProperty;
import org.spongepowered.api.data.property.block.MatterProperty.Matter;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the waterflow flag
 * @author Jimi
 *
 */
public class FlagWaterFlowListener {
		
	@Listener
	public void onWaterFlow(ChangeBlockEvent.Pre event) {
		if(!event.getLocations().isEmpty()) {
			BlockSnapshot block = event.getLocations().get(0).getExtent().createSnapshot(event.getLocations().get(0).getBlockX(), event.getLocations().get(0).getBlockY(), event.getLocations().get(0).getBlockZ());
			Location<World> location = event.getLocations().get(event.getLocations().size() - 1);
			Optional<MatterProperty> matter = block.getState().getProperty(MatterProperty.class);
			if(matter.isPresent() && matter.get().getValue().equals(Matter.LIQUID)) {
				BlockType blockType = block.getState().getType();
				if(blockType.equals(BlockTypes.WATER) || blockType.equals(BlockTypes.FLOWING_WATER)) {
					this.handleEvent(event, location, null);
				}
			}
		}
	}
	
	@Listener
	public void onWaterBucketFill(InteractItemEvent.Secondary event, @Root Player player) {
		if(event.getItemStack().getType().equals(ItemTypes.BUCKET) && event.getInteractionPoint().isPresent()) {
			BlockType block = player.getWorld().getBlock(event.getInteractionPoint().get().toInt()).getType();
			if(block.equals(BlockTypes.WATER) || block.equals(BlockTypes.FLOWING_WATER))
				this.handleEvent(event, player.getLocation(), player);
		}
	}
	
	@Listener
	public void onWaterBucketUse(InteractItemEvent.Secondary event, @Root Player player) {
		if(event.getItemStack().getType().equals(ItemTypes.WATER_BUCKET) && event.getInteractionPoint().isPresent()) {
			this.handleEvent(event, player.getLocation(), player);
		}
	}
	
	/*@Listener
	public void onWaterFlow(ChangeBlockEvent.Pre event, @Root LocatableBlock block) {
		BlockType type = block.getBlockState().getType();
		if(type.equals(BlockTypes.WATER) || type.equals(BlockTypes.FLOWING_WATER))
			this.handleEvent(event, block.getLocation(), null);
	}*/
	
	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.WATER_FLOW, location, player, RegionEventType.GLOBAL);
	}
}
