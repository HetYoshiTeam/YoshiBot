package me.fabian.jda.events;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Chat extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot() && event.getMessage().getContentRaw().toLowerCase().contains("hello")) {
            event.getChannel().sendMessage("hy there!").queue();
        }
    }
}
