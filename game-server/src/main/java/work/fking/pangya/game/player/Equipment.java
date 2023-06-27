package work.fking.pangya.game.player;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.model.IffObject;

import static work.fking.pangya.game.model.IffObject.TYPE_BALL;
import static work.fking.pangya.game.model.IffObject.TYPE_CLUBSET;

public class Equipment {

    private static final int EQUIPPED_ITEMS_SIZE = 10;

    private final int[] equippedItemIffIds = new int[EQUIPPED_ITEMS_SIZE];
    private int equippedClubSetUid;
    private int equippedCometIffId;
    private int equippedCharacterUid;
    private int equippedCaddieUid;

    private final Player player;

    public Equipment(Player player) {
        this.player = player;
    }

    public int equippedCharacterUid() {
        return equippedCharacterUid;
    }

    public int equippedCaddieUid() {
        return equippedCaddieUid;
    }

    public void updateEquippedItems(int[] itemIffIds) {
        if (itemIffIds.length != EQUIPPED_ITEMS_SIZE) {
            throw new IllegalArgumentException("Equipped item iff ids invalid length");
        }
        for (int i = 0; i < itemIffIds.length; i++) {
            if (IffObject.iffTypeFromId(itemIffIds[i]) != IffObject.TYPE_ITEM) {
                throw new IllegalArgumentException("Iff object " + itemIffIds[i] + " is not an item");
            }
            equippedItemIffIds[i] = itemIffIds[i];
        }
    }

    public void equipClubSet(Item clubSet) {
        if (clubSet.iffTypeId() != TYPE_CLUBSET) {
            throw new IllegalArgumentException("Item is not a clubSet");
        }
        // sanity check to see if we actually own the item
        if (player.inventory().existsByUid(clubSet.uid())) {
            equippedClubSetUid = clubSet.uid();
        }
    }

    public void equipComet(Item comet) {
        if (comet.iffTypeId() != TYPE_BALL) {
            throw new IllegalArgumentException("Item is not a comet");
        }
        // sanity check to see if we actually own the item
        if (player.inventory().existsByUid(comet.uid())) {
            equippedCometIffId = comet.iffId();
        }
    }

    public void equipCharacter(Character character) {
        equippedCharacterUid = character.uid();
    }

    public void equipCaddie(Caddie caddie) {
        equippedCaddieUid = caddie.uid();
    }

    public void encode(ByteBuf buffer) {
        buffer.writeIntLE(equippedCaddieUid);
        buffer.writeIntLE(equippedCharacterUid);
        buffer.writeIntLE(equippedClubSetUid);
        buffer.writeIntLE(equippedCometIffId);

        for (var itemIffId : equippedItemIffIds) {
            buffer.writeIntLE(itemIffId);
        }
        buffer.writeIntLE(0); // background
        buffer.writeIntLE(0); // frame
        buffer.writeIntLE(0); // sticker
        buffer.writeIntLE(0); // slot
        buffer.writeIntLE(0);
        buffer.writeIntLE(0); // title
        buffer.writeIntLE(0); // skinBg
        buffer.writeIntLE(0); // skinFrame
        buffer.writeIntLE(0); // skinSticker
        buffer.writeIntLE(0); // skinSlot
        buffer.writeIntLE(0);
        buffer.writeIntLE(0); // title
        buffer.writeIntLE(0); // mascot
        buffer.writeIntLE(0); // poster1
        buffer.writeIntLE(0); // poster2
    }
}
