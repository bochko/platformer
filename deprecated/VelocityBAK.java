package cairns.david.engine;

/**
	The Velocity class provides the ability to manipulate and specify a
	Velocity as a speed and direction. Using this class, you can
	create a velocity object with a particular speed and direction and
	then query the object to find our what the corresponding change in
	vertical and horizontal pixels per millisecond should be. These
	queries are achieved using the methods 'getdx' and 'getdy'.
	respectively.
	
	@author David Cairns
*/
public class VelocityBAK {

	// CHANGED TO FLOAT
	private	float angle;	// Angle of velocity in radians
	private float dangle;	// Angle expressed in degrees
	private float speed;	// Speed of change

	// CHANGED TO FLOAT
	private float dx;		// Above values broken down into x and y changes
	private float dy;

	/**
		Initialise a default velocity with 0 speed and an
		angle of 0 degrees.
	*/
	public VelocityBAK()
	{
		dx = 0.0f;
		dy = 0.0f;
		speed = 0.0f;
		angle = 0.0f;
		dangle = 0.0f;
	}

	/**
		Initialise a default velocity with a speed of 's' and an
		angle of 'a' degrees.
	*/
	public VelocityBAK(double s, double a)
	{
		dx = 0.0f;
		dy = 0.0f;
		speed = (float)s;
		dangle = (float)a;
		reCalc();
	}

	/**
		Recalculates the dx and dy values for the current
		speed and angle. Automatically called whenever you
		change the speed or angle.
	*/
	private void reCalc()
	{
		// Get the value of the angle in radians
		angle = (float) Math.toRadians(dangle);
		// Work out the change in pixels/millisecond in x direction
		dx = (float)(speed * Math.cos(angle));
		// Work out the change in pixels/millisecond in y direction
		dy = (float)(speed * Math.sin(angle));
	}

	/***
	 * Calculates an angle in degrees from a delta-x and delta-y. Used for debugging purposes.
	 * @author Boyan Atanasov
	 * @param dx delta-x
	 * @param dy delta-y
	 * @return an angle in degrees, rounded, to avoid precision errors.
	 */
	public float getAngleFromDxDy(double dx, double dy) {
		double cos_angle = speed * dx;
		double sin_angle = speed * dy;
		return (float)Math.toDegrees(Math.atan2(sin_angle, cos_angle));
	}

	/**
		Similar to the constructor. Set the velocity
		to a speed of 's' and an angle of 'a' degrees.
	*/
	public void setVelocity(double s, double a)
	{
		speed = (float) s;
		dangle = (float) a;
		angle = (float) Math.toRadians(a);
		reCalc();
	}

	/**
		Set the current angle to 'a' degrees whilst keeping the same
		value for 'speed'.
	*/
	public void setAngle(double a)
	{
		dangle = (float)a; // Angle in degree
		reCalc();
	}


	/**
		Set the current speed to 's' pixels/millisecond whilst keeping the same
		value for 'angle'.
	*/
	public void setSpeed(double s)
	{
		speed = (float)s;
		reCalc();
	}

	/**
		Get the current angle in degrees.
	*/
	public float getAngle()
	{
		return dangle;
	}

	/**
		Get the current speed in pixels/millisecond.
	*/
	public float getSpeed()
	{
		return speed;
	}

	/**
		Add the velocity 'v' to this velocity to produce a new
		angle and direction.
	*/
	public void add(VelocityBAK v)
	{
		dx += v.dx;
		dy += v.dy;

		speed = (float) Math.sqrt((dx*dx) + (dy*dy));
		angle = (float) Math.acos(dx/speed);
	}

	/**
		Subtract the velocity 'v' from this velocity to produce a new
		angle and direction.
	*/
	public void subtract(VelocityBAK v)
	{
		dx -= v.dx;
		dy -= v.dy;

		speed = (float) Math.sqrt((dx*dx) + (dy*dy));
		angle = (float) Math.acos(dx/speed);
	}

	/**
		Get the current speed in the x direction in
		pixels/millisecond. You would normally call this
		method after changing the angle or speed to find
		out the corresponding x and y components of the
		current angle and speed.
	*/
	public float getdx() { return dx; }

	/**
		Get the current speed in the y direction in
		pixels/millisecond. You would normally call this
		method after changing the angle or speed to find
		out the corresponding x and y components of the
		current angle and speed.
	*/
	public float getdy() { return dy; }

}
