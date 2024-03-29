package me.spoony.botanico.client.graphics.dialog;

import me.spoony.botanico.client.engine.Color;
import me.spoony.botanico.client.graphics.CallAlign;
import me.spoony.botanico.client.graphics.TextColors;
import me.spoony.botanico.client.graphics.gui.RendererItemSlot;
import me.spoony.botanico.common.util.position.GuiRectangle;
import me.spoony.botanico.client.views.GameView;
import me.spoony.botanico.common.dialog.DialogInventoryPlayer;
import me.spoony.botanico.common.entities.EntityPlayer;
import me.spoony.botanico.client.graphics.RendererGUI;
import me.spoony.botanico.client.input.BinaryInput;
import me.spoony.botanico.common.items.Inventory;
import me.spoony.botanico.common.items.ItemSlotMode;
import me.spoony.botanico.common.util.IntRectangle;

/**
 * Created by Colten on 11/10/2016.
 */
public class DialogRendererInventoryPlayer extends DialogRendererAdapter<DialogInventoryPlayer> {

  protected Inventory craftingInventory;
  protected IntRectangle dialogTextureSource;

  @Override
  public void init(DialogInventoryPlayer dialog) {
    super.init(dialog);

    dialogTextureSource = new IntRectangle(0, 0, 231, 135);
    this.dialogBounds
        .set(new GuiRectangle(0, 0, dialogTextureSource.width, dialogTextureSource.height));

    Inventory inv = GameView.getClient().getLocalPlayer().inventory;
    this.initPlayerItemSlots(inv, 4, 25);

    // CRAFTING SLOTS
    craftingInventory = dialog.inventory;
    craftingInventory.getSlot(2).setMode(ItemSlotMode.TAKE_ONLY);

    for (int i = 0; i < 4; i++) {
      rendererItemSlots.add(new RendererItemSlot(craftingInventory.getSlot(i), 157 + i * 18, 97));
    }

    for (int i = 4; i < 16; i++) {
      rendererItemSlots.add(new RendererItemSlot(craftingInventory.getSlot(i), 157 + (i%4) * 18, 76-((i/4)-1)*18));
    }
  }

  @Override
  public void onBinaryInputPressed(BinaryInput bin) {
    super.onBinaryInputPressed(bin);
  }

  @Override
  public void update(float delta) {

  }

  @Override
  public void render(RendererGUI rg) {
    this.centerDialogBounds(rg.guiViewport);

    rg.sprite(dialogBounds.x, dialogBounds.y,
        rg.getResourceManager().getTexture("dialog/dialog_inventory.png"), dialogTextureSource);

    this.renderItemSlots(rg);

    rg.text(offsetXByDialogBounds(150 / 2), offsetYByDialogBounds(dialogTextureSource.height - 14),
        "Inventory",
        new TextColors(new Color(173 / 255f, 100 / 255f, 0 / 255f, 1)), CallAlign.BOTTOM_CENTER);
  }
}
