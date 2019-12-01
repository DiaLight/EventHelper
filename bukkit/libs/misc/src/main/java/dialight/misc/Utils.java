package dialight.misc;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class Utils {


    public static boolean fastItemEquals(ItemStack st, ItemStack nd) {
        if(nd == null) return false;
        if(st.hashCode() != nd.hashCode()) return false;
        if(st.getType() != nd.getType()) return false;
        if(!st.getItemMeta().getDisplayName().equals(nd.getItemMeta().getDisplayName())) return false;
        if(st.getEnchantments().size() != nd.getEnchantments().size()) return false;
        if(st.getItemMeta().getLore().size() != nd.getItemMeta().getLore().size()) return false;
        final List<String>
                lst = st.getItemMeta().getLore(),
                lnd = nd.getItemMeta().getLore();
        for(int i = 0 ; i < st.getItemMeta().getLore().size() ; i++)
            if(!lst.get(i).equals(lnd.get(i))) return false;

        //return st.isSimilar(nd);
        return true;
    }

    public static void fastItemRemove(Inventory inv, ItemStack st) {
        for(int i = 0 ; i < inv.getContents().length ; i++)
            if(fastItemEquals(st, inv.getContents()[i])) inv.clear(i);
    }

    /**
     * Функция нахождения первого попавшегося блока
     * @param l Точка отсчета
     * @param v Шаг
     * @param i Количество шагов
     * @return Первый попавшийся блок
     */
    public static Location getTargetBlock(Location l, Vector v, int i) {
        Location r = l.clone();
        for (int j = 0; j < i; j++) {
            r.add(v);
            Block b = r.getBlock();
            if (b.getType().isSolid()) break;
        }
        return r;
    }

}
