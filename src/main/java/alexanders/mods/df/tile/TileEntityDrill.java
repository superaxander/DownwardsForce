package alexanders.mods.df.tile;

import alexanders.mods.df.entity.EntityDrill;
import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.inventory.Inventory;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.tile.Tile;
import de.ellpeck.rockbottom.api.tile.entity.TileEntityFueled;
import de.ellpeck.rockbottom.api.world.IWorld;
import de.ellpeck.rockbottom.api.world.TileLayer;

import static de.ellpeck.rockbottom.api.RockBottomAPI.TILE_REGISTRY;

public class TileEntityDrill extends TileEntityFueled {

    public int inventorySize;
    public Inventory inventory;
    public Inventory fuelInventory;
    public int maxHardness;
    public float tilesPerTick;
    private float fuelModifier;
    private boolean isMoving = false; // TODO: Is this variable even needed because isActive basically does the same thing
    private EntityDrill entity = null;

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
        this.isMoving = old.isMoving;
        this.entity = old.entity;
        this.fuelModifier = old.fuelModifier;
        this.tilesPerTick = old.tilesPerTick;
    }

    public TileEntityDrill(IWorld world, int x, int y) {
        super(world, x, y);
    }

    @Override
    public void load(DataSet set, boolean forSync) {
        super.load(set, forSync);
        maxHardness = set.getInt("maxHardness");
        inventorySize = set.getInt("inventorySize");
        fuelInventory.load(set.getDataSet("fuelInv"));
        inventory = new Inventory(inventorySize);
        inventory.load(set.getDataSet("inv"));
        isMoving = set.getBoolean("isMoving");
        tilesPerTick = set.getFloat("tilesPerTick");
        fuelModifier = set.getFloat("fuelModifier");
    }

    @Override
    public void save(DataSet set, boolean forSync) {
        super.save(set, forSync);
        set.addInt("maxHardness", maxHardness);
        set.addInt("inventorySize", inventorySize);
        DataSet fuelInv = new DataSet();
        fuelInventory.save(fuelInv);
        set.addDataSet("fuelInv", fuelInv);
        DataSet inv = new DataSet();
        inventory.save(inv);
        set.addDataSet("inv", inv);
        set.addBoolean("isMoving", isMoving);
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
            if (!this.isMoving) {
                // Start moving
                Tile ourTile = world.getTile(x, y);
                this.isMoving = true;
                world.addEntity(entity = new EntityDrill(world, ourTile.getName(), x - 1, y - 1));
                ourTile.doBreak(world, x, y, TileLayer.MAIN, null, false, false);
            }
        } else if (this.isMoving) {
            // Stop moving
            int newX = (int) (entity.x + (entity.x < 0 ? .5f : 1.5f));
            int newY = (int) Math.round(entity.y) + 1;
            TILE_REGISTRY.get(entity.name).doPlace(world, newX, newY, TileLayer.MAIN, null, null);
            entity.kill();
            world.removeTileEntity(newX, newY);
            this.isMoving = false;
            world.addTileEntity(new TileEntityDrill(newX, newY, this));
        }
    }
}
