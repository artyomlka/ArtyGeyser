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

import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.v844.Bedrock_v844;
import org.cloudburstmc.protocol.bedrock.codec.v859.Bedrock_v859;
import org.cloudburstmc.protocol.bedrock.codec.v860.Bedrock_v860;
import org.cloudburstmc.protocol.bedrock.codec.v898.Bedrock_v898;

import java.util.List;

/**
 * The wire codec and the Bedrock data used to advertise and initialize older clients.
 * Different protocols may share a palette when the on-wire runtime IDs are identical.
 */
public record LegacyBedrockProfile(BedrockCodec codec, String blockPalette, String itemPalette, String itemTags,
                                   List<String> versions) {
    public static final List<LegacyBedrockProfile> PROFILES = List.of(
        new LegacyBedrockProfile(Bedrock_v844.CODEC, "26_0", "1_21_110", "1_21_110",
            List.of("1.21.110", "1.21.111", "1.21.112", "1.21.113", "1.21.114")),
        new LegacyBedrockProfile(Bedrock_v859.CODEC, "26_0", "1_21_120", "1_21_120",
            List.of("1.21.120", "1.21.121", "1.21.122", "1.21.123")),
        new LegacyBedrockProfile(Bedrock_v860.CODEC, "26_0", "1_21_120", "1_21_120",
            List.of("1.21.124")),
        new LegacyBedrockProfile(Bedrock_v898.CODEC, "26_0", "1_21_130", "1_21_120",
            List.of("1.21.130", "1.21.131", "1.21.132"))
    );

    public int protocolVersion() {
        return codec.getProtocolVersion();
    }
}
