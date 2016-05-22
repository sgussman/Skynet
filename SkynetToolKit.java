/*
	var φ2 = Math.asin( Math.sin(φ1)*Math.cos(d/R) + Math.cos(φ1)*Math.sin(d/R)*Math.cos(brng) );
	var λ2 = λ1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(φ1), Math.cos(d/R)-Math.sin(φ1)*Math.sin(φ2));
*/


/* Assumptions:
	- Roll doesn't affect calculations
	- The camera's YPR picks out the center of the camera
	- Camera YPR and Drone YPR add (same origin)
	- The target is at ground level
*/

// import Math.*;
// O = Origin, T = Target
// lt = Latitude, ln = longitude
// d = Vector Distance (km), R = Earth's Radius (km)
// Hd = Hypotenus Distance (km)
public class SkynetToolKit {
	/**
	 * @LatLonEstimation estimates the latitude and longitude of a point given the drones YPR, the camera YPR, and the distance of the obj 
	 * @DroneYPR is the Yaw, Pitch, Roll of the drone. Roll is assumed not to impact the calculations.
	 * @CameraYPR is the Yaw, Pitch, Roll of the camera. Roll is assumed not to impact the calculations.
	 * @Olt is the latitude of the Origin, aka the drone
	 * @Oln is the longitude of the origin, aka the drone
	 * @Hd is the hypotenus distance, AKA the estimated distance from the camera to the obj.
	 */
	private static double R = 6371000; 	// Mean earth's radius in km
	public static double[] LatLonEstimation(double[] DroneYPR, double[] CameraYPR, double Olt, double Oln, double Hd) {
		//Combine YPRs to calculate Real YPR
		double yaw = DroneYPR[0] + CameraYPR[0];
		double pitch = DroneYPR[1] + CameraYPR[1];
		double roll = DroneYPR[2] + CameraYPR[2];

		// Turn YPR into a bearing
		double brg = Math.toRadians(yaw);
		System.out.println("Bearing: "+brg+" ("+yaw+")");
		// Get horizontal distance from the hypotenus
		double d = Math.cos(Math.toRadians(pitch)) * Hd;
		System.out.println("Horizontal Distance: "+d);
		
		// Calculate Latitude and Longitude
		System.out.println(Math.cos(Olt) * Math.sin(d/R) * Math.cos(brg));
		System.out.println(Math.cos(Olt));
		System.out.println(Math.sin(d/R));
		System.out.println(Math.cos(brg));
		double Tlt = Math.asin(Math.sin(Olt)* Math.cos(d/R) + Math.cos(Olt) * Math.sin(1000*d/R) * Math.cos(brg));
		// System.out.println(Tlt);
		// System.out.println(Olt);
		double Tln = Oln + Math.atan2(Math.sin(brg) * Math.sin(d/R) * Math.cos(Olt), Math.cos(d/R) - Math.sin(Olt) * Math.sin(Tlt));
		return new double[]{RadiansToLat(Tlt), RadiansToLon(Tln)};
	}

	/**
	 * @lt the latitude that needs to be converted to radians
	 */
	public static double latToRadians(double lt){
		//Throw an exception if the angle is outside [-pi/2, pi/2]
		try {
			if (lt > 90.0 || lt < -90.0){
				throw new Exception("Invalid latitude: Outside range [-pi/2, pi/2]");
			}
			return Math.toRadians(lt);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0.0;
		}
	}

	/**
	 * @angle is the the latitude that needs to be converted to degrees
	 */
	public static double RadiansToLat(double angle){
		// Throw an exception if the angle is outside [-pi/2, pi/2]
		try {
			double lt = Math.toDegrees(angle);
			if (lt > 90.0 || lt < -90.0){
				throw new Exception("Invalid latitude: Outside range [-pi/2, pi/2]");
			}
			return lt;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0.0;
		}
	}


	/**
	 * @ln the longitude that needs to be converted to radians
	 */
	public static double lonToRadians(double ln){
		try {
			//Throw an exception if the angle is outside [-pi, pi]
			if (ln > 180.0 || ln < -180.0){
				throw new Exception("Invalid longitude: Outside range [-pi, pi]");
			}
			return Math.toRadians(ln);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0.0;
		}
	}

	/**
	 * @angle is the the radians that need to be converted to Longitude
	 */
	public static double RadiansToLon(double angle){
		try {	
			// Throw an exception if the angle is outside [-pi, pi]
			double ln = Math.toDegrees(angle);
			if (ln > 180.0 || ln < -180.0){
				throw new Exception("Invalid latitude: Outside range [-pi, pi]");
			}
			return ln;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0.0;
		}
	}

	public static void main(String[] args) {
		System.out.println("Hello, world");
		
		// double a = RadiansToLon(Math.PI+10);
		// System.out.println(a);
		double[] DroneYPR = {0.0, 0.0, 0.0};
		double[] CameraYPR = {0.0, 0.0, 0.0};
		double lt = latToRadians(53.320556);
		double ln = lonToRadians(1.729722);
		double Hd = 10;
		double[] coords = LatLonEstimation(DroneYPR, CameraYPR, lt, ln, Hd);
		System.out.println("Estimated Coordinates: ");
		System.out.println(coords[0]+", "+coords[1]);
	}
}