package main;

import java.io.*;
/** * The Class Input is here to enter data with the keyboard.<br>
* The types below are supported by the Input class. <br><br>
* - String <br> - Integer (int) <br> - Double (double) - Boolean (boolean) <br> - Character (char) <br> <br><br>
* @author Patrice Rudaz (patrice.rudaz@hevs.ch)
* @author Cathy Berthouzoz (cathy.berthouzoz@hevs.ch)
* @version 2.0 - 12-12-2007
* @see #readString()
* @see #readInt()
* @see #readDouble()
* @see #readBoolean()
* @see #readChar()
*/
public class Input
{
	/** * Reads a String from the console.
	* @return The typed string
	* @see java.lang.String
	*/
	public static String readString()
	{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		try {return stdin.readLine(); }
		catch (Exception ex) { return "";}
	}

	/** * Reads an Integer from the console.
	* @return The typed Integer or 0 if the typed Integer is invalid
	* @see java.lang.Integer
	*/
	public static int readInt()
	{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		try { return Integer.parseInt(stdin.readLine()); }
		catch (Exception ex) { return 0; }
	}

	/** * Reads a Double from the console.
	* @return The typed Double or 0 if the typed Double is invalid
	* @see java.lang.Double
	*/
	public static double readDouble()
	{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		try { return Double.parseDouble(stdin.readLine()); }
		catch (Exception ex) { return 0; }
	}

	/** Reads a boolean from the console.
	* @return The typed boolean or false if the typed boolean is other than "true"
	*/
	public static boolean readBoolean()
	{
		
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		try { return (Boolean.valueOf(stdin.readLine())).booleanValue(); }
		catch (Exception ex) {return false; }
	}

	/** Reads a char from the console.
	* @return The typed char or the character 0 if the typed char is invalid
	*/
	public static char readChar()
	{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		try { return stdin.readLine().charAt(0); }
		catch (Exception ex) {return '\0'; }
	}
}