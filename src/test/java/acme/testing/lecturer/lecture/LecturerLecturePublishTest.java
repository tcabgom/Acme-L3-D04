
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lecture.Lecture;
import acme.testing.TestHarness;

public class LecturerLecturePublishTest extends TestHarness {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title) {
		// HINT: this test logs in as a lecturer, lists his or her courses, 
		// HINT+ selects one of them, updates it, and then checks that 
		// HINT+ the update has actually been performed.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My Lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because
		// HINT+ there is no button when it can't be published.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to update a lecture with a role other than "Lecturer",
		// HINT+ or using a lecturer who is not the owner.

		Collection<Lecture> lectures;
		String param;

		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer1");
		for (final Lecture lecture : lectures) {
			param = String.format("id=%d", lecture.getId());

			super.checkLinkExists("Sign in");
			super.request("/lecturer/lecture/publish", param);
			super.checkPanicExists();

			super.signIn("administrator1", "administrator1");
			super.request("/lecturer/lecture/publish", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant1", "assistant1");
			super.request("/lecturer/lecture/publish", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {
		Collection<Lecture> lectures;
		String params;

		super.signIn("assistant1", "assistant1");
		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer1");
		for (final Lecture lecture : lectures)
			if (!lecture.isDraftMode()) {
				params = String.format("id=%d", lecture.getId());
				super.request("/lecturer/lecture/publish", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to publish a lecture that wasn't registered by the principal,
		// HINT+ be it published or unpublished.

		Collection<Lecture> lectures;
		String params;

		super.signIn("lecturer2", "lecturer2");
		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer1");
		for (final Lecture lecture : lectures) {
			params = String.format("id=%d", lecture.getId());
			super.request("/lecturer/lecture/publish", params);
		}
		super.signOut();
	}
}
