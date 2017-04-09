package tn.hich.app.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class ExcelImporter {
	
	private OperationService service;
	
	public ExcelImporter(OperationService s) {
		this.service = s;
	}
	
	@PostConstruct
	public void init() throws XLSXImportationException{
		service.importXlsx();
	}

}
