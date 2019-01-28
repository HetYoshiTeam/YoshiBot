package me.fabian.jda.managers;

import java.util.List;

public interface CommandClient {

    void addCommand(Command command);
    void addCommand(Command command, int index);
    List<Command> getCommands();
    //void setListener(CommandListener listener);
    //CommandListener getListener();
}
