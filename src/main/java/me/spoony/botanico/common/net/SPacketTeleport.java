package me.spoony.botanico.common.net;

import me.spoony.botanico.client.BotanicoClient;
import me.spoony.botanico.common.level.IPlane;

/**
 * Created by Colten on 1/1/2017.
 */
public class SPacketTeleport extends AutoPacketAdapter implements IClientHandler {
    public double x;
    public double y;
    public int plane;

    @Override
    public void onReceive(BotanicoClient client) {
        client.getLocalLevel().planeID = plane;
        System.out.println(x+", "+y);
        client.getLocalPlayer().position.set(x, y);

        client.gameView.forceCenterCameraOnPlayer();
    }
}
