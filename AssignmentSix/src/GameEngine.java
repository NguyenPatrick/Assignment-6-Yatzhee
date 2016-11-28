/****************************************************************************
*
* Created by: Patrick Nguyen
* Created on: November 2016
* This GameEngine Class Allows The User To Play Games Of Yatzhee
* Assignment 6
*
****************************************************************************/
import java.util.Scanner;

public class GameEngine {

	static int NUMBER_OF_DIE = 5; // number of die in play
	private int _lowerScore = 0; // total score (lower section)
	private int _upperScore = 70; // upper score(upper section)
	

	// procedure to show the die and score sheet
	public void displayScoreSheetAndDice(ScoreSheet score, Dice[] allDie)
	{
		// calculates the score sheet
		score.resetUpperSection(this._upperScore);
		score.resetLowerSection(this._lowerScore);
		score.calculateSections(allDie);	
		
		// shows score sheet & die values
		score.displayInfo();
		displayDie(allDie);
	}
	
	// procedure to display the values of the die
	public void displayDie(Dice[] allDie)
	{
		// prints die values
		String displayDie = 
			     "====================== \n"
			+    "      ROLLED DIE \n" 
			+    "======================";
		
		System.out.println(displayDie);
		for (int i = 0; i < NUMBER_OF_DIE ; i++)
		{
			System.out.println("| " + (i + 1 )+ " | " + "Dice #" + (i+1) + " | " 
		+ allDie[i].getValue());
		}
		System.out.println();			
	}
	
	// procedure to check if any joker rule exceptions occur
	public void jokerRules(ScoreSheet score, Dice[] allDie)
	{
		// calculates all the values beforehand 
		score.resetUpperSection(this._upperScore);
		score.resetLowerSection(this._lowerScore);
		score.calculateSections(allDie);	
		
		// if the yatzhee bonus scenario is achieved
        score.calculateJokerRules();
		
		// lets the user select the full values of full house, small straight and large straight
		// in accordance to the joker rules. 								
		for (int count = 0; count < score.getDieOfTheSameValue().length; count ++)
		{
			if (score.getJokerOn().equals(true))																					
			{	
				// checks to see if there is a yahtzee
				if (score.getDieOfTheSameValue()[count] == 5)
				{
					// if the upper section row corresponding the the yahtzee is already taken
					if (score.getUpperSectionToKeep()[count] == 0)
					{
						score.updateJokerRules(); // plays the joker rules
					}
				}
			}
		}
		// shows score sheet & die values
		score.displayInfo();
		displayDie(allDie);
	}
	
	// starts a new game of yahtzee
	public void newGame()
	{
		ScoreSheet scoreCard = new ScoreSheet();
		Dice[] totalDie = new Dice[NUMBER_OF_DIE]; // array to store all the die
		Boolean oneTimeBonus = false; // allows bonus to be applied once
		Boolean oneTimeYahtzee = false;
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome To Yatzhee Java Style!");
		
		// creates six die
		for (int i = 0; i < NUMBER_OF_DIE; i++)
		{
			totalDie[i] = new Dice();
		}
		
		scoreCard.startSectionKeeping(); // allows to choose columns
			
		// loops the game for the required 13 rounds
		for(int turns = 0; turns < 13; turns ++)
		{
			System.out.println("================================================");
			System.out.println("          YOU ARE CURRENTLY ON TURN " + (turns + 1));
			System.out.println("================================================");
			
			// rolls six die
			for (int i = 0; i < NUMBER_OF_DIE; i++)
			{
				totalDie[i].roll();
			}
			
			// shows score sheet & die values
			jokerRules(scoreCard, totalDie);
	
			
			// limits player to 3 rolls in total (2 re-rolls)
			for (int reRolls = 0; reRolls < (3-1); reRolls++)
			{
				String yesOrNo;
				System.out.println("Do You Want To Re-Roll Any Die? Y/N. " + "You Have " + 
				(2 - reRolls) + " Re-Rolls Left. ");
				yesOrNo = scan.nextLine();
				
				// asks user if they want to re-roll
				while (!(yesOrNo.equals("Y") || yesOrNo.equals("N")))
				{					
					System.err.println("Invalid Input, Type Y/N. Do You Want To Re-Roll Any Die? Y/N. ");
					yesOrNo = scan.nextLine();
				}			
				
				// if user wants to re-roll any die
				if(yesOrNo.equals("Y"))
				{
					// variables for die selection
					int diceNumber;
					Boolean removeMoreDie = true;
					
					while(removeMoreDie == true)
					{
						try 
						{	System.out.println();
							System.out.println("Enter The Number Of The Die You Want To Remove. " +
						"Enter '-1' If You No Longer Want To Re-Roll Any Die. ");
							
							diceNumber = Integer.parseInt(scan.nextLine());	

							// restricts input between 1-5 (5 die)
							if(diceNumber > 0 && diceNumber <= NUMBER_OF_DIE)
							{
								// checks if dice has already been selected
								if (totalDie[diceNumber - 1].getUseState() == false)
								{
									System.out.println("Dice #" + diceNumber + " Is Preparing To Be Re-Rolled. ");
									totalDie[diceNumber - 1].use(); // prepares die for re-roll
								}
								else
								{
									System.err.println("Dice Has Already Been Chosen. ");
								}
							}
							else 
							{
								// user chooses not to pick any more die
								if(diceNumber == -1)
								{
									removeMoreDie = false;
								}
								else
	                            {
									System.err.println("Enter Dice Number Between 1 & 5. ");
								}
							}							
						} 			
						catch (NumberFormatException e) 
						{						
							System.err.println("Invalid Input! ");		    
						}
					}			
				}
				else
				{
					reRolls = 3; // gets out of the loop
				}
				
				// re-rolls the dice selected & resets them so they
				// can be used again if the user wishes to re-roll
				for (int count = 0; count < NUMBER_OF_DIE ; count ++ )
				{
					if (totalDie[count].getUseState() == true)
					{
						totalDie[count].roll(); // re-rolls die
						totalDie[count].reset(); // die can be re-rolled again
					}
				}								
				// joker rules + shows score sheet & die
				for(int count = 0; count < scoreCard.getDieOfTheSameValue().length; count ++)
				{
					if (scoreCard.getDieOfTheSameValue()[count] == 5)
					{
						if(oneTimeYahtzee == false)
						{
							oneTimeYahtzee = true;						
						}
						else
						{
							scoreCard.fixYahtzeeBonus();
						}
					}
				}
			
				jokerRules(scoreCard, totalDie);			
			}	
			
			// variables for usage in row selection
			Boolean errorCatch = true;
			Boolean rowCatch = true;
			Boolean upperSectionIsFull = true;
			Boolean lowerSectionIsFull = true;
			int chooseSection;
			int chooseColumn;
			
			// try and catch for section selection
			while(errorCatch == true)
			{
				try 
				{	System.out.println();
				    System.out.println("Would You Like To Select A Box From The Upper Section(Type '1')"
						+ " Or Lower Section(Type '2'). ");
					
				    // makes sure upper section is not full
				    for (int i = 0; i < scoreCard.getUpperSectionToKeep().length; i++)
				    {
				    	if (scoreCard.getUpperSectionToKeep()[i] != 0)
				    	{
				    		upperSectionIsFull = false;
				    	}
				    }
				    // makes sure lower section is not full
				    for (int i = 0; i < scoreCard.getLowerSectionToKeep().length; i++)
				    {
				    	if (scoreCard.getLowerSectionToKeep()[i] != 0)
				    	{
				    		lowerSectionIsFull = false;
				    	}
				    }
				    
				    chooseSection = Integer.parseInt(scan.nextLine());	
				    
				    // restricts input between 1 & 2 ( for 2 sections)
					if(chooseSection > 0 && chooseSection <= 2)
					{
						errorCatch = false;
							
						while(rowCatch == true)
						{
							// try and catch for row selection
							try 
							{										
								// upper section selection
								if (chooseSection == 1 && upperSectionIsFull.equals(false))							
								{
									System.out.println("You Chose The Upper Section. Now Enter The Number Of The Row"
											+ " You Want To Score (1 - 6),  If The Row Has Not Yet Been Selected. ");
									chooseColumn = Integer.parseInt(scan.nextLine());
									
									// restricts input to the corresponding number of the rows in the upper section
									if (chooseColumn > 0 && chooseColumn <= scoreCard.getUpperSectionToKeep().length)
									{
										// checks if row has already been scored
										if (scoreCard.getUpperSectionToKeep()[chooseColumn - 1] != 0)
										{
											// adds selected value from upper section to upper and total score
											this._upperScore = scoreCard.getUpperSectionRows()[6] + 
													scoreCard.getUpperSectionRows()[chooseColumn - 1];										
											scoreCard.getUpperSectionRowsStatus()[chooseColumn - 1] = " || ALREADY SELECTED ||";
											scoreCard.getUpperSectionToKeep()[chooseColumn - 1] = 0; // makes it unusable again
											rowCatch = false;
										}
										else
										{										
											System.err.println("Row Has Already Been Selected. ");
										}
									}
									else
									{
										System.err.println("Enter Row Between 1 & 6. ");
									}
								}		
								else
								{
									if (upperSectionIsFull.equals(true) && chooseSection != 2)
									{
										System.err.println("Upper Section Is Full! ");
										chooseSection = 2; // goes to other option
									}
								}
								
								// lower section selection
								if (chooseSection == 2  && lowerSectionIsFull.equals(false))
								{
									System.out.println("You Chose The Lower Section. Now Enter The Number Of The Row"
											+ " You Want To Score (1 - 7), If The Row Has Not Yet Been Selected. ");
									
									chooseColumn = Integer.parseInt(scan.nextLine());	
									
									// restricts input to the corresponding number of the rows in the lower section
									if (chooseColumn > 0 && chooseColumn <= scoreCard.getLowerSectionToKeep().length)
									{

										// checks if row has already been scored
										if (scoreCard.getLowerSectionToKeep()[chooseColumn - 1] != 0)
										{
											// adds selected value from lower section to total score
											this._lowerScore = scoreCard.getLowerSectionRows()[8] + 
													scoreCard.getLowerSectionRows()[chooseColumn - 1];
											scoreCard.getLowerSectionRowsStatus()[chooseColumn - 1] = " || ALREADY SELECTED ||";
											scoreCard.getLowerSectionToKeep()[chooseColumn - 1] = 0; // makes it unusable again
											rowCatch = false;
										}
										else
										{
											System.err.println("Row Has Already Been Selected. ");
										}
									}
									else
									{
										System.err.println("Enter Row Between 1 & 7. ");
									}
								}
								else
								{
									if (lowerSectionIsFull.equals(true) && chooseSection != 1 )
									{
										System.err.println("Lower Section Is Full! ");
										chooseSection = 2; // goes to other option
									}
								}
							}
							catch (NumberFormatException e) 
							{						
								System.err.println("Invalid Input!");		    
							}
						}												
					}								
					else 
					{
						System.err.println("Please Choose From The Upper Section(Type '1') Or From"
								+ " The Lower Section(Type '2'). ");
					}							
				} 			
				catch (NumberFormatException e) 
				{						
					System.err.println("Invalid Input! ");		    
				}
			}	
				
			// allows the bonus to be applied only once
			if(oneTimeBonus == false)
			{
				if (scoreCard.getBonusAchieved().equals(true))
				{
					this._upperScore = this._upperScore + 35;					
					oneTimeBonus = true;
				}
			}
			
			
			// shows score sheet and die
			jokerRules(scoreCard, totalDie);

		}

		scoreCard.calculateAndDisplayTotalScore();
		

	}
}
