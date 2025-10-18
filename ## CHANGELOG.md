### v1.4
+ Added configuration options support with Mod Menu and Cloth Config API and added the following options:

#####  <p>Lava Pickup:
<p>Allows enabling of whether lava can be picked up with a bucket or harm the player and destroy the bucket(default behavior).

#####  <p>Source Fluid Blocks Pickup:
<p>Allows toggling if picking up a fluid consumes the source block.

#####  <p>Waterlogging blocks:
<p>Toggles if waterlogging blocks is enabled.

#####  <p>Dissipating Waterlogged blocks:
<p>Toggles if waterlogged blocks should dissipate when broken.

#####  <p>Dissipating Ice blocks:
<p>Toggles if ice block's water should dissipate when broken.

#####  <p>Persistent Water in the End:
<p>In BTW water in the end can be placed normally, so you can toggle this if you do not want it.

+ Split client side environment code from the main package
+ Changed the main and client package directories from "hardcorefluidoverhaul" to "hfo_mod"
+ Updated the mod license in the mod to be CC-BY-4.0
+ Updated the mod to Fabric API 0.116.7 & Fabric Loader 0.17.2

### v1.3.1
+ Reverted a change that allowed to pickup water from flowing water too which caused a bug making it possible to pickup water infinitely
+ Updated the mod to Fabric API 0.115.1 & Fabric Loader 0.16.10

### v1.3
+ Made it possible to pick up water from flowing water as well as source blocks. (only in survival, in creative only source blocks can be picked up)
+ Fixed a bug where using sponges on water or trying to dispense water from a dispenser would crash the game.

### v1.2
+ Changed the mod so that the hardcore fluid logic will only apply if the player is not in creative mode/
This in turn allows normal use of the fluids in creative, which weren't available before this change.
+ Changed using the bucket on water to not take source blocks.
+ Changed the mod id from "hardcore-fluid-overhaul" to "hardcore_fluid_overhaul".
+ Removed ice blocks from dissipating their water when broken/melted
+ Updated the mod to Fabric API 0.114.0

### v1.1.1
+ Fixed the mod icon to show in the mod list and fixed it's display name ("hardcore-fluid-overhaul" to "Hardcore Fluid Overhaul")
+ Added mod description to show in the mod list

### v1.1
+ Added logic to make waterlogged blocks & Ice block to dissipate the water block they hold after being broken
+ Updated the mod to Fabric API 0.108.0 & Fabric Loader 0.16.9

### v1.0
+ Initial Release