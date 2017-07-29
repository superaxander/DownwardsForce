package alexanders.mods.df.renderer;

import alexanders.mods.df.DownwardsForce;
import alexanders.mods.df.tile.TileEntityDrill;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.container.ItemContainer;
import de.ellpeck.rockbottom.api.inventory.IInventory;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;

import static de.ellpeck.rockbottom.api.RockBottomAPI.createRes;
import static de.ellpeck.rockbottom.api.RockBottomAPI.getFuelValue;

public class DrillContainer extends ItemContainer {
    public DrillContainer(AbstractEntityPlayer player, TileEntityDrill tile, IInventory... containedInventories) {
        super(player, containedInventories);
        this.addSlot(new RestrictedSlot(tile.fuelInventory, 0, 90, 0, (instance) -> getFuelValue(instance) > 0));
        this.addSlotGrid(tile.inventory, 0, tile.inventorySize, 0, 30, 11); // @BUG: Logically this should be 10
        this.addPlayerInventory(player, 20, 80);
    }

    @Override
    public IResourceName getName() {
        return createRes(DownwardsForce.instance, "drill_container");
    }
}
