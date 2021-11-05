package com.mtihc.minecraft.treasurechest.v8.compatible.mmoitems;


import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MMOItemsSupport {
	private MMOItems mmoItemsPluginInstance;
	private boolean useMMOItems;

	public MMOItemsSupport(PluginManager pluginManager) {
		Plugin mmoItems = pluginManager.getPlugin("MMOItems");
		Plugin mythicLib = pluginManager.getPlugin("MythicLib");
		if (mmoItems instanceof MMOItems && mythicLib instanceof MythicLib) {
			useMMOItems = true;
			mmoItemsPluginInstance = (MMOItems) mmoItems;
		} else {
			useMMOItems = false;
		}
	}

	/**
	 * if there has MMOItems plugin
	 *
	 * @return boolean
	 */
	public boolean supported() {
		return useMMOItems;
	}

	public Optional<MMOItemWrapper> newMMOItemWrapper(@NotNull ItemStack itemStack) {
		NBTItem nbtItem = getNBTItem(itemStack);
		if (hasMMOItem(nbtItem)) {
			return Optional.of(new MMOItemWrapper(getMMOItemType(nbtItem), getMMOItemID(nbtItem), itemStack.getAmount()));
		} else {
			return Optional.empty();
		}
	}

	public Optional<ItemStack> buildItemStack(@NotNull MMOItemWrapper mmoItemWrapper) {
		return Optional
				.ofNullable(plugin().getMMOItem(plugin().getType(mmoItemWrapper.getType()), mmoItemWrapper.getId()))
				.map(mmoItem -> {
					ItemStack item = mmoItem.newBuilder().build();
					item.setAmount(mmoItemWrapper.getAmount());
					return item;
				});
	}

	/**
	 * get MMOItems plugin instance
	 *
	 * @return MMOItems
	 */
	private MMOItems plugin() {
		return mmoItemsPluginInstance;
	}

	/**
	 * get NBTItem from MythicLib plugin
	 *
	 * @param itemStack ItemStack
	 * @return NBTItem
	 */
	@NotNull
	private NBTItem getNBTItem(@NotNull ItemStack itemStack) {
		return NBTItem.get(itemStack);
	}

	private boolean hasMMOItem(@Nullable String type, @Nullable String id) {
		return MMOItems.plugin.getTemplates().hasTemplate(plugin().getType(type), id);
	}

	private boolean hasMMOItem(NBTItem nbtItem) {
		return hasMMOItem(getMMOItemType(nbtItem), getMMOItemID(nbtItem));
	}

	/**
	 * get MMOItem type string from NBTItem
	 *
	 * @param nbtItem NBTItem, see MMOItemsSupport::getMMOItemType
	 * @return MMOItem type string
	 */
	@Nullable
	private String getMMOItemType(@NotNull NBTItem nbtItem) {
		return nbtItem.getType();
	}

	/**
	 * get MMOItem id string from NBTItem
	 *
	 * @param nbtItem NBTItem, see MMOItemsSupport::getMMOItemType
	 * @return MMOItem id string
	 */
	@Nullable
	private String getMMOItemID(@NotNull NBTItem nbtItem) {
		// Although I don't want to hard code this
		// https://git.mythiccraft.io/mythiccraft/mmoitems/-/wikis/Main%20API%20Features
		String id = nbtItem.getString("MMOITEMS_ITEM_ID");
		// copy from MBTItem::getType
		return !"".equals(id) ? id : null;
	}

}
