package es.usefulearnings.engine;

import es.usefulearnings.engine.CoreSubscription;
import es.usefulearnings.entities.Company;

import java.util.HashMap;
import java.util.Map;

public class CoreSubscriptionImplementation implements CoreSubscription {

	final private Map<String, Company> companyMap;

	public CoreSubscriptionImplementation(final Map<String, Company> companyMap) {
		this.companyMap = companyMap;
	}


	@Override
	public void onLoaded() {

	}

	@Override
	public void onReloading() {

	}

	@Override
	public void onLoading() {

	}
}
