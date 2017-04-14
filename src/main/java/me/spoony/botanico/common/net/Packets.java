package me.spoony.botanico.common.net;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Created by Colten on 11/20/2016.
 */
public class Packets
{
    public static BiMap<Integer, Class<? extends Packet>> packets = HashBiMap.create();

    public static void init() {
        registerPacket(SPacketMessage.class);
        registerPacket(CPacketJoinRequest.class);
        registerPacket(SPacketNewEntity.class);
        registerPacket(SPacketPlayerEID.class);
        registerPacket(SPacketEntityMove.class);
        registerPacket(CPacketPlayerMove.class);
        registerPacket(SPacketChunk.class);
        registerPacket(SPacketBuildingChange.class);
        registerPacket(SPacketSlot.class);
        registerPacket(CPacketInventorySlotClick.class);
        registerPacket(CPacketInventoryButtonClick.class);
        registerPacket(CPacketBuildingInteraction.class);
        registerPacket(SPacketChangeDialog.class);
        registerPacket(CPacketChangeDialog.class);
        registerPacket(CPacketUseItem.class);
        registerPacket(SPacketTileChange.class);
        registerPacket(SPacketDialogData.class);
        registerPacket(SPacketRemoveEntity.class);
        registerPacket(SPacketHeartbeatMarco.class);
        registerPacket(CPacketHeartbeatPolo.class);
        registerPacket(SPacketBuildingData.class);
        registerPacket(SPacketEntityState.class);
        registerPacket(SPacketTeleport.class);
    }

    public static Packet getPacket(int id) {
        Preconditions.checkArgument(packets.containsKey(id), "Attempted to get instance of packet by unknown packet ID "+id);
        try
        {
            Class<? extends Packet> clazz = packets.get(id);
            Packet ret = clazz.newInstance();
            return ret;
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static int getID(Class<? extends Packet> clazz) {
        Preconditions.checkArgument(packets.inverse().containsKey(clazz), "Attempted to get ID of unregistered packet");
        return packets.inverse().containsKey(clazz) ? packets.inverse().get(clazz) : -1;
    }

    public static void registerPacket(Class<? extends Packet> clazz) {
        packets.put(packets.size(), clazz);
    }
}
