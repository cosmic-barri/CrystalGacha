# Crystal Gacha
## Github repository for Crystal Gacha Minecraft mod.

## Gacha Data:

Gacha's has a base charge of 0 and a base purity of 100. For every item they eat, these values will change. Once they get a charge of 50 or more, they will drop a crystal. The rarity of this crystal will depend on the level of purity that the gacha has at the time of dropping the crystal. Below are the values table.

### Charge and purity table for each item rarity:

| Rarity   | Charge | Purity |
|----------|--------|--------|
| Common   |   +1   |   -3   |
| Uncommon |   +3   |   -2   |
| Rare     |   +5   |   -1   |
| Epic     |   +7   |   -0   |

The gacha's crystals will drop an item when you right-click them. The dropped item is random except his rarity, that are linked to the crystal rarity. Here is the table of what crystal will be dropped depending on the level of purity of the gacha. Also, there is a random chance to get a better one beside the purity level.

### Crystal dropped table according to purity:

| Purity | Rarity    | Random Chance |
|--------|-----------|---------------|
| >=91   |  Epic     |      2%       |
| >=67   |  Rare     |     12%       |
| >=30   |  Uncommon |     22%       |
| <=29   |  Common   |      -        |

## Configurable Data:

### The mod had a config file located in .minecraft/config/gacha-config.toml. In this file you can config 2 blacklists and 1 option.

## Items Blacklist.

In the item blacklist you can specify items to make it impossible to get dropped via gacha's crystals. To make it you need to put the item mod name and the item name. To easily see it you can activate the in game option **advanced tooltips** by pressing **F3 + H**. By default, all the vanilla creative items are blacklisted.

To blacklist an item you have to add, to the comma separated list, something with this format "mod_name:item_name". For example "minecraft:stone_axe".

## Mods Blacklist.

If you want to blacklist all the items of a mod you can just put in this list the mod tag. To easily see it you can activate the in game option **advanced tooltips** by pressing **F3 + H**.

To blacklist a entire mod you have to add, to the comma separated list, something like that "mod_name". For example "minecraft".

## Spawn Eggs

There is an option to indicate if spawn eggs can be dropped or not by the crystals. True if you want, false if you don't. True by default.
