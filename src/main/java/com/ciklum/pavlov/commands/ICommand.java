package com.ciklum.pavlov.commands;

import lombok.Data;

/**
 * The Command interface is a design pattern Command
 */
public interface ICommand {
    /**
     * empty
     *
     * @return
     */
    boolean execute();
}
