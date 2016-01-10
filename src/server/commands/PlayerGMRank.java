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
public enum PlayerGMRank {

    NORMAL(new char[]{'/', '@'}, 0),
    INTERN(new char[]{'/', '!', '！'}, 1),
    GM(new char[]{'/', '!', '！'}, 2),
    SUPERGM(new char[]{'/', '!', '！'}, 3),
    ADMIN(new char[]{'/', '!', '！'}, 4);

    private final char[] commandPrefix;
    private final int level;

    private PlayerGMRank(char[] chs, int level) {
        commandPrefix = chs;
        this.level = level;
    }

    public char[] getCommandPrefix() {
        return commandPrefix;
    }

    public int getLevel() {
        return level;
    }

    public static PlayerGMRank getByLevel(int level) {
        for (PlayerGMRank i : PlayerGMRank.values()) {
            if (i.getLevel() == level) {
                return i;
            }
        }
        return PlayerGMRank.NORMAL;
    }
}
