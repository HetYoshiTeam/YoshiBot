package me.fabian.jda.managers;

import me.fabian.jda.YoshiBot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.*;
import java.util.stream.Collectors;

public class CommandClientImpl extends ListenerAdapter implements CommandClient {

    private final Map<String, Integer> commandIndex;
    private final List<Command> commands;
    private final YoshiBot yoshiBot;

    public CommandClientImpl(YoshiBot yoshiBot, long ownerId, List<Command> commands) {
        if (ownerId == -1)
            throw new IllegalArgumentException("Owner ID was set null or not set! Please provide an User ID to register as the owner!");
        this.commandIndex = new IdentityHashMap<>();
        this.commands = new ArrayList<>();
        this.yoshiBot = yoshiBot;
        for (Command command : commands) {
            addCommand(command);
        }
    }

    @Override
    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public void addCommand(Command command) {
        addCommand(command, commands.size());
    }

    @Override
    public void addCommand(Command command, int index) {
        if (index > commands.size() || index < 0)
            throw new ArrayIndexOutOfBoundsException("Index specified is invalid: [" + index + "/" + commands.size() + "]");
        String name = command.getCommandName();
        synchronized (commandIndex) {
            if (commandIndex.containsKey(name))
                throw new IllegalArgumentException("Command added has a name or alias that has already been indexed: \"" + name + "\"!");
            for (String alias : command.getAliases()) {
                if (commandIndex.containsKey(alias))
                    throw new IllegalArgumentException("Command added has a name or alias that has already been indexed: \"" + alias + "\"!");
                commandIndex.put(alias, index);
            }
            commandIndex.put(name, index);
            if (index < commands.size())
                commandIndex.keySet().stream().filter(key -> commandIndex.get(key) > index).collect(Collectors.toList()).forEach(key -> commandIndex.put(key, commandIndex.get(key) + 1));
        }
        commands.add(index, command);
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        boolean nickname = event.getGuild() != null && event.getGuild().getSelfMember().getNickname() != null;

        if (event.getGuild() == null)
            return;

        String[] parts = null;
        String rawContent = event.getMessage().getContentRaw();
        String prefix = YoshiBot.PREFIX;

        if (rawContent.toLowerCase().startsWith(prefix.toLowerCase()))
            parts = Arrays.copyOf(rawContent.substring(prefix.length()).trim().split("\\s+", 2), 2);
        else if (rawContent.toLowerCase().startsWith(((nickname ? "<@!" : "<@") + event.getJDA().getSelfUser().getId() + ">")))
            parts = Arrays.copyOf(rawContent.substring(((nickname ? "<@!" : "<@") + event.getJDA().getSelfUser().getId() + ">").length()).trim().split("\\s+", 2), 2);


        if (parts != null && (event.isFromType(ChannelType.PRIVATE) || event.getTextChannel().canTalk())) {
            String name = parts[0];
            String args = parts[1] == null ? "" : parts[1];
            commands.stream().filter(cmd -> cmd.isCommandFor(name)).findAny().ifPresent(command -> {
                CommandEvent cevent = new CommandEvent(yoshiBot, event, args, this, name);
                command.run(cevent);
            });
        }
    }


    private boolean noPermission(MessageReceivedEvent event, Command command) {
        if (event.getGuild() != null && event.getTextChannel() != null)
            for (Permission perm : command.getPermissions()) {
                if (!event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), perm)) {
                    event.getTextChannel().sendMessage("To use `" + command.getCommandName() + "` I need the **" + perm.toString().replaceAll("_", " ") + "** permission").queue();
                    return true;
                }
            }
        return false;
    }


}
