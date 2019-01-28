package me.fabian.jda.commands;

import me.fabian.jda.managers.Command;
import me.fabian.jda.managers.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;

import static me.fabian.jda.YoshiBot.PREFIX;

public class UserInfoCommand extends Command {

    public UserInfoCommand() {
        this.commandName = "userInfo";
        this.usage = PREFIX + commandName + " [user]";
        this.description = "shows information about a user";
        this.id = 2;
    }

    @Override
    protected void execute(CommandEvent event) {
        User user = event.getAuthor();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.WHITE);
        eb.setTitle(user.getName() + "'s info");
        eb.addField("ID:", user.getId(), false);
        event.reply(eb.build());
    }
}
