package com.dubiouscandle.candlelib.extendedmath;

public class Complex {
	private static final double HALF_SQRT2 = Math.sqrt(2) / 2;

	public final double re, im;

	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	public Complex sqrt() {
		double mag = Math.sqrt(re * re + im * im);

		double x = HALF_SQRT2 * Math.sqrt(mag + re);
		double y = Math.signum(im) * HALF_SQRT2 * Math.sqrt(mag - re);

		return new Complex(x, y);
	}

	public Complex square() {
		return new Complex(re * re - im * im, 2 * re * im);
	}

	public Complex add(Complex o) {
		return new Complex(re + o.re, im + o.im);
	}

	public Complex sub(Complex o) {
		return new Complex(re - o.re, im - o.im);
	}

	public Complex mul(Complex o) {
		return new Complex(re * o.re - im * o.im, re * o.im + im * o.re);
	}

	public Complex div(Complex o) {
		double invDenom = 1.0 / (o.re * o.re + o.im * o.im);

		return new Complex(invDenom * (re * o.re + im * o.im), invDenom * (im * o.re - re * o.im));
	}

	public Complex pow(Complex o) {
		double theta = Math.atan2(o.im, o.re);
		double r2 = re * re + im * im;
		double angleParam = theta * o.re + o.im * 0.5 * Math.log(r2);
		double factor = Math.pow(r2, 0.5 * o.re) * Math.exp(-theta * o.im);

		return new Complex(factor * Math.cos(angleParam), factor * Math.sin(angleParam));
	}

	public double abs() {
		return Math.sqrt(re * re + im * im);
	}

	public double arg() {
		return Math.atan2(im, re);
	}

	public double abs2() {
		return re * re + im * im;
	}

	@Override
	public int hashCode() {
		return Double.hashCode(im);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Complex other = (Complex) obj;
		return im == other.im && re == other.re;
	}

	@Override
	public String toString() {
		return re + " + " + im + "i";
	}

	public static final Complex ZERO = new Complex(0, 0);
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex i = new Complex(0, 1);
}
