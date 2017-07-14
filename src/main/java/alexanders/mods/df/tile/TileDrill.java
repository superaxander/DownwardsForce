package alexanders.mods.df.tile;

import alexanders.mods.df.DownwardsForce;
import alexanders.mods.df.renderer.DrillContainer;
import alexanders.mods.df.renderer.DrillGui;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.tile.MultiTile;
import de.ellpeck.rockbottom.api.tile.entity.TileEntity;
import de.ellpeck.rockbottom.api.util.Pos2;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;
import de.ellpeck.rockbottom.api.world.IWorld;

import java.util.List;

public class TileDrill extends MultiTile {
    private static final IResourceName DESC1_RESOURCE = RockBottomAPI.createRes(DownwardsForce.instance, "desc.drill1");
    private static final IResourceName DESC2_RESOURCE = RockBottomAPI.createRes(DownwardsForce.instance, "desc.drill2");
    private static final IResourceName DESC3_RESOURCE = RockBottomAPI.createRes(DownwardsForce.instance, "desc.drill3");
    private static final IResourceName DESC4_RESOURCE = RockBottomAPI.createRes(DownwardsForce.instance, "desc.drill4");
    
    private final int maxHardness;
    private final int maxDepth;
    private final int inventorySize;
    private final float tilesPerTick;
    private final float fuelModifier;

    public TileDrill(IResourceName name, int maxHardness, int maxDepth, float tilesPerTick, float fuelModifier, int inventorySize) {
        super(name);
        this.maxHardness = maxHardness;
        this.maxDepth = maxDepth;
        this.inventorySize = inventorySize;
        this.tilesPerTick = tilesPerTick;
        this.fuelModifier = fuelModifier;
        this.setForceDrop();
    }

    @Override
    public void describeItem(IAssetManager manager, ItemInstance instance, List<String> desc, boolean isAdvanced) {
        super.describeItem(manager, instance, desc, isAdvanced);
        desc.add(manager.localize(DESC1_RESOURCE, maxHardness));
        desc.add(manager.localize(DESC2_RESOURCE, inventorySize));
        desc.add(manager.localize(DESC3_RESOURCE, tilesPerTick));
        desc.add(manager.localize(DESC4_RESOURCE, fuelModifier));
    }

    @Override
    public boolean onInteractWith(IWorld world, int x, int y, double mouseX, double mouseY, AbstractEntityPlayer player) {
        Pos2 main = this.getMainPos(x, y, world.getMeta(x, y));
        TileEntityDrill tile = world.getTileEntity(main.getX(), main.getY(), TileEntityDrill.class);
        if (tile != null) {
            player.openGuiContainer(new DrillGui(player, tile), new DrillContainer(player, tile, tile.fuelInventory, tile.inventory, player.getInv()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canProvideTileEntity() {
        return true;
    }

    @Override
    public TileEntity provideTileEntity(IWorld world, int x, int y) {
        return isMainPos(x, y, world.getMeta(x, y)) ? new TileEntityDrill(world, x, y, maxHardness, maxDepth, tilesPerTick, fuelModifier, inventorySize) : null;
    }

    @Override
    protected boolean[][] makeStructure() {
        return new boolean[][]{
                {true, true, true},
                {true, true, true},
                {true, true, true},
                {false, true, false}
        };
    }

    @Override
    public boolean isFullTile() {
        return false;
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 4;
    }

    @Override
    public int getMainX() {
        return 1;
    }

    @Override
    public int getMainY() {
        return 1;
    }
}
