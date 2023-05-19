package acme.entities.enrolment;


import acme.entities.activity.Activity;
import acme.entities.lecture.Course;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.data.AbstractEntity;
import acme.roles.Student;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Collection;

@Entity
@Getter
@Setter
public class Enrolment extends AbstractEntity {

    // Serialisation identifier -----------------------------------------------

    protected static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------

    @Pattern(regexp = "^[A-Z]{1,3}[0-9]{3}$")
    @NotBlank
    @Column(unique = true)
    protected String code;

    @NotBlank
    @Length(max = 75)
    protected String motivation;

    @NotBlank
    @Length(max = 100)
    protected String goals;

    protected String creditCardHolder;

    protected String creditCardNibble;

    protected boolean isFinished;

    // Derived attributes -----------------------------------------------------

    protected double workTime;

    // Relationships ----------------------------------------------------------

    @ManyToOne(optional = false)
    @Valid
    @NotNull
    protected Student student;

    @ManyToOne(optional = false)
    @Valid
    @NotNull
    protected Course course;

    public double getEstimatedTotalTimeInHours(final Collection<Activity> activities) {


        long totalMillis = activities.stream().mapToLong(a -> (a.getPeriodEnd().getTime() - a.getPeriodStart().getTime())).sum();

        System.out.println("Millis: " + totalMillis);
        double totalHours = totalMillis/1000/60/60;
        System.out.println("Hours: " + totalHours);
        return Math.round(totalHours*100)/100.0;
    }

}
