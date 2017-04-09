package tn.hich.app.ui.views;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

import tn.hich.app.security.User;

@SpringView(name=View3.VIEW_NAME)
public class View3 extends VerticalLayout implements View{
	
	public static final String VIEW_NAME = "view3";
	public static final VaadinIcons ICON = VaadinIcons.TAG;
	
	@PostConstruct
	public void init(){
		Grid<User> grid = new Grid<>(User.class);
		User user= new User();
		user.setUsername("hichem");
		user.setPassword("hichem");
		grid.setItems(Arrays.asList(user));
		grid.setSizeFull();
		addComponent(grid);
		setSizeFull();
	}
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5016028750375258991L;

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
