package npm.calc.ipcm.npmcalc;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mo on 8/5/14.
 */
public class LegumeCalc extends ActionBarActivity{

    // Layout objects
    private ScrollView mainLayout;
    private TextView forageSpecies1;
    private TextView forageSpecies2;
    private TextView soil1;
    private TextView soil2;
    private TextView standDensity1;
    private TextView standDensity2;
    private TextView standDensity3;
    private TextView standDensityInfo;
    private TextView regrowth1;
    private TextView regrowth2;
    private TextView result;
    private TwoStateToggle species;
    private TwoStateToggle soilType;
    private ThreeStateToggle standDensity;
    private TwoStateToggle regrowth;


    private String legumeSpecies_Tag;
    private String legumeSoil_Tag;
    private String legumeDensity_Tag;
    private String legumeRegrowth_Tag;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.layout_creditcalc);
        //setContentView(R.layout.activity_main);

//        FrameLayout container = (FrameLayout) findViewById(R.id.container);
//
//
//        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View menuLayout = inflater.inflate(R.layout.layout_creditcalc, container, true);


        // Assign all of the instance variables
        mainLayout = (ScrollView) findViewById(R.id.main_parent);


        forageSpecies1 = (TextView) findViewById(R.id.forage_species1);
        forageSpecies2 = (TextView) findViewById(R.id.forage_species2);
        soil1 = (TextView) findViewById(R.id.soil_type1);
        soil2 = (TextView) findViewById(R.id.soil_type2);
        standDensity1 = (TextView) findViewById(R.id.stand_density1);
        standDensity2 = (TextView) findViewById(R.id.stand_density2);
        standDensity3 = (TextView) findViewById(R.id.stand_density3);
        standDensityInfo = (TextView) findViewById(R.id.density_info);
        regrowth1 = (TextView) findViewById(R.id.regrowth1);
        regrowth2 = (TextView) findViewById(R.id.regrowth2);
        result = (TextView)findViewById(R.id.output_text);

        species = new TwoStateToggle(forageSpecies1, forageSpecies2);
        soilType = new TwoStateToggle(soil1,soil2);
        regrowth = new TwoStateToggle(regrowth1,regrowth2);
        standDensity = new ThreeStateToggle(standDensity1, standDensity2, standDensity3);




        final LegumeHelper calc = new LegumeHelper(species, soilType, standDensity, regrowth, result);

        forageSpecies1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(species.getCurrentState() != 0)
                {
                    species.setState(0);
                }
                calc.calculate();

            }
        });

        forageSpecies2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(species.getCurrentState() != 1)
                {
                    species.setState(1);

                }
                calc.calculate();

            }
        });

        soil1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soilType.getCurrentState() != 0)
                {
                    soilType.setState(0);
                }
                calc.calculate();

            }
        });

        soil2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soilType.getCurrentState() != 1)
                {
                    soilType.setState(1);
                }
                calc.calculate();

            }
        });

        regrowth1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regrowth.getCurrentState() != 0) {
                    regrowth.setState(0);
                }
                calc.calculate();

            }
        });

        regrowth2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(regrowth.getCurrentState() != 1)
                {
                    regrowth.setState(1);
                }
                calc.calculate();

            }
        });

        standDensity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(standDensity.getCurrentState() != 0)
                {
                    standDensity.setState(0);
                    standDensityInfo.setText("70%-100% forage species, >4 plants/ft"+"\u00B2");
                }
                calc.calculate();

            }
        });

        standDensity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(standDensity.getCurrentState() != 1)
                {
                    standDensity.setState(1);
                    standDensityInfo.setText("30%-70% forage species, >1.5-4 plants/ft" +"\u00B2");
                }
                calc.calculate();

            }
        });

        standDensity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(standDensity.getCurrentState() != 2)
                {
                    standDensity.setState(2);
                    standDensityInfo.setText("0%-30% forage species, <1.5 plants/ft"+"\u00B2");
                }
                calc.calculate();

            }
        });



    }




    protected void onPause() {
        //final JSONObject jobjEmail = getJSONfile(urlemail);

        try {
            // Creating JSONObject from String


            switch (species.getCurrentState()){
                case 0: legumeSpecies_Tag = "Alfalfa";
                    break;
                case 1: legumeSpecies_Tag = "Other";
                    break;

            }
            switch (soilType.getCurrentState()){
                case 0: legumeSoil_Tag = "Medium/Fine";
                    break;
                case 1: legumeSoil_Tag = "Sand";
                    break;
            }
            switch (standDensity.getCurrentState()){
                case 0: legumeDensity_Tag = "Good";
                    break;
                case 1: legumeDensity_Tag = "Fair";
                    break;
                case 2: legumeDensity_Tag = "Poor";
                    break;
            }
            switch (regrowth.getCurrentState()){
                case 0: legumeRegrowth_Tag = "<8";
                    break;
                case 1: legumeRegrowth_Tag = ">8";
                    break;
            }


            JSONObject aJson = new JSONObject();

            aJson.put("species", legumeSpecies_Tag);
            aJson.put("soil", legumeSoil_Tag);
            aJson.put("density",legumeDensity_Tag );
            aJson.put("regrowth", legumeRegrowth_Tag);
            aJson.put("lCredit", result.getText());

//            JSONObject jLegume = new JSONObject();
//            jLegume.put("Legume", aJson);



            String FILENAME = "EmailLegume";
            String stringTemp = aJson.toString();

            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(stringTemp.getBytes());
            fos.close();

            Log.v("checkL", aJson.toString());


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onPause();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



}
