package com.studentmanagement.project;

public class DriverClass { 
		public static void main(String[] args){
			
			/* an object is created of type Student to access the non static methods
			 * present in the Student class
		     */
			Student student = new Student();        
			student.toExecuteFirst();     //non static method of Student class is called
		}

}
