package tn.hich.app.ui.views;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;

import tn.hich.app.model.Operation;

@SpringView(name = DatasetView.VIEW_NAME)
public class DatasetView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "Dataset";
	public static final VaadinIcons ICON = VaadinIcons.TABLE;

	private static final long serialVersionUID = -5016028750375258991L;

	// @Autowired
	// private OperationService service;
	//
	// @Autowired
	// private OperationRepository repo;

	@Autowired
	private OperationDataProvider provider;

	@PostConstruct
	public void init() {
		addComponent(grid());
		setSizeFull();
	}

	public Grid<Operation> grid() {
		Grid<Operation> grid = new Grid<>();

		//grid.addColumn(Operation::getIdentifier).setCaption("ID").setId(Operation.IDENTIFIER);
		grid.addColumn(Operation::getTaskCard).setCaption("Task Card").setId(Operation.TASK_CARD);
		grid.addColumn(Operation::getOperation).setCaption("Operation").setId(Operation.OPERATION);
		grid.addColumn(Operation::getZone).setCaption("Zone").setId(Operation.ZONE);
		grid.addColumn(Operation::getSkill).setCaption("Skill").setId(Operation.SKILL);
		grid.addColumn(Operation::getDescription).setCaption("Description").setId(Operation.DESCRIPTION);

		grid.setDataProvider(provider);

		addFilters(grid);
		grid.setSizeFull();
		return grid;
	}

	private void addFilters(Grid<Operation> grid) {
		HeaderRow headerRow = grid.addHeaderRowAt(1);
		Arrays.asList(Operation.TASK_CARD, Operation.OPERATION, Operation.SKILL, Operation.ZONE, Operation.DESCRIPTION)
				.forEach(key -> {
					HeaderCell taskcell = headerRow.getCell(key);
					TextField field = new TextField();
					field.setSizeFull();
					field.setHeight("30px");
					taskcell.setComponent(field);
					field.addValueChangeListener(e -> refreshGrid(grid));
				});
	}

	private void refreshGrid(Grid<Operation> grid) {
		HeaderRow row = grid.getHeaderRow(1);
		List<Column<Operation, ?>> cols = grid.getColumns();
		Predicate<Operation> predicate = o -> true;
		for (Column<Operation, ?> column : cols) {
			@SuppressWarnings("unchecked")
			HasValue<String> c = (HasValue<String>) row.getCell(column).getComponent();
			if (c.getValue() != null && !c.getValue().isEmpty()) {
				predicate = predicate.and(o -> valueProvider(column.getId()).apply(o).startsWith(c.getValue()));
			}
		}
		provider.setFilter(predicate);
	}

	private Function<Operation, String> valueProvider(String property) {
		if (Operation.DESCRIPTION.equals(property))
			return Operation::getDescription;
		if (Operation.OPERATION.equals(property))
			return Operation::getOperation;
		if (Operation.SKILL.equals(property))
			return Operation::getSkill;
		if (Operation.ZONE.equals(property))
			return Operation::getZone;
		if (Operation.TASK_CARD.equals(property))
			return Operation::getTaskCard;

		throw new UnsupportedOperationException("filtering not supported on property " + property);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
