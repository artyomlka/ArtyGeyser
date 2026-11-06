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

import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.v589.Bedrock_v589;
import org.cloudburstmc.protocol.bedrock.codec.v594.Bedrock_v594;
import org.cloudburstmc.protocol.bedrock.codec.v618.Bedrock_v618;
import org.cloudburstmc.protocol.bedrock.codec.v622.Bedrock_v622;
import org.cloudburstmc.protocol.bedrock.codec.v630.Bedrock_v630;
import org.cloudburstmc.protocol.bedrock.codec.v649.Bedrock_v649;
import org.cloudburstmc.protocol.bedrock.codec.v662.Bedrock_v662;
import org.cloudburstmc.protocol.bedrock.codec.v671.Bedrock_v671;
import org.cloudburstmc.protocol.bedrock.codec.v685.Bedrock_v685;
import org.cloudburstmc.protocol.bedrock.codec.v686.Bedrock_v686;
import org.cloudburstmc.protocol.bedrock.codec.v712.Bedrock_v712;
import org.cloudburstmc.protocol.bedrock.codec.v729.Bedrock_v729;
import org.cloudburstmc.protocol.bedrock.codec.v748.Bedrock_v748;
import org.cloudburstmc.protocol.bedrock.codec.v766.Bedrock_v766;
import org.cloudburstmc.protocol.bedrock.codec.v776.Bedrock_v776;
import org.cloudburstmc.protocol.bedrock.codec.v786.Bedrock_v786;
import org.cloudburstmc.protocol.bedrock.codec.v800.Bedrock_v800;
import org.cloudburstmc.protocol.bedrock.codec.v818.Bedrock_v818;
import org.cloudburstmc.protocol.bedrock.codec.v819.Bedrock_v819;
import org.cloudburstmc.protocol.bedrock.codec.v827.Bedrock_v827;
import org.cloudburstmc.protocol.bedrock.codec.v844.Bedrock_v844;
import org.cloudburstmc.protocol.bedrock.codec.v859.Bedrock_v859;
import org.cloudburstmc.protocol.bedrock.codec.v860.Bedrock_v860;
import org.cloudburstmc.protocol.bedrock.codec.v898.Bedrock_v898;

import java.util.List;

/**
 * The wire codec and the Bedrock data used to advertise and initialize older clients.
 * Different protocols may share a palette when the on-wire runtime IDs are identical.
 */
public record LegacyBedrockProfile(BedrockCodec codec, String blockPalette, String itemPalette, @Nullable String itemTags,
                                   List<String> versions) {
    public static final List<LegacyBedrockProfile> PROFILES = List.of(
        new LegacyBedrockProfile(Bedrock_v589.CODEC, "1_20_0", "1_20_0", null,
            List.of("1.20.0", "1.20.1")),
        new LegacyBedrockProfile(Bedrock_v594.CODEC, "1_20_10", "1_20_10", null,
            List.of("1.20.10", "1.20.12", "1.20.13", "1.20.15")),
        new LegacyBedrockProfile(Bedrock_v618.CODEC, "1_20_30", "1_20_30", null,
            List.of("1.20.30", "1.20.31", "1.20.32")),
        new LegacyBedrockProfile(Bedrock_v622.CODEC, "1_20_40", "1_20_40", null,
            List.of("1.20.40", "1.20.41")),
        new LegacyBedrockProfile(Bedrock_v630.CODEC, "1_20_50", "1_20_50", null,
            List.of("1.20.50", "1.20.51")),
        new LegacyBedrockProfile(Bedrock_v649.CODEC, "1_20_60", "1_20_60", null,
            List.of("1.20.60", "1.20.61", "1.20.62")),
        new LegacyBedrockProfile(Bedrock_v662.CODEC, "1_20_70", "1_20_70", null,
            List.of("1.20.70", "1.20.71", "1.20.72", "1.20.73")),
        new LegacyBedrockProfile(Bedrock_v671.CODEC, "1_20_80", "1_20_80", "1_20_80",
            List.of("1.20.80", "1.20.81")),
        new LegacyBedrockProfile(Bedrock_v685.CODEC, "1_21_0", "1_21_0", "1_21_0",
            List.of("1.21.0", "1.21.1")),
        new LegacyBedrockProfile(Bedrock_v686.CODEC, "1_21_0", "1_21_0", "1_21_0",
            List.of("1.21.2", "1.21.3")),
        new LegacyBedrockProfile(Bedrock_v712.CODEC, "1_21_20", "1_21_20", "1_21_20",
            List.of("1.21.20", "1.21.21", "1.21.22", "1.21.23")),
        new LegacyBedrockProfile(Bedrock_v729.CODEC, "1_21_30", "1_21_30", "1_21_30",
            List.of("1.21.30", "1.21.31")),
        new LegacyBedrockProfile(Bedrock_v748.CODEC, "1_21_40", "1_21_40", "1_21_40",
            List.of("1.21.40", "1.21.41", "1.21.42", "1.21.43", "1.21.44")),
        new LegacyBedrockProfile(Bedrock_v766.CODEC, "1_21_50", "1_21_50", "1_21_50",
            List.of("1.21.50", "1.21.51")),
        new LegacyBedrockProfile(Bedrock_v776.CODEC, "1_21_60", "1_21_60", "1_21_60",
            List.of("1.21.60", "1.21.61", "1.21.62")),
        new LegacyBedrockProfile(Bedrock_v786.CODEC, "1_21_70", "1_21_70", "1_21_70",
            List.of("1.21.70", "1.21.71", "1.21.72", "1.21.73")),
        new LegacyBedrockProfile(Bedrock_v800.CODEC, "1_21_80", "1_21_80", "1_21_70",
            List.of("1.21.80", "1.21.81", "1.21.82", "1.21.83", "1.21.84")),
        new LegacyBedrockProfile(Bedrock_v818.CODEC, "1_21_90", "1_21_90", "1_21_90",
            List.of("1.21.90", "1.21.91", "1.21.92")),
        new LegacyBedrockProfile(Bedrock_v819.CODEC, "1_21_90", "1_21_93", "1_21_90",
            List.of("1.21.93", "1.21.94")),
        new LegacyBedrockProfile(Bedrock_v827.CODEC, "1_21_100", "1_21_100", "1_21_100",
            List.of("1.21.100", "1.21.101")),
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
