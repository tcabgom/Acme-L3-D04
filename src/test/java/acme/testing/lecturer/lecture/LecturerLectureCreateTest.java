
package acme.testing.lecturer.lecture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerLectureCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String lecAbstract, final String learningTime, final String body, final String knowledge, final String furtherInformation, final String draftMode) {
		// HINT: this test authenticates as a lecturer and then lists his or her
		// HINT: lectures, creates a new one, and check that it's been created properly.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My Lectures");
		super.checkListingExists();

		super.clickOnButton("Create lecture");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("lecAbstract", lecAbstract);
		super.fillInputBoxIn("learningTime", learningTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("knowledge", knowledge);
		super.fillInputBoxIn("furtherInformation", furtherInformation);
		super.clickOnSubmit("Create");

		super.clickOnMenu("Lecturer", "My Lectures");
		super.checkListingExists();
		super.sortListing(0, "desc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, lecAbstract);
		super.checkColumnHasValue(recordIndex, 2, learningTime);
		super.checkColumnHasValue(recordIndex, 3, knowledge);
		super.checkColumnHasValue(recordIndex, 4, draftMode);
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("lecAbstract", lecAbstract);
		super.checkInputBoxHasValue("learningTime", learningTime);
		super.checkInputBoxHasValue("body", body);
		super.checkInputBoxHasValue("knowledge", knowledge);
		super.checkInputBoxHasValue("furtherInformation", furtherInformation);

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title, final String lecAbstract, final String learningTime, final String body, final String knowledge, final String furtherInformation) {
		// HINT: this test attempts to create lectures with incorrect data.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My Lectures");
		super.clickOnButton("Create lecture");
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("lecAbstract", lecAbstract);
		super.fillInputBoxIn("learningTime", learningTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("knowledge", knowledge);
		super.fillInputBoxIn("furtherInformation", furtherInformation);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create a lecture using 
		// HINT+ inappropriate roles.

		super.checkLinkExists("Sign in");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();

		super.signIn("administrator1", "administrator1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

	}
}
