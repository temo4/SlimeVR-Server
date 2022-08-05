package dev.slimevr.vr.trackers;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import dev.slimevr.vr.Device;


public class ReferenceAdjustedTracker<E extends Tracker> implements Tracker {

	public final E tracker;
	public final Quaternion yawFix = new Quaternion();
	public final Quaternion gyroFix = new Quaternion();
	public final Quaternion attachmentFix = new Quaternion();
	public final Quaternion rollFix = new Quaternion();
	protected float confidenceMultiplier = 1.0f;
	/**
	 * Changes between IMU axes and OpenGL/SteamVR axes
	 */
	private static final Quaternion axesOffset = new Quaternion()
		.fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X);

	public ReferenceAdjustedTracker(E tracker) {
		this.tracker = tracker;
	}

	@Override
	public boolean userEditable() {
		return this.tracker.userEditable();
	}

	@Override
	public void loadConfig(TrackerConfig config) {
		this.tracker.loadConfig(config);
	}

	@Override
	public void saveConfig(TrackerConfig config) {
		this.tracker.saveConfig(config);
	}

	/**
	 * Reset the tracker so that its current rotation is counted as (0, <HMD
	 * Yaw>, 0). This allows the tracker to be strapped to body at any pitch and
	 * roll.
	 * <p>
	 * Performs {@link #resetYaw(Quaternion)} for yaw drift correction.
	 */
	@Override
	public void resetFull(Quaternion reference) {
		tracker.resetFull(reference);

		Quaternion sensorRotation = new Quaternion();
		tracker.getRotation(sensorRotation);

		fixRoll(sensorRotation.clone());
		fixGyroscope(sensorRotation.clone());
		fixAttachment(sensorRotation.clone());

		fixYaw(reference);
	}

	/**
	 * Reset the tracker so that it's current yaw rotation is counted as <HMD
	 * Yaw>. This allows the tracker to have yaw independent of the HMD. Tracker
	 * should still report yaw as if it was mounted facing HMD, mounting
	 * position should be corrected in the source.
	 */
	@Override
	public void resetYaw(Quaternion reference) {
		tracker.resetYaw(reference);
		fixYaw(reference);
	}

	private void fixYaw(Quaternion reference) {
		// Use only yaw HMD rotation
		Quaternion targetRotation = new Quaternion(reference);
		targetRotation.fromAngles(0, targetRotation.getYaw(), 0);

		Quaternion sensorRotation = new Quaternion();
		tracker.getRotation(sensorRotation);
		rollFix.mult(sensorRotation, sensorRotation);
		axesOffset.mult(sensorRotation, sensorRotation);
		gyroFix.mult(sensorRotation, sensorRotation);
		sensorRotation.multLocal(attachmentFix);

		sensorRotation.fromAngles(0, sensorRotation.getYaw(), 0);

		yawFix.set(sensorRotation).inverseLocal().multLocal(targetRotation);
	}

	private void fixRoll(Quaternion sensorRotation) {
		rollFix.set(sensorRotation).inverseLocal();
		axesOffset.mult(rollFix, rollFix);
		rollFix.fromAngles(0, 0, rollFix.getRoll());
	}

	private void fixGyroscope(Quaternion sensorRotation) {
		rollFix.mult(sensorRotation, sensorRotation);
		axesOffset.mult(sensorRotation, sensorRotation);

		sensorRotation.fromAngles(0, sensorRotation.getYaw(), 0);

		gyroFix.set(sensorRotation).inverseLocal();
	}

	private void fixAttachment(Quaternion sensorRotation) {
		rollFix.mult(sensorRotation, sensorRotation);
		axesOffset.mult(sensorRotation, sensorRotation);

		gyroFix.mult(sensorRotation, sensorRotation);
		attachmentFix.set(sensorRotation).inverseLocal();
	}

	protected void adjustInternal(Quaternion store) {
		rollFix.mult(store, store);
		axesOffset.mult(store, store);
		gyroFix.mult(store, store);
		store.multLocal(attachmentFix);
		yawFix.mult(store, store);
	}

	@Override
	public boolean getRotation(Quaternion store) {
		tracker.getRotation(store);
		adjustInternal(store);
		return true;
	}

	@Override
	public boolean getPosition(Vector3f store) {
		return tracker.getPosition(store);
	}

	@Override
	public String getName() {
		return tracker.getName() + "/adj";
	}

	@Override
	public TrackerStatus getStatus() {
		return tracker.getStatus();
	}

	@Override
	public float getConfidenceLevel() {
		return tracker.getConfidenceLevel() * confidenceMultiplier;
	}

	@Override
	public TrackerPosition getBodyPosition() {
		return tracker.getBodyPosition();
	}

	@Override
	public void setBodyPosition(TrackerPosition position) {
		tracker.setBodyPosition(position);
	}

	@Override
	public void tick() {
		tracker.tick();
	}

	@Override
	public boolean hasRotation() {
		return tracker.hasRotation();
	}

	@Override
	public boolean hasPosition() {
		return tracker.hasPosition();
	}

	@Override
	public boolean isComputed() {
		return tracker.isComputed();
	}

	@Override
	public int getTrackerId() {
		return tracker.getTrackerId();
	}

	@Override
	public int getTrackerNum() {
		return tracker.getTrackerNum();
	}

	@Override
	public Device getDevice() {
		return tracker.getDevice();
	}

	@Override
	public Tracker get() {
		return this.tracker;
	}

	@Override
	public String getDisplayName() {
		return this.tracker.getDisplayName();
	}

	@Override
	public String getCustomName() {
		return this.tracker.getCustomName();
	}
}
