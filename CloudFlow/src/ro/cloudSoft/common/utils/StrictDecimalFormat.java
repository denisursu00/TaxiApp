package ro.cloudSoft.common.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;

public class StrictDecimalFormat extends DecimalFormat {


	@Override
	public Number parse(String numberAsString) throws ParseException {
		
		ParsePosition parsePosition = new ParsePosition(0);
		Number number = parse(numberAsString, parsePosition);
		
		if (number == null) {
		    throw createException(numberAsString);
		}
		
		if (parsePosition.getIndex() != numberAsString.length()) {
			throw createException(numberAsString);
		}
		
		return number;
	}
	
	private ParseException createException(String numberAsString) {
	    return new ParseException("Valoarea [" + numberAsString + "] nu respecta formatul [" + toLocalizedPattern() + "].", 0);
	}
}