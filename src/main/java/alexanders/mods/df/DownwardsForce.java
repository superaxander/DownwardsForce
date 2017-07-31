package alexanders.mods.df;

import alexanders.mods.df.entity.EntityDrill;
import alexanders.mods.df.item.DrillBitItem;
import alexanders.mods.df.tile.TileDrill;
import de.ellpeck.rockbottom.api.GameContent;
import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.construction.BasicRecipe;
import de.ellpeck.rockbottom.api.construction.resource.ResInfo;
import de.ellpeck.rockbottom.api.construction.resource.ResUseInfo;
import de.ellpeck.rockbottom.api.construction.resource.ResourceRegistry;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.mod.IMod;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;

import static de.ellpeck.rockbottom.api.GameContent.*;
import static de.ellpeck.rockbottom.api.construction.resource.ResourceRegistry.*;

public class DownwardsForce implements IMod {
    public static DownwardsForce instance;

    public DownwardsForce() {
        instance = this;
    }

    @Override
    public String getDisplayName() {
        return "DownwardsForce";
    }

    @Override
    public String getId() {
        return "df";
    }

    @Override
    public String getVersion() {
        return "@VERSION@";
    }

    @Override
    public String getResourceLocation() {
        return "/assets/" + getId();
    }

    @Override
    public String getDescription() {
        return RockBottomAPI.getGame().getAssetManager().localize(RockBottomAPI.createRes(this, "desc.mod"));
    }

    @Override
    public void init(IGameInstance game, IApiHandler apiHandler, IEventHandler eventHandler) {
        IResourceName drill = RockBottomAPI.createRes(this, "drill"); // Only used for entity registration
        IResourceName drill_basic = RockBottomAPI.createRes(this, "drill_basic");
        IResourceName drill_copper = RockBottomAPI.createRes(this, "drill_copper");
        IResourceName drill_bit_basic = RockBottomAPI.createRes(this, "drill_bit_basic");
        IResourceName drill_bit_copper = RockBottomAPI.createRes(this, "drill_bit_copper");
        String dbb = "drillBitBasic";
        String dbc = "drillBitCopper";
        String rp = "pickaxeRock";
        String s = "smelter";
        ResourceRegistry.addResources(rp, new ResInfo(ITEM_ROCK_PICK));
        ResourceRegistry.addResources(s, new ResInfo(TILE_SMELTER));
        TileDrill drillBasic = new TileDrill(drill_basic, 30, .25f, .5f, 10);
        TileDrill drillCopper = new TileDrill(drill_copper, 42, .5f, .25f, 20);
        DrillBitItem drillBitBasic = new DrillBitItem(drill_bit_basic);
        DrillBitItem drillBitCopper = new DrillBitItem(drill_bit_copper);
        drillBasic.register();
        drillCopper.register();
        drillBitBasic.register().addResource(dbb);
        drillBitCopper.register().addResource(dbc);

        RockBottomAPI.ENTITY_REGISTRY.register(drill, EntityDrill.class);

        // Add construction recipes
        RockBottomAPI.MANUAL_CONSTRUCTION_RECIPES.add(new BasicRecipe(
                new ItemInstance(drillBitBasic),
                new ResUseInfo(rp, 5),
                new ResUseInfo(RAW_STONE, 20)));

        RockBottomAPI.MANUAL_CONSTRUCTION_RECIPES.add(new BasicRecipe(
                new ItemInstance(drillBasic),
                new ResUseInfo(dbb),
                new ResUseInfo(RAW_STONE, 45),
                new ResUseInfo(WOOD_BOARDS, 25),
                new ResUseInfo(s)));

        RockBottomAPI.MANUAL_CONSTRUCTION_RECIPES.add(new BasicRecipe(
                new ItemInstance(drillBitCopper),
                new ResUseInfo(PROCESSED_COPPER, 20),
                new ResUseInfo(RAW_STONE, 20)));

        RockBottomAPI.MANUAL_CONSTRUCTION_RECIPES.add(new BasicRecipe(
                new ItemInstance(drillCopper),
                new ResUseInfo(dbc),
                new ResUseInfo(RAW_STONE, 45),
                new ResUseInfo(PARTLY_PROCESSED_COPPER, 5),
                new ResUseInfo(WOOD_BOARDS, 25),
                new ResUseInfo(s)));
    }
}
