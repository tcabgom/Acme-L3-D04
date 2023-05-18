
package acme.testing.assistant.tutorialSession;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AssistantTutorialSessionCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial-session/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final String title, final int tutorialSessionRecordIndex, final String sessionAbstract, final String sessionType, final String sessionStart, final String sessionEnd, final String moreInfo) {
		// HINT: this test authenticates as an assistant and then lists his or her
		// HINT: tutorials sessions, creates a new one, and check that it's been created properly.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My Tutorials");
		super.clickOnButton("Create");
		super.checkFormExists();

		super.clickOnMenu("Assistant", "My Tutorials");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.checkColumnHasValue(tutorialRecordIndex, 0, title);
		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkInputBoxHasValue("Title", title);
		super.clickOnButton("Manage Sessions");
		super.clickOnButton("Create New Tutorial Session");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract", sessionAbstract);
		super.fillInputBoxIn("sessionType", sessionType);
		super.fillInputBoxIn("sessionStart", sessionStart);
		super.fillInputBoxIn("sessionEnd", sessionEnd);
		super.fillInputBoxIn("moreInfo", moreInfo);

		super.clickOnSubmit("Create Session");
		super.checkNotErrorsExist();

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial-session/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialRecordIndex, final String title, final int tutorialSessionRecordIndex, final String sessionAbstract, final String sessionType, final String sessionStart, final String sessionEnd, final String moreInfo) {

	}

}
