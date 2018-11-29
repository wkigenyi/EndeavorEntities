/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.endeavourentities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.netbeans.api.options.OptionsDisplayer;
import org.openide.awt.StatusDisplayer;
import org.openide.util.NbPreferences;
import systems.tech247.systems.hr.endeavour.ta.TblCategory;
import systems.tech247.systems.hr.endeavour.ta.TblCompany;
import systems.tech247.systems.hr.endeavour.ta.TblDepartment;
import systems.tech247.systems.hr.endeavour.ta.TblEmpPaySlip;
import systems.tech247.systems.hr.endeavour.ta.TblEmployee;
import systems.tech247.systems.hr.endeavour.ta.TblLeaveApplication;

/**
 *
 * @author Admin
 */




public class EndeavorDataAccess {
    
    static ArrayList<TblEmployee> selectedEmployees = new ArrayList<>();
    
    static Map properties = new HashMap();
    static EntityManager em = getEntityManager();

    
    
    //Initiate the EntityManager from the user settings
    public static EntityManager getEntityManager(){
        String useDemo = NbPreferences.forModule(DatabaseSettingsPanel.class).get("UseDemo", "");
        if(useDemo.equals("true")){
            //Use Demo Settings
            //Check If the settings are correct, Prompt for Changes if they are
            String connectionUrl = "jdbc:sqlserver://"+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBServerName", "")+":"+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBPort", "")+";databaseName="+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBName", "")+";user="+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBLogin", "")+";password="+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBPassword", "");
        
        Connection con =  null;
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            Properties p=con.getClientInfo();
           
        }catch(Exception ex){
            StatusDisplayer.getDefault().setStatusText("Cannot Connect to Demo Database, Rectify Settings and Restart");
            OptionsDisplayer.getDefault().open();
        }
            
            
            
            
            
            
            
            
            
            connectionUrl = "jdbc:sqlserver://"+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBServerName", "")+":"+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBPort", "")+";databaseName="+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBName", "");
                
            
            
            
            
            
             
            properties.put("javax.persistence.jdbc.url",connectionUrl);//Set the url
            properties.put("javax.persistence.jdbc.user", NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBLogin", ""));//set the db user
            properties.put("javax.persistence.jdbc.password", NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBPassword", ""));
            //properties.put("javax.persistence.jdbc.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"); it is the same for live and demo
            StatusDisplayer.getDefault().setStatusText("URL: "+ connectionUrl);
            
        }else{
            //Use Live Settings
            //Test Live Settings Before Use
            String connectionUrl = "jdbc:sqlserver://"+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBServerNameLive", "")+":"+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBPortLive", "")+";databaseName="+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBNameLive", "")+";user="+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBLoginLive", "")+";password="+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBPasswordLive", "");
        
        Connection con =  null;
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            Properties p=con.getClientInfo();
           
        }catch(Exception ex){
            StatusDisplayer.getDefault().setStatusText("Cannot Connect to Live Database, Rectify Settings and Restart");
            
        }
            
            
            
            
            
            connectionUrl = "jdbc:sqlserver://"+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBServerNameLive", "")+":"+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBPortLive", "")+";databaseName="+
                NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBNameLive", "");
                
             
            properties.put("javax.persistence.jdbc.url",connectionUrl);//Set the url
            properties.put("javax.persistence.jdbc.user", NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBLoginLive", ""));//set the db user
            properties.put("javax.persistence.jdbc.password", NbPreferences.forModule(DatabaseSettingsPanel.class).get("DBPasswordLive", ""));
            //properties.put("javax.persistence.jdbc.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"); it is the same for live and demo
            StatusDisplayer.getDefault().setStatusText("URL: "+ connectionUrl);
            
        }
        //Subsitute the default properties with defined options
        try{
         EntityManager em = Persistence.createEntityManagerFactory("EndevourTALibPU",properties).createEntityManager();
         return em;
        }catch(Exception ex){
            OptionsDisplayer.getDefault().open();
            return null;
        } 
    }
    //Get The Company (ies)
    public static List<TblCompany> getCompanies(){
        TypedQuery query = em.createQuery("SELECT c FROM TblCompany c",TblCompany.class);
        return query.getResultList();
    }
    
    
    //Personnel Section
    public static List<TblEmployee> getEmployees(String sql){
        TypedQuery<TblEmployee> query =em.createQuery(sql, TblEmployee.class);
        //StatusDisplayer.getDefault().setStatusText(sql);
        return query.getResultList();
    }
    
    //Personnel Section
    public static List<TblLeaveApplication> getLeaveApplications(String sql){
        TypedQuery<TblLeaveApplication> query =em.createQuery(sql, TblLeaveApplication.class);
        //StatusDisplayer.getDefault().setStatusText(sql);
        return query.getResultList();
    }
    
    
    
    
    public static TblEmployee getEmployeeByEmpCode(int empCode){
        TypedQuery<TblEmployee> query = em.createQuery("SELECT e FROM TblEmployee e WHERE e.tblEmployeePK.empCode="+empCode+"", TblEmployee.class);
        return query.getResultList().get(0);
    }
    
    public static TblEmployee getEmployeeByClockinID(String clockinID){
        //StatusDisplayer.getDefault().setStatusText(clockinID);
        TypedQuery<TblEmployee> query = em.createQuery("SELECT t FROM TblEmployee t WHERE t.iCardNo = :iCardNo", TblEmployee.class);
        query.setParameter("iCardNo", clockinID);
        return query.getResultList().get(0);
    }
    
    public static void updateSelectedEmployee(TblEmployee e, Boolean add){
        if(add){
            if(!selectedEmployees.contains(e))
            selectedEmployees.add(e);
        }else{
            selectedEmployees.remove(e);
        }
        StatusDisplayer.getDefault().setStatusText(selectedEmployees.size()+" Employees Selected");
    }
    public static void saveEmployee(HashMap data,TblEmployee emp){
        //Saving data for an existing employee
        if(emp!=null){
            em.getTransaction().begin();
            TblEmployee employee = em.find(TblEmployee.class, emp.getTblEmployeePK());
            employee.setEmailAddress(data.get("email").toString());
            em.getTransaction().commit();
        }
    }
    
    
    public static List<TblDepartment> getDepartments(String search){
        TypedQuery<TblDepartment> query = em.createQuery("SELECT e FROM TblDepartment e WHERE e.departmentName LIKE '%"+search+"%'", TblDepartment.class);
        return query.getResultList();
    }
    public static TblDepartment getDepartment(int id){
        TypedQuery<TblDepartment> query = em.createQuery("SELECT e FROM TblDepartment e WHERE e.tblDepartmentPK.departmentCode = "+id+"", TblDepartment.class);
        return query.getResultList().get(0);
    }
    public static List<TblCategory> getCategories(String search){
        TypedQuery<TblCategory> query = em.createQuery("SELECT e FROM TblCategory e WHERE e.categoryName LIKE '%"+search+"%'", TblCategory.class);
        return query.getResultList();
    }
    //Payroll Section
    public static List<TblEmpPaySlip> getPaySlips(String sql){
        TypedQuery<TblEmpPaySlip> query = em.createQuery(sql,TblEmpPaySlip.class);
        
        return query.getResultList();
    }
    //System Periods (Closed Ones Only)
    public static List<Object[]> getClosedPeriods(){
        //Get the current Date
        int currentMonth = (new Date()).getMonth()+1;
        Query query = em.createNativeQuery("SELECT m.monthID,YEAR(m.FromDate) AS PeriodYear,y.YearID FROM tblMonth m INNER JOIN tblYear y on YEAR(m.FromDate) = YEAR(y.StartDate) WHERE m.Closed=1 OR m.monthID<? ORDER BY y.YearId DESC,m.MonthId DESC");
        query.setParameter(1, currentMonth);
        List<Object[]> list = query.getResultList();
        return list; 
    }
    //Get The Year By ID
    public static String getYearByID(int ID){
        Query query  = em.createNativeQuery("SELECT * FROM tblYear WHERE YearId = "+ID+"");
        List<Object[]>  result= query.getResultList();
        String year =(String)result.get(0)[2];
        return year.substring(5);
    }
    
    
}
