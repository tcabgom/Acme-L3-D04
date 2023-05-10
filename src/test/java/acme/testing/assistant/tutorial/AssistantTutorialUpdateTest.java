
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String course, final String tutorialAbstract, final String goals, final String estimatedTotalTime, final String numberOfSessions, final String draftMode,
		final String courseTitle) {
		// HINT: this test logs in as an assistant, lists his or her tutorials, 
		// HINT+ selects one of them, updates it, and then checks that 
		// HINT+ the update has actually been performed.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, title);
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("tutorialAbstract", tutorialAbstract);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("estimatedTotalTime", estimatedTotalTime);
		super.fillInputBoxIn("numberOfSessions", numberOfSessions);
		super.fillInputBoxIn("draftMode", draftMode);
		super.clickOnSubmit("Update");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, draftMode);
		super.checkColumnHasValue(recordIndex, 2, courseTitle);
		super.checkColumnHasValue(recordIndex, 3, numberOfSessions);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("tutorialAbstract", tutorialAbstract);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
		super.checkInputBoxHasValue("numberOfSessions", numberOfSessions);
		super.checkInputBoxHasValue("draftMode", draftMode);

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String code, final String title, final String course, final String tutorialAbstract, final String goals, final String estimatedTotalTime, final String numberOfSessions, final String draftMode,
		final String courseTitle) {
		// HINT: this test attempts to update a tutorial with wrong data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, title);
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("tutorialAbstract", tutorialAbstract);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("estimatedTotalTime", estimatedTotalTime);
		super.fillInputBoxIn("numberOfSessions", numberOfSessions);
		super.fillInputBoxIn("draftMode", draftMode);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();

	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to update a tutorial with a role other than "Assistant",
		// HINT+ or using an assistant who is not the owner.

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");
		for (final Tutorial t : tutorials)
			if (t.isDraftMode()) {

				param = String.format("id=%d", t.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/update", param);
				super.checkPanicExists();

				super.signIn("administrator1", "administrator1");
				super.request("/assistant/tutorial/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant2", "assistant2");
				super.request("/assistant/tutorial/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial/update", param);
				super.checkPanicExists();
				super.signOut();

			}

	}

}
