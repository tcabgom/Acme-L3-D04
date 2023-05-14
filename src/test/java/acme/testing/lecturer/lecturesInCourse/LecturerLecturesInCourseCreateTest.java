
package acme.testing.lecturer.lecturesInCourse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerLecturesInCourseCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lectures-in-course/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int lecRecordIndex, final int courseRecordIndex, final int lecInCourseRecordIndex, final String course, final String title, final String lecAbstract, final String learningTime, final String body,
		final String knowledge, final String furtherInformation, final String draftMode) {
		// HINT: this test authenticates as a lecturer and then lists his or her
		// HINT: lectures, add one to a course, and check that it's been added properly.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My Lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(lecRecordIndex);
		super.clickOnButton("Add to a course");

		super.fillInputBoxIn("course", course);
		super.clickOnSubmit("Add to the course");

		super.clickOnMenu("Lecturer", "My Courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(courseRecordIndex);
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnButton("Lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(lecInCourseRecordIndex, 0, title);
		super.checkColumnHasValue(lecInCourseRecordIndex, 1, lecAbstract);
		super.checkColumnHasValue(lecInCourseRecordIndex, 2, learningTime);
		super.checkColumnHasValue(lecInCourseRecordIndex, 3, knowledge);

		super.clickOnListingRecord(lecInCourseRecordIndex);
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
	@CsvFileSource(resources = "/lecturer/lectures-in-course/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title, final String lecAbstract, final String learningTime, final String body, final String knowledge, final String furtherInformation) {
		// HINT: this test attempts to add lectures to a course with incorrect data.
		/*
		 * super.signIn("lecturer1", "lecturer1");
		 * 
		 * super.clickOnMenu("Lecturer", "My Lectures");
		 * super.clickOnButton("Create");
		 * super.checkFormExists();
		 * 
		 * super.fillInputBoxIn("title", title);
		 * super.fillInputBoxIn("lecAbstract", lecAbstract);
		 * super.fillInputBoxIn("learningTime", learningTime);
		 * super.fillInputBoxIn("body", body);
		 * super.fillInputBoxIn("knowledge", knowledge);
		 * super.fillInputBoxIn("furtherInformation", furtherInformation);
		 * super.clickOnSubmit("Create");
		 * 
		 * super.checkErrorsExist();
		 * 
		 * super.signOut();
		 */
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to add a lecture to a course using 
		// HINT+ inappropriate roles.

		super.checkLinkExists("Sign in");
		super.request("/lecturer/lectures-in-course/create");
		super.checkPanicExists();

		super.signIn("administrator1", "administrator1");
		super.request("/lecturer/lectures-in-course/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/lecturer/lectures-in-course/create");
		super.checkPanicExists();
		super.signOut();

	}
}
