package me.spoony.botanico.common.buildings;

import me.spoony.botanico.client.ClientPlane;
import me.spoony.botanico.client.engine.Color;
import me.spoony.botanico.client.graphics.RendererGame;
import me.spoony.botanico.common.buildings.buildingentity.BuildingEntity;
import me.spoony.botanico.common.buildings.buildingentity.BuildingEntityFluidPipe;
import me.spoony.botanico.common.level.IPlane;
import me.spoony.botanico.common.util.IntRectangle;
import me.spoony.botanico.common.util.position.OmniPosition;
import me.spoony.botanico.common.util.position.TileDirection;

/**
 * Created by Colten on 1/1/2017.
 */
public class BuildingFluidPipe extends Building implements IBuildingEntityHost {

  public BuildingFluidPipe(int id) {
    super(id);

    this.shouldCollide = true;
    this.name = "fluid_pipe";
    this.alwaysBehindCharacter = false;
    this.hardness = 1;
    this.collisionBounds.set(.2d, .2d, .6d, .6d);
  }

  @Override
  public void render(RendererGame rg, ClientPlane level, OmniPosition position, int extra,
      Color color) {
    boolean n =
        level.getBuilding(position.getTileNeighbor(TileDirection.NORTH)) == Buildings.FLUID_PIPE;
    boolean s =
        level.getBuilding(position.getTileNeighbor(TileDirection.SOUTH)) == Buildings.FLUID_PIPE;
    boolean e = level.getBuilding(position.getTileNeighbor(TileDirection.EAST)) == Buildings.FLUID_PIPE;
    boolean w = level.getBuilding(position.getTileNeighbor(TileDirection.WEST)) == Buildings.FLUID_PIPE;

    if (n && s && e && w) { // 1
      rg.sprite(position, getTextureSheet(), new IntRectangle(0, 0, 16, 16), color,
          position.getGameY());
      return;
    }

    // First try to render a left-right pipe
    if (e && w) { // 4
      rg.sprite(position, getTextureSheet(), new IntRectangle(0, 32, 16, 16), color,
          position.getGameY());//lr

      if (n) {
        rg.sprite(position, getTextureSheet(), new IntRectangle(32, 0, 16, 16), color,
            position.getGameY());//n
      }
      if (s) {
        rg.sprite(position, getTextureSheet(), new IntRectangle(16, 0, 16, 16), color,
            position.getGameY());//s
      }
      return;
    }

    if (n && s) { // 4
      rg.sprite(position, getTextureSheet(), new IntRectangle(16, 32, 16, 16), color,
          position.getGameY()); //ud

      if (e) {
        rg.sprite(position, getTextureSheet(), new IntRectangle(0, 16, 16, 16), color,
            position.getGameY());//s
      }

      if (w) {
        rg.sprite(position, getTextureSheet(), new IntRectangle(16, 16, 16, 16), color,
            position.getGameY());//s
      }
      return;
    }

    if (n && e) {
      rg.sprite(position, getTextureSheet(), new IntRectangle(48, 0, 16, 16), color,
          position.getGameY()); //ud
      return;
    }
    if (n && w) {
      rg.sprite(position, getTextureSheet(), new IntRectangle(48, 16, 16, 16), color,
          position.getGameY()); //ud
      return;
    }
    if (s && e) {
      rg.sprite(position, getTextureSheet(), new IntRectangle(48, 32, 16, 16), color,
          position.getGameY()); //ud
      return;
    }
    if (s && w) {
      rg.sprite(position, getTextureSheet(), new IntRectangle(48, 48, 16, 16), color,
          position.getGameY()); //ud
      return;
    }

    if (n || s) {
      rg.sprite(position, getTextureSheet(), new IntRectangle(16, 32, 16, 16), color,
          position.getGameY()); //ud
      return;
    }

    rg.sprite(position, getTextureSheet(), new IntRectangle(0, 32, 16, 16), color,
        position.getGameY()); //ud
  }

  @Override
  public BuildingEntity createNewEntity(IPlane plane, OmniPosition position) {
    return new BuildingEntityFluidPipe(position, plane);
  }
}
