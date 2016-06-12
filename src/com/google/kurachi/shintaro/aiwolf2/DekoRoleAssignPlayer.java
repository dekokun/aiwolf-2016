/**
 *
 */
package com.google.kurachi.shintaro.aiwolf2;

import org.aiwolf.client.base.player.AbstractRoleAssignPlayer;

/**
 * @author dekokun
 *
 */
public class DekoRoleAssignPlayer extends AbstractRoleAssignPlayer {

    /* (non-Javadoc)
     * @see org.aiwolf.client.base.player.AbstractRoleAssignPlayer#getName()
     */
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public DekoRoleAssignPlayer() {
        setSeerPlayer(new DekoSeer());
        setPossessedPlayer(new DekoPossessed());
        setVillagerPlayer(new DekoVillager());
    }
}
