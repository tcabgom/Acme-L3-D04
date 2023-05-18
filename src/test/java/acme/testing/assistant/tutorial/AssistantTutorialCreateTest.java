
package acme.testing.assistant.tutorial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AssistantTutorialCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String course, final String tutorialAbstract, final String goals, final String estimatedTotalTime, final String numberOfSessions, final String draftMode,
		final String courseTitle) {
		// HINT: this test authenticates as an assistant and then lists his or her
		// HINT: tutorials, creates a new one, and check that it's been created properly.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.clickOnButton("Create");
		super.checkFormExists();

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("tutorialAbstract", tutorialAbstract);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("estimatedTotalTime", estimatedTotalTime);
		super.fillInputBoxIn("numberOfSessions", numberOfSessions);
		super.fillInputBoxIn("draftMode", draftMode);
		super.clickOnSubmit("Create");
		super.checkNotErrorsExist();

		super.clickOnMenu("Assistant", "My Tutorials");
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

		super.clickOnButton("Manage Sessions");

		super.checkListingExists();
		super.checkListingEmpty();

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String code, final String title, final String course, final String tutorialAbstract, final String goals, final String estimatedTotalTime, final String numberOfSessions, final String draftMode) {
		// HINT: this test authenticates as an assistant and then lists his or her
		// HINT: tutorials, creates a new one, and check that it's been created properly.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.clickOnButton("Create");
		super.checkFormExists();

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("tutorialAbstract", tutorialAbstract);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("estimatedTotalTime", estimatedTotalTime);
		super.fillInputBoxIn("numberOfSessions", numberOfSessions);
		super.fillInputBoxIn("draftMode", draftMode);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();

	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create a tutorial using 
		// HINT+ inappropriate roles.

		super.checkLinkExists("Sign in");
		super.request("/assistant/tutorial/create");
		super.checkPanicExists();

		super.signIn("administrator1", "administrator1");
		super.request("/assistant/tutorial/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/assistant/tutorial/create");
		super.checkPanicExists();
		super.signOut();

	}

}
