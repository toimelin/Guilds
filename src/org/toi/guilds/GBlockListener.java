package org.toi.guilds;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockDamageLevel;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockInteractEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;


public class GBlockListener extends BlockListener{

	GHolder gholder;
    public GBlockListener(GHolder gholder) {
    	this.gholder = gholder;
    }
    
    public void onBlockInteract(BlockInteractEvent event)
    {
    	if (!event.isCancelled())
    	{
	    	if(!(event.getEntity() instanceof Player)) {
				return;
			}
	    	Player player = (Player)event.getEntity();
	    	Block block = event.getBlock();
	    	Material mat = block.getType();

    		boolean canBuild = false;
    		boolean isInGuild = false;
	    	if (mat.equals(Material.BURNING_FURNACE) || mat.equals(Material.CHEST) || mat.equals(Material.WORKBENCH) || mat.equals(Material.LEVER) ||
	    			mat.equals(Material.CAKE_BLOCK) || mat.equals(Material.DISPENSER) || mat.equals(Material.FURNACE) || mat.equals(Material.STONE_BUTTON) ||
	    			mat.equals(Material.WOODEN_DOOR) || mat.equals(Material.POWERED_MINECART) || mat.equals(Material.WOOD_DOOR) || mat.equals(Material.TNT))
	    	{
		    	for (Guild guild : gholder.getGuilds())
				{
					if (guild.isInArea(block.getX(), block.getZ()))
					{
							for (GPlayer gp: guild.getPlayers())
						{
							if (player.getName().equalsIgnoreCase(gp.getName()))
							{
								isInGuild = true;
								if (guild.isPlayerAdmin(gp.getName()))
								{
									return;
								}
								if (mat.equals(Material.BURNING_FURNACE) || mat.equals(Material.FURNACE))
								{
									if (gp.getRank() >= guild.getPerm("use furnace"))
										canBuild = true;
								}
								else if (mat.equals(Material.CHEST))
								{
									if (gp.getRank() >= guild.getPerm("use chest"))
										canBuild = true;
								}
								else if (mat.equals(Material.WORKBENCH))
								{
									if (gp.getRank() >= guild.getPerm("use workbench"))
										canBuild = true;
								}
								else if (mat.equals(Material.DISPENSER))
								{
									if (gp.getRank() >= guild.getPerm("use dispenser"))
										canBuild = true;
								}
								else if (mat.equals(Material.CAKE_BLOCK))
								{
									if (gp.getRank() >= guild.getPerm("eat cake"))
										canBuild = true;
								}
								else if (mat.equals(Material.WOOD_DOOR) || mat.equals(Material.WOODEN_DOOR))
								{
									if (gp.getRank() >= guild.getPerm("open doors"))
										canBuild = true;
								}
								else if (mat.equals(Material.LEVER))
								{
									if (gp.getRank() >= guild.getPerm("pull levers"))
										canBuild = true;
								}
								else if (mat.equals(Material.STONE_BUTTON))
								{
									if (gp.getRank() >= guild.getPerm("push buttons"))
										canBuild = true;
								}
								else if (mat.equals(Material.TNT))
								{
									if (gp.getRank() >= guild.getPerm("trigger tnt"))
										canBuild = true;
								}
								break;
							}
							if (!canBuild)
					    		event.setCancelled(true);
						}
						break;
					}
				}
	    	}	
    	}
    }
    
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		Block blockPlaced = event.getBlock();
		boolean canBuild = false;
		if (!event.isCancelled())
		{
			for (Guild guild : gholder.getGuilds())
			{
				if (guild.isInArea(blockPlaced.getX(), blockPlaced.getZ()))
				{
					if (guild.getPlayers().size() > 0)
					{
						for (GPlayer gp: guild.getPlayers())
						{
							if (player.getName().equalsIgnoreCase(gp.getName()))
							{
								if (gp.getRank() >= guild.getPerm("build") || guild.isPlayerAdmin(gp.getName()))
								{
									canBuild = true;
									break;
								}
							}
						}
						if (!canBuild)
							event.setCancelled(true);
						break;
					}
				}
			}
		}
	}
	
	public void onBlockDamage(BlockDamageEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (!event.isCancelled())
		{
			if (event.getDamageLevel().equals(BlockDamageLevel.BROKEN))
			{
				boolean canBuild = false;
				for (Guild guild : gholder.getGuilds())
				{
					if (guild.isInArea(block.getX(), block.getZ()))
					{
						if (guild.getPlayers().size() > 0)
						{
							for (GPlayer gp: guild.getPlayers())
							{
								if (player.getName().equalsIgnoreCase(gp.getName()))
								{
									if (gp.getRank() >= guild.getPerm("destroy") || guild.isPlayerAdmin(gp.getName()))
									{
										canBuild = true;
										break;
									}
								}
							}
							if (!canBuild)
								event.setCancelled(true);
							break;
						}
					}
				}
			}
		}
	}
}
