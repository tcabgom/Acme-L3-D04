
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorialSession.TutorialSession;
import acme.testing.TestHarness;

public class AssistantTutorialSessionDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial-session/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int firstRecordIndex, final String firstTitle, final int secondRecordIndex, final String secondTitle) {

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.clickOnButton("Manage Sessions");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(firstRecordIndex, 0, firstTitle);
		super.checkColumnHasValue(secondRecordIndex, 0, secondTitle);

		super.clickOnListingRecord(firstRecordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Delete Session");
		super.checkNotErrorsExist();

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.clickOnButton("Manage Sessions");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(secondRecordIndex - 1, 0, secondTitle);

		super.signOut();

	}

	@Test
	public void test200Negative() {
		// HINT: There aren't any negative tests for this feature because you can't
		// HINT+ unsuccessfully delete a tutorial.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list the sessions of a tutorisl that is unpublished
		// HINT+ using a principal that didn't create it.

		Collection<TutorialSession> tutorialSessions;
		String param;

		tutorialSessions = this.repository.findManyTutorialSessionsByAssistantUsername("assistant1");

		for (final TutorialSession session : tutorialSessions) {

			param = String.format("id=%d", session.getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/tutorial-session/delete", param);
			super.checkPanicExists();

			super.signIn("administrator1", "administrator1");
			super.request("/assistant/tutorial-session/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant2", "assistant2");
			super.request("/assistant/tutorial-session/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/tutorial-session/delete", param);
			super.checkPanicExists();
			super.signOut();

		}
	}

}
