/**
 * Complex number class
 */
public class Complex {
    /**
     * Real part
     */
    public double real;
    /**
     * Imaginary part
     */
    public double imaginary;
    /**
     * New Complex number, this function is required for Wolfram Engine to be able to use this class
     *
     * @param reIn Real part
     * @param imIn Imaginary part
     */
    public Complex(double reIn, double imIn) {
        real = reIn;
        imaginary = imIn;
    }
    /**
     * Initializes to zero
     */
    public Complex() {
        real = 0;
        imaginary = 0;
    }
    /**
     * Returns real part of this Complex number, this function is required for Wolfram Engine to be able to use this class
     *
     * @return real part of this complex number
     */
    public double re() {
        return real;
    }
    /**
     * Returns imaginary part of this Complex number, this function is required for Wolfram Engine to be able to use this class
     *
     * @return imaginary part of this complex number
     */
    public double im() {
        return imaginary;
    }
    /**
     * Multiplies two complex numbers a and b
     *
     * @param a Complex number factor a
     * @param b Complex number factor b
     * @return Complex a times b
     */
    public static Complex multiplyComplex(Complex a, Complex b) {
        Complex out = new Complex(0, 0);
        out.real = a.real * b.real - a.imaginary * b.imaginary;
        out.imaginary = a.real * b.imaginary + a.imaginary * b.real;
        return out;
    }
    /**
     * The negation of this Complex number
     * @return instance of this complex number's negation
     */
    public Complex negate(){
        return new Complex(-real,-imaginary);
    }
    /**
     * This complex number's conjugate
     * @return instance of this complex number's conjugate
     */
    public Complex conjugate(){
        return  new Complex(real,-imaginary);
    }
    /**
     * Adds two complex numbers a and b
     *
     * @param a Complex number a
     * @param b Complex number b
     * @return Complex a+b
     */
    public static Complex addComplex(Complex a, Complex b) {
        Complex out = new Complex(a.real + b.real, a.imaginary + b.imaginary);
        return out;
    }
    /**
     * The length of this complex number
     *
     * @return the length of this complex number
     */
    public double radius() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }
    /**
     * The principle angle of this complex number
     *
     * @return the principle angle of this complex number
     */
    public double theta() {
        return Math.atan((imaginary / real));
    }
    /**
     * String of this complex number (real,imaginary)
     * @return String of this complex number (real,imaginary)
     */
    public String toString(){
        return "("+real+", "+imaginary+") ";
    }
}
