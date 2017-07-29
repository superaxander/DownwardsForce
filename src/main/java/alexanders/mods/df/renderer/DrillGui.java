package alexanders.mods.df.renderer;

import alexanders.mods.df.DownwardsForce;
import alexanders.mods.df.tile.TileEntityDrill;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.GuiContainer;
import de.ellpeck.rockbottom.api.gui.component.ComponentProgressBar;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;

import static de.ellpeck.rockbottom.api.RockBottomAPI.createRes;

public class DrillGui extends GuiContainer {
    public final TileEntityDrill tile;

    public DrillGui(AbstractEntityPlayer player, TileEntityDrill tile) {
        super(player, 198, 180);
        this.tile = tile;
    }

    @Override
    public void initGui(IGameInstance game) {
        super.initGui(game);
        this.components.add(new ComponentProgressBar(this, this.guiLeft + 74, this.guiTop, 8, 18, FIRE_COLOR, true, tile::getFuelPercentage));
    }

    @Override
    public IResourceName getName() {
        return createRes(DownwardsForce.instance, "drill_gui");
    }
}
