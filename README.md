# Chronos Carpet Addons

A carpet mod addon made for the [Chronos SMP](https://discord.gg/VvPucVAjUS).

## Rules

### armorStandDropItemFromExplosion
Armor stand drops it's item if killed by an explosion.
* Type: `boolean`
* Default value: `false`
* Categories: `CHRONOS`

### commandTotal
Enables /total command to know the total sum of a scoreboard. Taken from johan's carpet addons.
* Type: `boolean`
* Default value: `false`
* Categories: `SURVIVAL`, `COMMAND`, `CHRONOS`

### creativeOneHitKill
Allows players in Creative mode to kill entities in one hit. If the player is sneaking, other entities around the target get killed too. Taken from lunaar carpet addons.
* Type: `boolean`
* Default value: `false`
* Categories: `CREATIVE`, `CHRONOS`

### disableNetherPortalCollisionCheck
Disable laggy nether portal collision checks introduced in 1.19.3. Large mobs may suffocate in nether portals if this is enabled.
* Type: `boolean`
* Default value: `false`
* Categories: `OPTIMIZATION`, `CHRONOS`

### ignoreExistingRaids
Allows raids to start within 96 blocks of existing raids. May be useful for designing raid farms.
* Type: `boolean`
* Default value: `false`
* Categories: `CREATIVE`, `EXPERIMENTAL`, `CHRONOS`

### endGatewayCooldown
Toggle for end gateway cooldown. Taken from johan's carpet addons.
* Type: `boolean`
* Default value: `false`
* Categories: `SURVIVAL`, `CHRONOS`

### fakePlayerFallDamage
Toggle whether fake players take fall damage or not.
* Type: `boolean`
* Default value: `true`
* Categories: `CREATIVE`, `CHRONOS`

### maxBlockReachDistance
How far the player can interact with blocks. Requires a modded client to reach farther than what is allowed in vanilla.
* Type: `double`
* Default value: `6.0`
* Categories: `CREATIVE`, `CHRONOS`

### netheritePickaxeInstantMineDeepslate
Deepslate can be instant mined with netherite pickaxe and haste II.
(Must also be installed on the client to work)
* Type: `boolean`
* Default value: `false`
* Categories: `SURVIVAL`, `CHRONOS`

### noClipCollisionFix
Fixes noclip calculating all interactive block collisions when you exit noclip.
For example, if you fly through a portal while in noclip, when you exit noclip you will no longer go through the portal.
* Type: `boolean`
* Default value: `false`
* Categories: `BUGFIX`, `CHRONOS`

### oldFlintAndSteelBehavior
Backports 1.12 flint and steel behavior. Flint and steel can be used for updating observers / buds. Taken from johan's carpet addons.
* Type: `boolean`
* Default value: `false`
* Categories: `CREATIVE`, `CHRONOS`

### playerSit
The player will sit down after 3 quick sneaks. Taken from plusls carped addons.
* Type: `boolean`
* Default value: `false`
* Categories: `CHRONOS`

### totalScore
Display total score on the sidebar. Taken from johan's carpet addons.
* Type: `boolean`
* Default value: `false`
* Categories: `SURVIVAL`, `CHRONOS`

### scoreboardIgnoresBots
Bots don't appear on scoreboards and do not count in the total if they're not in a team. Real players need to be in a team! Taken from lunaar carpet addons. Based on code by JohanVonElectrum.
* Type: `boolean`
* Default value: `false`
* Categories: `SURVIVAL`, `CHRONOS`

## License

This mod is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.

Parts of this project have been taken from [plusls-carpet-addition](https://github.com/plusls/plusls-carpet-addition), [lunaar-carpet-addons](https://github.com/Lunaar-SMP/lunaar-carpet-addons), [Johan-Carpet](https://github.com/JohanVonElectrum/Johan-Carpet). The code has been modified. \
In the past some code was taken from [litetech-additions](https://github.com/LiteTechMC/litetech-additions), but has been removed due to redundancy.
