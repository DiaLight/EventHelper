package dialight.extensions;

import dialight.nms.ItemStackNms;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ItemStackBuilder {

    private ItemStack itemStack;

    public ItemStackBuilder() {
        this.itemStack = null;
    }
    public ItemStackBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStackBuilder reset(Material material) {
        this.itemStack = new ItemStack(material);
        return this;
    }

    public ItemStackBuilder let(Consumer<ItemStackBuilder> op) {
        op.accept(this);
        return this;
    }

    public ItemStackBuilder displayName(String name) {
        ItemMeta im = itemStack.getItemMeta();
        if(name != null && !name.isEmpty()) im.setDisplayName(name);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemStackBuilder lore(@NotNull String... lore) {
        return lore(Arrays.asList(lore));
    }
    public ItemStackBuilder lore(@NotNull List<String> lore) {
        ItemMeta im = itemStack.getItemMeta();
        if(!lore.isEmpty()) im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemStackBuilder addLore(@NotNull String... lore) {
        return addLore(Arrays.asList(lore));
    }
    public ItemStackBuilder addLore(@NotNull List<String> lore) {
        ItemMeta im = itemStack.getItemMeta();
        List<String> oldLore = im.getLore();
        if(oldLore == null) {
            if(!lore.isEmpty()) im.setLore(lore);
        } else {
            ArrayList<String> newLore = new ArrayList<>(oldLore);
            newLore.addAll(lore);
            if(!lore.isEmpty()) im.setLore(newLore);
        }
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemStackBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemStackBuilder nbt(String nbt) {
        itemStack = ItemStackNms.applyNbt(itemStack, nbt);
        return this;
    }

    public ItemStackBuilder durability(int value) {
        itemStack.setDurability((short) value);
        return this;
    }

    public ItemStackBuilder dyeColor(DyeColor color) {
        Colorable data = (Colorable) itemStack.getData();
        data.setColor(color);
        itemStack.setData((MaterialData) data);
        return this;
    }

    public ItemStack build() {
        if(this.itemStack == null) throw new IllegalStateException("Material have to be set");
//        ItemStackNms.dump(itemStack);
        return itemStack;
    }

}
