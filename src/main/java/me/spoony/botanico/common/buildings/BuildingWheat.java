package me.spoony.botanico.common.buildings;

import me.spoony.botanico.client.ClientPlane;
import me.spoony.botanico.client.engine.Color;
import me.spoony.botanico.client.graphics.RendererGame;
import me.spoony.botanico.common.buildings.buildingentity.BuildingEntity;
import me.spoony.botanico.common.buildings.buildingentity.BuildingEntityCrop;
import me.spoony.botanico.common.items.ItemStack;
import me.spoony.botanico.common.items.Items;
import me.spoony.botanico.common.level.IPlane;
import me.spoony.botanico.common.tiles.Tiles;
import me.spoony.botanico.common.util.IntRectangle;
import me.spoony.botanico.common.util.position.OmniPosition;
import me.spoony.botanico.server.level.ServerPlane;

import java.util.Random;

/**
 * Created by Colten on 12/4/2016.
 */
public class BuildingWheat extends Building implements IBuildingEntityHost {

  public BuildingWheat(int id) {
    super(id);
    this.name = "wheat";
    shouldCollide = false;
    this.hardness = .1f;
  }

  @Override
  public boolean canCreate(IPlane level, OmniPosition position) {
    return super.canCreate(level, position) && level.getTile(position) == Tiles.FERTILIZED_GROUND;
  }

  @Override
  public void create(IPlane level, OmniPosition position) {
    if (!(level instanceof ServerPlane)) {
      return;
    }
    ServerPlane serverLevel = (ServerPlane) level;

    serverLevel.setBuildingData(position, (byte) 0);
  }

  @Override
  public void render(RendererGame rg, ClientPlane level, OmniPosition position, int d,
      Color color) {
    rg.sprite(position, getTextureSheet(),
        new IntRectangle(160 + d * 16, 128, 16, 32), color,
        position.getGameY());
  }

  @Override
  public ItemStack[] getDrops(IPlane level, OmniPosition position) {
    if (!(level instanceof ServerPlane)) {
      return new ItemStack[]{new ItemStack(Items.WHEAT_SEEDS, 1)};
    }
    ServerPlane serverLevel = (ServerPlane) level;

    BuildingEntityCrop bec = (BuildingEntityCrop) serverLevel.getBuildingEntity(position);
    if (bec.isMature()) {
      return new ItemStack[]{new ItemStack(Items.WHEAT, new Random().nextInt(3) + 1)};
    } else {
      return new ItemStack[]{new ItemStack(Items.WHEAT_SEEDS, 1)};
    }
  }

  @Override
  public BuildingBreakMaterial getBreakParticle() {
    return BuildingBreakMaterial.PLANT;
  }

  @Override
  public BuildingEntity createNewEntity(IPlane plane, OmniPosition position) {
    return new BuildingEntityCrop(position, plane, (byte) 5);
  }
}
