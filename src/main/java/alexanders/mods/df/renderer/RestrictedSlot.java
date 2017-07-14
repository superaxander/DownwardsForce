package alexanders.mods.df.renderer;

import de.ellpeck.rockbottom.api.gui.container.ContainerSlot;
import de.ellpeck.rockbottom.api.inventory.IInventory;
import de.ellpeck.rockbottom.api.item.ItemInstance;

import java.util.function.Predicate;

public class RestrictedSlot extends ContainerSlot {
    private final Predicate<ItemInstance> predicate;

    public RestrictedSlot(IInventory inventory, int slot, int x, int y, Predicate<ItemInstance> predicate) {
        super(inventory, slot, x, y);
        this.predicate = predicate;
    }

    @Override
    public boolean canPlace(ItemInstance instance) {
        return predicate.test(instance);
    }
}
