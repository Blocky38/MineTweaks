package me.jishuna.minetweaks.tweaks.items;

import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.jishuna.commonlib.utils.FileUtils;
import me.jishuna.minetweaks.api.RegisterTweak;
import me.jishuna.minetweaks.api.tweak.Tweak;

@RegisterTweak(name = "armor_swap")
public class ArmorSwapTweak extends Tweak {
	public ArmorSwapTweak(JavaPlugin plugin, String name) {
		super(plugin, name);

		addEventHandler(PlayerInteractEvent.class, this::onInteract);
	}

	@Override
	public void reload() {
		FileUtils.loadResource(getOwningPlugin(), "Tweaks/Items/" + this.getName() + ".yml").ifPresent(config -> {
			loadDefaults(config, true);
		});
	}

	private void onInteract(PlayerInteractEvent event) {
		if (event.useItemInHand() == Result.DENY || event.getAction() != Action.RIGHT_CLICK_AIR)
			return;

		ItemStack item = event.getItem();
		if (item == null || item.getType().isAir())
			return;

		EquipmentSlot slot = getSlot(item);

		if (slot == null)
			return;

		Player player = event.getPlayer();
		EntityEquipment equipment = player.getEquipment();

		ItemStack current = equipment.getItem(slot);

		if (current == null || current.getType().isAir())
			return;

		equipment.setItem(event.getHand(), current);
		equipment.setItem(slot, item);
	}

	public EquipmentSlot getSlot(ItemStack item) {

		if (EnchantmentTarget.ARMOR_HEAD.includes(item))
			return EquipmentSlot.HEAD;

		if (EnchantmentTarget.ARMOR_TORSO.includes(item))
			return EquipmentSlot.CHEST;

		if (EnchantmentTarget.ARMOR_LEGS.includes(item))
			return EquipmentSlot.LEGS;

		if (EnchantmentTarget.ARMOR_FEET.includes(item))
			return EquipmentSlot.FEET;

		return null;
	}
}
