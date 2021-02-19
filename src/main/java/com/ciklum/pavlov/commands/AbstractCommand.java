package com.ciklum.pavlov.commands;

import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.util.io.CustomReader;
import com.ciklum.pavlov.util.io.CustomWriter;

import java.util.ResourceBundle;

public abstract class AbstractCommand implements ICommand {
    protected ApplicationContext context;
    protected CustomReader reader;
    protected CustomWriter writer;

    public AbstractCommand(ApplicationContext context) {
        this.context = context;
        this.reader = context.getReader();
        this.writer = context.getWriter();
    }

    protected ResourceBundle getResourceBundle() {
        return context.getResourceBundle();
    }

}
