
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialSessionShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial-session/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final String title, final int sessionRecordIndex, final String sessionTitle, final String sessionAbstract, final String sessionType, final String sessionStart, final String sessionEnd,
		final String moreInfo) {
		// HINT: this test signs in as an assistant, lists his or her tutorials, selects
		// HINT+ one of them and checks that it's as expected.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.checkColumnHasValue(tutorialRecordIndex, 0, title);
		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkInputBoxHasValue("Title", title);
		super.clickOnButton("Manage Sessions");

		super.sortListing(0, "asc");
		super.clickOnListingRecord(sessionRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", sessionTitle);
		super.checkInputBoxHasValue("abstract", sessionAbstract);
		super.checkInputBoxHasValue("sessionType", sessionType);
		super.checkInputBoxHasValue("sessionStart", sessionStart);
		super.checkInputBoxHasValue("sessionEnd", sessionEnd);
		super.checkInputBoxHasValue("moreInfo", moreInfo);

		super.signOut();

	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list the sessions of a tutorisl that is unpublished
		// HINT+ using a principal that didn't create it.

		Collection<Tutorial> tutorials;
		String param;

		super.signIn("assistant1", "assistant1");
		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial tutorial : tutorials)
			if (tutorial.isDraftMode()) {
				param = String.format("tutorialId=%d", tutorial.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial-session/show", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial-session/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant2", "assistant2");
				super.request("/assistant/tutorial-session/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial-session/show", param);
				super.checkPanicExists();
				super.signOut();
			}

	}

}
