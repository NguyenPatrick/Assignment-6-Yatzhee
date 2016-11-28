/****************************************************************************
*
* Created by: Patrick Nguyen
* Created on: November 2016
* This Dice Class Implements The Dice Object Into Yatzhee
* Assignment 6
*
****************************************************************************/

public class Dice {

	// properties
	private int _faceValue;
	private Boolean _hasBeenChosen = false;
	
	// method to re-roll die
	public void roll()
	{
		this._faceValue = (int)(Math.random() * 6 + 1);
    }
	// method to prepare die for re-roll
	public void use()
	{
		this._hasBeenChosen = true;
    }
	// method to make die re-rollable again
	public void reset()
	{
		this._hasBeenChosen = false;
    }

	// gets face value
    public int getValue()
    {
        return this._faceValue;   
    }
    // get the state of availability of the die
    public Boolean getUseState()
    {
        return this._hasBeenChosen;   
    }
}

