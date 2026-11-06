/*
 * Copyright (c) 2019-2022 GeyserMC. http://geysermc.org
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

package org.geysermc.geyser.session.cache;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.geyser.entity.type.ItemFrameEntity;
import org.geysermc.geyser.util.MathUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Item frames are addressed by block position and refreshed together when their chunk is sent.
 */
public final class ItemFrameCache {
    private final Long2ObjectMap<Map<Vector3i, ItemFrameEntity>> chunks = new Long2ObjectOpenHashMap<>();

    public void add(Vector3i position, ItemFrameEntity frame) {
        chunks.computeIfAbsent(chunkKey(position), key -> new Object2ObjectOpenHashMap<>()).put(position, frame);
    }

    public @Nullable ItemFrameEntity get(Vector3i position) {
        Map<Vector3i, ItemFrameEntity> frames = chunks.get(chunkKey(position));
        return frames == null ? null : frames.get(position);
    }

    public void remove(Vector3i position, ItemFrameEntity frame) {
        long key = chunkKey(position);
        Map<Vector3i, ItemFrameEntity> frames = chunks.get(key);
        if (frames != null && frames.remove(position, frame) && frames.isEmpty()) {
            chunks.remove(key);
        }
    }

    public Collection<ItemFrameEntity> inChunk(int x, int z) {
        Map<Vector3i, ItemFrameEntity> frames = chunks.get(MathUtils.chunkPositionToLong(x, z));
        return frames == null ? List.of() : frames.values();
    }

    public void clear() {
        chunks.clear();
    }

    private static long chunkKey(Vector3i position) {
        return MathUtils.chunkPositionToLong(position.getX() >> 4, position.getZ() >> 4);
    }
}
