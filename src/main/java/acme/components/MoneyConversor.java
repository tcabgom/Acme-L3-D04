
package acme.components;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import acme.framework.components.datatypes.Money;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MoneyConversor {

	public boolean checkMoneyNeedsConversion(final Money m) {
		return true; // SystemConfiguration.getSystemCurrency() != m.getCurrency()
	}

	public Money performMoneyConversion(final Money input) {
		final Money output = new Money();
		final String inputCurrency = input.getCurrency();
		final String outputCurrency = "EUR";
		final String apiKey = "sel9cRBhSI9IT0etGGr2lVnoBviM5SaRSah9Y3bv";

		final String url = String.format("https://api.freecurrencyapi.com/v1/latest?key=%s&base_currency=%s&currencies=%s", apiKey, inputCurrency, outputCurrency);
		final OkHttpClient httpClient = new OkHttpClient();
		final Request request = new Request.Builder().url(url).build();

		try {
			final ObjectMapper object = new ObjectMapper();
			final Response response = httpClient.newCall(request).execute();
			output.setAmount(object.readTree(response.body().string()).get("data").get(input.getCurrency()).asDouble() * input.getAmount());
		} catch (final IOException exception) {
			exception.getMessage();
		}

		return output;
	}

}
