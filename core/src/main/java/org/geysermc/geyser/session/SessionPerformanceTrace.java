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

package org.geysermc.geyser.session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper;
import org.cloudburstmc.protocol.bedrock.netty.codec.compression.CompressionCodec;
import org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket;
import org.geysermc.geyser.GeyserLogger;
import org.geysermc.geyser.session.cache.TeleportCache;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Opt-in packet timing for one authenticated Bedrock account.
 * Time and duration fields are nanoseconds from the process's monotonic clock.
 */
public final class SessionPerformanceTrace extends ChannelOutboundHandlerAdapter {
    private static final String TARGET_XUID = System.getProperty("Geyser.Audit.Xuid", "");
    private static final AtomicInteger NEXT_ID = new AtomicInteger();

    private final GeyserLogger logger;
    private final String prefix;
    private long nextTickNanos;
    private long previousInputNanos;

    private SessionPerformanceTrace(GeyserSession session) {
        logger = session.getGeyser().getLogger();
        prefix = "[geyser-audit " + NEXT_ID.incrementAndGet() + " protocol=" + session.protocolVersion() + "] ";
        log("start wallMillis=" + System.currentTimeMillis() + " nanoTime=" + System.nanoTime());
    }

    public static @Nullable SessionPerformanceTrace attach(GeyserSession session) {
        if (TARGET_XUID.isEmpty() || !TARGET_XUID.equals(session.xuid())) {
            return null;
        }

        SessionPerformanceTrace trace = new SessionPerformanceTrace(session);
        session.getUpstream().getSession().getPeer().getChannel().pipeline()
            .addBefore(CompressionCodec.NAME, "geyser-performance-trace", trace);
        return trace;
    }

    public void scheduleTicks(int periodNanos) {
        nextTickNanos = System.nanoTime() + periodNanos;
    }

    public void tick(int periodNanos) {
        long now = System.nanoTime();
        log("tick at=" + now + " late=" + Math.max(0, now - nextTickNanos));
        nextTickNanos += periodNanos;
    }

    public void playerInput(long tick, long receivedNanos) {
        long now = System.nanoTime();
        long gap = previousInputNanos == 0 ? 0 : receivedNanos - previousInputNanos;
        previousInputNanos = receivedNanos;
        log("input at=" + receivedNanos + " tick=" + tick + " gap=" + gap + " queueWait=" + (now - receivedNanos));
    }

    public void chunk(LevelChunkPacket packet, long startedNanos, int blockEntities) {
        long now = System.nanoTime();
        log("chunk_queued at=" + now + " translateStart=" + startedNanos + " translateTime=" + (now - startedNanos)
            + " dimension=" + packet.getDimension() + " x=" + packet.getChunkX() + " z=" + packet.getChunkZ()
            + " sections=" + packet.getSubChunksLength() + " blockEntities=" + blockEntities + " bytes=" + packet.getData().readableBytes());
    }

    public void javaBatchFinished() {
        log("java_batch_finished at=" + System.nanoTime());
    }

    public void teleport(String stage, TeleportCache teleport, float distanceSquared) {
        log("teleport at=" + System.nanoTime() + " stage=" + stage + " id=" + teleport.getTeleportConfirmId()
            + " unconfirmedInputs=" + teleport.getUnconfirmedFor() + " distanceSquared=" + distanceSquared);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (msg instanceof BedrockBatchWrapper batch && batch.getUncompressed() != null && batch.getCompressed() != null) {
            int chunks = 0;
            for (var wrapper : batch.getPackets()) {
                if (wrapper.getPacket() instanceof LevelChunkPacket) {
                    chunks++;
                }
            }
            log("bedrock_batch at=" + System.nanoTime() + " packets=" + batch.getPackets().size()
                + " chunks=" + chunks + " bytes=" + batch.getUncompressed().readableBytes()
                + " compressedBytes=" + batch.getCompressed().readableBytes() + " writable=" + ctx.channel().isWritable());
        }
        ctx.write(msg, promise);
    }

    private void log(String message) {
        logger.info(prefix + message);
    }
}
