package cs.trend.client;

public enum Source {

	CAS1("Cassette 1"), CAS2("Cassette 2"), CAS3("Cassette 3"), CAS4("Cassette 4"), CAS5("Cassette 5"), CAS6(
			"Cassette 6"), CAS7("Cassette 7"), CAS8("Cassette 8"), CAS9("Cassette 9"), CAS10("Cassette 10");
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
