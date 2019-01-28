package me.fabian.jda.commands;

import me.fabian.jda.managers.Command;
import me.fabian.jda.managers.CommandEvent;

import java.time.temporal.ChronoUnit;

import static me.fabian.jda.YoshiBot.PREFIX;

public class PingCommand extends Command {

    public PingCommand() {
        this.commandName = "ping";
        this.description = "What's the ping of the bot atm !";
        this.usage = PREFIX + commandName;
        this.id = 3;

    }

    @Override
    protected void execute(CommandEvent event) {
        event.getChannel().sendMessage("Pinging ...").queue((m) ->
                m.editMessage("\uD83C\uDFD3 Ping: " +
                        event.getMessage().getCreationTime().until(m.getCreationTime(), ChronoUnit.MILLIS) + "ms |  " + "Websocket: " + event.getJDA().getPing() + "ms").queue());
    }

}

