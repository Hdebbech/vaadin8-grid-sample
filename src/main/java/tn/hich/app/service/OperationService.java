package tn.hich.app.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.hich.app.model.Operation;
import tn.hich.app.repo.OperationRepository;

/**
 * 
 * @author hichem
 *
 */
@Service
public class OperationService {

	private static final Logger LOG = LoggerFactory.getLogger(OperationService.class);

	private EntityManager em;

	@Autowired
	private OperationRepository repo;

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Transactional
	public void importXlsx() throws XLSXImportationException {
		LOG.info("Importing opbdv.xlsx into database...");
		LOG.info("Deleting all Records from table " + Operation.TABLE + "...");
		em.createNativeQuery("TRUNCATE TABLE " + Operation.TABLE).executeUpdate();
		LOG.info("Begin Import...");
		long start = System.currentTimeMillis();
		try (InputStream excelFile = getClass().getResourceAsStream("/opbdv.xlsx");
				Workbook workbook = new XSSFWorkbook(excelFile);) {
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			// skip header row
			iterator.next();
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				Operation op = toEntity(currentRow);
				em.persist(op);
			}
			LOG.info("opbdv.xlsx imported successfully, elapsed time: " + (System.currentTimeMillis() - start) / 100
					+ " sec");
		} catch (Exception e) {
			LOG.error("opbdv.xlsx could not be imported", e);
			throw new XLSXImportationException(e);
		}
	}

	private Operation toEntity(Row currentRow) {
		Cell taskCardCell = currentRow.getCell(0);
		Cell opCell = currentRow.getCell(1);
		Cell descCell = currentRow.getCell(2);
		Cell phaseCell = currentRow.getCell(3);
		Cell ataCell = currentRow.getCell(4);
		Cell zoneCell = currentRow.getCell(5);
		Cell skillCell = currentRow.getCell(6);
		Cell checkCell = currentRow.getCell(7);
		Cell typeOpCell = currentRow.getCell(8);
		Cell tconCell = currentRow.getCell(9);
		Cell tcompCell = currentRow.getCell(10);
		Operation op = new Operation();
		op.setOperation(asString(opCell));
		op.setTaskCard(asString(taskCardCell));
		op.setDescription(asString(descCell));
		op.setPhase(asString(phaseCell));
		op.setAta(asString(ataCell));
		op.setZone(asString(zoneCell));
		op.setSkill(asString(skillCell));
		op.setCheck(asString(checkCell));
		op.setOperationType(asString(typeOpCell));
		op.setTcons(asString(tconCell));
		op.setTcomp(asString(tcompCell));
		return op;
	}

	@SuppressWarnings("deprecation")
	private String asString(Cell cell) {
		if (cell.getCellTypeEnum() == CellType.STRING) {
			return cell.getStringCellValue();
		} else {
			return Double.valueOf(cell.getNumericCellValue()).toString();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Operation> getOperations() {
		Query query = em.createQuery("SELECT e FROM Operation e");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Cacheable
	public List<Operation> findByCheck(String queryName) {
		return em.createNamedQuery(queryName).getResultList();
	}
	
	@Transactional(readOnly=true)
	public List<Operation> find(int offset, int limit, Map<String, Boolean> sortOrders) {

		List<Sort.Order> orders = sortOrders.entrySet().stream()
				.map(e -> new Sort.Order(e.getValue() ? Sort.Direction.ASC : Sort.Direction.DESC, e.getKey()))
				.collect(Collectors.toList());

		int page = offset / 100;
		int next = (offset + limit) / 100;
		if (page == next) {
			PageRequest pageRequest = new PageRequest(page, 100);
			List<Operation> items = new ArrayList<>(
					repo.findAll(pageRequest).getContent().subList(offset % 100, (offset + limit) % 100));
			if (!orders.isEmpty())
				sort(items, new Sort(orders));
			return items;
		}
		PageRequest pageRequest1 = new PageRequest(page, 100);
		PageRequest pageRequest2 = new PageRequest(next, 100);
		List<Operation> page1 = repo.findAll(pageRequest1).getContent();
		List<Operation> page2 = repo.findAll(pageRequest2).getContent();

		List<Operation> finalpage = new ArrayList<>(page1.subList(offset % 100, page1.size()));
		finalpage.addAll(page2.subList(0, (offset + limit) % 100));
		if (!orders.isEmpty())
			sort(finalpage, new Sort(orders));
		return finalpage;
	}

	public void sort(List<Operation> items, Sort sort) {
		Comparator<Operation> comparator = null;
		Iterator<Order> iter = sort.iterator();
		Order first = iter.next();
		comparator = getComparator(first.getProperty(), first.getDirection());
		while (iter.hasNext()) {
			Order order = iter.next();
			comparator = comparator.thenComparing(getComparator(order.getProperty(), order.getDirection()));
		}
		items.sort(comparator);
	}

	public int count() {
		return Math.toIntExact(repo.count());
	}

	private Comparator<Operation> getComparator(String property, Direction direction) {
		boolean asc = direction == Direction.ASC;
		if (Operation.IDENTIFIER.equals(property))
			return asc ? (o1, o2) -> o1.getIdentifier().compareTo(o2.getIdentifier())
					: (o1, o2) -> o2.getIdentifier().compareTo(o1.getIdentifier());
		if (Operation.TASK_CARD.equals(property))
			return asc ? (o1, o2) -> o1.getTaskCard().compareTo(o2.getTaskCard())
					: (o1, o2) -> o2.getTaskCard().compareTo(o1.getTaskCard());
		if (Operation.OPERATION.equals(property))
			return asc ? (o1, o2) -> o1.getOperation().compareTo(o2.getOperation())
					: (o1, o2) -> o2.getOperation().compareTo(o1.getOperation());
		if (Operation.ZONE.equals(property))
			return asc ? (o1, o2) -> o1.getZone().compareTo(o2.getZone())
					: (o1, o2) -> o2.getZone().compareTo(o1.getZone());
		if (Operation.SKILL.equals(property))
			return asc ? (o1, o2) -> o1.getSkill().compareTo(o2.getSkill())
					: (o1, o2) -> o2.getZone().compareTo(o1.getZone());
		if (Operation.DESCRIPTION.equals(property))
			return asc ? (o1, o2) -> o1.getDescription().compareTo(o2.getDescription())
					: (o1, o2) -> o2.getDescription().compareTo(o1.getDescription());
		throw new UnsupportedOperationException("sorting not supported on property " + property);

	}

}
