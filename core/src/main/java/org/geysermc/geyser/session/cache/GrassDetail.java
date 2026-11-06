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

import org.cloudburstmc.math.vector.Vector2i;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.BlockChangeEntry;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateSubChunkBlocksPacket;
import org.geysermc.geyser.level.block.Blocks;
import org.geysermc.geyser.level.block.type.BlockState;
import org.geysermc.geyser.level.chunk.BlockStorage;
import org.geysermc.geyser.level.chunk.GeyserChunk;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.BitStorage;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.DataPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.GlobalPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.Palette;

import java.util.BitSet;

/**
 * Keeps decorative grass near the player without changing the Java chunk cache.
 */
public final class GrassDetail {
    private static final int UPDATE_FLAGS = 1 << UpdateBlockPacket.Flag.NETWORK.ordinal();

    private final GeyserSession session;
    private final int radius;
    private Vector2i center;

    public GrassDetail(GeyserSession session) {
        this.session = session;
        this.radius = session.getChunkCache().isCache() ? Math.min(4, session.getGeyser().config().gameplay().grassDetailDistance()) : -1;
    }

    public boolean hideInChunk(int x, int z) {
        if (radius < 0 || session.getErosionHandler().isActive()) {
            return false;
        }
        if (center == null) {
            Vector3i position = session.getPlayerEntity().position().toInt();
            center = Vector2i.from(position.getX() >> 4, position.getZ() >> 4);
        }
        return !isNear(center, x, z);
    }

    public BlockDefinition definition(BlockState state, Vector3i position) {
        if (radius >= 0 && isGrass(state) && hideInChunk(position.getX() >> 4, position.getZ() >> 4)) {
            return session.getBlockMappings().getBedrockAir();
        }
        return session.getBlockMappings().getBedrockBlock(state);
    }

    public static boolean isGrass(BlockState state) {
        return state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS)
            || state.is(Blocks.FERN) || state.is(Blocks.LARGE_FERN)
            || state.is(Blocks.SHORT_DRY_GRASS) || state.is(Blocks.TALL_DRY_GRASS);
    }

    public void move(Vector2i next) {
        if (radius < 0 || session.getErosionHandler().isActive()) {
            return;
        }
        Vector2i previous = center;
        center = next;
        if (previous != null && !previous.equals(next)) {
            refreshChangedChunks(previous, next, true);
            refreshChangedChunks(next, previous, false);
        }
    }

    public void reset() {
        center = null;
    }

    private boolean isNear(Vector2i origin, int x, int z) {
        return Math.abs(x - origin.getX()) <= radius && Math.abs(z - origin.getY()) <= radius;
    }

    private void refreshChangedChunks(Vector2i included, Vector2i excluded, boolean hide) {
        for (int x = included.getX() - radius; x <= included.getX() + radius; x++) {
            for (int z = included.getY() - radius; z <= included.getY() + radius; z++) {
                if (!isNear(excluded, x, z)) {
                    refreshChunk(x, z, hide);
                }
            }
        }
    }

    private void refreshChunk(int chunkX, int chunkZ, boolean hide) {
        ChunkCache cache = session.getChunkCache();
        GeyserChunk chunk = cache.getChunk(chunkX, chunkZ);
        if (chunk == null) {
            return;
        }
        int minY = session.getBedrockDimension().minY() >> 4;
        int maxY = minY + (session.getBedrockDimension().height() >> 4);
        for (int index = 0; index < chunk.sections().length; index++) {
            DataPalette data = chunk.sections()[index];
            int sectionY = cache.getChunkMinY() + index;
            if (data == null || sectionY < minY || sectionY >= maxY) {
                continue;
            }
            Palette palette = data.getPalette();
            boolean global = palette instanceof GlobalPalette;
            BitSet grass = new BitSet();
            if (!global) {
                for (int i = 0; i < palette.size(); i++) {
                    if (isGrass(BlockState.of(palette.idToState(i)))) {
                        grass.set(i);
                    }
                }
                if (grass.isEmpty()) {
                    continue;
                }
            }

            int baseX = chunkX << 4;
            int baseY = sectionY << 4;
            int baseZ = chunkZ << 4;
            UpdateSubChunkBlocksPacket packet = new UpdateSubChunkBlocksPacket();
            packet.setPosition(Vector3i.from(baseX, baseY, baseZ));
            BitStorage storage = data.getStorage();
            for (int yzx = 0; yzx < BlockStorage.SIZE; yzx++) {
                int paletteId = storage == null ? 0 : storage.get(yzx);
                if (!global && !grass.get(paletteId)) {
                    continue;
                }
                BlockState state = BlockState.of(global ? paletteId : palette.idToState(paletteId));
                if (global && !isGrass(state)) {
                    continue;
                }
                Vector3i position = Vector3i.from(baseX + (yzx & 15), baseY + (yzx >> 8), baseZ + ((yzx >> 4) & 15));
                BlockDefinition definition = hide ? session.getBlockMappings().getBedrockAir() : session.getBlockMappings().getBedrockBlock(state);
                packet.getStandardBlocks().add(new BlockChangeEntry(position, definition, UPDATE_FLAGS, -1, BlockChangeEntry.MessageType.NONE));
            }
            if (!packet.getStandardBlocks().isEmpty()) {
                session.sendUpstreamPacket(packet);
            }
        }
    }
}
