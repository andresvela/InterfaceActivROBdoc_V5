package com.bdoc.interfaces.webservice.actions.implementations;

public class BdocPDFSecurityOption {

	private boolean canPrint;
	private boolean canModify;
	private boolean canAnotate;
	private boolean canCopy;

	private String ownerPasswd;
	private String userPasswsd;
	
	
	public boolean isActivate()
	{
		return !canAnotate||!canModify||!canPrint||!canCopy;
	}
	
	public boolean isCanPrint() {
		return canPrint;
	}

	public void setCanPrint(boolean canPrint) {
		this.canPrint = canPrint;
	}

	public boolean isCanModify() {
		return canModify;
	}

	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}

	public boolean isCanAnotate() {
		return canAnotate;
	}

	public void setCanAnotate(boolean canAnotate) {
		this.canAnotate = canAnotate;
	}

	public boolean isCanCopy() {
		return canCopy;
	}

	public void setCanCopy(boolean canSave) {
		this.canCopy = canSave;
	}

	public String toString()
	{
		return "\nCanPrint="+canPrint+"\nCanSave="+canCopy+"\nCanModify="+canModify+"\nCanAnotate="+canAnotate;
	}

	public String getOwnerPasswd() {
		return ownerPasswd;
	}

	public void setOwnerPasswd(String ownerPasswd) {
		this.ownerPasswd = ownerPasswd;
	}

	public String getUserPasswsd() {
		return userPasswsd;
	}

	public void setUserPasswsd(String userPasswsd) {
		this.userPasswsd = userPasswsd;
	}
	
	
}
