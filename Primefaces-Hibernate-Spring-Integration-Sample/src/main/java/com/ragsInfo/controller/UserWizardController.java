package com.ragsInfo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ragsInfo.entity.City;
import com.ragsInfo.entity.Tagging;
import com.ragsInfo.entity.User;
import com.ragsInfo.service.UserWizardService;

@ManagedBean
@RequestScoped
@Component
public class UserWizardController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user = new User();
	private List<User> usersList = new ArrayList<>();
	private boolean editflag;
	private boolean skip;
	FacesMessage msg=null;
	private List<User> selectedUsersList = new ArrayList<>();
	private boolean multiSelectedUser;
	private Map<String,Integer> taggings;
	
	
	public Map<String, Integer> getTaggings() {
		return taggings;
	}

	public void setTaggings(Map<String, Integer> taggings) {
		this.taggings = taggings;
	}

	public List<User> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<User> usersList) {
		this.usersList = usersList;
	}

	public List<User> getSelectedUsersList() {
		return selectedUsersList;
	}

	public void setSelectedUsersList(List<User> selectedUsersList) {
		this.selectedUsersList = selectedUsersList;
	}

	public boolean isMultiSelectedUser() {
		return multiSelectedUser;
	}

	public void setMultiSelectedUser(boolean multiSelectedUser) {
		this.multiSelectedUser = multiSelectedUser;
	}


	@Autowired
	private UserWizardService userWizardService;

	@PostConstruct
	public void init() {
		try {
			setEditflag(false);
			usersList.clear();
			selectedUsersList.clear();
			usersList = userWizardService.getUsersList();
			List<Tagging> taggingList = userWizardService.getTaggingList();
			taggings=taggingList.stream().collect(Collectors.toMap(Tagging::getTagName, Tagging::getTagId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UserWizardService getUserWizardService() {
		return userWizardService;
	}

	public void setUserWizardService(UserWizardService userWizardService) {
		this.userWizardService = userWizardService;
	}

	public boolean isEditflag() {
		return editflag;
	}

	public void setEditflag(boolean editflag) {
		this.editflag = editflag;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String onFlowProcess(FlowEvent event) {
		if (skip) {
			skip = false; // reset in case user goes back
			return "confirm";
		} else {
			return event.getNewStep();
		}	
	}
	
	

	/**
	 * save the user details
	 */
	public void save() {
		try {
			userWizardService.register(user);
			System.out.println("User is saved to database");
			init();
			msg = new FacesMessage("Successful", "Created :" + user.getFirstname());
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	 /**
	 * @param e
	 */
	public void loadUserDetails(ActionEvent e) {
		try {	
			
			setEditflag(false);
		  ExternalContext ec = FacesContext.getCurrentInstance()
                    .getExternalContext();
						ec.redirect(ec.getRequestContextPath()
						        + "/pages/home.xhtml");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	}

	
	/**
	 * @param user
	 */
	public void deleteUser(User user) {
	    System.out.println(user);
	    try {
			userWizardService.deleteUser(user);
			init();
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Deletion was Successful", "Deleted User is :" + user.getUsername());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ExternalContext ec = FacesContext.getCurrentInstance()
                    .getExternalContext();
						ec.redirect(ec.getRequestContextPath()
						        + "/pages/home.xhtml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * @param user
	 */
	public void editUser(User user) {
	    System.out.println(user);
	    try {
	    	setUser(user);
	    	setEditflag(true);
	    	ExternalContext ec = FacesContext.getCurrentInstance()
                    .getExternalContext();
						ec.redirect(ec.getRequestContextPath()
						        + "/pages/home.xhtml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 */
	public void addUser() {
	    try {
	    	User user=new User();
	    	setUser(user);
	    	setEditflag(false);
	    	ExternalContext ec = FacesContext.getCurrentInstance()
                    .getExternalContext();
						ec.redirect(ec.getRequestContextPath()
						        + "/pages/home.xhtml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void selectedList(User user) {

		if (!user.isSelectedUser()) {
			System.out.println("please sleect an checkbox to associate");
			selectedUsersList.remove(user);
		} else {
			System.out.println("Associated");
			selectedUsersList.add(user);

		}

	}

	public void multiSelectedList(List<User> users) {
		try {
			selectedUsersList.clear();
			if (!isMultiSelectedUser()) {
				selectedUsersList.addAll(users);
				for (User user : users) {
					user.setSelectedUser(true);
				}
			} else {
				for (User user : users) {
					user.setSelectedUser(false);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void saveNewPassword()
	{
		try {
			checkConfirmPassword();
			User user = getUser();
			user.setPassword(user.getNewPassword());
			userWizardService.register(user);
			init();
		FacesMessage msg1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Login with new credentials",
					"Password was changed by the user");
			FacesContext.getCurrentInstance().addMessage(null, msg1);
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/pages/login.xhtml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	public void checkConfirmPassword()
	{
		try {
		if (getUser().getConfirmPassword() != null && getUser().getPassword() != null) {
			if (!getUser().getConfirmPassword().equals(getUser().getNewPassword())) {
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Mismatch","mismatch");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ExternalContext ec = FacesContext.getCurrentInstance()
	                    .getExternalContext();
								ec.redirect(ec.getRequestContextPath()
								        + "/pages/changePassword.xhtml");
							}
			}
		}
		 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	public void cancelNewPassword()
	{
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/pages/home.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	

}