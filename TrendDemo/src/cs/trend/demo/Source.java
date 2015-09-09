package cs.trend.demo;

public enum Source {

	CAS_1("Cassette 1"), CAS_2("Cassette 2"), CAS_3("Cassette 3"), CAS_4("Cassette 4"), CAS_5("Cassette 5"), CAS_6(
			"Cassette 6"), CAS_7("Cassette 7"), CAS_8("Cassette 8"), CAS_9("Cassette 9"), CAS_10("Cassette 10");
	// MASTER_LOCKER("Master Locker");

	private String displayName;

	private Source(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return this.displayName;
	}

}
