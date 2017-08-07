package alexanders.mods.df.tile;

import alexanders.mods.df.entity.EntityDrill;
import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.inventory.IInventory;
import de.ellpeck.rockbottom.api.inventory.Inventory;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.tile.Tile;
import de.ellpeck.rockbottom.api.tile.entity.IInventoryHolder;
import de.ellpeck.rockbottom.api.tile.entity.TileEntityFueled;
import de.ellpeck.rockbottom.api.util.Direction;
import de.ellpeck.rockbottom.api.world.IWorld;
import de.ellpeck.rockbottom.api.world.TileLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.ellpeck.rockbottom.api.RockBottomAPI.TILE_REGISTRY;
import static java.util.stream.IntStream.range;

public class TileEntityDrill extends TileEntityFueled implements IInventoryHolder {

    public int inventorySize;
    public Inventory inventory;
    public Inventory fuelInventory; //TODO: make this only accept fuel items
    public int maxHardness;
    public float tilesPerTick;
    public boolean isContained = false;
    private float fuelModifier;
    public EntityDrill entity = null;

    public TileEntityDrill(IWorld world, int x, int y, int maxHardness, float tilesPerTick, float fuelModifier, int inventorySize) {
        super(world, x, y);
        this.maxHardness = maxHardness;
        this.inventorySize = inventorySize;
        this.tilesPerTick = tilesPerTick;
        this.fuelModifier = fuelModifier;
        this.fuelInventory = new Inventory(1);
        this.inventory = new Inventory(this.inventorySize);
    }

    private TileEntityDrill(int x, int y, TileEntityDrill old) {
        super(old.world, x, y);
        this.maxHardness = old.maxHardness;
        this.inventorySize = old.inventorySize;
        this.fuelInventory = old.fuelInventory;
        this.inventory = old.inventory;
        this.entity = old.entity;
        this.fuelModifier = old.fuelModifier;
        this.tilesPerTick = old.tilesPerTick;
    }

    public TileEntityDrill(IWorld world, int x, int y) {
        super(world, x, y);
        this.fuelInventory = new Inventory(1);
    }

    @Override
    public void load(DataSet set, boolean forSync) {
        super.load(set, forSync);
        maxHardness = set.getInt("maxHardness");
        inventorySize = set.getInt("inventorySize");
        fuelInventory.load(set.getDataSet("fuelInv"));
        inventory = new Inventory(inventorySize);
        inventory.load(set.getDataSet("inv"));
        tilesPerTick = set.getFloat("tilesPerTick");
        fuelModifier = set.getFloat("fuelModifier");
    }

    @Override
    public void save(DataSet set, boolean forSync) {
        super.save(set, forSync);
        if (!forSync)
            System.out.println("Saving");
        set.addInt("maxHardness", maxHardness);
        set.addInt("inventorySize", inventorySize);
        DataSet fuelInv = new DataSet();
        fuelInventory.save(fuelInv);
        set.addDataSet("fuelInv", fuelInv);
        DataSet inv = new DataSet();
        inventory.save(inv);
        set.addDataSet("inv", inv);
        set.addFloat("tilesPerTick", tilesPerTick);
        set.addFloat("fuelModifier", fuelModifier);
    }

    @Override
    protected boolean tryTickAction() {
        return true;
    }

    @Override
    protected float getFuelModifier() {
        return fuelModifier;
    } //TODO: Maybe make this even take into account depth?

    @Override
    protected ItemInstance getFuel() {
        return fuelInventory.get(0);
    }

    @Override
    protected void removeFuel() {
        fuelInventory.remove(0, 1);
    }

    @Override
    protected void onActiveChange(boolean active) {
        if (active) {
            if (!isContained) {
                // Start moving
                Tile ourTile = world.getState(x, y).getTile();
                world.addEntity(entity = new EntityDrill(world, ourTile.getName(), x - 1, y));
                ourTile.doBreak(world, x, y, TileLayer.MAIN, null, false, false);
            }
        } else {
            // Stop moving
            if (isContained) {
                int newX = (int) (entity.x + (entity.x < 0 ? .5f : 1.5f));
                int newY = (int) Math.round(entity.y);
                System.out.println(entity);
                System.out.println(entity.name);
                TILE_REGISTRY.get(entity.name).doPlace(world, newX, newY, TileLayer.MAIN, null, null);
                entity.kill();
                world.removeTileEntity(newX, newY);
                world.addTileEntity(new TileEntityDrill(newX, newY, this));
            }
        }
    }

    @Override
    public IInventory getInventory() {
        return new CombinedInventory(fuelInventory, inventory);
    }

    @Override
    public List<Integer> getInputSlots(ItemInstance instance, Direction dir) {
        return Collections.singletonList(0);
    }

    @Override
    public List<Integer> getOutputSlots(Direction dir) {
        List<Integer> integers = new ArrayList<>(inventorySize - 1);
        range(1, inventorySize).forEach(integers::add);
        return integers;
    }
}
