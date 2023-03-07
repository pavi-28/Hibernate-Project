package com.studentmanagement.project;

import java.util.Scanner;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.cfg.Configuration;
import jakarta.persistence.*;

/* Here class name is taken as table name (Student) 
 * because of the @Entity annotation.
 */
@Entity
@DynamicUpdate
public class Student {
	/* scanner object private to this class is created to accept userInput */
	private static Scanner scanner = new Scanner(System.in);
	
	public Student(){
	}
	
	/* A method is created to prompt user to enter 
	 * userName and password to login and 
	 * Calling login method by passing parameters
	 */
	public void toExecuteFirst() {
		System.out.println("!!! Login First !!!");
		System.out.print("Enter username: ");
		String userName = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();
		/* calling the toLogin method */
		toLogin(userName, password);
	}

	/* @Id annotation denotes the primary key of the table created */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer studentId;

	@Column(length = 20, nullable = false)
	private String studentName;

	private String standard;

	private long contactNo;

	/*
	 * To access the private attributes of the system, public getter and setter
	 * methods are created 
	 */
	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public long getContactNo() {
		return contactNo;
	}

	public void setContactNo(long contactNo) {
		this.contactNo = contactNo;
	}

	@Override
	public String toString() {
		return "\n[studentId=" + studentId + ", studentName=" + studentName + ", standard=" + standard
				+ ", contactNo=" + contactNo + "]";
	}

	/*
	 * Defining a login method using if condition to check username and password,
	 * entered by user is equal to the value initialized or not. Only when the
	 * conditions are true, the system gets logged in and display the menu. Else,
	 * the system fails to login
	 */
	public static void toLogin(String userName, String password) {
		if (userName.equals("Room1") && password.equals("Login123")) { 
			System.out.println("successfully logged in");
			
			System.out.println("\n1. To Insert a student");
			System.out.println("2. To Display student by Id entered");
			System.out.println("3. To Update a student data by Id");
			System.out.println("4. To Delete a student");
			System.out.println("5. Press 5 to Logout \n");
			
			/* once the system logged in, session opened and the system
			 * is ready to accept user input.
			 * these loop statements and switch case is given under try-catch block,
			 * so that the exception thrown is handled.
			 */
			try 
			{
				Configuration config = new Configuration().configure().addAnnotatedClass(Student.class);
				SessionFactory sessionFact = config.buildSessionFactory(); 
				Session session = sessionFact.openSession();
				
				/* variable to store user input value */
				byte userInput;    
				/* do-loop executes at least once before checking condition */
				do {                     
					userInput = scanner.nextByte();
					
					/* passing userInput to switch case as case number */
					switch(userInput) {     
					
					/* userInput = 1 - toAdd method is called (inserting records) */
					case 1:
						toCreateStudent(session); 
						break;
					/* userInput = 2 - toDisplaybyId method is called */
					case 2:
						toDisplaybyId(session); 
						break;
					/* userInput = 3 - toUpdate method is called */
					case 3:
						toUpdate(session); 
						break;					
					/* userInput = 4 - toDeletebyId method is called */
					case 4:
						toDeletebyId(session); 
						break;					
					/* when 5 is the userInput, user is logged out 
					 */
					case 5: 
						toLogout();
						//session.close();
						break;
					
					/* if the userInput doesn't match case value, default statement is executed */
					default: 
						System.out.println(" Enter the Correct valid Input! ");
					}
				}
			
				/* When user input is 5, the corresponding case's method call is executed.
				 * So, that the system gets logged out. 
				 * Until input is not equal to 5, the do-loop gets executed
				 */
				while (userInput != 5);
			}
			catch(HibernateException hib_Excep) {
				System.out.println(hib_Excep);
			}
			catch(Exception exception) {
				System.out.println(exception);
			}
		}
		
		/* If the if-condition fails, then the else part is executed */
		else {
			System.out.println("Login Failed!!!");
		}
	}
		
	

	/* toCreateStudent method is defined to insert data into a table 
	 * in a database. It is done with the help of Hibernate Framework.
	 */
	@SuppressWarnings("deprecation")
	public static void toCreateStudent(Session session) {
		session.beginTransaction();         //session begins
		//save method returns a value of object type and is type-casted and stored as integer.
		Integer id = (Integer) session.save(getStudent());     
		System.out.println("Student is created with id :"+id);
		//statements written after session.beginTransaction is committed.
		session.getTransaction().commit();   
	}

	/* this method is created to set values to the private attributes
	 * of the Student class using setter methods
	 * and is accessed in toCreateStudent method
	 */
	public static Student getStudent() {
		Student studObj = new Student();
		System.out.println("Enter student name");
		String studName = scanner.next();
		studObj.setStudentName(studName);
		System.out.println("Enter standard");
		String standrd = scanner.next();
		studObj.setStandard(standrd);
		System.out.println("Enter contact no.");
		Long contNo = scanner.nextLong();
		studObj.setContactNo(contNo);
		return studObj;
	}
	
	/* toDisplaybyId method is defined to search for that particular id and 
	 * display the record from a database using hibernate
	 */
	public static void toDisplaybyId(Session session){
		System.out.println("Enter id you want to display");
		int studId = scanner.nextInt();
		//get method gets the object/data of that particular type and id.
		Student studObj = session.get(Student.class, studId);
		if(studObj != null) {
			System.out.println(studObj);
		}
		else {
			System.out.println("Student id doesnt exists..");

		}
	}
	
	/* toUpdatebyId method is defined to update that specific record 
	 * corresponding to id entered, in a database
	 */ 
	public static void toUpdate(Session session) {
		System.out.println("Enter id you want to update");
		int studId = scanner.nextInt();
		Student studObj = session.get(Student.class, studId);
		if(studObj != null) { 
			session.beginTransaction();
			System.out.println("Enter contact number you want to update");
			long studContact = scanner.nextLong();
			studObj.setContactNo(studContact);
			System.out.println("Enter standard you want to update");
			String studStandard = scanner.next();
			studObj.setStandard(studStandard);
			//persist method saves the object without returning anything.
			session.persist(studObj); 
			session.getTransaction().commit();
		}
		else {
			System.out.println("Student id doesnt exists..");
		}
	}
		/* toDeletebyId method is defined to  delete that specific record 
		 * corresponding to id entered, from a database 
		 */
		public static void toDeletebyId(Session session) {
			System.out.println("Enter id you want to delete");
			int studId = scanner.nextInt();
			Student studObj = session.get(Student.class, studId);
			if(studObj != null) {
				session.beginTransaction();
				//remove method deletes(removes) that particular object passed to it.
				session.remove(studObj);          
				session.getTransaction().commit();
			}
			else {
				System.out.println("Student id doesnt exists..");
			}	
		}

		/* Defining logout() method for logging out the system */
		public static void toLogout() {
			System.out.println("Logged out!!! \nThank You! Visit Again!!!");
		}
		
	}



