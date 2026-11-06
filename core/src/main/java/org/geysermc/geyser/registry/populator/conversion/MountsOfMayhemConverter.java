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

package org.geysermc.geyser.registry.populator.conversion;

import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.item.type.Item;

import java.util.HashMap;
import java.util.Map;

public final class MountsOfMayhemConverter {
    public static Map<Item, Item> itemMappings(Map<Item, Item> newerReplacements) {
        Map<Item, Item> mappings = new HashMap<>(newerReplacements);
        mappings.put(Items.WOODEN_SPEAR, Items.WOODEN_SWORD);
        mappings.put(Items.STONE_SPEAR, Items.STONE_SWORD);
        mappings.put(Items.COPPER_SPEAR, Items.COPPER_SWORD);
        mappings.put(Items.IRON_SPEAR, Items.IRON_SWORD);
        mappings.put(Items.GOLDEN_SPEAR, Items.GOLDEN_SWORD);
        mappings.put(Items.DIAMOND_SPEAR, Items.DIAMOND_SWORD);
        mappings.put(Items.NETHERITE_SPEAR, Items.NETHERITE_SWORD);
        mappings.put(Items.COPPER_NAUTILUS_ARMOR, Items.COPPER_HORSE_ARMOR);
        mappings.put(Items.IRON_NAUTILUS_ARMOR, Items.IRON_HORSE_ARMOR);
        mappings.put(Items.GOLDEN_NAUTILUS_ARMOR, Items.GOLDEN_HORSE_ARMOR);
        mappings.put(Items.DIAMOND_NAUTILUS_ARMOR, Items.DIAMOND_HORSE_ARMOR);
        mappings.put(Items.NETHERITE_NAUTILUS_ARMOR, Items.DIAMOND_HORSE_ARMOR);
        mappings.put(Items.NETHERITE_HORSE_ARMOR, Items.DIAMOND_HORSE_ARMOR);
        mappings.put(Items.NAUTILUS_SPAWN_EGG, Items.PUFFERFISH_SPAWN_EGG);
        mappings.put(Items.ZOMBIE_NAUTILUS_SPAWN_EGG, Items.PUFFERFISH_SPAWN_EGG);
        mappings.put(Items.CAMEL_HUSK_SPAWN_EGG, Items.CAMEL_SPAWN_EGG);
        mappings.put(Items.PARCHED_SPAWN_EGG, Items.SKELETON_SPAWN_EGG);
        return mappings;
    }

    private MountsOfMayhemConverter() {
    }
}
