package me.invertmc.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.invertmc.Feudal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class StackUtil {
    /* 27 */ public static final Feudal plugin = Feudal.getPlugin(Feudal.class);
    /* 28 */ public static final BukkitScheduler scheduler = plugin.getServer().getScheduler();
    /* 29 */ public static final Logger logger = plugin.getLogger();

    /* 31 */ public static final String NAME = plugin.getDescription().getName();
    /* 32 */ public static final String VERSION = plugin.getDescription().getVersion();

    public static void dumpStack(Throwable t) {
        /* 36 */ ChatColor a = ChatColor.GREEN;
        /* 37 */ ChatColor b = ChatColor.GRAY;
        /* 38 */ String prefix = "[" + NAME + "] ";
        /* 39 */ StringWriter sw = new StringWriter();
        /* 40 */ PrintWriter pw = new PrintWriter(sw);
        /* 41 */ t.printStackTrace(pw);
        /* 42 */ String[] sts = sw.toString().replace("\r", "").split("\n");
        /* 43 */ StackTraceElement[] rawElements = t.getStackTrace();
        /* 44 */ List<StackTraceElement> elements = new ArrayList<StackTraceElement>();
        /* 45 */ for (StackTraceElement element : rawElements) {
            /* 46 */ if (element.getClassName().contains("lenis0012"))
                /* 47 */ elements.add(element);
        }
        /* 49 */ String[] out = new String[sts.length + elements.size() * 4 + 9];
        /* 50 */ out[0] = (prefix + ChatColor.RED + "Internal error!");
        /* 51 */ out[1] = ("");
        /* 52 */ out[2] = (prefix + "Include the following into your bug report:");
        /* 53 */ out[3] = (prefix + "          ====== " + a + "STACK TRACE" + b + " ======");
        /* 54 */ int j = 0;
        /* 55 */ for (int i = 4; i - 4 < sts.length; i++)
            /* 56 */ out[i] = (prefix + sts[(j = i - 4)]);
        /* 57 */ j += 4;
        /* 58 */ out[(j++)] = (prefix + "          ====== " + a + "DUMP" + b + " ======");
        /* 59 */ out[(j++)] = (prefix + "plugin name: " + NAME);
        /* 60 */ out[(j++)] = (prefix + "plugin version: " + VERSION);
        /* 61 */ out[(j++)] = (prefix + "bukkit version: " + Bukkit.getBukkitVersion());
        /* 62 */ out[(j++)] = (prefix + "description: " + t.getMessage());
        /* 63 */ int k = 1;
        /* 64 */ for (StackTraceElement e : elements) {
            /* 65 */ out[(j++)] = (prefix + "          ====== " + a + "Element #" + k + b + " ======");
            /* 66 */ out[(j++)] = (prefix + "class: " + e.getClassName());
            /* 67 */ out[(j++)] = (prefix + "at line: " + e.getLineNumber());
            /* 68 */ out[(j++)] = (prefix + "method: " + e.getMethodName());
            /* 69 */ k++;
        }
        /* 71 */ Runnable task = new SyncMessagePair(null, out, null);
        /* 72 */ scheduler.scheduleSyncDelayedTask(plugin, task);
    }

    public static class SyncMessagePair implements Runnable {
        private CommandSender receiver;
        private String[] messages;
        private Level level;

        public SyncMessagePair(Player player, String[] messages, Level level) {
            /* 81 */ this.receiver = (player != null ? player : Bukkit.getServer().getConsoleSender());
            /* 82 */ this.messages = messages;
            /* 83 */ this.level = level;
        }

        public void run() {
            try {
                /* 88 */ if (((this.receiver instanceof Player)) && (!((Player) this.receiver).isOnline())) {
                    /* 89 */ return;
                }
                /* 91 */ boolean toPlayer = this.receiver instanceof Player;

                /* 93 */ for (String msg : this.messages) {
                    /* 94 */ if (msg != null) {
                        /* 95 */ if ((!toPlayer) && (this.level != null))
                            /* 96 */ msg = "[" + this.level.toString() + "] " + msg;
                        this.receiver.sendMessage(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
