package me.fabian.jda.managers;


import me.fabian.jda.YoshiBot;

import java.util.ArrayList;
import java.util.List;

public class CommandClientBuilder {

    private long ownerId;
    private final YoshiBot yoshiBot;
    private final List<Command> commands = new ArrayList<>();

    public CommandClientBuilder(YoshiBot yoshiBot) {
        this.yoshiBot = yoshiBot;
    }

    public CommandClient build() {
        return new CommandClientImpl(yoshiBot, ownerId, commands);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public CommandClientBuilder setOwnerId(long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    private CommandClientBuilder addCommand(Command command) {
        commands.add(command);
        return this;
    }

    public CommandClientBuilder addCommands(Command... commands) {
        for (Command command : commands) {
            this.addCommand(command);

        }
        return this;
    }
}