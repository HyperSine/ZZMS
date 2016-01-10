/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.commands;

/**
 *
 * @author Pungin
 */
public enum CommandType {

    NORMAL(0),
    TRADE(1);
    private final int level;

    CommandType(int level) {
        this.level = level;
    }

    public int getType() {
        return level;
    }
}
