package nycuro.kits.data.clasic;

import cn.nukkit.Player;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import nycuro.api.API;
import nycuro.database.DatabaseMySQL;
import nycuro.database.objects.ProfileSkyblock;
import nycuro.kits.CommonKit;
import nycuro.kits.type.*;

/**
 * author: NycuRO
 * SkyblockCore Project
 * API 1.0.0
 */
public class KnightKit extends CommonKit {

    @Override
    public NameKit getKit() {
        return NameKit.KNIGHT;
    }

    @Override
    public TypeKit getType() {
        return TypeKit.CLASSIC;
    }

    @Override
    public double getPrice() {
        return 5000d;
    }

    @Override
    public StatusKit getStatus(Player player) {
        return StatusKit.UNLOCKED;
    }

    @Override
    public Item getHelmet() {
        Item item = Item.get(Item.DIAMOND_HELMET);
        item.setCustomName(API.getMainAPI().symbol + getKit().getName() + API.getMainAPI().empty + TypeClothes.HELMET.getType());
        return item;
    }

    @Override
    public Item getArmor() {
        Item item = Item.get(Item.DIAMOND_CHESTPLATE);
        item.setCustomName(API.getMainAPI().symbol + getKit().getName() + API.getMainAPI().empty + TypeClothes.ARMOR.getType());
        return item;
    }

    @Override
    public Item getPants() {
        Item item = Item.get(Item.DIAMOND_LEGGINGS);
        item.setCustomName(API.getMainAPI().symbol + getKit().getName() + API.getMainAPI().empty + TypeClothes.PANTS.getType());
        return item;
    }

    @Override
    public Item getBoots() {
        Item item = Item.get(Item.DIAMOND_BOOTS);
        item.setCustomName(API.getMainAPI().symbol + getKit().getName() + API.getMainAPI().empty + TypeClothes.BOOTS.getType());
        return item;
    }

    @Override
    public Item getSword() {
        Item item = Item.get(Item.STONE_SWORD);
        item.addEnchantment(Enchantment.get(Enchantment.ID_DAMAGE_ALL).setLevel(1));
        item.setCustomName(API.getMainAPI().symbol + getKit().getName() + API.getMainAPI().empty + TypeItems.SWORD.getType());
        return item;
    }

    @Override
    public Item getPickaxe() {
        Item item = Item.get(Item.STONE_PICKAXE);
        item.addEnchantment(Enchantment.get(Enchantment.ID_FORTUNE_DIGGING).setLevel(2));
        item.setCustomName(API.getMainAPI().symbol + getKit().getName() + API.getMainAPI().empty + TypeItems.PICKAXE.getType());
        return item;
    }

    @Override
    public Item getAxe() {
        Item item = Item.get(Item.STONE_AXE);
        item.addEnchantment(Enchantment.get(Enchantment.ID_FORTUNE_DIGGING).setLevel(2));
        item.setCustomName(API.getMainAPI().symbol + getKit().getName() + API.getMainAPI().empty + TypeItems.AXE.getType());
        return item;
    }

    @Override
    public Item getShovel() {
        Item item = Item.get(Item.STONE_SHOVEL);
        item.addEnchantment(Enchantment.get(Enchantment.ID_FORTUNE_DIGGING).setLevel(2));
        item.setCustomName(API.getMainAPI().symbol + getKit().getName() + API.getMainAPI().empty + TypeItems.SHOVEL.getType());
        return item;
    }

    @Override
    public Item[] getOtherItems() {
        Item obsidian = Item.get(Item.OBSIDIAN, 0, 32);
        Item tnt = Item.get(Item.TNT, 0, 16);
        Item bread = Item.get(Item.BREAD, 0, 32);
        bread.setCustomName(API.getMainAPI().symbol + getKit().getName() + API.getMainAPI().empty + "Bread");
        tnt.setCustomName(API.getMainAPI().symbol + getKit().getName() + API.getMainAPI().empty + "TNT");
        obsidian.setCustomName(API.getMainAPI().symbol + getKit().getName() + API.getMainAPI().empty + "Obsidian");
        return new Item[] {
                obsidian,
                tnt,
                bread
        };
    }

    @Override
    public Item[] getArmorContents() {
        return new Item[] {
                this.getHelmet(),
                this.getArmor(),
                this.getPants(),
                this.getBoots()

        };
    }

    @Override
    public Item[] getInventoryContents() {
        return new Item[] {
                this.getSword(),
                this.getPickaxe(),
                this.getAxe(),
                this.getShovel()

        };
    }

    @Override
    public boolean hasEnoughDollars(Player player) {
        ProfileSkyblock profileSkyblock = DatabaseMySQL.profileSkyblock.get(player.getName());
        double dollars = profileSkyblock.getDollars();
        return getPrice() < dollars;
    }

    @Override
    public boolean canAddKit(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        for (Item item : playerInventory.getArmorContents()) {
            if (item.getId() != 0) {
                return false;
            }
        }
        return (getArmorContents().length + getInventoryContents().length + getOtherItems().length) < 36 - playerInventory.getContents().size();
    }

    @Override
    public long getTimer() {
        return 1000 * 60 * 60 * 24;
    }

    @Override
    public boolean passTimer(Player player) {
        ProfileSkyblock profileSkyblock = DatabaseMySQL.profileSkyblock.get(player.getName());
        long time = profileSkyblock.getCooldown();
        return (getTimer() - (System.currentTimeMillis() - time)) <= 0;
    }

    @Override
    public void sendKit(Player player) {
        ProfileSkyblock profileSkyblock = DatabaseMySQL.profileSkyblock.get(player.getName());
        if (passTimer(player)) {
            if (canAddKit(player)) {
                if (hasEnoughDollars(player)) {
                    player.getInventory().setArmorContents(getArmorContents());
                    player.getInventory().addItem(getInventoryContents());
                    player.getInventory().addItem(getOtherItems());
                    profileSkyblock.setCooldown(System.currentTimeMillis());
                    profileSkyblock.setDollars(profileSkyblock.getDollars() - getPrice());
                    API.getMessageAPI().sendReceiveKitMessage(player, getKit());
                } else {
                    double dollars = profileSkyblock.getDollars();
                    API.getMessageAPI().sendUnsuficientMoneyMessage(player, getPrice() - dollars);
                }
            } else {
                API.getMessageAPI().sendFullInventoryMessage(player);
            }
        } else {
            long time = profileSkyblock.getCooldown();
            API.getMessageAPI().sendCooldownMessage(player, System.currentTimeMillis() - time, getTimer());
        }
    }
}