package pl.polskieligi.log.distance;

public class Distance<W, P> {
	private final Double distance;
	private final W webObject;
	private final P persObject;

	Distance(Double distance, W webObject, P persObject) {
		this.distance = distance;
		this.webObject = webObject;
		this.persObject = persObject;
	}

	public Double getDistance() {
		return distance;
	}

	public W getWebObject() {
		return webObject;
	}

	public P getPersObject() {
		return persObject;
	}
}
