package tn.hich.app.ui.views;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.shared.data.sort.SortDirection;

import tn.hich.app.model.Operation;
import tn.hich.app.service.OperationService;

@Component
public class OperationDataProvider extends AbstractBackEndDataProvider<Operation, Predicate<Operation>>
		implements
		ConfigurableFilterDataProvider<Operation, Predicate<Operation>, Predicate<Operation>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4512328795357183451L;
	
	@Autowired
	private OperationService service;
	private Predicate<Operation> filter ;

	@Override
	public boolean isInMemory() {
		return false;
	}

	@Override
	public void setFilter(Predicate<Operation> filter) {
		this.filter=filter;
		refreshAll();
	}

	@Override
	protected Stream<Operation> fetchFromBackEnd(Query<Operation, Predicate<Operation>> query) {
		List<Operation> op = service.find(query.getOffset(), query.getLimit(), query.getSortOrders().stream().collect(
				Collectors.toMap(sort -> sort.getSorted(), sort -> sort.getDirection() == SortDirection.ASCENDING)));
		
		if(filter != null){
			return op.stream().filter(filter);
		}
		return op.stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Operation, Predicate<Operation>> query) {
		return service.count();
	}

	

}
