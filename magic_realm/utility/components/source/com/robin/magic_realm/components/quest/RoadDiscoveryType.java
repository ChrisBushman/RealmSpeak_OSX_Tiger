package com.robin.magic_realm.components.quest;

import com.robin.magic_realm.components.PathDetail;

public final class RoadDiscoveryType {
	private final String _name;
	private final int _ordinal;
	private RoadDiscoveryType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final RoadDiscoveryType SecretPassages = new RoadDiscoveryType("SecretPassages", 0);
	public static final RoadDiscoveryType HiddenPaths = new RoadDiscoveryType("HiddenPaths", 1);
	public static final RoadDiscoveryType PathsOrPassages = new RoadDiscoveryType("PathsOrPassages", 2);

	private static final RoadDiscoveryType[] _VALUES = { SecretPassages, HiddenPaths, PathsOrPassages };
	public static RoadDiscoveryType[] values() { RoadDiscoveryType[] r = new RoadDiscoveryType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static RoadDiscoveryType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}

	// --- enum methods ---
	public boolean matchesSecretPassages() {
		return this == SecretPassages || this == PathsOrPassages;
	}

	public boolean matchesHiddenPaths() {
		return this == HiddenPaths || this == PathsOrPassages;
	}

	public boolean matches(PathDetail path) {
		return (path.isSecret() && matchesSecretPassages()) || (path.isHidden() && matchesHiddenPaths());
	}
}