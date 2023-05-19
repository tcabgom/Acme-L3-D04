
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorialSession.TutorialSession;
import acme.testing.TestHarness;

public class AssistantTutorialSessionUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial-session/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final String title, final String sessionAbstract, final String sessionType, final String sessionStart, final String sessionEnd, final String moreInfo) {
		// HINT: this test authenticates as an assistant and then lists his or her
		// HINT: tutorials sessions, updates a new one, and check that it's been updated properly.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(0);
		super.clickOnButton("Manage Sessions");
		super.checkListingExists();
		super.clickOnListingRecord(0);
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("sessionAbstract", sessionAbstract);
		super.fillInputBoxIn("sessionType", sessionType);
		super.fillInputBoxIn("sessionStart", sessionStart);
		super.fillInputBoxIn("sessionEnd", sessionEnd);
		super.fillInputBoxIn("moreInfo", moreInfo);

		super.clickOnSubmit("Update Session");
		super.checkNotErrorsExist();

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(0);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("sessionAbstract", sessionAbstract);
		super.checkInputBoxHasValue("sessionType", sessionType);
		super.checkInputBoxHasValue("sessionStart", sessionStart);
		super.checkInputBoxHasValue("sessionEnd", sessionEnd);
		super.checkInputBoxHasValue("moreInfo", moreInfo);

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial-session/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final String title, final String sessionAbstract, final String sessionType, final String sessionStart, final String sessionEnd, final String moreInfo) {
		// HINT: this test authenticates as an assistant and then lists his or her
		// HINT: tutorials sessions, updates a new one, and check that it's been updated properly.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(0);
		super.clickOnButton("Manage Sessions");
		super.checkListingExists();
		super.clickOnListingRecord(0);
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("sessionAbstract", sessionAbstract);
		super.fillInputBoxIn("sessionType", sessionType);
		super.fillInputBoxIn("sessionStart", sessionStart);
		super.fillInputBoxIn("sessionEnd", sessionEnd);
		super.fillInputBoxIn("moreInfo", moreInfo);

		super.clickOnSubmit("Update Session");
		super.checkErrorsExist();
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
			super.request("/assistant/tutorial-session/update", param);
			super.checkPanicExists();

			super.signIn("administrator1", "administrator1");
			super.request("/assistant/tutorial-session/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant2", "assistant2");
			super.request("/assistant/tutorial-session/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/tutorial-session/update", param);
			super.checkPanicExists();
			super.signOut();

		}
	}

}
