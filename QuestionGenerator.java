package exe.boothsMultiplication;

import java.util.Random;

import exe.XMLfibQuestion;
import exe.XMLmcQuestion;
import exe.XMLmsQuestion;
import exe.XMLtfQuestion;
import exe.question;
import exe.boothsMultiplication.BoothMultiplication;//to access constant RegN defs


public class QuestionGenerator {
    public static final int REGM = BoothMultiplication.REGM;
    public static final int REGA = BoothMultiplication.REGA;
    public static final int REGQ = BoothMultiplication.REGQ;
    public static final int Q1   = BoothMultiplication.Q1;
    public static final int COUNT= BoothMultiplication.COUNT;

    public final int NUMQUESTIONS = 7;
	private ShowFile show;
    private GAIGSpane  trace;
	private UniqueIDGen id = new UniqueIDGen();
    private Random rand    = new Random();
    private boolean[] asked= new boolean[NUMQUESTIONS+1];
//    private int[]   askTime;

    public QuestionGenerator(ShowFile show, GAIGSpane pane) {
        this.show = show;
        this.trace = pane;
    }

	//This is messy and not the best way to do it, but it's only a proof of concept
    //Should be broken out to each method.
	public question getQuestion(int type){
        if (type < 1 || type > NUMQUESTIONS) {
            return null;
        }
        else {
            question ret = null;
            if (type == 1){
                GAIGSregister OldQ   = getRegister(-2, REGQ);//(GAIGSregister)trace.get(trace.size()-2,"RegQ");
                GAIGSregister OldQ_1 = getRegister(-2, Q1);//(GAIGSregister)trace.get(trace.size()-2,"Q_1");
                ret = getType1Question(OldQ.getBit(0),
                                        OldQ_1.getBit(0));
            }
            //an ask-once-only question
            else if (type == 2) {getType2Question(show);}
            //to be called right after new registers are added, but before they're drawn.
            else if (type == 3) {
                GAIGSregister OldQ   = getRegister(-2, REGQ);//(GAIGSregister)trace.get(trace.size()-2,"RegQ");
                GAIGSregister OldQ_1 = getRegister(-2, Q1);//(GAIGSregister)trace.get(trace.size()-2,"Q_1");

                int select  = rand.nextInt(3);
                String ref  = (select == 0 ? "M": (select == 1 ? "A" : "Q"));
                String phref= (select == 0 ? "A": (select == 1 ? "Q" : "M"));

                GAIGSregister oldReg = getRegister(-2, select);//(GAIGSregister)trace.get(trace.size()-2, "Reg" + ref);
                GAIGSregister newReg = getRegister(-1, select);//(GAIGSregister)trace.get("Reg" + ref);
                GAIGSregister phony  = getRegister(-1, (select+1) % 3);//(GAIGSregister)trace.get("Reg" + phref);

               ret = getType3Question(OldQ.getBit(0), OldQ_1.getBit(0), oldReg, newReg, phony, ref); 
            }
            else if (type == 4) {
                if (asked[4])
                ret = getType4Question(getRegister(-1, REGQ).getSize(), (CountBox)getRegister(-1, COUNT), show);
            }
            else if (type == 5) {ret = (asked[5] ? null : getType5Question(show));}
            else if (type == 6) {ret = (asked[6] ? null : getType6Question(show));}
//          else if (type == 7) {ret = (asked[7] ? null : getType7Question(getRegister(-1, REGA), show));}

            else {}
 
            asked[type] = true;
            return ret;
        }
	}

    public question getComparisonQuestion() {return getQuestion(getInRange(2) == 0 ? 1 : 3);}//could be random also.

    public question getAdditionQuestion()   {return getQuestion(2);}//ditto

    public question getShiftQuestion()      {return getQuestion(getInRange(2) == 0 ? 5 : 4);}

    private int selectOnCount(int mod) {
        //return (((GAIGSregister)trace.get("RegQ")).getSize() - ((CountBox)trace.get("Count")).getCount() ) % mod;
        return (getRegister(-1, REGQ).getSize() - ((CountBox) getRegister(-1, COUNT) ).getCount() ) % mod;
    }
	/**
	 * Returns a random question of the "Which operation will occur next?" flavor.
	 * Call at the comparison frame of the loop.
	 *
	 */
	private question getType1Question(int Q0, int Q_1) {
		int select = rand.nextInt(3);
		question ret = null;
		int pcalc = Q0 - Q_1;

		if (select == 0) {
			XMLmsQuestion ret1 = new XMLmsQuestion(show, id.next() );
			ret1.setQuestionText("The least significant bit of Q and the bit \u03B2 are " + Q0 + " and " + Q_1 + 
			" respectively. Select all the operations that will occur on this iteration of the loop.");

			ret1.addChoice("Addition");
			ret1.addChoice("Subtraction");

			if (pcalc != 0) {
				ret1.setAnswer(pcalc == -1 ? 1 : 2);
			}

			ret1.addChoice("Sign-preserving Right Shift");
			ret1.setAnswer(3);

			ret = ret1;

		}
		else if (select == 1) {
			XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
			ret1.setQuestionText("Which of these operations shall be executed next?");
			ret1.addChoice("Addition");
			ret1.addChoice("Subtraction");
			ret1.addChoice("Sign-preserving Right Shift");
			ret1.addChoice("None of the other choices");

			ret1.setAnswer(pcalc == 0 ? 3 : (pcalc == 1 ? 2 : 1) );

			ret = ret1;
		}
		else {
			XMLtfQuestion ret1 = new XMLtfQuestion(show, id.next() );
//			ret1.setQuestionText("Both an addition (A + M) and an arithmetic right shift will occur in the next iteration of the loop.");
//			ret1.setAnswer(pcalc != 0 && pcalc != 1);
            int asked = rand.nextInt(3) - 1;

            String quetext = (asked != 0 ? (asked == -1 ? "An addition (A + M) and a " : "A subtraction (A - M) and a ") : "Only a ") + 
                "sign-preserving right shift will occur during the current iteration of the loop";

            ret1.setQuestionText(quetext);
            ret1.setAnswer(asked==pcalc);

			ret = ret1;
		}

		//   ret.shuffle();

		return ret;
	}

    /*
    * Returns a random question of the "What can be done in a loop iteration" flavor.
    * To be called whenever, but only once.
    */
//  private question getType2Question(ShowFile show) {
//      XMLtfQuestion ret = new XMLtfQuestion(show, id.next() );
//      ret.setQuestionText("It is possible for an addition and subtraction to occur in the same iteration of the loop.");
//      ret.setAnswer(false);
//      return ret;
//  }

    private question getType2Question(ShowFile show) {
        int select = getInRange(2);
        question ret = null;

        if (select == 0) {
            XMLfibQuestion ret1 = new XMLfibQuestion(show, id.next() );
            ret1.setQuestionText("What will be the most significant bit of register A be after the call to signPreservingRightShift?");
            ret1.setAnswer("" + getRegister(-1, REGA).getBit(getRegister(-1, REGA).getSize() - 1));

            ret = ret1;
        }

        if (select == 1) {
            XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
            ret1.setQuestionText("What will the most significant bit of register A be after the call to signPreservingRightShift?");
            ret1.addChoice("0");
            ret1.addChoice("1");
            ret1.addChoice("Not possible to determine");
            ret1.addChoice("" + getRegister(-1, REGA).getBit(getRegister(-1, REGA).getSize() - 1));
            ret1.setAnswer(4);

            ret = ret1;
        }

        return ret;
    }
	/**
	 * Returns a random question of the "Predict the value of the register" flavor.
	 * Call at the comparison frame of the loop. 
	 *
	 */
	private question getType3Question(int Q0, int Q_1, GAIGSregister oldReg, GAIGSregister newReg, GAIGSregister phony, 
			String regName) {

		int select = rand.nextInt(4);
		question ret = null;
		int pcalc = Q0 - Q_1;

        String phonyChoice   = phony.toString();
        String correctChoice = newReg.toString();
        String oldValChoice  = oldReg.toString();
        String confuseChoice = (pcalc == 0 ? rightSignShift(addBinNum(phony.toIntArray(), oldReg.toIntArray() ) ) : rightSignShift(oldReg.toString() ));
        if (confuseChoice.equals(oldValChoice) ) {
            confuseChoice    = (confuseChoice.charAt(0) == '0' ? "1" : "0") + confuseChoice.substring(1);
        }

		if (select == 0) {
			XMLfibQuestion ret1 = new XMLfibQuestion(show, id.next() );

            int select2 = rand.nextInt(2);

            if (select2 == 0) {ret1.setQuestionText("What will the binary value in register " + regName + " be after the next operation is executed?");}
            else             {ret1.setQuestionText( "What will the binary value in register " + regName + " be after the " 
                + (pcalc == 0 ? "SHIFT" : (pcalc == -1) ? "ADDITION" : "SUBTRACTION") + " operation finishes executing?");}
			ret1.setAnswer(correctChoice);

			ret = ret1;
		}
		else if (select == 1) {
			XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
			ret1.setQuestionText("What will the value in register " + regName + " be after the next operation is executed?");
			ret1.addChoice(correctChoice);
			ret1.setAnswer(1);
            if (!oldValChoice.equals(correctChoice)) { 
                ret1.addChoice(oldValChoice);
            }
            if (!phonyChoice.equals(correctChoice) && !phonyChoice.equals(oldValChoice)) {
                ret1.addChoice(phonyChoice) ;
            }
            if (!confuseChoice.equals(phonyChoice) && !confuseChoice.equals(oldValChoice)) {
                ret1.addChoice(confuseChoice);
            }

            ret = ret1;
		}
		else {
            int select2 = rand.nextInt(2);
            XMLtfQuestion ret1 = new XMLtfQuestion(show, id.next() );
            
            ret1.setQuestionText("After the next operation finishes executing, " + 
                "the value of register " + regName + " will be " + 
                (select2 == 0 ? correctChoice : confuseChoice));
            ret1.setAnswer(select2 == 0);

            ret = ret1;
		}

		return ret;
	}

    /**
    * Returns a question regarding relation between working product and final result.
    * Should be called after a shift operation.
    * Probably needs to be called only once, or only once per variation.
    */
    private question getType4Question(int QLEN, CountBox count, ShowFile show) {
        int select = rand.nextInt(QLEN-1);
        question ret = null;

        if (select == 0) {
            XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
            ret1.setQuestionText("How many values currently in register Q will be represented in the final value?");
            ret1.addChoice("" + (QLEN - count.getCount() + 1) );
            ret1.setAnswer(1);
            ret1.addChoice("" + (count.getCount() == QLEN-count.getCount()+1 ? QLEN-count.getCount() : count.getCount() ) );
            ret1.addChoice("None");

            int extra = getInRange(QLEN - 1) + 1;
            if (extra != QLEN - count.getCount() + 1 && extra != (count.getCount() == QLEN-count.getCount()+1 ? QLEN-count.getCount() : count.getCount() ))
                ret1.addChoice("" + extra);

            ret1.shuffle();
            ret = ret1;
        }
/*      else {
            XMLfibQuestion ret1 = new XMLfibQuestion(show, id.next() );
            ret1.setQuestionText("QLEN is the number of bits in register Q. COUNT is the current value of the variable count." +
                 " Express in terms of QLEN and COUNT the number of bits in register Q which will be represented in the final answer. " + 
                 "It must be as short as possible: no spaces, extra parentheses, or unneccessary arithmetic.");
            ret1.setAnswer("qlen-count+1");
            ret1.setAnswer("qlen+1-count");
            ret1.setAnswer("1+qlen-count");
            ret1.setAnswer("(qlen-count)+1");
            ret1.setAnswer("1+(qlen-count)");

            ret1.setAnswer("-count+qlen+1");
            ret1.setAnswer("-count+1+qlen");
            ret1.setAnswer("1-count+qlen");
            ret1.setAnswer("1+(-count)+qlen");

            ret1.setAnswer("-(count-qlen)+1");
            ret1.setAnswer("1+(-(count-qlen))");

            ret1.setAnswer("-(count-qlen-1)");
            ret1.setAnswer("-(count-1-qlen)");
            ret1.setAnswer("-(-1+count-qlen)");

            ret1.setAnswer("qlen-(count-1)");
            ret1.setAnswer("qlen-(-1+count)");
            ret1.setAnswer("-((count-1)-qlen)");
            ret1.setAnswer("-((-1+count)-qlen)");

            ret = ret1;
        }*/

        return ret;
    }

    /**
    * Returns a question regarding the relation of the size of the registers to the number of shift operations.
    * Should only be called once.
    * Probably after a shift occurs
    * TODO change to how many shifts are left
    */
    private question getType5Question(ShowFile show) {
        question ret = null;

        int select = rand.nextInt(2);

        if (select == 0) {
            XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
            ret1.setQuestionText("How many total shift operations will occur during the execution of the algorithm?");
            ret1.addChoice("" + getRegister(-1, REGQ).getSize() );
            ret1.setAnswer(1);
            ret1.addChoice("It depends on the bit values in Q");
            ret1.addChoice("" + BoothMultiplication.numLines(getRegister(0, REGQ).toString() ));
            ret1.addChoice("" + (((CountBox)getRegister(-1, COUNT)).getCount() - 1) );;

            ret = ret1;
        }
        else {
            XMLfibQuestion ret1 = new XMLfibQuestion(show, id.next() );
            ret1.setQuestionText("From now until the algorithm finishes executing, " + 
                "how many more shift operations will occur" + 
                "(not including this one)?");
            ret1.setAnswer("" + (((CountBox)getRegister(-1, COUNT)).getCount() - 1) );
            ret = ret1;
        }

        return ret;
    }

    private question getType6Question(ShowFile show) {
        return null;
    }

    /*
    * Returns a question regarding the mathematical significance of a right sign-preserving shift.
    * Should be called after a shift occurs.
    * And only once.
    */
//  private question getType6Question(ShowFile show) {
//      XMLmcQuestion ret = new XMLmcQuestion(show, id.next() );
//      ret.setQuestionText("What is the significance of the arithmetic (sign-preserving) right shift?");
//      ret.addChoice("Multiply the value of registers A and Q by 2");
//      ret.addChoice("Divide the value of A and Q by 2");
//      ret.setAnswer(2);
//      ret.addChoice("Adds 1 to the value in A and Q");
//      ret.addChoice("Moves unneccessary bits out of registers A and Q");
//
//      return ret;
//  }

	/*
	 * Returns a random question of the "Sign in A matches sign of final product" flavor.
	 * Call after a shift operation.
	 *
	 */
//  private question getType7Question(GAIGSregister RegA, ShowFile show) {
//      XMLtfQuestion ret = new XMLtfQuestion(show, id.next() );
//      ret.setQuestionText("The sign of the current value in register A matches the sign of the final product.");
//      int curAValue = binStrToInt(RegA.toString() );
//      int finalProd = binStrToInt(getRegister(0, REGQ).toString() ) * binStrToInt(getRegister(0, REGM).toString() );
//      ret.setAnswer(Math.signum((double)curAValue) == Math.signum((double)finalProd) || 
//          (curAValue == 0 && finalProd >= 0) );
//
//      System.out.println("" + curAValue + "\t" + finalProd + "\t" + RegA);
//
//      return ret;
//  }

    private int binStrToInt(String binstr) {
        int sum   =  0 ;
        int toadd =  1 ;
        char prev = '0';

        for (int i = binstr.length()-1; i >= 0; --i) {
            sum   += toadd * (prev - binstr.charAt(i) );
            prev  =  binstr.charAt(i);
            toadd =  toadd * 2;
        }

        return sum;
    }

    private String addBinNum(int[] num1, int[] num2) {
        int carry  = 0;
        int csum   = 0; 
        String ret = "";
         
        for (int i= ((num1.length) - 1); i >= 0; --i) {
            csum  = carry + num1[i] + num2[i];
            ret   = ret + (csum % 2);
            carry = csum / 2;
        }   
             
        return ret;
    }

    private String rightSignShift(String num) {return ((new Character(num.charAt(0))).toString() + num.substring(0, num.length()-1) );}

    /** Returns a random number from 0 to range-1*/
    private int getInRange(int range) {return rand.nextInt(range);}

    private GAIGSregister getRegister(int level, int reg) {
    	if (level < 0){ //Get Relative to the length of the trace
    		return (GAIGSregister)((GAIGSpane)trace.get(trace.size()+level)).get(reg);
    	} //Level 0 is actually labels
        return (GAIGSregister)((GAIGSpane)trace.get(level+1)).get(reg);
    }

	//Heavy Duty, t'is even its own class
    //You know it.
	private static class UniqueIDGen {
		private int id;

		public UniqueIDGen() {
			id = 0;
		}

		public String next() {return ("" + id++);}
	}
}
