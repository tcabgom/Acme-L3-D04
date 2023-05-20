
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String course, final String code, final String tutorialAbstract, final String goals, final String estimatedTotalHours, final String numberOfSessions, final String draftMode) {
		// HINT: this test authenticates as an assistant, lists his or her tutorials, clicks on  
		// HINT+ one of them, and then checks that the listing has the expected data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("tutorialAbstract", tutorialAbstract);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalHours);
		super.checkInputBoxHasValue("numberOfSessions", numberOfSessions);
		super.checkInputBoxHasValue("draftMode", draftMode);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a listing
		// HINT+ that doesn't involve entering any data in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show an unpublished tutorial by someone who is not the principal.

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");
		for (final Tutorial t : tutorials)
			if (t.isDraftMode()) {

				param = String.format("id=%d", t.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/show", param);
				super.checkPanicExists();

				super.signIn("administrator1", "administrator1");
				super.request("/assistant/tutorial/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant2", "assistant2");
				super.request("/assistant/tutorial/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial/show", param);
				super.checkPanicExists();
				super.signOut();

			}

	}

}
