package alexanders.mods.df.entity;

import alexanders.mods.df.renderer.DrillContainer;
import alexanders.mods.df.renderer.DrillGui;
import alexanders.mods.df.renderer.DrillRenderer;
import alexanders.mods.df.tile.TileEntityDrill;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.entity.Entity;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.render.entity.IEntityRenderer;
import de.ellpeck.rockbottom.api.tile.Tile;
import de.ellpeck.rockbottom.api.util.BoundBox;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;
import de.ellpeck.rockbottom.api.world.IWorld;
import de.ellpeck.rockbottom.api.world.TileLayer;
import org.newdawn.slick.util.Log;

import java.util.List;

import static de.ellpeck.rockbottom.api.RockBottomAPI.createRes;

public class EntityDrill extends Entity {
    private static BoundBox bb = new BoundBox(0, 0, 3, 4);
    public IResourceName name;
    private DrillRenderer renderer;
    private TileEntityDrill tileEntity;
    private float breakProgress;

    public EntityDrill(IWorld world, IResourceName name, int x, int y) {
        super(world);
        renderer = new DrillRenderer(name.addSuffix(".active"));
        this.name = name;
        tileEntity = (TileEntityDrill) world.getTileEntity(x + 1, y + 1);
        this.setPos(x, y);
        this.breakProgress = 0;
    }

    public EntityDrill(IWorld world) {
        super(world);
    }


    @Override
    public BoundBox getBoundingBox() {
        return bb;
    }

    @Override
    public void save(DataSet set) {
        super.save(set);
        set.addString("resourceName", renderer.texture.getDomain() + "/" + renderer.texture.getResourceName());
        DataSet te = new DataSet();
        tileEntity.save(set, false);
        set.addDataSet("te", te);
        set.addFloat("breakProgress", breakProgress);
    }

    @Override
    public void load(DataSet set) {
        super.load(set);
        renderer = new DrillRenderer(createRes(set.getString("resourceName")));
        tileEntity = new TileEntityDrill(world, 0, 0); // The location doesn't matter since we update when we stop moving
        tileEntity.load(set.getDataSet("te"), false);
        breakProgress = set.getFloat("breakProgress");
    }

    @Override
    public void update(IGameInstance game) {
        super.update(game);
        this.tileEntity.update(game);
        if (this.tileEntity.isActive()) {
            for (int x = 0; x < 3; x++) {
                int x2 = (int) (this.x + .5 * (this.x < 0 ? -1 : 1)) + x;
                int y2 = (int) Math.round(this.y) - 1;

                FakePlayer player = FakePlayer.getInstance(world);
                Tile tile = world.getTile(TileLayer.MAIN, x2, y2);
                if (tile.isAir())
                    continue;
                if ((breakProgress += tileEntity.tilesPerTick) >= 1f) {
                    breakProgress -= 1f;

                    if (canMineTile(y2, tile.getHardness(world, x2, y2, TileLayer.MAIN))) {
                        tile.doBreak(world, x2, y2, TileLayer.MAIN, player, false, false);
                        List<ItemInstance> drops = tile.getDrops(world, x2, y2, TileLayer.MAIN, player);
                        if (drops != null)
                            drops.forEach(drop -> {
                                if (drop != null) {
                                    if (tileEntity.fuelInventory.get(0) != null)
                                        if (drop.getItem() == tileEntity.fuelInventory.get(0).getItem())
                                            drop = tileEntity.fuelInventory.add(drop, true);
                                    if (drop != null)
                                        tileEntity.inventory.add(drop, false);
                                }
                            });
                    }
                }
            }
        }
    }

    private boolean canMineTile(int y, float hardness) {
        if (y > tileEntity.maxDepth / 2) {
            hardness += Math.pow(-y, .45) - tileEntity.maxHardness;
        } else {
            hardness += Math.pow(-y - tileEntity.maxDepth / 2, .54) - tileEntity.maxHardness;
        }
        return hardness < 0;
    }

    @Override
    public boolean onInteractWith(AbstractEntityPlayer player, double mouseX, double mouseY) {
        if (tileEntity != null) {
            player.openGuiContainer(new DrillGui(player, tileEntity), new DrillContainer(player, tileEntity, tileEntity.fuelInventory, tileEntity.inventory, player.getInv()));
            return true;
        } else {
            Log.error("The drill's tile entity was null! Please report");
            return false;
        }
    }

    @Override
    public IEntityRenderer getRenderer() {
        return renderer;
    }
}
