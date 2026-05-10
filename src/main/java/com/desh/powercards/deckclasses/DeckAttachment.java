package com.desh.powercards.deckclasses;

import com.desh.powercards.PowerCards;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class DeckAttachment {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PowerCards.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerDeckData>> DECK_DATA =
            ATTACHMENT_TYPES.register("deck_data", () -> AttachmentType
                    .builder(PlayerDeckData::new)
                    .serialize(new IAttachmentSerializer<CompoundTag, PlayerDeckData>() {
                        @Override
                        public CompoundTag write(PlayerDeckData data, HolderLookup.Provider provider) {
                            return data.serializeNBT(provider);
                        }

                        @Override
                        public PlayerDeckData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
                            PlayerDeckData data = new PlayerDeckData();
                            data.deserializeNBT(provider, tag);
                            return data;
                        }
                    })
                    .build());
}
