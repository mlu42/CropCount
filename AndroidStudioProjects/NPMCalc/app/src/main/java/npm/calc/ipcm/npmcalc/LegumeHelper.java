package npm.calc.ipcm.npmcalc;

/*
 * A class that assists with performing the calculations for the final result
 */

import android.widget.TextView;

public class LegumeHelper {


	private TwoStateToggle species;
	private ThreeStateToggle standDensity;
	private TwoStateToggle soil;
	private TwoStateToggle regrowth;
    private TextView result;

	
	// A table with all of the possible results that can be easily used to look up the result
	private int[][] resultTable = new int[3][4];

    {
        resultTable[0][0] = 150;
        resultTable[0][1] = 190;
        resultTable[0][2] = 100;
        resultTable[0][3] = 140;
        resultTable[1][0] = 120;
        resultTable[1][1] = 160;
        resultTable[1][2] = 70;
        resultTable[1][3] = 110;
        resultTable[2][0] = 90;
        resultTable[2][1] = 130;
        resultTable[2][2] = 40;
        resultTable[2][3] = 80;
    }

	/*
	 * A constructor that takes all of the necessary layout objects to be read from or written to
	 */
	public LegumeHelper(TwoStateToggle sp, TwoStateToggle s, ThreeStateToggle sd, TwoStateToggle rg, TextView rs)
	{
		species = sp;
		soil = s;
		standDensity = sd;
		regrowth = rg;
        result = rs;


	}
	
	/*
	 * Calculates the range by deciding which indexes to look up
	 * and looking them up in the table
	 */
	public void calculate()
	{

        int r = 0;
		int c = 0;
        int temp = 0;
        if(species.getCurrentState()> -1 && soil.getCurrentState()> -1 && standDensity.getCurrentState()> -1 && regrowth.getCurrentState()> -1) {
            r = standDensity.getCurrentState();

            if (soil.getCurrentState() == 0) {
                c =regrowth.getCurrentState();
            } else  {
                if(regrowth.getCurrentState() == 0){
                    c=2;
                }
                else{
                    c
                            =3;
                }
            }


            if (species.getCurrentState() == 0) {
                result.setText(((Integer)resultTable[r][c]).toString());
            } else {
                temp = (int) Math.round(resultTable[r][c] * 0.8);
                result.setText(((Integer)temp).toString());
            }
        }


	}
	
}
