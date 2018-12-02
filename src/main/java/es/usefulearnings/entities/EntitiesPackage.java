package es.usefulearnings.entities;

import java.io.*;
import java.util.Map;

public class EntitiesPackage implements Serializable, Savable {

	public final static String EXTENSION = ".epk";

	private final Map<String, Company> _companies;

	public long getDateId() {
		return _dateId;
	}

	private long _dateId;


	EntitiesPackage(final Map<String, Company> companies, final long dateId) {
		_companies = companies;
		_dateId = dateId;
	}

	public Map<String, Company> getCompanies() {
		return _companies;
	}

	@Override
	public void save(final File fileToSave) throws IOException {
		final String location = fileToSave.getAbsolutePath()
			+ File.separator
			+ _companies.size() + "C"
			+ EXTENSION;

		FileOutputStream data = new FileOutputStream(location);
		ObjectOutputStream stream = new ObjectOutputStream(data);
		stream.writeObject(this);
		stream.close();
		data.close();
	}
}