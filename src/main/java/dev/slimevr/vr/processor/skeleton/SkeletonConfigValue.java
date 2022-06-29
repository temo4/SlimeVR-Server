package dev.slimevr.vr.processor.skeleton;

import java.util.HashMap;
import java.util.Map;


public enum SkeletonConfigValue {
	HEAD(
		1,
		"Head",
		"headShift",
		"Head shift",
		0.1f,
		new BoneType[] { BoneType.HEAD }
	),
	NECK(
		2,
		"Neck",
		"neckLength",
		"Neck length",
		0.1f,
		new BoneType[] { BoneType.NECK }
	),
	CHEST(
		3,
		"Chest",
		"chestLength",
		"Chest length",
		0.32f,
		new BoneType[] { BoneType.CHEST }
	),
	WAIST(
		4,
		"Waist",
		"waistLength",
		"Waist length",
		0.2f,
		new BoneType[] { BoneType.WAIST }
	),
	HIP(
		5,
		"Hip",
		"hipLength",
		"Hip length",
		0.04f,
		new BoneType[] { BoneType.HIP }
	),
	HIP_OFFSET(
		6,
		"Hip offset",
		"hipOffset",
		"Hip offset",
		0.0f,
		new BoneType[] { BoneType.HIP_TRACKER }
	),
	HIPS_WIDTH(
		7,
		"Hips width",
		"hipsWidth",
		"Hips width",
		0.26f,
		new BoneType[] { BoneType.LEFT_HIP, BoneType.RIGHT_HIP }
	),
	LEFT_UPPER_LEG(
		8,
		"Left upper leg length",
		"leftUpperLegLength",
		"Left upper leg length",
		0.42f,
		new BoneType[] { BoneType.LEFT_UPPER_LEG }
	),
	RIGHT_UPPER_LEG(
		9,
		"Right upper leg length",
		"rightUpperLegLength",
		"Right upper leg length",
		0.42f,
		new BoneType[] { BoneType.RIGHT_UPPER_LEG }
	),
	LEFT_LOWER_LEG(
		10,
		"Left lower leg length",
		"leftLowerLegLength",
		"Left lower leg length",
		0.50f,
		new BoneType[] { BoneType.LEFT_LOWER_LEG }
	),
	RIGHT_LOWER_LEG(
		11,
		"Right lower leg length",
		"rightLowerLegLength",
		"Right lower leg length",
		0.50f,
		new BoneType[] { BoneType.RIGHT_LOWER_LEG }
	),
	FOOT_LENGTH(
		12,
		"Foot length",
		"footLength",
		"Foot length",
		0.05f,
		new BoneType[] { BoneType.LEFT_FOOT, BoneType.RIGHT_FOOT }
	),
	FOOT_SHIFT(
		13,
		"Foot shift",
		"footShift",
		"Foot shift",
		-0.05f,
		new BoneType[] { BoneType.LEFT_LOWER_LEG, BoneType.RIGHT_LOWER_LEG }
	),
	SKELETON_OFFSET(
		14,
		"Skeleton offset",
		"skeletonOffset",
		"Skeleton offset",
		0.0f,
		new BoneType[] { BoneType.CHEST_TRACKER, BoneType.HIP_TRACKER,
			BoneType.LEFT_KNEE_TRACKER, BoneType.RIGHT_KNEE_TRACKER,
			BoneType.LEFT_FOOT_TRACKER, BoneType.RIGHT_KNEE_TRACKER }
	),
	CONTROLLER_Z(
		15,
		"Controller distance z",
		"controllerDistanceZ",
		"Controller distance z",
		0.13f,
		new BoneType[] { BoneType.LEFT_CONTROLLER, BoneType.RIGHT_CONTROLLER,
			BoneType.LEFT_HAND, BoneType.RIGHT_HAND }
	),
	CONTROLLER_Y(
		16,
		"Controller distance y",
		"controllerDistanceY",
		"Controller distance y",
		0.035f,
		new BoneType[] { BoneType.LEFT_CONTROLLER, BoneType.RIGHT_CONTROLLER,
			BoneType.LEFT_HAND, BoneType.RIGHT_HAND }
	),
	LOWER_ARM(
		17,
		"Lower arm length",
		"lowerArmLength",
		"Lower arm length",
		0.25f,
		new BoneType[] { BoneType.LEFT_LOWER_ARM, BoneType.RIGHT_LOWER_ARM }
	),
	SHOULDERS_DISTANCE(
		18,
		"Shoulders distance",
		"shoulersDistance",
		"Shoulders distance",
		0.08f,
		new BoneType[] { BoneType.LEFT_SHOULDER, BoneType.RIGHT_SHOULDER }
	),
	SHOULDERS_WIDTH(
		19,
		"Shoulders width",
		"shoulersWidth",
		"Shoulders width",
		0.36f,
		new BoneType[] { BoneType.LEFT_SHOULDER, BoneType.RIGHT_SHOULDER }
	),
	UPPER_ARM(
		20,
		"Upper arm length",
		"upperArmLength",
		"Upper arm length",
		0.25f,
		new BoneType[] { BoneType.LEFT_UPPER_ARM, BoneType.RIGHT_UPPER_ARM }
	),
	ELBOW_OFFSET(
		21,
		"Elbow offset",
		"elbowOffset",
		"Elbow offset",
		0f,
		new BoneType[] { BoneType.LEFT_ELBOW_TRACKER, BoneType.RIGHT_ELBOW_TRACKER }
	),;

	public static final SkeletonConfigValue[] values = values();
	private static final String CONFIG_PREFIX = "body.";
	private static final Map<String, SkeletonConfigValue> byStringVal = new HashMap<>();
	private static final Map<Number, SkeletonConfigValue> byIdVal = new HashMap<>();

	static {
		for (SkeletonConfigValue configVal : values()) {
			byIdVal.put(configVal.id, configVal);
			byStringVal.put(configVal.stringVal.toLowerCase(), configVal);
		}
	}

	public final int id;
	public final String stringVal;
	public final String configKey;
	public final String label;
	public final float defaultValue;
	public final BoneType[] affectedBones;

	SkeletonConfigValue(
		int id,
		String stringVal,
		String configKey,
		String label,
		float defaultValue,
		BoneType[] affectedBones
	) {
		this.id = id;
		this.stringVal = stringVal;
		this.configKey = CONFIG_PREFIX + configKey;
		this.label = label;

		this.defaultValue = defaultValue;

		this.affectedBones = affectedBones
			== null ? new BoneType[0] : affectedBones;
	}

	public static SkeletonConfigValue getByStringValue(String stringVal) {
		return stringVal == null ? null : byStringVal.get(stringVal.toLowerCase());
	}

	public static SkeletonConfigValue getById(int id) {
		return byIdVal.get(id);
	}
}
