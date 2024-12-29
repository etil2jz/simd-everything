package ovh.quic.simdeverything.mixin;

import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import jdk.incubator.vector.*;

@Mixin(Vec3.class)
public class SIMDVec3Mixin {

	private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

	@Overwrite
	public Vec3 add(double d, double e, double f) {
		DoubleVector v1 = DoubleVector.fromArray(SPECIES, new double[] { this.x, this.y, this.z }, 0);
		DoubleVector v2 = DoubleVector.broadcast(SPECIES, d);
		v2 = v2.withLane(1, e);
		v2 = v2.withLane(2, f);
		DoubleVector result = v1.add(v2);
		return new Vec3(result.get(0), result.get(1), result.get(2));
	}

	@Overwrite
	public Vec3 subtract(double d, double e, double f) {
		DoubleVector v1 = DoubleVector.fromArray(SPECIES, new double[] { this.x, this.y, this.z }, 0);
		DoubleVector v2 = DoubleVector.broadcast(SPECIES, d);
		v2 = v2.withLane(1, e);
		v2 = v2.withLane(2, f);
		DoubleVector result = v1.sub(v2);
		return new Vec3(result.get(0), result.get(1), result.get(2));
	}

	@Overwrite
	public Vec3 scale(double d) {
		DoubleVector v1 = DoubleVector.fromArray(SPECIES, new double[] { this.x, this.y, this.z }, 0);
		DoubleVector result = v1.mul(d);
		return new Vec3(result.get(0), result.get(1), result.get(2));
	}

	@Overwrite
	public Vec3 multiply(Vec3 vec3) {
		DoubleVector v1 = DoubleVector.fromArray(SPECIES, new double[] { this.x, this.y, this.z }, 0);
		DoubleVector v2 = DoubleVector.fromArray(SPECIES, new double[] { vec3.x, vec3.y, vec3.z }, 0);
		DoubleVector result = v1.mul(v2);
		return new Vec3(result.get(0), result.get(1), result.get(2));
	}

	@Overwrite
	public double distanceToSqr(Vec3 vec3) {
		DoubleVector v1 = DoubleVector.fromArray(SPECIES, new double[] { this.x, this.y, this.z }, 0);
		DoubleVector v2 = DoubleVector.fromArray(SPECIES, new double[] { vec3.x, vec3.y, vec3.z }, 0);
		DoubleVector delta = v1.sub(v2);
		double sumOfSquares = delta.mul(delta).reduceLanes(VectorOperators.ADD);
		return sumOfSquares;
	}

}