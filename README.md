<img src="https://geysermc.org/img/geyser-1760-860.png" alt="Geyser" width="600"/>

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Discord](https://img.shields.io/discord/613163671870242838.svg?color=%237289da&label=discord)](https://discord.gg/geysermc)
[![Crowdin](https://badges.crowdin.net/e/51361b7f8a01644a238d0fe8f3bddc62/localized.svg)](https://translate.geysermc.org/)

Geyser is a bridge between Minecraft: Bedrock Edition and Minecraft: Java Edition, closing the gap from those wanting to play true cross-platform.

Geyser is an [Open Collaboration](https://opencollaboration.dev/) project.

## What is Geyser?
Geyser is a proxy, bridging the gap between Minecraft: Bedrock Edition and Minecraft: Java Edition servers.
The ultimate goal of this project is to allow Minecraft: Bedrock Edition users to join Minecraft: Java Edition servers as seamlessly as possible. However, due to the nature of Geyser translating packets over the network of two different games, *do not expect everything to work perfectly!*

Special thanks to the DragonProxy project for being a trailblazer in protocol translation and for all the team members who have joined us here!

## ArtyGeyser Velocity

The fork builds the Velocity plugin. The compatibility changes are awaiting a build and client verification.

| Bedrock releases in the codec registry | Protocol ID |
| --- | --- |
| 1.20.0–1.20.1 | 589 |
| 1.20.10–1.20.15 | 594 |
| 1.20.30–1.20.32 | 618 |
| 1.20.40–1.20.41 | 622 |
| 1.20.50–1.20.51 | 630 |
| 1.20.60–1.20.62 | 649 |
| 1.20.70–1.20.73 | 662 |
| 1.20.80–1.20.81 | 671 |
| 1.21.0–1.21.1 | 685 |
| 1.21.2–1.21.3 | 686 |
| 1.21.20–1.21.23 | 712 |
| 1.21.30–1.21.31 | 729 |
| 1.21.40–1.21.44 | 748 |
| 1.21.50–1.21.51 | 766 |
| 1.21.60–1.21.62 | 776 |
| 1.21.70–1.21.73 | 786 |
| 1.21.80–1.21.84 | 800 |
| 1.21.90–1.21.92 | 818 |
| 1.21.93–1.21.94 | 819 |
| 1.21.100–1.21.101 | 827 |
| 1.21.110–1.21.114 | 844 |
| 1.21.120–1.21.123 | 859 |
| 1.21.124 | 860 |
| 1.21.130–1.21.132 | 898 |
| 26.0–26.3 | 924 |
| 26.10–26.13 | 944 |
| 26.20–26.23 | 975 |
| 26.30–26.35 | 1001 |
| 26.40–26.44 | 2168 |
| 26.45 | 2169 |
| 26.50–26.52 | 2193 |
| 26.60–26.62 | 2225 |

The historical palettes and item data come from Geyser's Git history. The files named "legacy_block_ids" index the 26.0 palette and preserve each older palette's original runtime IDs. Their exact state matches were derived with [BedrockBlockUpgradeSchema](https://github.com/opencollab-incubator/BedrockBlockUpgradeSchema/tree/1077ee640ed39f60266f7a7c8ab331ef76d7931e) through schema 0331. Regenerate the tables if the order of either palette changes.

For content unavailable on an older client, the fork uses a visual substitute. Java block states, collision data and game mechanics still come from Paper. A substitute can have different client-side visuals or prediction. Waypoints use the legacy player location mechanism on 1.21.80–26.3 and the built-in locator icons on 26.10. Client features that require packets absent from a version are limited on that version.

Geyser connects to Velocity as Java 26.3. For a Paper 1.21.11 backend, use a Velocity release that accepts Java 26.3 and [ViaVersion on Paper](https://geysermc.org/wiki/geyser/supported-versions/). [ViaVersion 5.12.0](https://github.com/ViaVersion/ViaVersion/releases/tag/5.12.0) added Java 26.3 client support.

For old Bedrock clients, configure "bedrock.transport" as "both". For example, "bedrock.port: 19132" is the UDP port the player enters to connect over RakNet. "bedrock.webrtc-port: 19133" is a separate UDP listener for NetherNet and is not the RakNet address players enter. The actual ports depend on the deployment.

Paper 1.21.11 defaults to 75 sent chunks per second per player in "paper-global.yml" under "chunk-loading-basic.player-max-chunk-send-rate". Record the configured value alongside the world view and simulation distances when taking a baseline trace.

For a profiling run, start Velocity with "-DGeyser.Audit.Xuid=<test-account-xuid>" before "-jar". The selected account's trace records chunk encoding and bytes, Java batch completion, outgoing Bedrock batch size before and after compression, session tick lateness, input queue wait, and teleport confirmations and resends. Times are monotonic nanoseconds. The start record pairs the monotonic clock with epoch milliseconds for comparison with external logs. Trace logging itself adds work during the run.

## Setting Up
Take a look [here](https://geysermc.org/wiki/geyser/setup/) for how to set up Geyser.

## Links:
- Website: https://geysermc.org
- Docs: https://geysermc.org/wiki/geyser/
- Download: https://geysermc.org/download
- Discord: https://discord.gg/geysermc
- Donate: https://opencollective.com/geysermc
- Test Server: `test.geysermc.org` port `25565` for Java and `19132` for Bedrock

## What's Left to be Added/Fixed
- Near-perfect movement (to the point where anticheat on large servers is unlikely to ban you)
- Some Entity Flags

## What can't be fixed
There are a few things Geyser is unable to support due to various differences between Minecraft Bedrock and Java. For a list of these limitations, see the [Current Limitations](https://geysermc.org/wiki/geyser/current-limitations/) page.

## Compiling
1. Clone the repo to your computer
2. Navigate to the Geyser root directory and run `git submodule update --init --recursive`. This command downloads all the needed submodules for Geyser and is a crucial step in this process.
3. Set `GRADLE_USER_HOME` to your cache directory on D: before building on Windows.
4. Run `.\gradlew.bat :velocity:build`. The plugin is generated at `bootstrap/velocity/build/libs/Geyser-Velocity.jar`. The GitHub Actions build runs on manual dispatch.

## Contributing
Any contributions are appreciated. Please feel free to reach out to us on [Discord](https://discord.gg/geysermc) if
you're interested in helping out with Geyser.

## Libraries Used:
- [Adventure Text Library](https://github.com/KyoriPowered/adventure)
- [CloudburstMC Bedrock Protocol Library](https://github.com/CloudburstMC/Protocol)
- [GeyserMC's Java Protocol Library](https://github.com/GeyserMC/MCProtocolLib)
- [TerminalConsoleAppender](https://github.com/Minecrell/TerminalConsoleAppender)
- [Simple Logging Facade for Java (slf4j)](https://github.com/qos-ch/slf4j)
