
package acme.testing.lecturer.lecture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerLectureListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseRecordIndex, final String code, final int lectureRecordIndex, final String title, final String lecAbstract, final String learningTime, final String knowledge, final String draftMode) {
		// HINT: this test authenticates as a lecturer, lists his or her lectures from one course only,
		// HINT+ and then checks that the listing has the expected data.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My Courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(courseRecordIndex, 0, code);
		super.clickOnListingRecord(courseRecordIndex);
		super.checkInputBoxHasValue("code", code);
		super.clickOnButton("Lectures");

		super.checkColumnHasValue(lectureRecordIndex, 0, title);
		super.checkColumnHasValue(lectureRecordIndex, 1, lecAbstract);
		super.checkColumnHasValue(lectureRecordIndex, 2, learningTime);
		super.checkColumnHasValue(lectureRecordIndex, 3, knowledge);
		super.checkColumnHasValue(lectureRecordIndex, 4, draftMode);

		super.signOut();

	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a listing
		// HINT+ that doesn't involve entering any data in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list all of the lectures using 
		// HINT+ inappropriate roles.

		super.checkLinkExists("Sign in");
		super.request("/lecturer/lecture/list");
		super.checkPanicExists();

		super.signIn("administrator1", "administrator1");
		super.request("/lecturer/lecture/list");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/lecturer/lecture/list");
		super.checkPanicExists();
		super.signOut();

	}
}
