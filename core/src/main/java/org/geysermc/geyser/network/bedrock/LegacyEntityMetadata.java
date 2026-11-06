/*
 * Copyright (c) 2019-2026 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.geysermc.geyser.network.bedrock;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.GeyserCodecMetadataAccess;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.AddItemEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityDataPacket;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Old codecs reject fields they do not recognize. Filter only the outbound copy so a
 * session can keep using the complete Java entity state for simulation and updates.
 */
final class LegacyEntityMetadata {
    private final EntityDataTypeMap dataTypes;
    private final EnumSet<EntityFlag> supportedFlags = EnumSet.noneOf(EntityFlag.class);

    private LegacyEntityMetadata(BedrockCodec codec) {
        dataTypes = GeyserCodecMetadataAccess.entityDataTypes(codec);
        var transformer = dataTypes.fromType(EntityDataTypes.FLAGS).getTransformer();
        BedrockCodecHelper helper = codec.createHelper();
        EntityDataMap metadata = new EntityDataMap();
        EnumMap<EntityFlag, Boolean> singleFlag = new EnumMap<>(EntityFlag.class);
        for (EntityFlag flag : EntityFlag.values()) {
            singleFlag.put(flag, true);
            try {
                // Cloudburst rejects a flag that is absent from the codec's TypeMap.
                transformer.serialize(helper, metadata, singleFlag);
                supportedFlags.add(flag);
            } catch (IllegalArgumentException ignored) {
            }
            singleFlag.clear();
        }
    }

    static BedrockCodec apply(BedrockCodec codec) {
        var filter = new LegacyEntityMetadata(codec);
        var builder = codec.toBuilder();
        filter.wrap(codec, builder, AddEntityPacket.class, AddEntityPacket::getMetadata, AddEntityPacket::clone, AddEntityPacket::setMetadata);
        filter.wrap(codec, builder, AddPlayerPacket.class, AddPlayerPacket::getMetadata, AddPlayerPacket::clone, AddPlayerPacket::setMetadata);
        filter.wrap(codec, builder, AddItemEntityPacket.class, AddItemEntityPacket::getMetadata, AddItemEntityPacket::clone, AddItemEntityPacket::setMetadata);
        filter.wrap(codec, builder, SetEntityDataPacket.class, SetEntityDataPacket::getMetadata, SetEntityDataPacket::clone, SetEntityDataPacket::setMetadata);
        return builder.build();
    }

    private <T extends BedrockPacket> void wrap(BedrockCodec codec, BedrockCodec.Builder builder, Class<T> type,
                                                Function<T, EntityDataMap> metadata, Function<T, T> copy,
                                                BiConsumer<T, EntityDataMap> replace) {
        BedrockPacketSerializer<T> serializer = codec.getPacketDefinition(type).getSerializer();
        builder.updateSerializer(type, new BedrockPacketSerializer<>() {
            @Override
            public void serialize(ByteBuf buffer, BedrockCodecHelper helper, T packet) {
                EntityDataMap original = metadata.apply(packet);
                EntityDataMap filtered = filter(original);
                if (filtered != original) {
                    packet = copy.apply(packet);
                    replace.accept(packet, filtered);
                }
                serializer.serialize(buffer, helper, packet);
            }

            @Override
            public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, T packet) {
                serializer.deserialize(buffer, helper, packet);
            }
        });
    }

    private EntityDataMap filter(EntityDataMap metadata) {
        var flags = metadata.getFlags();
        boolean filterFlags = flags != null && !supportedFlags.containsAll(flags.keySet());
        if (!filterFlags && metadata.keySet().stream().allMatch(type -> dataTypes.fromType(type) != null)) {
            return metadata;
        }

        EntityDataMap filtered = new EntityDataMap();
        metadata.forEach((type, value) -> {
            if (type != EntityDataTypes.FLAGS && type != EntityDataTypes.FLAGS_2 && dataTypes.fromType(type) != null) {
                filtered.put(type, value);
            }
        });
        if (flags != null) {
            EnumMap<EntityFlag, Boolean> selected = new EnumMap<>(flags);
            selected.keySet().retainAll(supportedFlags);
            filtered.putFlags(selected);
        }
        return filtered;
    }
}
