package acme.testing.student.enrolment;

import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class StudentEnrolmentDeleteTest extends TestHarness {

    @ParameterizedTest
    @CsvFileSource(resources = "/student/enrolment/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test100positive(final String code, final String motivation, final String goals, final String course) {
        super.signIn("student1", "student1");

        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(1, "asc");

        super.checkColumnHasValue(1,1,code);
        super.clickOnListingRecord(0);

        super.clickOnSubmit("Delete");
        super.clickOnListingRecord(0);

        super.checkInputBoxHasValue("code", code);
        super.checkInputBoxHasValue("motivation", motivation);
        super.checkInputBoxHasValue("goals", goals);
        super.checkInputBoxHasValue("course", course);

        super.signOut();
    }

}
