package me.spoony.botanico.common.buildings;

import me.spoony.botanico.client.engine.Color;
import me.spoony.botanico.client.graphics.RendererGame;
import me.spoony.botanico.client.ClientPlane;
import me.spoony.botanico.common.util.IntRectangle;
import me.spoony.botanico.common.util.position.OmniPosition;

/**
 * Created by Colten on 11/10/2016.
 */
public class BuildingFlower extends Building {

  public BuildingFlower(int id, String name) {
    super(id);
    this.name = name;
    shouldCollide = false;

    this.hardness = .1f;
  }

  @Override
  public void render(RendererGame rg, ClientPlane level, OmniPosition position, int d,
      Color color) {
    rg.sprite(position, getTextureSheet(),
        new IntRectangle(48 + d * 16, 192, 16, 16),
        color, position.getGameY());
  }

  @Override
  public BuildingBreakMaterial getBreakParticle() {
    return BuildingBreakMaterial.PLANT;
  }
}
