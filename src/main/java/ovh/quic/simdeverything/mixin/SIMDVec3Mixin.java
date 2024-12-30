package ovh.quic.simdeverything.mixin;

import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import jdk.incubator.vector.*;

@Mixin(Vec3.class)
public class SIMDVec3Mixin {
	@Shadow @Final public double x;
	@Shadow @Final public double y;
	@Shadow @Final public double z;

	private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;
	private static final ThreadLocal<double[]> VECTOR_BUFFER = ThreadLocal.withInitial(() -> new double[SPECIES.length() * 2]);

	@Overwrite
	public Vec3 add(double d, double e, double f) {
		double[] buffer = VECTOR_BUFFER.get();
		buffer[0] = this.x;
		buffer[1] = this.y;
		buffer[2] = this.z;

		buffer[3] = d;
		buffer[4] = e;
		buffer[5] = f;

		DoubleVector v1 = DoubleVector.fromArray(SPECIES, buffer, 0);
		DoubleVector v2 = DoubleVector.fromArray(SPECIES, buffer, 3);
		DoubleVector result = v1.add(v2);

		return new Vec3(result.lane(0), result.lane(1), result.lane(2));
	}

	@Overwrite
	public Vec3 subtract(double d, double e, double f) {
		double[] buffer = VECTOR_BUFFER.get();
		buffer[0] = this.x;
		buffer[1] = this.y;
		buffer[2] = this.z;

		buffer[3] = d;
		buffer[4] = e;
		buffer[5] = f;

		DoubleVector v1 = DoubleVector.fromArray(SPECIES, buffer, 0);
		DoubleVector v2 = DoubleVector.fromArray(SPECIES, buffer, 3);
		DoubleVector result = v1.sub(v2);

		return new Vec3(result.lane(0), result.lane(1), result.lane(2));
	}

	@Overwrite
	public Vec3 scale(double d) {
		double[] buffer = VECTOR_BUFFER.get();
		buffer[0] = this.x;
		buffer[1] = this.y;
		buffer[2] = this.z;

		DoubleVector v1 = DoubleVector.fromArray(SPECIES, buffer, 0);
		DoubleVector result = v1.mul(d);

		return new Vec3(result.lane(0), result.lane(1), result.lane(2));
	}

	@Overwrite
	public Vec3 multiply(Vec3 vec3) {
		double[] buffer = VECTOR_BUFFER.get();
		buffer[0] = this.x;
		buffer[1] = this.y;
		buffer[2] = this.z;

		buffer[3] = vec3.x;
		buffer[4] = vec3.y;
		buffer[5] = vec3.z;

		DoubleVector v1 = DoubleVector.fromArray(SPECIES, buffer, 0);
		DoubleVector v2 = DoubleVector.fromArray(SPECIES, buffer, 3);
		DoubleVector result = v1.mul(v2);

		return new Vec3(result.lane(0), result.lane(1), result.lane(2));
	}

	@Overwrite
	public double distanceToSqr(Vec3 vec3) {
		double[] buffer = VECTOR_BUFFER.get();
		buffer[0] = this.x;
		buffer[1] = this.y;
		buffer[2] = this.z;

		buffer[3] = vec3.x;
		buffer[4] = vec3.y;
		buffer[5] = vec3.z;

		DoubleVector v1 = DoubleVector.fromArray(SPECIES, buffer, 0);
		DoubleVector v2 = DoubleVector.fromArray(SPECIES, buffer, 3);

		DoubleVector delta = v1.sub(v2);

		return (double)delta.mul(delta).reduceLanes(VectorOperators.ADD);
	}
}