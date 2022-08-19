# Documentation

Redderstone currently contains very few components, to keep the mod focused
on the "one feature" focus of Singularity. In this case, that feature
is the system itself, which lets you breach the ordinary Redstone cap of 15
using some specialized tools.

## Feature

It's just the one, but it's an important one if you want to understand the
mod.

Redstone can be emitted and transmitted past level 15. It uses regular
redstone to do this, you just connect your redstone to sources from this
mod, and they'll take over from there.

Redstone signals above 15 can only be transmitted either directly through
Redstone wire or through components built to handle them. 
Wires can connect horizontally, and are able to step up or
down up to one block, even if there is another block in the way.
Higher signals do not exhibit quasi-connectivity or some other typical of
low signals, such as transmission through blocks.

Lastly, high signals will not transmit fully through components that are
not made to handle them, and these components will often step down their
signal to 15. Thankfully, some components are craftable using specially
mixed "Vermilion Dust" which let you use high Redstone signals to their
fullest!

## Blocks

### Alternator

The Alternator is the earliest available method to boost Redstone signals.
It converts static Redstone signals to higher-level alternating signals.

You can learn more about it [here](blocks/alternator.md).

### Inverter

The Inverter is used to invert the phase of some higher-level Redstone
signals. Given an alternating signal, it will invert its phase. It is 
often recombined with the original signal in order to convert an 
alternating signal back into a (dirty) direct signal.

You can learn more about it [here](blocks/inverter.md)

### Furnace Heater

The Furnace Heater accepts high Redstone signals and heats Furnaces,
Smokers, and Blast Furnaces.

You can learn more about it [here](blocks/furnace_heater.md)

## Items

### Vermillion Dust

Vermillion dust is a blend of Redstone often used in building components
specialized for higher Redstone signals.

You can learn more about it [here](items/vermilion_dust.md)