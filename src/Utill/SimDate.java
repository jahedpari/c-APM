package Utill;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SimDate {

	public int day;
	public int month;
	public int year;

	public SimDate() {
		day = 0;
		month = 0;
		year = 0;
	}

	public SimDate(int Day, int Month, int Year) // throws MyException
	{
		day = Day;
		month = Month;
		year = Year;
	}

	// convert from type 22/06/2013
	public static SimDate convertType1(String[] res) // throws MyException
	{

		int Day = Integer.parseInt(res[0]);
		int Month = Integer.parseInt(res[1]);
		int Year = Integer.parseInt(res[2]);

		SimDate sd = new SimDate();

		sd = new SimDate(Day, Month, Year);

		return sd;

	}

	// convert from type Jul-31-2010
	public static SimDate convertType2(String[] res) // throws MyException
	{

		int Day = Integer.parseInt(res[1]);

		int Year = Integer.parseInt(res[2]);
		String monthLetter = res[0];

		int Month = 0;

		if (monthLetter.toLowerCase().contains("jan"))
			Month = 1;
		else if (monthLetter.toLowerCase().contains("feb"))
			Month = 2;
		else if (monthLetter.toLowerCase().contains("mar"))
			Month = 3;
		else if (monthLetter.toLowerCase().contains("apr"))
			Month = 4;
		else if (monthLetter.toLowerCase().contains("may"))
			Month = 5;
		else if (monthLetter.toLowerCase().contains("jun"))
			Month = 6;
		else if (monthLetter.toLowerCase().contains("jul"))
			Month = 7;
		else if (monthLetter.toLowerCase().contains("aug"))
			Month = 8;
		else if (monthLetter.toLowerCase().contains("sep"))
			Month = 9;
		else if (monthLetter.toLowerCase().contains("oct"))
			Month = 10;
		else if (monthLetter.toLowerCase().contains("nov"))
			Month = 11;
		else if (monthLetter.toLowerCase().contains("dec"))
			Month = 12;

		SimDate sd = new SimDate();

		sd = new SimDate(Day, Month, Year);

		return sd;

	}

	public static SimDate convertToDate(String date) // throws MyException
	{

		String[] res = date.replace("\"", "").split("/");

		if (res.length == 3)
			return convertType1(res);

		else {// to read data in format of Jul-31-2010
			res = date.split("-");
			// System.out.println("size: "+ res.length+"  "+ date);
			if (res.length == 3) {
				return convertType2(res);
			}

			else {
				// throw new MyException("Date is wrong");
				return null;

			}
		}

		// System.out.println(date+"   converted to:  "+giveDate()+"");
	}

	// returns true if the date given is before or equal to argument
	public boolean beforeOrEqual(SimDate sd) {
		if (sd.year > this.year)
			return true;

		else if (sd.year < this.year)
			return false;
		// if same year
		else {
			if (sd.month > this.month)
				return true;

			else if (sd.month < this.month)
				return false;

			// if same year and month
			else {
				if (sd.day > this.day)
					return true;

				else if (sd.day < this.day)
					return false;

				else
					return true;

			}

		}

	}

	public boolean after(SimDate sd) {
		if (sd.year < this.year)
			return true;

		else if (sd.year > this.year)
			return false;
		// if same year
		else {
			if (sd.month < this.month)
				return true;

			else if (sd.month > this.month)
				return false;

			// if same year and month
			else {
				if (sd.day < this.day)
					return true;

				else if (sd.day > this.day)
					return false;

				else
					return false;

			}

		}

	}

	@SuppressWarnings("static-access")
	public int giveWeek() {

		Calendar cal1 = new GregorianCalendar(year, month - 1, day);

		return cal1.WEEK_OF_YEAR;

	}

	public String giveDate() {
		return day + "/" + month + "/" + year;
	}

	@Override
	public String toString() {
		return day + "/" + month + "/" + year;

	}

	public boolean isEqual(SimDate date) {
		// System.out.println(date.day +"  "date.day+"  "+ date.year);

		return (day == date.day && month == date.month && year == date.year);
	}

	/*
	 * it makes a new object for field enter (Calendar.DATE or Calendar.Month or
	 * Calendar.Year) and the quantity to add. to decrease just add negative
	 * numbers
	 */
	public SimDate add(int field, int amount) {

		Calendar cal1 = new GregorianCalendar(year, month - 1, day);

		cal1.add(field, amount);

		// add 1 to calendar month as it starts from 0 in calendar class
		int month = cal1.get(Calendar.MONTH);

		month = month + 1;

		SimDate newDate = new SimDate(cal1.get(Calendar.DAY_OF_MONTH), month,
				cal1.get(Calendar.YEAR));

		return newDate;

	}

	/*
	 * return difference between two dates if the answer is negative then the
	 * object was earlier and vice versa
	 */
	public long compare(SimDate date) {
		Calendar cal1 = new GregorianCalendar(year, month - 1, day);
		Calendar cal2 = new GregorianCalendar(date.year, date.month - 1,
				date.day);

		return (cal1.getTimeInMillis() - cal2.getTimeInMillis());

	}

	/*
	 * return 1 if the object is more near to date 1 return 2 if the object is
	 * more near to date 2 return 0 if equla
	 */
	public int isMoreNear(SimDate date1, SimDate date2) {

		if (Math.abs(compare(date1)) == Math.abs(compare(date2)))
			return 0;
		else if (Math.abs(compare(date1)) < Math.abs(compare(date2)))
			return 1;
		else
			return 2;
	}

}
