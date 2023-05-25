
package acme.testing.lecturer.lecturesInCourse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerLecturesInCourseDeleteTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lectures-in-course/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int lecRecordIndex, final String courseCode) {
		// HINT: this test authenticates as a lecturer and then lists his or her
		// HINT: lectures, add one to a course, and check that it's been added properly.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My Lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(lecRecordIndex);
		super.clickOnButton("Delete from course");

		super.fillInputBoxIn("course", courseCode);
		super.clickOnSubmit("Remove from course");

		super.checkNotErrorsExist();

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lectures-in-course/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int lecRecordIndex, final String courseCode) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My Lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(lecRecordIndex);
		super.clickOnButton("Delete from course");

		super.fillInputBoxIn("course", courseCode);
		super.clickOnSubmit("Remove from course");

		super.checkErrorsExist();

		super.signOut();

	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to add a lecture to a course using 
		// HINT+ inappropriate roles.

		super.checkLinkExists("Sign in");
		super.request("/lecturer/lectures-in-course/delete");
		super.checkPanicExists();

		super.signIn("administrator1", "administrator1");
		super.request("/lecturer/lectures-in-course/delete");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/lecturer/lectures-in-course/delete");
		super.checkPanicExists();
		super.signOut();

	}
}
