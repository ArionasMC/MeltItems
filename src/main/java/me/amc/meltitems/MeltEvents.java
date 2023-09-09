package me.amc.meltitems;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
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

}
