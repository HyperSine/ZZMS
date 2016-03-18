/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extensions.temporary;

/**
 *
 * @author Itzik
 */
public enum NPCOption {

    NO_ESC(0x1),
    PLAYER(0x2),
    ANOTHER_NPC(0x4);
    private final int type;

    private NPCOption(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
