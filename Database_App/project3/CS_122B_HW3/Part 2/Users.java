
public class Users {
	
	public String username;
	public String hostname;
	public boolean createDB = false;
	public boolean dropDB= false;
	public boolean createProc= false;
	public boolean createTable= false;
	public boolean dropTable= false;
	public boolean alterTable= false;
	public boolean deleteTable= false;
	public boolean selectCol= false;
	public boolean insertCol= false;
	public boolean updateCol= false;
	public boolean executeProc= false;
	
	public Users(String username,String hostname){
		this.username = username;
		this.hostname = hostname;
	}
	
	
	public String getAddress(){
		return "'"+username+"'@'"+hostname+"'";
	}
	
	public String getName(){
		return username;
	}
	
	
	
	public void grantAll(){
		createDB = true;
		dropDB= true;
		createProc= true;
		createTable= true;
		dropTable= true;
		alterTable= true;
		deleteTable= true;
		selectCol= true;
		insertCol= true;
		updateCol= true;
		executeProc= true;
	}
	public void revokeAll(){
		createDB = false;
		dropDB= false;
		createProc= false;
		createTable= false;
		dropTable= false;
		alterTable= false;
		deleteTable= false;
		selectCol= false;
		insertCol= false;
		updateCol= false;
		executeProc= false;
	}
	
	public void grantCreateDB(){
		createDB = true;
	}
	public void revokeCreateDB(){
		createDB = false;
	}
	public void grantDropDB(){
		dropDB= true;
	}
	public void revokeDropDB(){
		dropDB= false;
	}
	public void grantCreateProc(){
		createProc= true;
	}
	public void revokeCreateProc(){
		createProc= false;
	}
	public void grantCreateTable(){
		createTable= true;
	}
	public void revokeCreateTable(){
		createTable= false;
	}
	public void grantDropTable(){
		dropTable= true;
	}
	public void revokeDropTable(){
		dropTable= false;
	}
	public void grantAlterTable(){
		alterTable= true;
	}
	public void revokeAlterTable(){
		alterTable= false;
	}
	public void grantDeleteTable(){
		deleteTable= true;
	}
	public void revokeDeleteTable(){
		deleteTable= true;
	}
	public void grantSelectCol(){
		selectCol= true;
	}
	public void revokeSelectCol(){
		selectCol= false;
	}
	public void grantInsertCol(){
		insertCol= true;
	}
	public void revokeInsertCol(){
		insertCol= false;
	}
	public void grantUpdateCol(){
		updateCol= true;
	}
	public void revokeUpdateCol(){
		updateCol= false;
	}
	public void grantExecuteProc(){
		executeProc= true;
	}
	public void revokeExecuteProc(){
		executeProc= false;
	}
	
	
	
	public boolean getCreateDB(){
		return createDB;
	}
	public boolean getDropDB(){
		return dropDB;
	}
	public boolean getCreateProc(){
		return createProc;
	}
	public boolean getCreateTable(){
		return createTable;
	}
	public boolean getDropTable(){
		return dropTable;
	}
	public boolean getAlterTable(){
		return alterTable;
	}
	public boolean getDeleteTable(){
		return deleteTable;
	}
	public boolean getSelectCol(){
		return selectCol;
	}
	public boolean getInsertCol(){
		return insertCol;
	}
	public boolean getUpdateCol(){
		return updateCol;
	}
	public boolean getExecuteProc(){
		return executeProc;
	}

}
