package ru.mipt.sign.data.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mipt.sign.data.ImportData;
import ru.mipt.sign.forex.Bar;

public class ImportDataOutput extends ImportData {

	@Override
	protected void process(String str) {
		Double open, close, low, high;
		String buf;
		String sdata = str.substring(0, str.indexOf('-'));
		SimpleDateFormat format = new SimpleDateFormat(DATA_PATTERN, Locale.US);
        Date date = null;
		try {
			date = format.parse(sdata);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		str = str.substring(str.indexOf('-') + 1).trim();
		//Open
		buf = str.substring(0, str.indexOf(','));
		open = Double.parseDouble(buf);
		str = str.substring(str.indexOf(',') + 1);
		//Close
		buf = str.substring(0, str.indexOf(','));
		close = Double.parseDouble(buf);
		str = str.substring(str.indexOf(',') + 1);
		//Low
		buf = str.substring(0, str.indexOf(','));
		low = Double.parseDouble(buf);
		str = str.substring(str.indexOf(',') + 1);
		//High
		high = Double.parseDouble(str);
		data.add(new Bar(date, open, close, low, high));
		//"add" method adds item to the end of the list, so we need sort data in file from newest to latest
	}

}
