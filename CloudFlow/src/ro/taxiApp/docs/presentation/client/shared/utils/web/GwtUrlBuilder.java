package ro.taxiApp.docs.presentation.client.shared.utils.web;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 */
public class GwtUrlBuilder {

    private final String address;

    private Map<String, Object> parameterMap = new LinkedHashMap<String, Object>(); // Trebuie pastrata ordinea.

    public GwtUrlBuilder(String address) {
        if (address == null) {
            throw new IllegalArgumentException("Adresa URL-ului nu poate fi nula.");
        }
        this.address = address;
    }

    public GwtUrlBuilder setParameter(String name, Object value) {
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

            String parameterValueAsString = parameterValue.toString();

            url.append(isFirstParameter ? "?" : "&");
            url.append(parameterName).append("=").append(parameterValueAsString);

            isFirstParameter = false;
        }

        return url.toString();
    }
}