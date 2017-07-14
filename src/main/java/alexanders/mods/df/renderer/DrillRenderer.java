package alexanders.mods.df.renderer;

import alexanders.mods.df.entity.EntityDrill;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.render.entity.IEntityRenderer;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;
import de.ellpeck.rockbottom.api.world.IWorld;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class DrillRenderer implements IEntityRenderer<EntityDrill> {
    public final IResourceName texture;

    public DrillRenderer(IResourceName texture) {
        this.texture = texture;
    }

    @Override
    public void render(IGameInstance game, IAssetManager manager, Graphics g, IWorld world, EntityDrill entity, float x, float y, Color light) {
        manager.getTexture(texture).drawWithLight(x, y - 4, 36 / 12f, 48 / 12f, new Color[]{light, light, light, light});
    }
}
