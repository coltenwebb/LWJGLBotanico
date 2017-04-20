package me.spoony.botanico.common.net.server;

import com.google.gson.Gson;
import me.spoony.botanico.client.BotanicoClient;
import me.spoony.botanico.client.views.GameView;
import me.spoony.botanico.common.dialog.Dialog;
import me.spoony.botanico.common.dialog.DialogKnappingStation;
import me.spoony.botanico.common.net.AutoPacketAdapter;
import me.spoony.botanico.common.net.IClientHandler;
import me.spoony.botanico.common.net.IPacketInterpreter;
import me.spoony.botanico.common.net.Packet;
import me.spoony.botanico.common.net.PacketDecoder;
import me.spoony.botanico.common.net.PacketEncoder;

/**
 * Created by Colten on 12/2/2016.
 */
public class SPacketDialogData extends AutoPacketAdapter implements IClientHandler {

  private static Gson gson = new Gson();
  public String dialogData;

  @Override
  public void onReceive(BotanicoClient client) {
    Dialog dialog = GameView.get().getDialog();
    if (dialog instanceof IPacketInterpreter) {
      ((IPacketInterpreter) dialog).fromJson(gson, dialogData);
    }
  }
}