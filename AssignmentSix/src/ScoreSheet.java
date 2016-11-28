/****************************************************************************
*
* Created by: Patrick Nguyen
* Created on: November 2016
* This ScoreSheet Class Implements The ScoreSheet Object Into Yatzhee
* Assignment 6
*
****************************************************************************/
import java.util.Arrays;
import java.util.stream.IntStream;

public class ScoreSheet {
	
	// values of upper of a lower sections
	private int[] _upperSectionRows = new int[9];
	private int[] _lowerSectionRows = new int[9];
	// determines the number of the rows that are selected
	private int[] _upperSectionRowsToKeep = new int[6];
	private int[] _lowerSectionRowsToKeep = new int[7];
	// validity status of upper & lower section (has user chosen row yet)
	private String[] _upperSectionRowsStatus = new String[6];
	private String[] _lowerSectionRowsStatus = new String[7];
	// number of die that have same value
	private int[] _dieOfSameValue = new int[6]; 
	private Boolean _yahtzeeRolled = false;
	private Boolean _bonusAchieved = false;
	private Boolean _jokerOn = false;
	
	private int _totalScore = 0;
	// _upperSectionRows
	// [0] = aces
	// [1] = twos
	// [2] = threes
	// [3] = fours
	// [4] = fives
	// [5] = sixes
	// [6] = total score
	// [7] = bonus
	// [8] = total
		
	// _lowerSectionRows
	// [0] = 3 of a kind
	// [1] = 4 of a kind
	// [2] = full house
	// [3] = small straight
	// [4] = large straight
	// [5] = yatzhee
	// [6] = chance
	// [7] = yatzhee bonus
	// [8] = grand total
	
	// initial startup --> sets usability of upper and lower rows
	public void startSectionKeeping()
	{
		for (int count = 0; count < this._upperSectionRowsToKeep.length; count++)
		{
			this._upperSectionRowsToKeep[count] = (count + 1);
		}
		
		for (int count = 0; count < this._lowerSectionRowsToKeep.length; count++)
		{
			this._lowerSectionRowsToKeep[count] = (count + 1);
		}
		
		for (int count = 0; count < this._upperSectionRowsStatus.length; count++)
		{
			this._upperSectionRowsStatus[count] = "";
		}
		
		for (int count = 0; count < this._lowerSectionRowsStatus.length; count++)
		{
			this._lowerSectionRowsStatus[count] = "";
		}
	}
	
	// resets some elements in the upper section of the score sheet
	public void resetUpperSection(int upperScore)
	{
		// doesn't reset values of rows already selected by user
		for (int count = 0; count < this._upperSectionRowsStatus.length; count++)
		{
			if (this._upperSectionRowsStatus[count].equals(""))
			{
				this._upperSectionRows[count] = 0;
			}
		}
		
		this._upperSectionRows[6] = upperScore; // updates upper score
		this._dieOfSameValue = new int[6]; // sets all values to 0
	}
	
	// resets some elements in the lower section of the score sheet
	public void resetLowerSection(int totalScore)
	{
		// doesn't reset values of rows already selected by user
		for (int count = 0; count < this._lowerSectionRowsStatus.length; count++)
		{
			if (this._lowerSectionRowsStatus[count].equals(""))
			{
				this._lowerSectionRows[count] = 0;
			}
		}
		
		this._lowerSectionRows[8] = totalScore; // updates total score
	}
	
	// gets all of the properties
	public Boolean getYahtzeeRolled()
	{
		return this._yahtzeeRolled;
	}
	public Boolean getJokerOn()
	{
		return this._jokerOn;
	}
	public Boolean getBonusAchieved()
	{
		return this._bonusAchieved;
	}
	public int[] getUpperSectionToKeep()
	{
		return this._upperSectionRowsToKeep;
	}
	public int[] getLowerSectionToKeep()
	{
		return this._lowerSectionRowsToKeep;
	}	
	public int[] getUpperSectionRows()
	{
		return this._upperSectionRows;
	}
	public int[] getLowerSectionRows()
	{
		return this._lowerSectionRows;
	}
	public String[] getUpperSectionRowsStatus()
	{
		return this._upperSectionRowsStatus;
	}
	public String[] getLowerSectionRowsStatus()
	{
		return this._lowerSectionRowsStatus;
	}
	public int[] getDieOfTheSameValue()
	{
		return this._dieOfSameValue;
	}
	

	// method fixes a yahtzee bonus glitch (IMPORTANT)
	public void fixYahtzeeBonus()
	{
		this._lowerSectionRows[7] = this._lowerSectionRows[7] - 100;
	}
	

	// if the joker rule scenario actually occurs
	public void updateJokerRules()
	{
		this._lowerSectionRows[2] = 25;
		this._lowerSectionRows[3] = 30;
		this._lowerSectionRows[4] = 40;
	}
	
	public void calculateJokerRules()
	{
		for(int count = 0; count < this._dieOfSameValue.length; count++)
		{	
			// accounts for multiple yatzhee rolls & applies joker rules
			if(this._dieOfSameValue[count] == 5)
			{
				// yahtzee
				if (this._lowerSectionRowsStatus[5].equals(""))
				{
					this._lowerSectionRows[5] = 50; 
					this._yahtzeeRolled = true;
				}		
				
				// according to the joker rules, if a player score 0 for yatzhee and then score another
				// yatzhee, they do NOT get the yatzhee bonus (what this code does)
				if (!(this._lowerSectionRowsStatus[5].equals("")) && (this._lowerSectionRows[5] != 0)
						&& this._yahtzeeRolled == true)
				{
					this._lowerSectionRows[7] = this._lowerSectionRows[7] + 100;
					this._lowerSectionRows[8] = this._lowerSectionRows[8] + 100;
				}		
				// check to see if the user scored 0 for yatzhee, if they did, the joker rules are applied
				
				if (this._lowerSectionRowsToKeep[5] == 0 && this._lowerSectionRows[5] == 0)
				{
					this._jokerOn = true;
				}
			}	
		}		
	}
	// method to calculate all of the scenarios for the lower section of the score sheet
	public void calculateSections(Dice[] allDie)
	{
		// algorithm to calculate values of upper section (also used on score sheet)
		for (int count = 0; count < 6; count ++)
		{
			for (int count2 = 0; count2 < allDie.length; count2++)
			{
				if (this._upperSectionRowsStatus[count].equals(""))
				{
					if(allDie[count2].getValue() == (count + 1))
					{
						this._upperSectionRows[count] = this._upperSectionRows[count] + count + 1;					
					}
				}
			}
		}
		
		// absolute values of the upper section (not always used on score sheet)
		int[] upperSectionRowsTemp = new int[9];
		
		for (int count = 0; count < 6; count ++)
		{
			for (int count2 = 0; count2 < allDie.length; count2++)
			{
				if(allDie[count2].getValue() == (count + 1))
				{
					upperSectionRowsTemp[count] = upperSectionRowsTemp[count] + count + 1;
					this._dieOfSameValue[count] = this._dieOfSameValue[count] + 1;
				}
			}
		}

		
		// bonus for upper score, yahtzee bonus
		if (this._bonusAchieved == false)
		{
			if (this._upperSectionRows[6] >= 63)
			{
				this._upperSectionRows[7] = 35;
				this._upperSectionRows[8] = this._upperSectionRows[6] + 35;
				this._bonusAchieved = true;
			}
			else
			{		
				this._upperSectionRows[8] = this._upperSectionRows[6];
			}
		}
		
		

		// calculations for the lower section
		int[] dieFaceValues = new int[5];	
		int[] dieValues = Arrays.copyOf(upperSectionRowsTemp, 6);	// array of the die number values
		int sumOfDieValues =  IntStream.of(dieValues).sum(); // gets sum of previous array
		
		// gets face values of all die and adds them to an array
		for (int count = 0; count < allDie.length; count ++)
		{
			dieFaceValues[count] = allDie[count].getValue();
		}
		
		// sorts array from lowest to highest
		Arrays.sort(dieFaceValues);
		
		// variables for determining straights
		int streaktempVal = 0;
		int streak = 1;
		
		// uses the ordered array to find largest "streak"
		for (int count = 0; count < dieFaceValues.length; count ++)
		{
			if (streaktempVal == dieFaceValues[count])
			{
				streak = streak + 1;
			}			
			streaktempVal = dieFaceValues[count] + 1;        
		}
		
		// small straight
		if (streak >= 4)
		{
			if (this._lowerSectionRowsStatus[3].equals(""))
			{
				this._lowerSectionRows[3] = 30;
			}
		}
		
		// large straight
		if (streak == 5)
		{
			if (this._lowerSectionRowsStatus[4].equals(""))
			{
				this._lowerSectionRows[4] = 40;
			}
		}			
								
		// calculates some columns of bottom section using the number of die with
		// the same face value
		for(int count = 0; count < this._dieOfSameValue.length; count++)
		{					
			// full house
			if(this._dieOfSameValue[count] == 2 || this._dieOfSameValue[count] == 3)
			{
				int refVal = this._dieOfSameValue[count];
				
				for(int count2 = 0; count2 < this._dieOfSameValue.length; count2++)
				{	
	   			    if (this._dieOfSameValue[count2] == (5 - refVal))
	   			    {
	   			    	if (this._lowerSectionRowsStatus[2].equals(""))
	   					{
	   			    		this._lowerSectionRows[2] = 25;
	   					}
	   			    }
				}			
			}
			// three of a kind
			if (this._dieOfSameValue[count] >= 3)
			{
				if (this._lowerSectionRowsStatus[0].equals(""))
				{
					this._lowerSectionRows[0] = sumOfDieValues;
				}
			}
			// four of a kind
			if(this._dieOfSameValue[count] >= 4)
			{
				if (this._lowerSectionRowsStatus[1].equals(""))
				{
					this._lowerSectionRows[1] = sumOfDieValues;
				}
			}
									
			// chance
			if (this._lowerSectionRowsStatus[6].equals(""))
			{
				this._lowerSectionRows[6] = sumOfDieValues;			
			}	
		}
		
		
	}

	
	public void calculateAndDisplayTotalScore()
	{
		this._totalScore = this._upperSectionRows[8] + this._lowerSectionRows[8];
		System.out.println("Congralutions! You Finished Yatzhee! Your Final Score Is: " + this._totalScore);
	}
	
	
	public void displayInfo()
	{
		// displays Score Sheet
		String upperSection = 
			     "================================ \n"
			+    "   SCORE SHEET: UPPER SECTION \n" 
			+    "================================ \n"
			+    "| 1 | Aces         | " + this._upperSectionRows[0] + this._upperSectionRowsStatus[0] + "\n"
			+    "| 2 | Twos         | " + this._upperSectionRows[1] + this._upperSectionRowsStatus[1] +  "\n"
			+    "| 3 | Threes       | " + this._upperSectionRows[2] + this._upperSectionRowsStatus[2] +  "\n"
			+    "| 4 | Fours        | " + this._upperSectionRows[3] + this._upperSectionRowsStatus[3] +  "\n"
			+    "| 5 | Fives        | " + this._upperSectionRows[4] + this._upperSectionRowsStatus[4] +  "\n"
			+    "| 6 | Sixes        | " + this._upperSectionRows[5] + this._upperSectionRowsStatus[5] +  "\n"
			+    "|   | Total Score  | " + this._upperSectionRows[6] + "\n"
			+    "|   | Bonus        | " + this._upperSectionRows[7] + "\n"
			+    "|   | Total        | " + this._upperSectionRows[8];
	
	    String lowerSection = 
			     "================================ \n"
			+    "   SCORE SHEET: LOWER SECTION \n" 
			+    "================================ \n"
			+    "| 1 | Three Of A Kind  | " + this._lowerSectionRows[0] + this._lowerSectionRowsStatus[0] +  "\n"
			+    "| 2 | Four Of A Kind   | " + this._lowerSectionRows[1] + this._lowerSectionRowsStatus[1] +   "\n"
			+    "| 3 | Full House       | " + this._lowerSectionRows[2] + this._lowerSectionRowsStatus[2] +   "\n"
			+    "| 4 | Small Straight   | " + this._lowerSectionRows[3] + this._lowerSectionRowsStatus[3] +   "\n"
			+    "| 5 | Large Straight   | " + this._lowerSectionRows[4] + this._lowerSectionRowsStatus[4] +   "\n"
			+    "| 6 | Yahtzee          | " + this._lowerSectionRows[5] + this._lowerSectionRowsStatus[5] +   "\n"
			+    "| 7 | Chance           | " + this._lowerSectionRows[6] + this._lowerSectionRowsStatus[6] +   "\n"
			+    "|   | Yatzhee Bonus    | " + this._lowerSectionRows[7] + "\n"
			+    "|   | Total Score      | " + this._lowerSectionRows[8] + "\n";

	    System.out.println(upperSection);
	    System.out.println(lowerSection);				
	}	
}