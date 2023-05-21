package acme.testing.student.enrolment;

import acme.framework.testing.AbstractTest;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class StudentEnrolmentListTest extends TestHarness {

    @ParameterizedTest
    @CsvFileSource(resources = "/student/enrolment/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test100Positive(final int recordIndex, final String course, final String code, final String motivation) {
        // HINT: this test authenticates as a student, lists his or her enrolments only,
        // HINT+ and then checks that the listing has the expected data.

        signIn("student1", "student1");
        super.clickOnMenu("Student", "See your enrolments");
        super.checkListingExists();

        super.sortListing(1, "asc");

        super.checkColumnHasValue(recordIndex, 0, course);
        super.checkColumnHasValue(recordIndex, 1, code);
        super.checkColumnHasValue(recordIndex, 2, motivation);

        super.signOut();
    }

    @Test
    public void test200Negative() {
        // HINT: there aren't any negative tests for this feature because it's a listing
        // HINT+ that doesn't involve entering any data in any forms.
    }

    @Test
    public void test300Hacking() {

        super.signIn("administrator1", "administrator1");
        super.request("/student/enrolment/list");
        super.checkPanicExists();
        super.signOut();

        super.signIn("lecturer1", "lecturer1");
        super.request("/student/enrolment/list");
        super.checkPanicExists();
        super.signOut();

    }

}
