package ro.cloudSoft.common.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class NumberUtils {
	
	public static double parseNumber(String numberAsString, String format, char decimalSeparator, char groupingSeparator) throws ParseException {
	    DecimalFormatSymbols symbols = getSymbols(decimalSeparator, groupingSeparator);
		return parseNumber(numberAsString, format, symbols);
	}
	
	public static double parseNumber(String numberAsString, String format, String decimalSeparator, String groupingSeparator) throws ParseException {
		if ((decimalSeparator.length() != 1) || (groupingSeparator.length() != 1)) {
			throw new IllegalArgumentException("Separatoarele trebuie sa fie caractere ([" + decimalSeparator + "], [" + groupingSeparator + "])");
		}
		return parseNumber(numberAsString, format, decimalSeparator.charAt(0), groupingSeparator.charAt(0));
	}
	
	public static double parseNumber(String numberAsString, String format, DecimalFormatSymbols symbols) throws ParseException {
	    DecimalFormat formatter = getFormatter(format, symbols);
	    return formatter.parse(numberAsString).doubleValue();
	}
	
	public static String formatNumber(Number number, String format, char decimalSeparator, char groupingSeparator) {
	    DecimalFormatSymbols symbols = getSymbols(decimalSeparator, groupingSeparator);
		return formatNumber(number, format, symbols);
	}
	
	public static String formatNumber(Number number, String format, DecimalFormatSymbols symbols) {
	    DecimalFormat formatter = getFormatter(format, symbols);
		return formatter.format(number);
	}
	
	private static DecimalFormatSymbols getSymbols(char decimalSeparator, char groupingSeparator) {
	    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
	    symbols.setDecimalSeparator(decimalSeparator);
	    symbols.setGroupingSeparator(groupingSeparator);
		return symbols;
	}
	
	public static DecimalFormat getFormatter(String format, char decimalSeparator, char groupingSeparator) {
		return getFormatter(format, getSymbols(decimalSeparator, groupingSeparator));
	}
	
	private static DecimalFormat getFormatter(String format, DecimalFormatSymbols symbols) {
		
	    // Constructor cu BUG - nu va tine cont de simboluri si va da exceptie la formatul meu (0,### - virgula fiind separator zecimale).
	    // DecimalFormat formatter = new DecimalFormat(format, symbols)
	    
	    DecimalFormat formatter = new StrictDecimalFormat();
	    formatter.setDecimalFormatSymbols(symbols);
	    formatter.applyLocalizedPattern(format);
		return formatter;
	}
	
	public static int divideRoundingUp(int dividend, int divisor) {
		int quotient = dividend / divisor;
		int remainder = dividend % divisor;
		return ((remainder > 0) ? (quotient + 1) : quotient);
	}
}