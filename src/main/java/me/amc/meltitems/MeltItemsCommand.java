package me.amc.meltitems;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MeltItemsCommand implements CommandExecutor {

     @Override
     public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
          if(args.length == 1) {
               if(args[0].equalsIgnoreCase("reload")) {
                    MainCore.instance.reloadConfig();
                    MainCore.instance.initFurnaceRecipes();
                    sender.sendMessage("MeltItems has successfully been reloaded!");
               }
          }
          return true;
     }
}
