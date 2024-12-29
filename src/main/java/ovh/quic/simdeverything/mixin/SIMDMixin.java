package ovh.quic.simdeverything.mixin;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VoxelShape.class)
public class SIMDMixin {
	private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_256;
}