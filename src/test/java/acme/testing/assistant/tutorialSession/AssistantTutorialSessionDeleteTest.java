
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorialSession.TutorialSession;
import acme.testing.TestHarness;

public class AssistantTutorialSessionDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionTestRepository repository;

	// Test data --------------------------------------------------------------


	@Test
	public void test100Positive() {

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.clickOnButton("Manage Sessions");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(0, 0, "<h1>Este atributo es muy bonito</h1>");
		super.checkColumnHasValue(1, 0, "A normal Title ");

		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.clickOnSubmit("Delete Session");
		super.checkNotErrorsExist();

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.clickOnButton("Manage Sessions");
		super.checkColumnHasValue(1, 0, "A normal Title");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(0, 0, "A normal Title");

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
