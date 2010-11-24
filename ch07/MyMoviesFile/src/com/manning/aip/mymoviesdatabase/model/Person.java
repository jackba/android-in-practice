package com.manning.aip.mymoviesdatabase.model;

public class Person {

   private final String firstName;
   private final String lastName;
   
   public Person(final String firstName, final String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
   }

   public String getFirstName() {
      return this.firstName;
   }

   public String getLastName() {
      return this.lastName;
   }   

   @Override
   public String toString() {
      return "Person [firstName=" + this.firstName + ", lastName=" + this.lastName + "]";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.firstName == null) ? 0 : this.firstName.hashCode());
      result = prime * result + ((this.lastName == null) ? 0 : this.lastName.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof Person))
         return false;
      Person other = (Person) obj;
      if (this.firstName == null) {
         if (other.firstName != null)
            return false;
      } else if (!this.firstName.equals(other.firstName))
         return false;
      if (this.lastName == null) {
         if (other.lastName != null)
            return false;
      } else if (!this.lastName.equals(other.lastName))
         return false;
      return true;
   }   
}
