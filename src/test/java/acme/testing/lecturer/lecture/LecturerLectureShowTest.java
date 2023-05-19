
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lecture.Lecture;
import acme.testing.TestHarness;

public class LecturerLectureShowTest extends TestHarness {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String lecAbstract, final String learningTime, final String body, final String knowledge, final String furtherInformation, final String draftMode) {
		// HINT: this test signs in as a lecturer, lists all of the lectures, click on  
		// HINT+ one of them, and checks that the form has the expected data.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My Lectures");
		super.sortListing(0, "asc");
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

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a listing
		// HINT+ that doesn't involve entering any data in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show an unpublished lecture by someone who is not the principal.

		Collection<Lecture> lectures;
		String param;

		lectures = this.repository.findManyLecturesByLecturerUsername("employer1");
		for (final Lecture lecture : lectures)
			if (lecture.isDraftMode()) {
				param = String.format("id=%d", lecture.getId());

				super.checkLinkExists("Sign in");
				super.request("/lecturer/lecture/show", param);
				super.checkPanicExists();

				super.signIn("administrator1", "administrator1");
				super.request("/lecturer/lecture/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer2", "lecturer2");
				super.request("/lecturer/lecture/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/lecturer/lecture/show", param);
				super.checkPanicExists();
				super.signOut();
			}
	}
}
