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
     * This function may or may not work, was used in trial and error normalizations of validSolutionCoefficientMultiplication() from ecam.post, not currently being used,
     * The negative signs of the Math library trig functions was awkward
     * @param a Complex base
     * @param exponent to the power of
     * @return a^exponent
     */
    public static Complex toThePowerOf(Complex a, double exponent) {
        Complex out = new Complex(0, 0);
        double radius = (a.real * a.real + a.imaginary * a.imaginary);
        radius = Math.sqrt(radius);
        double theta = Math.PI / 2;
        //if (Double.isNaN(theta)) theta = 0;
        if (radius == 0){
            theta = 0;
        } else {
            theta = Math.atan(((Math.abs(a.imaginary) / Math.abs(a.real)) ));

        }
        double aa = 0;
        double bb = 0;
        if (a.imaginary < 0) {
            aa = 1;
        } else {
            aa = 0;
        }
        if (a.real < 0) {
            bb = 1;
        } else {
            bb = 0;
        }
        if (aa == 0 && bb == 0) {
        } else if (aa == 1 && bb == 0) {
            theta += 3 * Math.PI / 2;
        } else if (aa == 0 && bb == 1) {
            theta += Math.PI / 2;
        } else {
            theta += Math.PI;
        }
        theta = theta * exponent;
        theta = theta % (Math.PI * 2);
        if (theta >= 0 && theta < Math.PI / 2) {
            aa = 1;
            bb = 1;
        } else if (theta >= Math.PI / 2 && theta < Math.PI) {
            aa = 1;
            bb = -1;
        } else if (theta >= Math.PI && theta < 3 * Math.PI / 2) {
            aa = -1;
            bb = -1;
        } else {
            aa = -1;
            bb = 1;
        }
        double thetaQuarter = theta % (Math.PI / 4);
        if (radius == 0) {
            radius = Math.pow(radius, exponent);
        } else {
            radius = 0;
        }
        out.real = bb * radius * Math.cos(thetaQuarter);
        out.imaginary = aa * radius * Math.sin(thetaQuarter);
        return out;
    }

    public String toString(){
        return "("+real+", "+imaginary+") ";
    }
}
