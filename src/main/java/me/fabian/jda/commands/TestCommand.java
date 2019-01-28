package me.fabian.jda.commands;


import me.fabian.jda.managers.Command;
import me.fabian.jda.managers.CommandEvent;

import static me.fabian.jda.YoshiBot.PREFIX;

public class TestCommand extends Command {

    public TestCommand() {
        this.commandName = "test";
        this.usage = PREFIX + commandName;
        this.id = 1;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Hello there!");
    }
}
