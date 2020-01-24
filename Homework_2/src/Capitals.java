/**
 * Kenny Akers
 * Mr. Paige
 * Homework #2
 * 8/31/17
 */
public final class Capitals {

	private Capitals() {} // There are no objects of this class.

	public static final City Montgomery    = new City("Montgomery",     "AL", 32.3617, -86.2792);
	public static final City Juneau        = new City("Juneau",         "AK", 58.3014, -134.422);
	public static final City Phoenix       = new City("Phoenix",        "AZ", 33.4500, -112.067);
	public static final City LittleRock    = new City("Little Rock",    "AR", 34.7361, -92.3311);
	public static final City Sacramento    = new City("Sacramento",     "CA", 38.5556, -121.469);
	public static final City Denver        = new City("Denver",         "CO", 39.7618, -104.881);
	public static final City Hartford      = new City("Hartford",       "CT", 41.7627, -72.6743);
	public static final City Dover         = new City("Dover",          "DE", 39.1619, -75.5267);
	public static final City Tallahassee   = new City("Tallahassee",    "FL", 30.4550, -84.2533);
	public static final City Atlanta       = new City("Atlanta",        "GA", 33.7550, -84.3900);
	public static final City Honolulu      = new City("Honolulu",       "HI", 21.3000, -157.817);
	public static final City Boise         = new City("Boise",          "ID", 43.6167, -116.200);
	public static final City Springfield   = new City("Springfield",    "IL", 39.6983, -89.6197);
	public static final City Indianapolis  = new City("Indianapolis",   "IN", 39.7910, -86.1480);
	public static final City DesMoines     = new City("Des Moines",     "IA", 41.5908, -93.6208);
	public static final City Topeka        = new City("Topeka",         "KS", 39.0558, -95.6894);
	public static final City Frankfort     = new City("Frankfort",      "KY", 38.1970, -84.8630);
	public static final City BatonRouge    = new City("Baton Rouge",    "LA", 30.4500, -91.1400);
	public static final City Augusta       = new City("Augusta",        "ME", 44.3070, -69.7820);
	public static final City Annapolis     = new City("Annapolis",      "MD", 38.9729, -76.5012);
	public static final City Boston        = new City("Boston",         "MA", 42.3581, -71.0636);
	public static final City Lansing       = new City("Lansing",        "MI", 42.7336, -84.5467);
	public static final City SaintPaul     = new City("Saint Paul",     "MN", 44.9442, -93.0936);
	public static final City Jackson       = new City("Jackson",        "MI", 32.2989, -90.1847);
	public static final City JeffersonCity = new City("Jefferson City", "MO", 38.5767, -92.1736);
	public static final City Helena        = new City("Helena",         "MT", 46.5958, -112.027);
	public static final City Lincoln       = new City("Lincoln",        "NE", 40.8106, -96.6803);
	public static final City CarsonCity    = new City("Carson City",    "NV", 39.1608, -119.754);
	public static final City Concord       = new City("Concord",        "NH", 43.2067, -71.5381);
	public static final City Trenton       = new City("Trenton",        "NJ", 40.2237, -74.7640);
	public static final City SantaFe       = new City("Santa Fe",       "NM", 35.6672, -105.964);
	public static final City Albany        = new City("Albany",         "NY", 42.6525, -73.7572);
	public static final City Raleigh       = new City("Raleigh",        "NC", 35.7667, -78.6333);
	public static final City Bismarck      = new City("Bismarck",       "ND", 46.8133, -100.779);
	public static final City Columbus      = new City("Columbus",       "OH", 39.9833, -82.9833);
	public static final City OklahomaCity  = new City("Oklahoma City",  "OK", 35.4822, -97.5350);
	public static final City Salem         = new City("Salem",          "OR", 44.9308, -123.029);
	public static final City Harrisburg    = new City("Harrisburg",     "PA", 40.2697, -76.8756);
	public static final City Providence    = new City("Providence",     "RI", 41.8236, -71.4222);
	public static final City Columbia      = new City("Columbia",       "SC", 34.0006, -81.0347);
	public static final City Pierre        = new City("Pierre",         "SD", 44.3680, -100.336);
	public static final City Nashville     = new City("Nashville",      "TN", 36.1667, -86.7833);
	public static final City Austin        = new City("Austin",         "TX", 30.2500, -97.7500);
	public static final City SaltLakeCity  = new City("Salt Lake City", "UT", 40.7500, -111.883);
	public static final City Montpelier    = new City("Montpelier",     "VT", 44.2597, -72.5750);
	public static final City Richmond      = new City("Richmond",       "VA", 37.5333, -77.4667);
	public static final City Olympia       = new City("Olympia",        "WA", 47.0425, -122.893);
	public static final City Charleston    = new City("Charleston",     "WV", 38.3472, -81.6333);
	public static final City Madison       = new City("Madison",        "WI", 43.0667, -89.4000);
	public static final City Cheyenne      = new City("Cheyenne",       "WY", 41.1456, -104.802);

	private static final City[] capitals = {
		Albany,
		Annapolis,
		Atlanta,
		Augusta,
		Austin,
		BatonRouge,
		Bismarck,
		Boise,
		Boston,
		CarsonCity,
		Charleston,
		Cheyenne,
		Columbia,
		Columbus,
		Concord,
		Denver,
		DesMoines,
		Dover,
		Frankfort,
		Harrisburg,
		Hartford,
		Helena,
		Honolulu,
		Indianapolis,
		Jackson,
		JeffersonCity,
		Juneau,
		Lansing,
		Lincoln,
		LittleRock,
		Madison,
		Montgomery,
		Montpelier,
		Nashville,
		OklahomaCity,
		Olympia,
		Phoenix,
		Pierre,
		Providence,
		Raleigh,
		Richmond,
		Sacramento,
		SaintPaul,
		Salem,
		SaltLakeCity,
		SantaFe,
		Springfield,
		Tallahassee,
		Topeka,
		Trenton,
	};

	public static City find(String name) {
		for (City city : capitals) {
			if (name.equals(city.name())) return city;
			if (name.equals(city.state())) return city;
		}
		return null;
	}
}
