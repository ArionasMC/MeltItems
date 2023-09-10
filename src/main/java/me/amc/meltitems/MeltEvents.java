package me.amc.meltitems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.Damageable;

public class MeltEvents implements Listener {

     public MeltEvents() {
          Bukkit.getServer().getPluginManager().registerEvents(this, MainCore.instance);
     }

     @EventHandler
     public void onSmelt(FurnaceSmeltEvent event) {
          for(RecipeMaker rm : MainCore.instance.recipeMakers) {
               if(rm.getSource() != event.getSource().getType()) continue;
               if(rm.getSource().getMaxDurability() == 0) return;

               Damageable meta = (Damageable) event.getSource().getItemMeta();
               int durability = event.getSource().getType().getMaxDurability() - meta.getDamage();
               double percentage = 100.0*durability/event.getSource().getType().getMaxDurability();

               event.setResult(rm.getRecipeModel().getResult((int)Math.ceil(percentage)));

               return;
          }
     }

     @EventHandler
     public void onShiftClick(InventoryClickEvent event) {
          if (event.getInventory().getType().equals(InventoryType.FURNACE)) {
               if (event.isShiftClick() || event.getRawSlot() == 0) {
                    if (event.isShiftClick()) {
                         Player p = (Player) event.getWhoClicked();
                         Bukkit.getServer().getScheduler().runTaskLater(MainCore.instance, p::updateInventory, 0);
                    }
               }
          }
     }

}
