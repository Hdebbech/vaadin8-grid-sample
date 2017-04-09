package tn.hich.app.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "/login")
public class LoginUI extends UI {
	
	
	@Autowired
	private AuthenticationProvider provider;

	/**
	 * 
	 */
	private static final long serialVersionUID = 8978834192013644704L;

	@Override
	protected void init(VaadinRequest request) {
		Component loginForm = buildLoginForm();
		VerticalLayout l = new VerticalLayout();
		l.addComponent(loginForm);
		l.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
		l.setSizeFull();
		setContent(l);
	}

	private Component buildLoginForm() {
		final VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSizeUndefined();
		loginPanel.setSpacing(true);
		Responsive.makeResponsive(loginPanel);
		loginPanel.addStyleName("login-panel");

		loginPanel.addComponent(buildLabels());
		loginPanel.addComponent(buildFields());
		//loginPanel.addComponent(new CheckBox("Remember me", true));
		return loginPanel;
	}

	private Component buildFields() {
		HorizontalLayout fields = new HorizontalLayout();
		fields.setSpacing(true);
		fields.addStyleName("fields");

		final TextField username = new TextField("Username");
		username.setIcon(VaadinIcons.USER);
		username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		final PasswordField password = new PasswordField("Password");
		password.setIcon(VaadinIcons.LOCK);
		password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		final Button signin = new Button("Log In");
		signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
		signin.setClickShortcut(KeyCode.ENTER);
		signin.focus();

		fields.addComponents(username, password, signin);
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
		signin.addClickListener(e-> {
			Authentication auth = new UsernamePasswordAuthenticationToken(username.getValue(),password.getValue());
			try{
				Authentication authenticated = provider.authenticate(auth);
	            SecurityContextHolder.getContext().setAuthentication(authenticated);
	            getPage().setLocation(getPage().getLocation().toString().replace("login", ""));
			}catch(BadCredentialsException ex){
				username.clear();
				username.focus();
				username.setComponentError(new UserError("Bad Credentials"));
				password.clear();
			}
			
			
		});
		return fields;
	}

	private Component buildLabels() {
		CssLayout labels = new CssLayout();
		labels.addStyleName("labels");

		Label welcome = new Label("Welcome");
		welcome.setSizeUndefined();
		welcome.addStyleName(ValoTheme.LABEL_H4);
		welcome.addStyleName(ValoTheme.LABEL_COLORED);
		labels.addComponent(welcome);

//		Label title = new Label("QuickTickets Dashboard");
//		title.setSizeUndefined();
//		title.addStyleName(ValoTheme.LABEL_H3);
//		title.addStyleName(ValoTheme.LABEL_LIGHT);
//		labels.addComponent(title);
		return labels;
	}

}
