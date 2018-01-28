/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.entity.SpawnEntityEvent;

import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.PermissionUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the mobspawn flag
 * @author Jimi
 *
 */
public class FlagMobSpawnListener {

	/*@Listener
	public void onMobSpawn(SpawnEntityEvent event, @Root EntitySpawnCause cause) {
		if(!event.getEntities().isEmpty()) {
			if(cause.getEntity() instanceof Player)
				this.handleEvent(event, event.getEntities().get(0), (Player)cause.getEntity());
			else
				this.handleEvent(event, event.getEntities().get(0), null);
		}
	}
	
	@Listener
	public void onMobSpawn(SpawnEntityEvent event, @Root SpawnCause cause) {
		if(!event.getEntities().isEmpty() && !(cause.getType().equals(SpawnTypes.PLACEMENT)) && !(cause.getType().equals(SpawnTypes.DROPPED_ITEM)) && !(cause.getType().equals(SpawnTypes.CHUNK_LOAD))) {
			this.handleEvent(event, event.getEntities().get(0), null);
		}
	}*/
	
	private void handleEvent(SpawnEntityEvent event, Entity entity, Player player)
	{
		EntityType type = entity.getType();
		if(!FlagUtils.isBlockEntity(type) && !FlagUtils.isExplosion(type) && !FlagUtils.isVehicle(type))
		{
			String name = type.getId().toLowerCase();
			Region region = RegionUtils.getRegion(entity.getLocation());
			if(region != null) {
				boolean cancel = !region.getMobSpawn("all") || !region.getMobSpawn(name);
				if(player != null)
					cancel = cancel && !PermissionUtils.hasPermission(player, RegionPermission.REGION);
				if(cancel) {
					event.setCancelled(true);
					if(player != null)
						MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
				}
			}
		}
	}
}