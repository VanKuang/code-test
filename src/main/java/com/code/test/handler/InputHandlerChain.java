package com.code.test.handler;

import java.util.Arrays;
import java.util.List;

public class InputHandlerChain implements InputHandler {

    private final List<InputHandler> handlerList;

    public InputHandlerChain(final InputHandler... handlers) {
        this.handlerList = Arrays.asList(handlers);
    }

    @Override
    public void process(final String input) {
        for (final InputHandler inputHandler : handlerList) {
            inputHandler.process(input);
        }
    }
}
