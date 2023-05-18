
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lecture.Lecture;
import acme.testing.TestHarness;

public class LecturerLectureUpdateTest extends TestHarness {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String lecAbstract, final String learningTime, final String body, final String knowledge, final String furtherInformation, final String draftMode) {
		// HINT: this test logs in as a Lecturer, lists his or her lectures, 
		// HINT+ selects one of them, updates it, and then checks that 
		// HINT+ the update has actually been performed.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My Lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		//		super.checkColumnHasValue(recordIndex, 0, title);
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("lecAbstract", lecAbstract);
		super.fillInputBoxIn("learningTime", learningTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("knowledge", knowledge);
		super.fillInputBoxIn("furtherInformation", furtherInformation);
		super.clickOnSubmit("Update");

		super.checkListingExists();
		super.sortListing(0, "asc");
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
		super.checkInputBoxHasValue("draftMode", draftMode);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title1, final String title, final String lecAbstract, final String learningTime, final String body, final String knowledge, final String furtherInformation) {
		// HINT: this test attempts to update a lecture with wrong data.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My Lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, title1);
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("lecAbstract", lecAbstract);
		super.fillInputBoxIn("learningTime", learningTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("knowledge", knowledge);
		super.fillInputBoxIn("furtherInformation", furtherInformation);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to update a course with a role other than "Lecturer",
		// HINT+ or using a lecturer who is not the owner.

		Collection<Lecture> lectures;
		String param;

		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer1");
		for (final Lecture lecture : lectures) {
			param = String.format("id=%d", lecture.getId());

			super.checkLinkExists("Sign in");
			super.request("/lecturer/lecture/update", param);
			super.checkPanicExists();

			super.signIn("administrator1", "administrator1");
			super.request("/lecturer/lecture/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer2", "lecturer2");
			super.request("/lecturer/lecture/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant1", "assistant1");
			super.request("/lecturer/lecture/update", param);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
