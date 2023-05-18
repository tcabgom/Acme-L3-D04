
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialSessionListTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial-session/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final String title, final int tutorialSessionRecordIndex, final String sessionTitle, final String sessionType) {
		// HINT: this test authenticates as an assistant, lists his or her tutorials only,
		// HINT+ selects one of them, and check that it has the expected sessions.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(2, "desc");

		// Buscar el tutorial con las sesiones de prueba
		super.checkColumnHasValue(tutorialRecordIndex, 0, title);
		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkInputBoxHasValue("Title", title);
		super.clickOnButton("Manage Sessions");

		// Comprobar valor de las sesiones de prueba
		super.checkListingExists();
		super.checkColumnHasValue(tutorialSessionRecordIndex, 0, sessionTitle);
		super.checkColumnHasValue(tutorialSessionRecordIndex, 1, sessionType);

		super.signOut();

	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a listing that
		// HINT+ doesn't involve entering any data into any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list the sessions of a tutorisl that is unpublished
		// HINT+ using a principal that didn't create it.

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial tutorial : tutorials)
			if (tutorial.isDraftMode()) {
				param = String.format("tutorialId=%d", tutorial.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial-session/list", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial-session/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant2", "assistant2");
				super.request("/assistant/tutorial-session/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial-session/list", param);
				super.checkPanicExists();
				super.signOut();
			}

	}

}
