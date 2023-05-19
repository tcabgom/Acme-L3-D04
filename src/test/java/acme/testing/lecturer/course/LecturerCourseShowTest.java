
package acme.testing.lecturer.course;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lecture.Course;
import acme.testing.TestHarness;

public class LecturerCourseShowTest extends TestHarness {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String courseAbstract, final String retailPrice, final String furtherInformation, final String activityType) {
		// HINT: this test authenticates as a lecturer, lists his or her courses, clicks on  
		// HINT+ one of them, and then checks that the listing has the expected data.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "My Courses");
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("courseAbstract", courseAbstract);
		super.checkInputBoxHasValue("retailPrice", retailPrice);
		super.checkInputBoxHasValue("furtherInformation", furtherInformation);
		super.checkInputBoxHasValue("activityType", activityType);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a listing
		// HINT+ that doesn't involve entering any data in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show an unpublished course by someone who is not the principal.

		Collection<Course> courses;
		String param;

		courses = this.repository.findManyCoursesByLecturerUsername("lecturer1");
		for (final Course c : courses)
			if (c.isDraftMode()) {

				param = String.format("id=%d", c.getId());

				super.checkLinkExists("Sign in");
				super.request("/lecturer/course/show", param);
				super.checkPanicExists();

				super.signIn("administrator1", "administrator1");
				super.request("/lecturer/course/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer2", "lecturer2");
				super.request("/lecturer/course/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/lecturer/course/show", param);
				super.checkPanicExists();
				super.signOut();

			}
	}
}
