package com.servlet.ejournal.controller.commands;

import com.servlet.ejournal.controller.commands.impl.ChangeLocaleCommand;
import com.servlet.ejournal.exceptions.NoSuchCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.servlet.ejournal.constants.CommandNameConstants.*;

class TestCommandPool {
    @Test
    void testGetActualCommand() {
        assertDoesNotThrow(() -> CommandPool.getCommand(CHANGE_LOCALE_COMMAND));
        assertEquals(ChangeLocaleCommand.class, CommandPool.getCommand(CHANGE_LOCALE_COMMAND).getClass());
    }

    @Test
    void testNoSuchCommandException() {
        assertThrows(NoSuchCommandException.class, () -> CommandPool.getCommand("undefined command"));
    }
}
