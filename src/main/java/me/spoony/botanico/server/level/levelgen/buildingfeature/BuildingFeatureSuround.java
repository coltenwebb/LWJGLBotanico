package me.spoony.botanico.server.level.levelgen.buildingfeature;

import com.google.common.collect.Range;
import me.spoony.botanico.common.buildings.Building;
import me.spoony.botanico.common.level.Chunk;
import me.spoony.botanico.common.tiles.Tile;

import java.util.Random;

/**
 * Created by Colten on 11/26/2016.
 */
public class BuildingFeatureSuround implements BuildingFeature {

  Building toSurround;
  float popularity;
  Building building;
  Tile allowedTile;
  int size;

  public BuildingFeatureSuround(Building toSurround, float popularity, Building building,
      Tile allowedTile, int size) {
    this.toSurround = toSurround;
    this.popularity = popularity;
    this.building = building;
    this.allowedTile = allowedTile;
    this.size = size;
  }

  @Override
  public void generate(Random random, long seed, boolean[] biome, Chunk chunk) {
    for (int xi = 0; xi < Chunk.CHUNK_SIZE; xi++) {
      for (int yi = 0; yi < Chunk.CHUNK_SIZE; yi++) {
        if (biome[xi*32+yi] == false) {
          continue;
        }

        float val = random.nextFloat();
        if (val < popularity && chunk.tiles[xi * 32 + yi] == allowedTile
            && chunk.buildings[xi * 32 + yi] == toSurround) {
          int chainlength = size;

          for (int i = 0; i < chainlength; i++) {
            int xmod = random.nextInt(3) - 1 + xi;
            int ymod = random.nextInt(3) - 1 + yi;

            if (!Range.closed(0, 31).contains(xmod) || !Range.closed(0, 31).contains(ymod)) {
              continue;
            }

            if ((chunk.tiles[xmod*32+ymod] == allowedTile || allowedTile == null) && (
                chunk.buildings[xmod*32+ymod] == null)) {
              chunk.buildings[xmod*32+ymod] = building;
            }
          }
        }
      }
    }
  }
}