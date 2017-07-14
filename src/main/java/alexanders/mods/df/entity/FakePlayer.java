package alexanders.mods.df.entity;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.Gui;
import de.ellpeck.rockbottom.api.gui.container.ItemContainer;
import de.ellpeck.rockbottom.api.inventory.IInventory;
import de.ellpeck.rockbottom.api.inventory.Inventory;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.net.chat.IChatLog;
import de.ellpeck.rockbottom.api.net.packet.IPacket;
import de.ellpeck.rockbottom.api.render.IPlayerDesign;
import de.ellpeck.rockbottom.api.world.IChunk;
import de.ellpeck.rockbottom.api.world.IWorld;
import org.newdawn.slick.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakePlayer extends AbstractEntityPlayer {
    public static Map<IWorld, FakePlayer> instanceMap = new HashMap<>(1);


    public static FakePlayer getInstance(IWorld world) {
        if (instanceMap.containsKey(world))
            return instanceMap.get(world);
        else
            return instanceMap.put(world, new FakePlayer(world));
    }

    private FakePlayer(IWorld world) {
        super(world);
    }

    @Override
    public void openGuiContainer(Gui gui, ItemContainer container) {

    }

    @Override
    public void openContainer(ItemContainer container) {

    }

    @Override
    public void closeContainer() {

    }

    @Override
    public ItemContainer getContainer() {
        return null;
    }

    @Override
    public void resetAndSpawn(IGameInstance game) {

    }

    @Override
    public void sendPacket(IPacket packet) {

    }

    @Override
    public boolean move(int type) {
        return false;
    }

    @Override
    public void onChunkLoaded(IChunk chunk) {

    }

    @Override
    public void onChunkUnloaded(IChunk chunk) {

    }

    @Override
    public List<IChunk> getChunksInRange() {
        return null;
    }

    @Override
    public int getCommandLevel() {
        return 0;
    }

    @Override
    public ItemContainer getInvContainer() {
        return null;
    }

    @Override
    public Inventory getInv() {
        return null;
    }

    @Override
    public int getSelectedSlot() {
        return 0;
    }

    @Override
    public void setSelectedSlot(int slot) {

    }

    @Override
    public String getChatColorFormat() {
        return null;
    }

    @Override
    public void sendMessageTo(IChatLog chat, String message) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public IPlayerDesign getDesign() {
        return null;
    }

    @Override
    public int getMaxHealth() {
        return 0;
    }

    @Override
    public int getRegenRate() {
        return 0;
    }

    @Override
    public void onChange(IInventory inv, int slot, ItemInstance newInstance) {

    }
}
