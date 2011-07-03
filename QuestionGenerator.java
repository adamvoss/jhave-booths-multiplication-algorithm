package exe.boothsMultiplication;

import java.util.Random;

import exe.ShowFile;
import exe.XMLfibQuestion;
import exe.XMLmcQuestion;
import exe.XMLmsQuestion;
import exe.XMLtfQuestion;
import exe.question;
import exe.boothsMultiplication.BoothsMultiplication;//to access constant RegN defs


public class QuestionGenerator {
    public static final int REGM = BoothsMultiplication.REGM;
    public static final int REGA = BoothsMultiplication.REGA;
    public static final int REGQ = BoothsMultiplication.REGQ;
    public static final int Q1   = BoothsMultiplication.Q1;
    public static final int COUNT= BoothsMultiplication.COUNT;
    public static final int FIRST= BoothsMultiplication.REGSTART;

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
                GAIGSprimitiveRegister OldQ   = getRegister(trace.size()-2, REGQ);//(GAIGSprimitiveRegister)trace.get(trace.size()-2,"RegQ");
                GAIGSprimitiveRegister OldQ_1 = getRegister(trace.size()-2, Q1);//(GAIGSprimitiveRegister)trace.get(trace.size()-2,"Q_1");
                ret = getType1Question(OldQ.getBit(OldQ.getSize()-1),
                                        OldQ_1.getBit(0));
            }
            //an ask-once-only question
            else if (type == 2) {ret = (asked[2] ? null : getType2Question(show));}
            //to be called right after new registers are added, but before they're drawn.
            else if (type == 3) {
                GAIGSprimitiveRegister OldQ   = getRegister(trace.size()-2, REGQ);//(GAIGSprimitiveRegister)trace.get(trace.size()-2,"RegQ");
                GAIGSprimitiveRegister OldQ_1 = getRegister(trace.size()-2, Q1);//(GAIGSprimitiveRegister)trace.get(trace.size()-2,"Q_1");

                int select  = ((int)Math.abs(rand.nextInt() )) % 3;
                String ref  = (select == 0 ? "M": (select == 1 ? "A" : "Q"));
                String phref= (select == 0 ? "A": (select == 1 ? "Q" : "M"));

                GAIGSprimitiveRegister oldReg = getRegister(trace.size()-2, select);//(GAIGSprimitiveRegister)trace.get(trace.size()-2, "Reg" + ref);
                GAIGSprimitiveRegister newReg = getRegister(trace.size()-1, select);//(GAIGSprimitiveRegister)trace.get("Reg" + ref);
                GAIGSprimitiveRegister phony  = getRegister(trace.size()-1, (select+1) % 3);//(GAIGSprimitiveRegister)trace.get("Reg" + phref);

               ret = getType3Question(OldQ.getBit(OldQ.getSize()-1), OldQ_1.getBit(0), oldReg, newReg, phony, ref); 
            }
            else if (type == 4) {
                if (asked[4])
                ret = getType4Question(getRegister(trace.size()-1, REGQ).getSize(), (CountBox)getRegister(trace.size()-1, COUNT), show);
            }
            else if (type == 5) {ret = (asked[5] ? null : getType5Question(show));}
            else if (type == 6) {ret = (asked[6] ? null : getType6Question(show));}
            else if (type == 7) {ret = (asked[7] ? null : getType7Question(getRegister(trace.size()-1, REGA), show));}

            else {}
 
            asked[type] = true;
            return ret;
        }
	}

    public question getComparisonQuestion() {return getQuestion(selectOnCount(2) == 0 ? 1 : 3);}//could be random also.

    public question getAdditionQuestion()   {return getQuestion(selectOnCount(2) == 0 ? 2 : 5);}//ditto

    public question getShiftQuestion() {
        int select = selectOnCount(3);

        return getQuestion(select == 0 ? 7 : (select == 1 ? 4 : 6));
    }

    private int selectOnCount(int mod) {
        //return (((GAIGSprimitiveRegister)trace.get("RegQ")).getSize() - ((CountBox)trace.get("Count")).getCount() ) % mod;
        return (getRegister(trace.size()-1, REGQ).getSize() - ((CountBox) getRegister(trace.size()-1, COUNT) ).getCount() ) % mod;
    }
	/**
	 * Returns a random question of the "Which operation will occur next?" flavor.
	 * Call at the comparison frame of the loop.
	 *
	 */
	private question getType1Question(int Q0, int Q_1) {
		int select = ((int)Math.abs(rand.nextInt() )) % 3;
		question ret = null;
		int pcalc = Q0 - Q_1;

		if (select == 0) {
			XMLmsQuestion ret1 = new XMLmsQuestion(show, id.next() );
			ret1.setQuestionText("Q(0) and Q(-1) are " + Q0 + " and " + Q_1 + 
			" respectively. Select all the operations that will occur on this iteration of the loop.");

			ret1.addChoice("Addition");
			ret1.addChoice("Subtraction");

			if (pcalc != 0) {
				ret1.setAnswer(pcalc == -1 ? 1 : 2);
			}

			ret1.addChoice("Arithmetic Right Shift");
			ret1.setAnswer(3);

			ret = ret1;

		}
		else if (select == 1) {
			XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
			ret1.setQuestionText("Which operation will occur on the next snapshot?");
			ret1.addChoice("Addition");
			ret1.addChoice("Subtraction");
			ret1.addChoice("Arithmetic Right Shift");
			ret1.addChoice("None of the other choices.");

			ret1.setAnswer(pcalc == 0 ? 3 : (pcalc == 1 ? 2 : 1) );

			ret = ret1;
		}
		else {
			XMLtfQuestion ret1 = new XMLtfQuestion(show, id.next() );
//			ret1.setQuestionText("Both an addition (A + M) and an arithmetic right shift will occur in the next iteration of the loop.");
//			ret1.setAnswer(pcalc != 0 && pcalc != 1);
            int asked = ((int)(Math.abs(rand.nextInt() ) ) % 3) - 1;

            String quetext = (asked != 0 ? (asked == -1 ? "An addition (A + M) and an " : "A subtraction (A - M) and an ") : "Only an ") + 
                "arithmetic right shift will occur during the next iteration of the loop";

            ret1.setQuestionText(quetext);
            ret1.setAnswer(asked==pcalc);

			ret = ret1;
		}

		//   ret.shuffle();

		return ret;
	}

    /**
    * Returns a random question of the "What can be done in a loop iteration" flavor.
    * To be called whenever, but only once.
    */
    private question getType2Question(ShowFile show) {
        XMLtfQuestion ret = new XMLtfQuestion(show, id.next() );
        ret.setQuestionText("It is possible for an addition and subtraction to occur in the same iteration of the loop.");
        ret.setAnswer(false);
        return ret;
    }

	/**
	 * Returns a random question of the "Predict the value of the register" flavor.
	 * Call at the comparison frame of the loop. 
	 *
	 */
	private question getType3Question(int Q0, int Q_1, GAIGSprimitiveRegister oldReg, GAIGSprimitiveRegister newReg, GAIGSprimitiveRegister phony, 
			String regName) {

		int select = ((int)Math.abs(rand.nextInt() )) % 4;
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

            int select2 = ((int) Math.abs(rand.nextInt() )) % 2;

            if (select == 0) {ret1.setQuestionText("What will the value in register " + regName + " be on the next snapshot?");}
            else             {ret1.setQuestionText("What will the value in register " + regName + " be after the " 
                + (pcalc == 0 ? "SHIFT" : (pcalc == -1) ? "ADDITION" : "SUBTRACTION") + " operation finishes executing?");}
			ret1.setAnswer(correctChoice);

			ret = ret1;
		}
		else if (select == 1) {
			XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
			ret1.setQuestionText("What will the value in register " + regName + " be on the next snapshot?");
			ret1.addChoice(correctChoice);
			ret1.setAnswer(1);
            if (!oldValChoice.toString().equals(correctChoice.toString() ) ) ret1.addChoice(oldValChoice);
            if (!phonyChoice.toString().equals( correctChoice.toString() ) ) ret1.addChoice(phonyChoice) ;
            ret1.addChoice(confuseChoice);

            ret = ret1;
		}
		else {
            int select2 = ((int)Math.abs(rand.nextInt() )) % 2;
            XMLtfQuestion ret1 = new XMLtfQuestion(show, id.next() );
            
            switch(select2) {
                case 0: ret1.setQuestionText("The value of register " + regName + " will be " + correctChoice + " on the next snapshot.");
                    ret1.setAnswer(true);
                    break;
                case 1: ret1.setQuestionText("The value of register " + regName + " will be " + confuseChoice + " on the next snapshot.");
                    ret1.setAnswer(false);
                    break;
                default: break;
            }

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
        int select = ((int)Math.abs(rand.nextInt() ) ) % 2;
        question ret = null;

        if (select == 0) {
            XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
            ret1.setQuestionText("How many values currently in register Q will be represented in the final value?");
            ret1.addChoice("" + (QLEN - count.getCount() + 1) );
            ret1.setAnswer(1);
            ret1.addChoice("" + (count.getCount() == QLEN-count.getCount()+1 ? QLEN-count.getCount() : count.getCount() ) );
            ret1.addChoice("None");

            ret1.shuffle();
            ret = ret1;
        }
        else {
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
        }

        return ret;
    }

    /**
    * Returns a question regarding the relation of the size of the registers to the number of shift operations.
    * Should only be called once.
    * Probably after a shift occurs
    */
    private question getType5Question(ShowFile show) {
        question ret = null;

        int select = ((int)Math.abs(rand.nextInt() )) % 2;

        if (select == 0) { 
            XMLfibQuestion ret1 = new XMLfibQuestion(show, id.next() );
            ret1.setQuestionText("In terms of QLEN, the number of bits in register Q, express the total number of shift operations performed" + 
                " during the exectution of the algorithm.");
            ret1.setAnswer("qlen");
            ret = ret1;
        }
        else {
            XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
            ret1.setQuestionText("How many shift operations will occur during the execution of the algorithm?");
            ret1.addChoice("" + getRegister(trace.size()-1, REGQ).getSize() );
            ret1.setAnswer(1);
            ret1.addChoice("" + (2 * getRegister(trace.size()-1, REGQ).getSize() ));
            ret1.addChoice("It depends on values in Q");
            ret1.addChoice("" + BoothsMultiplication.numLines(getRegister(0, REGQ).toString() ));//((GAIGSprimitiveRegister)trace.get(0, "RegQ")).toString() ) );

            ret = ret1;
        }

        return ret;
    }

    /**
    * Returns a question regarding the mathematical significance of a right sign-preserving shift.
    * Should be called after a shift occurs.
    * And only once.
    */
    private question getType6Question(ShowFile show) {
        XMLmcQuestion ret = new XMLmcQuestion(show, id.next() );
        ret.setQuestionText("What is the significance of the arithmetic (sign-preserving) right shift?");
        ret.addChoice("Multiply the value of registers A and Q by 2");
        ret.addChoice("Divide the value of A and Q by 2");
        ret.setAnswer(2);
        ret.addChoice("Adds 1 to the value in A and Q");
        ret.addChoice("Moves unneccessary bits out of registers A and Q");

        return ret;
    }

	/**
	 * Returns a random question of the "Sign in A matches sign of final product" flavor.
	 * Call after a shift operation.
	 *
	 */
    private question getType7Question(GAIGSprimitiveRegister RegA, ShowFile show) {
        XMLtfQuestion ret = new XMLtfQuestion(show, id.next() );
        ret.setQuestionText("The sign of the current value in register A matches the sign of the final product.");
        int curAValue = binStrToInt(RegA.toString() );
        int finalProd = binStrToInt(getRegister(FIRST, REGQ).toString() ) * binStrToInt(getRegister(FIRST, REGM).toString() );
        ret.setAnswer(Math.signum((double)curAValue) == Math.signum((double)finalProd) || 
            (curAValue == 0 && finalProd >= 0) );

//      System.out.println("" + curAValue + "\t" + finalProd + "\t" + RegA);

        return ret;
    }

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

    /** Returns a random number from 1 to range-1*/
    private int getInRange(int range) {return ((int)Math.abs(rand.nextInt() )) % range;}

    private GAIGSprimitiveRegister getRegister(int level, int reg) {
        return (GAIGSprimitiveRegister)((GAIGSpane)trace.get(level)).get(reg);
    }

	//Heavy Duty, tis even its own class
    //You know it.
	private static class UniqueIDGen {
		private int id;

		public UniqueIDGen() {
			id = 0;
		}

		public String next() {return ("" + id++);}
	}
}
