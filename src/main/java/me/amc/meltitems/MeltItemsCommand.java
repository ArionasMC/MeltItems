package me.amc.meltitems;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class MeltItemsCommand implements CommandExecutor {

     private Permission reloadPerm;

     public MeltItemsCommand() {
          reloadPerm = new Permission("meltitems.reload");
          Bukkit.getServer().getPluginManager().addPermission(reloadPerm);
     }

     @Override
     public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
          if(args.length == 1) {
               if(args[0].equalsIgnoreCase("reload")) {
                    if(sender.hasPermission(reloadPerm)) {
                         MainCore.instance.reloadConfig();
                         MainCore.instance.initFurnaceRecipes();
                         sender.sendMessage(ChatColor.GREEN+"MeltItems has successfully been reloaded!");
                    } else {
                         sender.sendMessage(ChatColor.RED+"You do not have permission to do that!");
                    }
               }
          } else {
               sender.sendMessage(ChatColor.YELLOW+"MeltItems by Arionas_MC");
               if(sender.hasPermission(reloadPerm))
                    sender.sendMessage(ChatColor.YELLOW+"- /meltitems reload :: Reload plugin's configuration file");
          }
          return true;
     }

}
