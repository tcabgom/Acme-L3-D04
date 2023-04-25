
package acme.components;

import java.io.IOException;

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
		final String outputCurrency = "EUR";
		final String apiURL = "westilldonthaveone";
		final String apiKey = "westilldonthaveone";

		final String url = String.format("%?key=%s", apiURL, apiKey);
		final OkHttpClient httpClient = new OkHttpClient();
		final Request request = new Request.Builder().url(url).build();

		try {
			final Response response = httpClient.newCall(request).execute();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return output;
	}

}
