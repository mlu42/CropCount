package npm.calc.ipcm.npmcalc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;


/**
 * Perform the Manure Calculator Activity with user inputs
 */
public class ManureCalc extends Activity {
    private ScrollView mainLayout;


    private TextView incorpTime_title;
    private TextView manure_source_title;

    private TextView incorpTime1;
    private TextView incorpTime2;
    private TextView incorpTime3;
    private ThreeStateToggle incorpTime;


    private TextView input_title;
    private TextView input_reset;
    private TextView input_avail;
    private TextView inputN_title;
    private TextView display_availN;
    private TextView display_totalN;
    private EditText availN;
    private EditText totalN;
    private TextView inputP2O5_title;
    private EditText availP2O5;
    private EditText totalP2O5;
    private TextView display_availP;
    private TextView display_totalP;
    private TextView inputK2O_title;
    private EditText availK2O;
    private EditText totalK2O;
    private TextView display_availK;
    private TextView display_totalK;
    private TextView inputS_title;
    private EditText availS;
    private EditText totalS;
    private TextView display_availS;
    private TextView display_totalS;


    private TextView decrease;
    private TextView increase;
    private TextView countRes;
    private double incrementor = 1.0;
    private TwoStateToggle counter;

    private Spinner spinner;

    private TextView resultN;
    private TextView resultP;
    private TextView resultK;
    private TextView resultS;

    private static String urlmanure = "ManureCredits.json";
    private static String urlemail = "EmailInfo.json";


    private String manureSpecies_Tag = "";
    private String manureType_Tag = "";
    private String incorptime_Tag = "";

    final Handler handler = new Handler();

    final int upperbound = 50;
    final int lowerbound = 1;
    final String unitSolid = " ton/acre";
    final String unitLiquid = " gal/acre";

    private double rateN;
    private double rateP;
    private double rateK;
    private double rateS;

    private double output_availN = 0;
    private double output_availP = 0;
    private double output_availK = 0;
    private double output_availS = 0;


    private boolean inTotalSide = false;
    private boolean justreset = false;
    private boolean fromTotal = false;

    private JSONObject obj;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manure);

        // Assign all of the instance variables
        mainLayout = (ScrollView) findViewById(R.id.main_parent);

        manure_source_title = (TextView) findViewById(R.id.manure_item_header);
        incorpTime_title = (TextView) findViewById(R.id.incorp_time_title);

        incorpTime1 = (TextView) findViewById(R.id.incorp_time1);
        incorpTime2 = (TextView) findViewById(R.id.incorp_time2);
        incorpTime3 = (TextView) findViewById(R.id.incorp_time3);


        //input buttons
        input_avail = (TextView) findViewById(R.id.title_input_avail);
        input_reset = (TextView) findViewById(R.id.title_input_reset);
        input_title = (TextView) findViewById(R.id.title_input_total);

        inputN_title = (TextView) findViewById(R.id.N_input_title);
        availN = (EditText) findViewById(R.id.N_input_avail);
        totalN = (EditText) findViewById(R.id.N_input_total);
        display_availN = (TextView) findViewById(R.id.display_availN);
        display_totalN = (TextView) findViewById(R.id.display_totalN);

        inputP2O5_title = (TextView) findViewById(R.id.P_input_title);
        availP2O5 = (EditText) findViewById(R.id.P_input_avail);
        totalP2O5 = (EditText) findViewById(R.id.P_input_total);
        display_availP = (TextView) findViewById(R.id.display_availP);
        display_totalP = (TextView) findViewById(R.id.display_totalP);

        inputK2O_title = (TextView) findViewById(R.id.K_input_title);
        availK2O = (EditText) findViewById(R.id.K_input_avail);
        totalK2O = (EditText) findViewById(R.id.K_input_total);
        display_availK = (TextView) findViewById(R.id.display_availK);
        display_totalK = (TextView) findViewById(R.id.display_totalK);

        inputS_title = (TextView) findViewById(R.id.S_input_title);
        availS = (EditText) findViewById(R.id.S_input_avail);
        totalS = (EditText) findViewById(R.id.S_input_total);
        display_availS = (TextView) findViewById(R.id.display_availS);
        display_totalS = (TextView) findViewById(R.id.display_totalS);


        decrease = (TextView) findViewById(R.id.minus);
        increase = (TextView) findViewById(R.id.plus);
        counter = new TwoStateToggle(decrease, increase);
        countRes = (TextView) findViewById(R.id.manure_input);

        resultN = (TextView) findViewById(R.id.manure_output0);
        resultP = (TextView) findViewById(R.id.manure_output1);
        resultK = (TextView) findViewById(R.id.manure_output2);
        resultS = (TextView) findViewById(R.id.manure_output3);


        incorpTime = new ThreeStateToggle(incorpTime1, incorpTime2, incorpTime3);


        //read the jason file
        obj = getJSONfile(urlmanure);


        ///////////////Set the spinner adapter ///////////////////
        spinner = (Spinner) findViewById(R.id.manure_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.manure_source, android.R.layout.simple_spinner_item);
        adapter.notifyDataSetChanged();
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner


        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String selected;

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)

                selected = parent.getItemAtPosition(pos).toString();
                manureSpecies_Tag = spinner.getSelectedItem().toString();
                fillInputFields();
                countRes.setText(String.valueOf(changeRateUnit()));

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
                selected = "";
            }


        });

        setDisplayVisible(false);





    }




    @Override
    public void onResume(){

        super.onResume();



        //the reset button
        input_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cleanResultFields();
                ableInput();
                setDisplayVisible(false);
                clearDisplayFields();

                inTotalSide = false;
                justreset = true;

            }
        });


        //time selctor
        incorpTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incorpTime.getCurrentState() != 0) {
                    incorpTime.setState(0);

                }
                fillInputFields();

            }
        });

        incorpTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incorpTime.getCurrentState() != 1) {
                    incorpTime.setState(1);
                }
                fillInputFields();

            }
        });

        incorpTime3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incorpTime.getCurrentState() != 2) {
                    incorpTime.setState(2);
                }
                fillInputFields();

            }
        });





        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counter.setState(0);
                if (incrementor > lowerbound) {
                    incrementor -= 0.5;

                    countRes.setText(changeRateUnit());

                }


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        counter.reSetState();
                    }
                }, 200);

                calculateResult();


            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counter.setState(1);
                if (incrementor < upperbound) {
                    incrementor += 0.5;

                    countRes.setText(changeRateUnit());
                }


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        counter.reSetState();
                    }
                }, 200);


                calculateResult();


            }
        });

        //use to handle long touch and accelerate the increment of decrement
        decrease.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    counter.setState(0);
                    Log.i("repeatBtn", "MotionEvent.ACTION_DOWN");
                    handler.removeCallbacks(mUpdateTaskdown);
                    handler.postAtTime(mUpdateTaskdown, SystemClock.uptimeMillis() + 500);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i("repeatBtn", "MotionEvent.ACTION_UP");
                    handler.removeCallbacks(mUpdateTaskdown);
                    counter.reSetState();
                }//end else
                return false;
            }//end on touch

        });//end b other button


        increase.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionevent) {

                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    counter.setState(1);
                    Log.i("repeatBtn", "MotionEvent.ACTION_DOWN");
                    handler.removeCallbacks(mUpdateTaskup);
                    handler.postAtTime(mUpdateTaskup, SystemClock.uptimeMillis() + 500);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i("repeatBtn", "MotionEvent.ACTION_UP");
                    handler.removeCallbacks(mUpdateTaskup);
                    counter.reSetState();
                }//end else
                return false;
            } //end onTouch
        }); //end b my button


        //**************************************
        // Listener to change input text fields
        //**************************************
        availN.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!justreset) {

                    setInputAvailVisible(true);
                        availN.setFilters(new InputFilter[]{new DigitsKeyListener(
                                Boolean.FALSE, Boolean.TRUE) {
                            int beforeDecimal = 2,afterDecimal = 1;

                            @Override
                            public CharSequence filter(CharSequence source, int start, int end,
                                                       Spanned dest, int dstart, int dend) {
                                String etText = availN.getText().toString().trim();
                                Log.i("dotttt", etText);
                                String temp = availN.getText() + source.toString();

                                if (etText.length() > 0) {
                                    if (temp.equals(".")) {
                                        return "0.";
                                    } else if (temp.toString().indexOf(".") == -1) {
                                        // no decimal point placed yet
                                        if (temp.length() > beforeDecimal) {
                                            return "";
                                        }
                                    } else {
                                        int dotPosition;
                                        int cursorPositon = availN.getSelectionStart();
                                        if (etText.indexOf(".") == -1) {
                                            Log.i("First time Dot", etText.toString().indexOf(".") + " " + etText);
                                            dotPosition = temp.indexOf(".");
                                            Log.i("dot Positon", cursorPositon + "");
                                            Log.i("dot Positon", etText + "");
                                            Log.i("dot Positon", dotPosition + "");
                                        } else {
                                            dotPosition = etText.indexOf(".");
                                            Log.i("dot Positon", cursorPositon + "");
                                            Log.i("dot Positon", etText + "");
                                            Log.i("dot Positon", dotPosition + "");
                                        }
                                        if (cursorPositon <= dotPosition) {
                                            String x = "";
                                            if (fromTotal) {
                                                x = "true";
                                            } else {
                                                x = "false";
                                            }

                                            Log.i("availN", Integer.toString(dotPosition) + ", " + cursorPositon + "," + etText + "," + x);
                                            String beforeDot = etText.substring(0, dotPosition);
                                            if (beforeDot.length() < beforeDecimal) {
                                                return source;
                                            } else {
                                                if (source.toString().equalsIgnoreCase(".")) {
                                                    return source;
                                                } else {
                                                    return "";
                                                }

                                            }
                                        } else {
                                            Log.i("cursor position", "in right");
                                            temp = temp.substring(temp.indexOf(".") + 1);
                                            if (temp.length() > afterDecimal) {
                                                return "";
                                            }
                                        }

                                    }
                                }
                                return super.filter(source, start, end, dest, dstart, dend);

                            }
                        }});

                    fillInputFields();
                    //calculateResult();

                }


                justreset = false;
            }
        });
        availK2O.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!justreset) {

                        setInputAvailVisible(true);
                        availK2O.setFilters(new InputFilter[]{new DigitsKeyListener(
                                Boolean.FALSE, Boolean.TRUE) {
                            int beforeDecimal = 2
                                    ,
                                    afterDecimal = 1;

                            @Override
                            public CharSequence filter(CharSequence source, int start, int end,
                                                       Spanned dest, int dstart, int dend) {
                                String etText = availK2O.getText().toString();
                                String temp = availK2O.getText() + source.toString();

                                if (etText.length() > 0) {
                                    if (temp.equals(".")) {
                                        return "0.";
                                    } else if (temp.toString().indexOf(".") == -1) {
                                        // no decimal point placed yet
                                        if (temp.length() > beforeDecimal) {
                                            return "";
                                        }
                                    } else {
                                        int dotPosition;
                                        int cursorPositon = availK2O.getSelectionStart();
                                        if (etText.indexOf(".") == -1) {

                                            dotPosition = temp.indexOf(".");

                                        } else {
                                            dotPosition = etText.indexOf(".");

                                        }
                                        if (cursorPositon <= dotPosition) {

                                            String beforeDot = etText.substring(0, dotPosition);
                                            if (beforeDot.length() < beforeDecimal) {
                                                return source;
                                            } else {
                                                if (source.toString().equalsIgnoreCase(".")) {
                                                    return source;
                                                } else {
                                                    return "";
                                                }

                                            }
                                        } else {

                                            temp = temp.substring(temp.indexOf(".") + 1);
                                            if (temp.length() > afterDecimal) {
                                                return "";
                                            }
                                        }
                                    }
                                }
                                return super.filter(source, start, end, dest, dstart, dend);
                            }
                        }});


                    fillInputFields();


                }



                justreset = false;
            }
        });
        availP2O5.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!justreset) {

                        setInputAvailVisible(true);
                        availP2O5.setFilters(new InputFilter[]{new DigitsKeyListener(
                                Boolean.FALSE, Boolean.TRUE) {
                            int beforeDecimal = 2
                                    ,
                                    afterDecimal = 1;

                            @Override
                            public CharSequence filter(CharSequence source, int start, int end,
                                                       Spanned dest, int dstart, int dend) {
                                String etText = availP2O5.getText().toString();
                                String temp = availP2O5.getText() + source.toString();
                                if (etText.length() > 0) {
                                    if (temp.equals(".")) {
                                        return "0.";
                                    } else if (temp.toString().indexOf(".") == -1) {
                                        // no decimal point placed yet
                                        if (temp.length() > beforeDecimal) {
                                            return "";
                                        }
                                    } else {
                                        int dotPosition;
                                        int cursorPositon = availP2O5.getSelectionStart();
                                        if (etText.indexOf(".") == -1) {

                                            dotPosition = temp.indexOf(".");

                                        } else {
                                            dotPosition = etText.indexOf(".");

                                        }
                                        if (cursorPositon <= dotPosition) {

                                            String beforeDot = etText.substring(0, dotPosition);
                                            if (beforeDot.length() < beforeDecimal) {
                                                return source;
                                            } else {
                                                if (source.toString().equalsIgnoreCase(".")) {
                                                    return source;
                                                } else {
                                                    return "";
                                                }

                                            }
                                        } else {

                                            temp = temp.substring(temp.indexOf(".") + 1);
                                            if (temp.length() > afterDecimal) {
                                                return "";
                                            }
                                        }
                                    }
                                }
                                return super.filter(source, start, end, dest, dstart, dend);
                            }
                        }});
                    //}
                    fillInputFields();
                    //calculateResult();

                }


                justreset = false;
            }
        });
        availS.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!justreset) {


//                    if (!inTotalSide) {
//                        disableInput(true);
//                    }
//
//                    if (!fromTotal) {
                    setInputAvailVisible(true);
                        availS.setFilters(new InputFilter[]{new DigitsKeyListener(
                                Boolean.FALSE, Boolean.TRUE) {
                            int beforeDecimal = 2
                                    ,
                                    afterDecimal = 1;

                            @Override
                            public CharSequence filter(CharSequence source, int start, int end,
                                                       Spanned dest, int dstart, int dend) {
                                String etText = availS.getText().toString();
                                String temp = availS.getText() + source.toString();
                                if (etText.length() > 0) {
                                    if (temp.equals(".")) {
                                        return "0.";
                                    } else if (temp.toString().indexOf(".") == -1) {
                                        // no decimal point placed yet
                                        if (temp.length() > beforeDecimal) {
                                            return "";
                                        }
                                    } else {
                                        int dotPosition;
                                        int cursorPositon = availS.getSelectionStart();
                                        if (etText.indexOf(".") == -1) {

                                            dotPosition = temp.indexOf(".");

                                        } else {
                                            dotPosition = etText.indexOf(".");

                                        }
                                        if (cursorPositon <= dotPosition) {

                                            String beforeDot = etText.substring(0, dotPosition);
                                            if (beforeDot.length() < beforeDecimal) {
                                                return source;
                                            } else {
                                                if (source.toString().equalsIgnoreCase(".")) {
                                                    return source;
                                                } else {
                                                    return "";
                                                }

                                            }
                                        } else {

                                            temp = temp.substring(temp.indexOf(".") + 1);
                                            if (temp.length() > afterDecimal) {
                                                return "";
                                            }
                                        }
                                    }
                                }
                                return super.filter(source, start, end, dest, dstart, dend);
                            }
                        }});
                    //}
                    fillInputFields();
                    //calculateResult();


                }
                justreset = false;


            }

        });


        //total change here
        totalN.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isEmpty(totalN) && !justreset) {


                    setInputAvailVisible(false);

                    totalN.setFilters(new InputFilter[]{new DigitsKeyListener(
                            Boolean.FALSE, Boolean.TRUE) {
                        int beforeDecimal = 2
                                ,
                                afterDecimal = 1;

                        @Override
                        public CharSequence filter(CharSequence source, int start, int end,
                                                   Spanned dest, int dstart, int dend) {
                            String etText = totalN.getText().toString();
                            String temp = totalN.getText() + source.toString();
                            if (temp.equals(".")) {
                                return "0.";
                            } else if (temp.toString().indexOf(".") == -1) {
                                // no decimal point placed yet
                                if (temp.length() > beforeDecimal) {
                                    return "";
                                }
                            } else {
                                int dotPosition;
                                int cursorPositon = totalN.getSelectionStart();
                                if (etText.indexOf(".") == -1) {

                                    dotPosition = temp.indexOf(".");

                                } else {
                                    dotPosition = etText.indexOf(".");

                                }
                                if (cursorPositon <= dotPosition) {

                                    String beforeDot = etText.substring(0, dotPosition);
                                    if (beforeDot.length() < beforeDecimal) {
                                        return source;
                                    } else {
                                        if (source.toString().equalsIgnoreCase(".")) {
                                            return source;
                                        } else {
                                            return "";
                                        }

                                    }
                                } else {

                                    temp = temp.substring(temp.indexOf(".") + 1);
                                    if (temp.length() > afterDecimal) {
                                        return "";
                                    }
                                }
                            }

                            return super.filter(source, start, end, dest, dstart, dend);
                        }
                    }});


                    fillInputFields();


                }
                justreset = false;




            }
        });
        totalK2O.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isEmpty(totalK2O) && !justreset) {

                    setInputAvailVisible(false);

                    totalK2O.setFilters(new InputFilter[]{new DigitsKeyListener(
                            Boolean.FALSE, Boolean.TRUE) {
                        int beforeDecimal = 2,afterDecimal = 1;

                        @Override
                        public CharSequence filter(CharSequence source, int start, int end,
                                                   Spanned dest, int dstart, int dend) {
                            String etText = totalK2O.getText().toString();
                            String temp = totalK2O.getText() + source.toString();
                            if (temp.equals(".")) {
                                return "0.";
                            } else if (temp.toString().indexOf(".") == -1) {
                                // no decimal point placed yet
                                if (temp.length() > beforeDecimal) {
                                    return "";
                                }
                            } else {
                                int dotPosition;
                                int cursorPositon = totalK2O.getSelectionStart();
                                if (etText.indexOf(".") == -1) {

                                    dotPosition = temp.indexOf(".");

                                } else {
                                    dotPosition = etText.indexOf(".");

                                }
                                if (cursorPositon <= dotPosition) {

                                    String beforeDot = etText.substring(0, dotPosition);
                                    if (beforeDot.length() < beforeDecimal) {
                                        return source;
                                    } else {
                                        if (source.toString().equalsIgnoreCase(".")) {
                                            return source;
                                        } else {
                                            return "";
                                        }

                                    }
                                } else {

                                    temp = temp.substring(temp.indexOf(".") + 1);
                                    if (temp.length() > afterDecimal) {
                                        return "";
                                    }
                                }
                            }

                            return super.filter(source, start, end, dest, dstart, dend);
                        }
                    }});

                    fillInputFields();

                }
                justreset = false;

            }
        });

        totalP2O5.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isEmpty(totalP2O5) && !justreset) {

                    setInputAvailVisible(false);

                    totalP2O5.setFilters(new InputFilter[]{new DigitsKeyListener(
                            Boolean.FALSE, Boolean.TRUE) {
                        int beforeDecimal = 2
                                ,
                                afterDecimal = 1;

                        @Override
                        public CharSequence filter(CharSequence source, int start, int end,
                                                   Spanned dest, int dstart, int dend) {
                            String etText = totalP2O5.getText().toString();
                            String temp = totalP2O5.getText() + source.toString();
                            if (temp.equals(".")) {
                                return "0.";
                            } else if (temp.toString().indexOf(".") == -1) {
                                // no decimal point placed yet
                                if (temp.length() > beforeDecimal) {
                                    return "";
                                }
                            } else {
                                int dotPosition;
                                int cursorPositon = totalP2O5.getSelectionStart();
                                if (etText.indexOf(".") == -1) {

                                    dotPosition = temp.indexOf(".");

                                } else {
                                    dotPosition = etText.indexOf(".");

                                }
                                if (cursorPositon <= dotPosition) {

                                    String beforeDot = etText.substring(0, dotPosition);
                                    if (beforeDot.length() < beforeDecimal) {
                                        return source;
                                    } else {
                                        if (source.toString().equalsIgnoreCase(".")) {
                                            return source;
                                        } else {
                                            return "";
                                        }

                                    }
                                } else {

                                    temp = temp.substring(temp.indexOf(".") + 1);
                                    if (temp.length() > afterDecimal) {
                                        return "";
                                    }
                                }
                            }

                            return super.filter(source, start, end, dest, dstart, dend);
                        }
                    }});


                    fillInputFields();

                }
                justreset = false;


            }
        });
        totalS.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isEmpty(totalS) && !justreset) {

                    setInputAvailVisible(false);

                    totalS.setFilters(new InputFilter[]{new DigitsKeyListener(
                            Boolean.FALSE, Boolean.TRUE) {
                        int beforeDecimal = 2
                                ,
                                afterDecimal = 1;

                        @Override
                        public CharSequence filter(CharSequence source, int start, int end,
                                                   Spanned dest, int dstart, int dend) {
                            String etText = totalS.getText().toString();
                            String temp = totalS.getText() + source.toString();
                            if (temp.equals(".")) {
                                return "0.";
                            } else if (temp.toString().indexOf(".") == -1) {
                                // no decimal point placed yet
                                if (temp.length() > beforeDecimal) {
                                    return "";
                                }
                            } else {
                                int dotPosition;
                                int cursorPositon = totalS.getSelectionStart();
                                if (etText.indexOf(".") == -1) {

                                    dotPosition = temp.indexOf(".");

                                } else {
                                    dotPosition = etText.indexOf(".");

                                }
                                if (cursorPositon <= dotPosition) {

                                    String beforeDot = etText.substring(0, dotPosition);
                                    if (beforeDot.length() < beforeDecimal) {
                                        return source;
                                    } else {
                                        if (source.toString().equalsIgnoreCase(".")) {
                                            return source;
                                        } else {
                                            return "";
                                        }

                                    }
                                } else {

                                    temp = temp.substring(temp.indexOf(".") + 1);
                                    if (temp.length() > afterDecimal) {
                                        return "";
                                    }
                                }
                            }

                            return super.filter(source, start, end, dest, dstart, dend);
                        }
                    }});


                    fillInputFields();
                }
                justreset = false;


            }
        });
    }


    /*
    * accelerate the increment of decrement
    * */
    private Runnable mUpdateTaskup = new Runnable() {
        public void run() {
            if (incrementor < upperbound) {
                incrementor += 0.5;

                countRes.setText(String.valueOf(changeRateUnit()));
                Log.i("repeatBtn", "repeat click");
            }
            handler.postAtTime(this, SystemClock.uptimeMillis() + 100);

        }//end run
    };// end runnable

    /*
    * accelerate the increment of decrement
    * */
    private Runnable mUpdateTaskdown = new Runnable() {
        public void run() {
            if (incrementor > lowerbound) {
                incrementor -= 0.5;


                countRes.setText(String.valueOf(changeRateUnit()));
                Log.i("repeatBtn", "repeat click");
            }
            handler.postAtTime(this, SystemClock.uptimeMillis() + 100);

        }//end run
    };// end Runnable





    private void cleanResultFields() {
        resultN.setText("00");
        resultP.setText("00");
        resultK.setText("00");
        resultS.setText("00");
    }


    private void disableInput(boolean l) {

        availN.setFocusableInTouchMode(l);
        availP2O5.setFocusableInTouchMode(l);
        availK2O.setFocusableInTouchMode(l);
        availS.setFocusableInTouchMode(l);
        availN.setFocusable(l);
        availP2O5.setFocusable(l);
        availK2O.setFocusable(l);
        availS.setFocusable(l);

        boolean temp = !l;
        totalN.setFocusableInTouchMode(temp);
        totalN.setFocusable(temp);
        totalP2O5.setFocusableInTouchMode(temp);
        totalP2O5.setFocusable(temp);
        totalK2O.setFocusableInTouchMode(temp);
        totalK2O.setFocusable(temp);
        totalS.setFocusableInTouchMode(temp);
        totalS.setFocusable(temp);

    }

    private void ableInput() {

        fromTotal = false;
        availN.setText("");
        availP2O5.setText("");
        availK2O.setText("");
        availS.setText("");

        totalN.setText("");
        totalP2O5.setText("");
        totalK2O.setText("");
        totalS.setText("");

        clearDisplayFields();

        availN.setVisibility(View.VISIBLE);
        availP2O5.setVisibility(View.VISIBLE);
        availK2O.setVisibility(View.VISIBLE);
        availS.setVisibility(View.VISIBLE);

        totalN.setVisibility(View.VISIBLE);
        totalP2O5.setVisibility(View.VISIBLE);
        totalK2O.setVisibility(View.VISIBLE);
        totalS.setVisibility(View.VISIBLE);

        resultN.setText("00");
        resultP.setText("00");
        resultK.setText("00");
        resultS.setText("00");

    }



    private void setDisplayVisible(boolean boo){
        if(boo){
            display_availN.setVisibility(View.VISIBLE);
            display_totalN.setVisibility(View.VISIBLE);
            display_availP.setVisibility(View.VISIBLE);
            display_totalP.setVisibility(View.VISIBLE);
            display_availK.setVisibility(View.VISIBLE);
            display_totalK.setVisibility(View.VISIBLE);
            display_availS.setVisibility(View.VISIBLE);
            display_totalS.setVisibility(View.VISIBLE);
        }else{

            display_availN.setVisibility(View.GONE);
            display_totalN.setVisibility(View.GONE);
            display_availP.setVisibility(View.GONE);
            display_totalP.setVisibility(View.GONE);
            display_availK.setVisibility(View.GONE);
            display_totalK.setVisibility(View.GONE);
            display_availS.setVisibility(View.GONE);
            display_totalS.setVisibility(View.GONE);

        }


    }

    private void clearDisplayFields(){
        display_availN.setText("");
        display_totalN.setText("");
        display_availP.setText("");
        display_totalP.setText("");
        display_availK.setText("");
        display_totalK.setText("");
        display_availS.setText("");
        display_totalS.setText("");


    }

    private void setInputAvailVisible(boolean boo){
        if(boo){
            availN.setVisibility(View.VISIBLE);
            availP2O5.setVisibility(View.VISIBLE);
            availK2O.setVisibility(View.VISIBLE);
            availS.setVisibility(View.VISIBLE);

            totalN.setVisibility(View.GONE);
            totalP2O5.setVisibility(View.GONE);
            totalK2O.setVisibility(View.GONE);
            totalS.setVisibility(View.GONE);

            display_availN.setVisibility(View.GONE);
            display_totalN.setVisibility(View.VISIBLE);
            display_availP.setVisibility(View.GONE);
            display_totalP.setVisibility(View.VISIBLE);
            display_availK.setVisibility(View.GONE);
            display_totalK.setVisibility(View.VISIBLE);
            display_availS.setVisibility(View.GONE);
            display_totalS.setVisibility(View.VISIBLE);



        }else{
            availN.setVisibility(View.GONE);
            availP2O5.setVisibility(View.GONE);
            availK2O.setVisibility(View.GONE);
            availS.setVisibility(View.GONE);

            totalN.setVisibility(View.VISIBLE);
            totalP2O5.setVisibility(View.VISIBLE);
            totalK2O.setVisibility(View.VISIBLE);
            totalS.setVisibility(View.VISIBLE);

            display_availN.setVisibility(View.VISIBLE);
            display_totalN.setVisibility(View.GONE);
            display_availP.setVisibility(View.VISIBLE);
            display_totalP.setVisibility(View.GONE);
            display_availK.setVisibility(View.VISIBLE);
            display_totalK.setVisibility(View.GONE);
            display_availS.setVisibility(View.VISIBLE);
            display_totalS.setVisibility(View.GONE);

        }


    }

    /*
     * help to change rate unit when appropriate
     */
    private String changeRateUnit() {
        String temp;

        if (manureSpecies_Tag.contains("solid")) {
            temp = Double.toString(incrementor) + unitSolid;
            availN.setHint("lb/ton");
            availP2O5.setHint("lb/ton");
            availK2O.setHint("lb/ton");
            availS.setHint("lb/ton");

            totalN.setHint("lb/ton");
            totalP2O5.setHint("lb/ton");
            totalK2O.setHint("lb/ton");
            totalS.setHint("lb/ton");

            display_availN.setHint("lb/ton");
            display_totalN.setHint("lb/ton");
            display_availP.setHint("lb/ton");
            display_totalP.setHint("lb/ton");
            display_availK.setHint("lb/ton");
            display_totalK.setHint("lb/ton");
            display_availS.setHint("lb/ton");
            display_totalS.setHint("lb/ton");

        } else {
            availN.setHint("lb/1000gal");
            availP2O5.setHint("lb/1000gal");
            availK2O.setHint("lb/1000gal");
            availS.setHint("lb/1000gal");

            totalN.setHint("lb/1000gal");
            totalP2O5.setHint("lb/1000gal");
            totalK2O.setHint("lb/1000gal");
            totalS.setHint("lb/1000gal");

            display_availN.setHint("lb/1000gal");
            display_totalN.setHint("lb/1000gal");
            display_availP.setHint("lb/1000gal");
            display_totalP.setHint("lb/1000gal");
            display_availK.setHint("lb/1000gal");
            display_totalK.setHint("lb/1000gal");
            display_availS.setHint("lb/1000gal");
            display_totalS.setHint("lb/1000gal");


            temp = Integer.toString((int) (incrementor * 1000)) + unitLiquid;
        }

        return temp;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    /*
    * call when need to calculate the result.
    */
    public void fillInputFields() {

        //??????
        if (!manureSpecies_Tag.isEmpty() && incorpTime.getCurrentState() > -1) {

            switch (incorpTime.getCurrentState()) {
                case 0:
                    incorptime_Tag = "<";
                    break;
                case 1:
                    incorptime_Tag = "-";
                    break;
                case 2:
                    incorptime_Tag = ">";
                    break;

            }
            boolean fillin = parseJASON(obj);
            DecimalFormat format = new DecimalFormat("##.#");
            if (fillin) {
                double temp;
                if (!isEmpty(totalN)) {
                    temp = Double.parseDouble(totalN.getText().toString()) * rateN;
                    temp = Double.valueOf(format.format(temp));
                    output_availN= temp;
                    display_availN.setText(Double.toString(temp));


                }
                if (!isEmpty(totalP2O5)) {
                    temp = Double.parseDouble(totalP2O5.getText().toString()) * rateP;
                    temp = Double.valueOf(format.format(temp));
                    output_availP=temp;
                    display_availP.setText(Double.toString(temp));

                }
                if (!isEmpty(totalK2O)) {
                    temp = Double.parseDouble(totalK2O.getText().toString()) * rateK;
                    temp = Double.valueOf(format.format(temp));
                    output_availK=temp;
                    display_availK.setText(Double.toString(temp));

                }
                if (!isEmpty(totalS)) {
                    temp = Double.parseDouble(totalS.getText().toString()) * rateS;
                    temp = Double.valueOf(format.format(temp));
                    output_availS=temp;
                    display_availS.setText(Double.toString(temp));

                }


                if (!isEmpty(availN)) {
                    output_availN = Double.parseDouble(availN.getText().toString());
                    temp = output_availN / rateN;
                    temp = Double.valueOf(format.format(temp));

                    display_totalN.setText(Double.toString(temp));


                }
                if (!isEmpty(availP2O5)) {
                    output_availP =  Double.parseDouble(availP2O5.getText().toString());
                    temp = output_availP/ rateP;
                    temp = Double.valueOf(format.format(temp));
                    output_availP=temp;
                    display_totalP.setText(Double.toString(temp));

                }
                if (!isEmpty(availK2O)) {
                    output_availK = Double.parseDouble(availK2O.getText().toString());
                    temp =  output_availK/ rateK;
                    temp = Double.valueOf(format.format(temp));
                    output_availK = temp;
                    display_totalK.setText(Double.toString(temp));

                }
                if (!isEmpty(availS)) {
                    output_availS = Double.parseDouble(availS.getText().toString());
                    temp = output_availS / rateS;
                    temp = Double.valueOf(format.format(temp));
                    output_availS = temp;
                    display_totalS.setText(Double.toString(temp));

                }





            }

        }


        calculateResult();


    }


    /*
    * get the jason file
    * */
    private JSONObject getJSONfile(String url) {

        JSONObject jObj = null;
        String myjsonstring = "";

        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(url)));
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }

            myjsonstring = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(myjsonstring);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jObj;
    }

    /*
    * parse the jason object into text and set them into the text view
    * */
    private boolean parseJASON(JSONObject jObj) {

        String aJasonrsltN = "";
        String aJasonrsltP = "";
        String aJasonrsltK = "";
        String aJasonrsltS = "";

        try {
            // Creating JSONObject from String
            JSONObject aJsonManure = jObj.getJSONObject("Analyzed_Manure");

            JSONObject aJasonAnimal = aJsonManure.getJSONObject(manureSpecies_Tag);


            JSONObject aJasonN = aJasonAnimal.getJSONObject("N");

            aJasonrsltN = aJasonN.getString(incorptime_Tag);

            aJasonrsltK = aJasonAnimal.getString("K");
            aJasonrsltP = aJasonAnimal.getString("P");
            aJasonrsltS = aJasonAnimal.getString("S");


            rateN = Double.parseDouble(aJasonrsltN) / 100.0;
            rateP = Double.parseDouble(aJasonrsltP) / 100.0;
            rateK = Double.parseDouble(aJasonrsltK) / 100.0;
            rateS = Double.parseDouble(aJasonrsltS) / 100.0;

            Log.v("EditText", aJasonrsltN + " " + aJasonAnimal);

            return true;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }


    }


    private boolean calculateResult() {



        if (isEmpty(availN) && isEmpty(availP2O5) && isEmpty(availK2O) && isEmpty(availS) &&
                isEmpty(display_availN) &&
                isEmpty(display_availP) &&
                isEmpty(display_availK) &&
                isEmpty(display_availS) ) {
            return false;
        }



        //???
        if (!manureSpecies_Tag.isEmpty()) {
            if (manureSpecies_Tag.contains("solid")) {
                manureType_Tag = "Solid";
            } else {
                manureType_Tag = "Liquid";

            }
        }

        int rsltN = 00;
        int rsltK = 00;
        int rsltP = 00;
        int rsltS = 00;

        if (!isEmpty(availN)) {
            rsltN = (int) Math.round(Double.parseDouble(availN.getText().toString()) * incrementor);
            resultN.setText(Integer.toString(rsltN));
        }

        if (!isEmpty(availP2O5)) {
            rsltP = (int) Math.round(Double.parseDouble(availP2O5.getText().toString()) * incrementor);
            resultP.setText(Integer.toString(rsltP));
        }

        if (!isEmpty(availK2O)) {
            rsltK = (int) Math.round(Double.parseDouble(availK2O.getText().toString()) * incrementor);
            resultK.setText(Integer.toString(rsltK));
        }
        if (!isEmpty(availS)) {
            rsltS = (int) Math.round(Double.parseDouble(availS.getText().toString()) * incrementor);
            resultS.setText(Integer.toString(rsltS));
        }

        if (!isEmpty(display_availN)) {

            rsltN = (int) Math.round(Double.parseDouble(display_availN.getText().toString()) * incrementor);
            resultN.setText(Integer.toString(rsltN));
        }

        if (!isEmpty(display_availP)) {
            rsltP = (int) Math.round(Double.parseDouble(display_availP.getText().toString()) * incrementor);
            resultP.setText(Integer.toString(rsltP));
        }

        if (!isEmpty(display_availK)) {
            rsltK = (int) Math.round(Double.parseDouble(display_availK.getText().toString()) * incrementor);
            resultK.setText(Integer.toString(rsltK));
        }
        if (!isEmpty(display_availS)) {
            rsltS = (int) Math.round(Double.parseDouble(display_availS.getText().toString()) * incrementor);
            resultS.setText(Integer.toString(rsltS));
        }



            if (rsltN > 1000 || rsltP > 1000 || rsltK > 1000 || rsltS > 1000) {
                boolean bigger = false;
                adjustTextSize(rsltN, resultN, bigger);
                adjustTextSize(rsltK, resultK, bigger);
                adjustTextSize(rsltP, resultP, bigger);
                adjustTextSize(rsltS, resultS, bigger);
            } else {
                boolean bigger = true;
                adjustTextSize(rsltN, resultN, bigger);
                adjustTextSize(rsltK, resultK, bigger);
                adjustTextSize(rsltP, resultP, bigger);
                adjustTextSize(rsltS, resultS, bigger);


            }




        //Log.v("EditText", "Did not end in the loop");
        return true;
    }


    /*
    * Test if the fields are empty
    */
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0 &&
                etText.getText().toString().trim().length() < 5) {
            return false;
        } else {
            return true;
        }
    }
    private boolean isEmpty(TextView vText) {
        if (vText.getText().toString().trim().length() > 0 &&
                vText.getText().toString().trim().length() < 5) {
            return false;
        } else {
            return true;
        }
    }


    /*
    * adjust the text size
    */
    private void adjustTextSize(int n, TextView v, boolean bigger) {
        if (bigger) {
            v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
        } else {
            v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
        }

    }

    /*
    * When the activity is set aside, store the data in an internal file
    */
    @Override
    protected void onPause() {

        try {
            // Creating JSONObject from String

            JSONObject aJson = new JSONObject();


            if (manureSpecies_Tag.contains("solid")) {
                manureType_Tag = "Solid";
            } else {
                manureType_Tag = "Liquid";
            }

            aJson.put("analyzed", "y");
            aJson.put("type", manureType_Tag);
            aJson.put("source", manureSpecies_Tag);
            aJson.put("time", incorptime_Tag);
            aJson.put("count", incrementor);
            aJson.put("MCreditN", resultN.getText());
            aJson.put("MCreditP", resultP.getText());
            aJson.put("MCreditK", resultK.getText());
            aJson.put("MCreditS", resultS.getText());


            aJson.put("AvailN", output_availN);
            aJson.put("AvailP", output_availP);
            aJson.put("AvailK", output_availK);
            aJson.put("AvailS", output_availS);


            String FILENAME = "EmailManure";
            String string = aJson.toString();

            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();

            Log.v("checkM", aJson.toString());


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calls the parent onPause() method
        super.onPause();


    }

}



