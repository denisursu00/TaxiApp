package ro.cloudSoft.common.utils.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Builder pentru construirea programatica a unui URL cu parametri
 * 
 * 
 * @version 2011.03.04
 */
public class UrlBuilder {

    private static final String FORMAT_PARAMETER_AND_VALUE = "%s=%s";

    private final String address;

    private Map<String, Object> parameterMap = Maps.newLinkedHashMap(); // Trebuie pastrata ordinea.

    public UrlBuilder(String address) {
        if (address == null) {
            throw new IllegalArgumentException("Adresa URL-ului nu poate fi nula.");
        }
        this.address = address;
    }

    public UrlBuilder setParameter(String name, Object value) {
        parameterMap.put(name, value);
        return this;
    }

    public String build() {

        StringBuilder url = new StringBuilder(address);

        boolean isFirstParameter = true;
        for (String parameterName : parameterMap.keySet()) {

            Object parameterValue = parameterMap.get(parameterName);
            if (parameterValue == null) {
                continue;
            }

            String parameterValueAsString = null;
            try {
                parameterValueAsString = URLEncoder.encode(parameterValue.toString(), "UTF-8");
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException("Nu s-a putut gasi encoding-ul UTF-8!", uee);
            }

            url.append(isFirstParameter ? "?" : "&");
            url.append(String.format(FORMAT_PARAMETER_AND_VALUE, parameterName, parameterValueAsString));

            isFirstParameter = false;
        }

        return url.toString();
    }
}