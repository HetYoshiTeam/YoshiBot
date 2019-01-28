package me.fabian.jda.utils;

import me.fabian.jda.YoshiBot;
import me.fabian.jda.managers.CommandEvent;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.function.Consumer;

public class Helpers {

    private final YoshiBot yoshiBot;

    public Helpers(YoshiBot yoshiBot) {
        this.yoshiBot = yoshiBot;
    }

    public User getUserByArgs(CommandEvent event, String arg) {//Without null
        User user = getUserByArgsN(event, arg);
        if (user == null) user = event.getAuthor();
        return user;
    }

    public User getUserByArgsN(CommandEvent event, String arg) {//With null
        User user = null;
        if (!arg.matches("\\s+") && !arg.isEmpty()) {
            if (event.getMessage().getMentionedUsers().size() > event.getOffset())
                user = event.getMessage().getMentionedUsers().get(event.getOffset());
            else if (arg.matches("\\d+") && event.getJDA().getUserById(arg) != null)
                user = event.getJDA().asBot().getShardManager().getUserById(arg);
            else if (event.getGuild() != null && event.getGuild().getMembersByName(arg, true).size() > 0)
                user = event.getGuild().getMembersByName(arg, true).get(0).getUser();
            else if (event.getGuild() != null && event.getGuild().getMembersByNickname(arg, true).size() > 0)
                user = event.getGuild().getMembersByNickname(arg, true).get(0).getUser();
        }
        return user;
    }

    public long getTextChannelByArgsN(CommandEvent event, String arg) {
        long id = -1L;
        if (event.getMessage().getMentionedChannels().size() == 1) {
            id = event.getMessage().getMentionedChannels().get(0).getIdLong();
        } else if (arg.matches("\\d+") && event.getGuild().getTextChannelById(arg) != null) {
            id = Long.valueOf(arg);
        } else if (arg.equalsIgnoreCase("null")) {
            id = 0L;
        } else if (event.getGuild().getTextChannelsByName(arg, true).size() > 0) {
            id = event.getGuild().getTextChannelsByName(arg, true).get(0).getIdLong();
        }
        return id;
    }

    public long getVoiceChannelByArgsN(CommandEvent event, String arg) {
        long id = -1L;
        if (event.getMessage().getMentionedChannels().size() == 1) {
            id = event.getMessage().getMentionedChannels().get(0).getIdLong();
        } else if (arg.matches("\\d+") && event.getGuild().getVoiceChannelById(arg) != null) {
            id = Long.valueOf(arg);
        } else if (arg.equalsIgnoreCase("null")) {
            id = 0L;
        } else if (event.getGuild().getVoiceChannelsByName(arg, true).size() > 0) {
            id = event.getGuild().getVoiceChannelsByName(arg, true).get(0).getIdLong();
        }
        return id;
    }

    public Role getRoleByArgs(CommandEvent event, String arg) {
        if (!arg.matches("\\s+") && !arg.isEmpty()) {
            if (event.getMessage().getMentionedRoles().size() > 0) return event.getMessage().getMentionedRoles().get(0);
            else if (arg.matches("\\d+") && event.getGuild().getRoleById(arg) != null)
                return event.getGuild().getRoleById(arg);
            else if (event.getGuild() != null && event.getGuild().getRolesByName(arg, true).size() > 0)
                return event.getGuild().getRolesByName(arg, true).get(0);
        }
        return null;
    }

    public void retrieveUserByArgs(CommandEvent event, String arg, Consumer<User> success) {
        User user = getUserByArgsN(event, arg);
        if (user != null)
            success.accept(user);
        else if (arg.matches("\\d+"))
            event.getJDA().asBot().getShardManager().retrieveUserById(arg).queue(success);
        else success.accept(event.getAuthor());
    }

    public void retrieveUserByArgsN(CommandEvent event, String arg, Consumer<User> success) {
        User user = getUserByArgsN(event, arg);
        if (user != null)
            success.accept(user);
        else if (arg.matches("\\d+"))
            event.getJDA().asBot().getShardManager().retrieveUserById(arg).queue(success);
        else success.accept(null);
    }

    public long parseTimeFromArgs(String[] args) {
        long millis = -1;
        switch (args.length) {
            case 0:
                break;
            case 1: {
                if (args[0].matches("(\\d)|(\\d\\d)")) {
                    millis = 1000 * Short.parseShort(args[0]);
                }
                break;
            }
            case 2: {
                if (args[0].matches("(\\d)|(\\d\\d)") && args[1].matches("(\\d)|(\\d\\d)")) {
                    millis = 60000 * Short.parseShort(args[0]) + 1000 * Short.parseShort(args[1]);
                }
                break;
            }
            case 3:
            default: {
                if (args[0].matches("(\\d)|(\\d\\d)") && args[1].matches("(\\d)|(\\d\\d)") && args[2].matches("(\\d)|(\\d\\d)")) {
                    millis = 3600000 * Short.parseShort(args[0]) + 60000 * Short.parseShort(args[1]) + 1000 * Short.parseShort(args[2]);
                }
                break;
            }
        }
        return millis;
    }

    public boolean canNotInteract(CommandEvent event, User target) {
        if (event.getGuild().getMember(target).getRoles().size() > 0 && event.getGuild().getSelfMember().getRoles().size() > 0) {
            if (!event.getGuild().getSelfMember().getRoles().get(0).canInteract(event.getGuild().getMember(target).getRoles().get(0))) {
                event.reply("Can't modify a member with higher or equal highest role than myself");
                return true;
            }
        } else if (event.getGuild().getSelfMember().getRoles().size() == 0) {
            event.reply("Can't modify a member with higher or equal highest role than myself");
            return true;
        }
        return false;
    }

    public boolean canNotInteract(CommandEvent event, Role target) {
        if (event.getGuild().getSelfMember().getRoles().size() > 0) {
            if (!event.getGuild().getSelfMember().getRoles().get(0).canInteract(target)) {
                event.reply("Can't modify a member with higher or equal highest role than myself");
                return true;
            }
        } else {
            event.reply("Can't modify a member with higher or equal highest role than myself");
            return true;
        }
        return false;
    }



}
