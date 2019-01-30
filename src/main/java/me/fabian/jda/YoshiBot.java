package me.fabian.jda;

import me.fabian.jda.commands.PingCommand;
import me.fabian.jda.commands.TestCommand;
import me.fabian.jda.commands.UserInfoCommand;
import me.fabian.jda.dbs.Variables;
import me.fabian.jda.managers.CommandClientBuilder;
import me.fabian.jda.utils.Helpers;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class YoshiBot {

    private final Config config;
    private final Variables variables;
    private final Helpers helpers;
    public static String PREFIX;


    private YoshiBot() throws LoginException {
        config = new Config();
        variables = new Variables();
        helpers = new Helpers(this);
        PREFIX = config.getValue("prefix");

        CommandClientBuilder client = new CommandClientBuilder(this)
                .setOwnerId(Long.parseLong(config.getValue("ownerId")))
                .addCommands(
                new TestCommand(),
                new PingCommand(),
                new UserInfoCommand());

        new JDABuilder(AccountType.BOT)
                .setAutoReconnect(true)
                .setGame(Game.playing(PREFIX + "help"))
                .setToken(config.getValue("token"))
                .addEventListener(client.build())
                .build();
    }


    public static void main(String[] args) {
        try {
            new YoshiBot();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public Variables getVariables() {
        return variables;
    }

    public Helpers getHelpers() {
        return helpers;
    }
}
