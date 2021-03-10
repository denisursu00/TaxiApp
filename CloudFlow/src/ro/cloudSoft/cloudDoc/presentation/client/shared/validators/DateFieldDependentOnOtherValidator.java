package ro.cloudSoft.cloudDoc.presentation.client.shared.validators;

import java.util.Date;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 * Valideaza un camp de tip data, comparand valoarea cu valoarea altui camp de tip data.
 * 
 * 
 */
public class DateFieldDependentOnOtherValidator implements Validator {

	private final ComparisonType comparisonType;
	private final DateField otherDateField;
	
	/**
	 * @param comparisonType tipul comparatiei
	 * @param dateField campul de tip data ce va fi validat
	 * @param otherDateField campul de tip data al carei valoare va fi folosita in comparatie
	 */
	public DateFieldDependentOnOtherValidator(final ComparisonType comparisonType, final DateField dateField, final DateField otherDateField) {
		
		this.comparisonType = comparisonType;
		this.otherDateField = otherDateField;
		
		this.otherDateField.getDatePicker().addListener(Events.Select, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent event) {
				/*
				 * Avand in vedere ca cele 2 campuri sunt legate intre ele,
				 * cand se schimba valoarea campului cu care se face comparatia,
				 * trebuie sa validez campul corespunzator validatorului.
				 */
				dateField.validate();
			}
		});
	}

	@Override
	public String validate(Field<?> field, String value) {
		
		DateField dateField = (DateField) field;
		
		Date date = dateField.getValue();
		Date otherDate = otherDateField.getValue();
		
		if ((date == null) || (otherDate == null)) {
			// Nu pot compara datele daca una din ele e nula.
			return null;
		}
		
		if (!comparisonType.complies(date, otherDate)) {
			return comparisonType.getLocalizedErrorMessage(otherDateField.getFieldLabel());
		} else {
			return null;
		}
	}
	
	/**
	 * Tipul comparatiei intre 2 date calendaristice
	 * 
	 * 
	 */
	public static enum ComparisonType {
		
		LESS_THAN_OR_EQUAL() {
			
			@Override
			public boolean complies(Date date, Date otherDate) {
				return (date.compareTo(otherDate) <= 0);
			}
			
			@Override
			public String getLocalizedErrorMessage(String otherDateName) {
				return GwtLocaleProvider.getMessages().DATE_MUST_BE_LESS_THAN_OR_EQUAL(otherDateName);
			}
		},
		MORE_THAN_OR_EQUAL() {
			
			@Override
			public boolean complies(Date date, Date otherDate) {
				return (date.compareTo(otherDate) >= 0);
			}
			
			@Override
			public String getLocalizedErrorMessage(String otherDateName) {
				return GwtLocaleProvider.getMessages().DATE_MUST_BE_MORE_THAN_OR_EQUAL(otherDateName);
			}
		};
		
		/**
		 * Verifica daca data specificata este conform cu cealalta d.p.d.v. a tipului comparatiei.
		 * 
		 * @param date data specificata
		 * @param otherDate cealalta data cu care se face comparatia
		 * 
		 * @return true / false
		 */
		public abstract boolean complies(Date date, Date otherDate);
		
		/**
		 * Returneaza mesajul de eroare corespunzator tipului de comparatie.
		 * In mesaj se va include si numele datei cu care s-a facut comparatia si a esuat.
		 */
		public abstract String getLocalizedErrorMessage(String otherDateName);
	}
}