package me.spoony.botanico.common.net.server;

import me.spoony.botanico.client.BotanicoClient;
import me.spoony.botanico.common.entities.Entity;
import me.spoony.botanico.common.net.AutoPacketAdapter;
import me.spoony.botanico.common.net.IClientHandler;

/**
 * Created by Colten on 11/20/2016.
 */
public class SPacketEntityMove extends AutoPacketAdapter implements IClientHandler
{
    public int eid;
    public double x;
    public double y;

    @Override
    public void onReceive(BotanicoClient client) {
        for (Entity e : client.getLocalLevel().getEntities()) {
            if (e.eid == eid) {
                e.posX = x;
                e.posY = y;

                if (e == client.getLocalPlayer()) {
                    client.gameView.forceCenterCameraOnPlayer();
                }
                return;
            }
        }
        System.out.println("[ClientPacketHandler] Received entity move with unknown EID: "+eid);
    }
}
