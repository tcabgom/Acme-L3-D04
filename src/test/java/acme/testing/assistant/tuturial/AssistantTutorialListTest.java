
package acme.testing.assistant.tuturial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AssistantTutorialListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive() {
		// HINT: this test authenticates as an assistant, lists his or her tutorials only,
		// HINT+ and then checks that the listing has the expected data.

		super.signIn("assistant1", "assistant1");
		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		// Comprobar valor de las sesiones de prueba

		super.signOut();

	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a listing
		// HINT+ that doesn't involve entering any data in any forms.
	}

}
