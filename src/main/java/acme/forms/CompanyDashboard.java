
package acme.forms;

import java.util.Map;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	protected Map<String, Integer>	totalPracticesPerMonth;

	protected Statistics			practicumStatistic;

	protected Statistics			sessionStatistic;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
