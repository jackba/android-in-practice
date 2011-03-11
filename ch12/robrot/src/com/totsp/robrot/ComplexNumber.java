package com.totsp.robrot;

public class ComplexNumber {
   private double imaginary;
   private double real;

   public ComplexNumber(double real, double imag) {
      this.real = real;
      this.imaginary = imag;
   }

   public void setValue(ComplexNumber copy) {
      this.real = copy.real;
      this.imaginary = copy.imaginary;
   }

   public ComplexNumber add(ComplexNumber b) {
      ComplexNumber a = this;
      double real = a.real + b.real;
      double imag = a.imaginary + b.imaginary;
      this.real = real;
      this.imaginary = imag;
      return this;
   }

   public double abs() {
      return Math.hypot(this.real, this.imaginary);
   }

   public ComplexNumber multiply(ComplexNumber b) {
      ComplexNumber a = this;
      double real = (a.real * b.real) - (a.imaginary * b.imaginary);
      double imag = (a.real * b.imaginary) + (a.imaginary * b.real);

      this.real = real;
      this.imaginary = imag;

      return this;
   }
}
