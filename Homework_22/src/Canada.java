public final class Canada {
	
	public static class Province extends State {
		public Province(String code, String name) {
			super(code, name);
		}
	}

	public final static Province Alberta              = new Province("AB", "Alberta");
	public final static Province BritishColumbia      = new Province("BC", "BritishColumbia");
	public final static Province Manitoba             = new Province("MB", "Manitoba");
	public final static Province NewBrunswick         = new Province("NB", "NewBrunswick");
	public final static Province Newfoundland         = new Province("NL", "Newfoundland");
	public final static Province NorthwestTerritories = new Province("NT", "NorthwestTerritories");
	public final static Province NovaScotia           = new Province("NS", "NovaScotia");
	public final static Province NunavutTerritories   = new Province("NU", "NunavutTerritories");
	public final static Province Ontario              = new Province("ON", "Ontario");
	public final static Province PrinceEdwardIsland   = new Province("PE", "PrinceEdwardIsland");
	public final static Province Quebec               = new Province("QC", "Quebec");
	public final static Province Saskatchewan         = new Province("SK", "Saskatchewan");
	public final static Province Yukon                = new Province("YT", "Yukon");
		
    // List of all the provinces.

	public final static Province[] provinces = {
		Alberta,		BritishColumbia,		Manitoba, 			NewBrunswick,
		Newfoundland,	NorthwestTerritories, 	NovaScotia,			NunavutTerritories,
		Ontario, 		PrinceEdwardIsland,		Quebec,				Saskatchewan,
		Yukon
	};

	// Abbreviations for the provinces.

	public final static Province AB = Alberta;
	public final static Province BC = BritishColumbia;
	public final static Province MB = Manitoba;
	public final static Province NB = NewBrunswick;
	public final static Province NL = Newfoundland;
	public final static Province NT = NorthwestTerritories;
	public final static Province NS = NovaScotia;
	public final static Province NU = NunavutTerritories;
	public final static Province ON = Ontario;
	public final static Province PE = PrinceEdwardIsland;
	public final static Province QC = Quebec;
	public final static Province SK = Saskatchewan;
	public final static Province YT = Yukon;

	// Set the provincial capitals.

	static {
		capital(AB, "Edmonton",      54.5444, 113.4009);
		capital(BC, "Victoria",      48.4284, 123.3656);
		capital(MB, "Winnipeg",      49.8951,  97.1384);
		capital(NB, "Fredericton",   45.9636,  66.6421);
		capital(NL, "StJohns",       46.5615,  52.7126);
		capital(NT, "Yellowknife",   62.4540, 114.3718);
		capital(NS, "Halifax",       44.6488,  63.5752);
		capital(NU, "Iqaluit",       63.7467,  68.5170);
		capital(ON, "Toronto",       43.6532,  79.3832);
		capital(PE, "Charlottetown", 46.2382,  63.1311);
		capital(QC, "Quebec",        52.9399,  73.5491);
		capital(SK, "Regina",        50.4452, 104.6189);
		capital(YT, "Whitehorse",    60.7212, 135.0568);
	}

	// Set up the neighbors for each province.

	static {
		Alberta.neighbors(new Province[] {
			BritishColumbia,
			Yukon,
			NorthwestTerritories,
			Saskatchewan
		});

		BritishColumbia.neighbors(new Province[] {
			Yukon,
			NorthwestTerritories,
			Alberta
		});

		Manitoba.neighbors(new Province[] {
			Saskatchewan,
			NunavutTerritories,
			Ontario
		});

		NewBrunswick.neighbors(new Province[] {
			Quebec,
			PrinceEdwardIsland,
			NovaScotia
		});

		Newfoundland.neighbors(new Province[] {
			Quebec
		});

		NorthwestTerritories.neighbors(new Province[] {
			Yukon,
			NunavutTerritories,
			Saskatchewan,
			Alberta,
			BritishColumbia
		});

		NovaScotia.neighbors(new Province[] {
			NewBrunswick,
			PrinceEdwardIsland
		});

		NunavutTerritories.neighbors(new Province[] {
			NorthwestTerritories,
			Manitoba
		});

		Ontario.neighbors(new Province[] {
			Manitoba,
			Quebec
		});

 		PrinceEdwardIsland.neighbors(new Province[] {
			NewBrunswick,
			NovaScotia
		});

		Quebec.neighbors(new Province[] {
			Ontario,
			Newfoundland,
			NewBrunswick
		});

		Saskatchewan.neighbors(new Province[] {
			Alberta,
			NorthwestTerritories,
			Manitoba
		});

		Yukon.neighbors(new Province[] {
			NorthwestTerritories,
			BritishColumbia
		});

	}


	public static Province find(String name) {
		for (Province province : provinces) {
			if (province.name().equals(name)) return province;
			if (province.code().equals(name)) return province;
		}
		return null;
	}

	private static void capital(Province province, String name, double longitude, double latitude) {
		province.capital(new City(name, province, longitude, latitude));
	}
}
