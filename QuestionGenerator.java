package exe.boothsMultiplication;

import java.util.Random;

import exe.ShowFile;
import exe.XMLfibQuestion;
import exe.XMLmcQuestion;
import exe.XMLmsQuestion;
import exe.XMLtfQuestion;
import exe.question;


public class QuestionGenerator {
	private ShowFile show;
	private GAIGStrace trace;
	private UniqueIDGen id = new UniqueIDGen();
    private Random rand    = new Random();
    private enum RegisterName {REGM, REGA, REGQ, REGQ_1}

	public QuestionGenerator(ShowFile show, GAIGStrace trace){
		this.show=show;
		this.trace=trace;
	}

	//This is messy and not the best way to do it, but it's only a proof of concept
	public question getQuestion(int type){
		if (type == 1){
			GAIGSregister OldQ   = (GAIGSregister)trace.get("RegQ");
			GAIGSregister OldQ_1 = (GAIGSregister)trace.get("Q_1");
			return getType1Question(OldQ.getBit(OldQ.getSize()-1),
									OldQ_1.getBit(0));
		}
        else if (type == 2) {
            
        }
        //to be called right after new registers are added, but before they're drawn.
        else if (type == 3) {
            GAIGSregister OldQ   = (GAIGSregister)trace.get(trace.size()-2,"RegQ");
            GAIGSregister OldQ_1 = (GAIGSregister)trace.get(trace.size()-2,"Q_1");

            int select  = ((int)Math.abs(rand.nextInt() )) % 3;
            String ref  = (select == 0 ? "M": (select == 1 ? "A" : "Q"));
            String phref= (select == 0 ? "A": (select == 1 ? "Q" : "M"));

            GAIGSregister oldReg = (GAIGSregister)trace.get(trace.size()-2, "Reg" + ref);
            GAIGSregister newReg = (GAIGSregister)trace.get("Reg" + ref);
            GAIGSregister phony  = (GAIGSregister)trace.get("Reg" + phref);

           return getType3Question(OldQ.getBit(OldQ.getSize()-1), OldQ_1.getBit(0), oldReg, newReg, phony, ref); 
        }
        else if (type == 7) {
            GAIGSregister RegA = (GAIGSregister)trace.get("RegA");
            return getType7Question(RegA, show);
        }

		else return null;

        return null;
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
			ret1.setQuestionText("Which operation will occur on the next slide?");
			ret1.addChoice("Addition");
			ret1.addChoice("Subtraction");
			ret1.addChoice("Arithmetic Right Shift");
			ret1.addChoice("None of the other choices.");

			ret1.setAnswer(pcalc == 0 ? 3 : (pcalc == 1 ? 2 : 1) );

			ret = ret1;
		}
		else {
			XMLtfQuestion ret1 = new XMLtfQuestion(show, id.next() );
			ret1.setQuestionText("Both an addition (+M) and an arithmetic right shift will occur in the next iteration of the loop.");
			ret1.setAnswer(pcalc != 0 && pcalc != 1);

			ret = ret1;
		}

		//   ret.shuffle();

		return ret;
	}

    /**
    * Returns a random question of the "What can be done in a loop iteration" flavor.
    * To be called whenever
    */
    private question getType2Question(ShowFile show) {
        XMLtfQuestion ret = new XMLtfQuestion(show, id.next() );
        int select = ((int)Math.abs(rand.nextInt() )) % 2;
        if (select == 0)
            ret.setQuestionText("It is possible for an addition and subtraction to occur in the same iteration of the loop.");
        else
            ret.setQuestionText("It is possible for both an arithmetic shift and either addition or subtraction to occur in " +
                 "the same iteration of the loop");
        ret.setAnswer(select != 0);
        return ret;
    }

	/**
	 * Returns a random question of the "Predict the value of the register" flavor.
	 * Call at the comparison frame of the loop. 
	 *
	 */
	private question getType3Question(int Q0, int Q_1, GAIGSregister oldReg, GAIGSregister newReg, GAIGSregister phony, 
			String regName) {

		int select = ((int)Math.abs(rand.nextInt() )) % 4;
		question ret = null;
		int pcalc = Q0 - Q_1;

        String phonyChoice   = phony.toString();
        String correctChoice = newReg.toString();
        String oldValChoice  = oldReg.toString();
        String confuseChoice = (pcalc == 0 ? rightSignShift(addBinNum(phony.toIntArray(), oldReg.toIntArray() ) ) : rightSignShift(oldReg.toString() ));
        //Sorry for that above line, Adam!

		if (select == 0) {
			XMLfibQuestion ret1 = new XMLfibQuestion(show, id.next() );

            int select2 = ((int) Math.abs(rand.nextInt() )) % 2;

            if (select == 0) {ret1.setQuestionText("What will the value in register " + regName + " be on the next slide?");}
            else             {ret1.setQuestionText("What will the value in register " + regName + " be after the " 
                + (pcalc == 0 ? "SHIFT" : (pcalc == -1) ? "ADDITION" : "SUBTRACTION") + " operation finishes executing?");}
			ret1.setAnswer(correctChoice);

			ret = ret1;
		}
		else if (select == 1) {
			XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
			ret1.setQuestionText("What will the value in register " + regName + " be on the next slide?");
			ret1.addChoice(correctChoice);
			ret1.setAnswer(1);
            if (!oldValChoice.toString().equals(correctChoice.toString() ) ) ret1.addChoice(oldValChoice);
			ret1.addChoice(phonyChoice);
            ret1.addChoice(confuseChoice);

            ret = ret1;
		}
		else {
            int select2 = ((int)Math.abs(rand.nextInt() )) % 2;
            XMLtfQuestion ret1 = new XMLtfQuestion(show, id.next() );
            
            switch(select2) {
                case 0: ret1.setQuestionText("The value of register " + regName + " will be " + correctChoice + " on the next slide.");
                    ret1.setAnswer(true);
                    break;
                case 1: ret1.setQuestionText("The value of register " + regName + " will be " + confuseChoice + " on the next slide.");
                    ret1.setAnswer(false);
                    break;
                default: break;
            }

            ret = ret1;
		}

		return ret;
	}

    private question getType4Question(int QLEN, CountBox count, ShowFile show) {
        int select = ((int)Math.abs(rand.nextInt() ) ) % 2;
        question ret = null;

        if (select == 0) {
            XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
            ret1.setQuestionText("How many values currently in register Q will be represented in the final value?");
            ret1.addChoice("" + count.getCount() );
            ret1.setAnswer(1);
            ret1.addChoice("" + (QLEN - count.getCount()) );
            ret1.addChoice("None");
            if (count.getCount() != 1) ret1.addChoice("1");

            ret1.shuffle();
            ret = ret1;
        }
        else {
            XMLfibQuestion ret1 = new XMLfibQuestion(show, id.next() );
            ret1.setQuestionText("QLEN is the number of bits in register Q. COUNT is the current value of the variable count." +
                 " Express in terms of QLEN and COUNT the number of bits in register Q which will be represented in the final answer.");
            ret1.setAnswer("QLEN-COUNT");
            ret1.setAnswer("(QLEN-COUNT)");
            ret1.setAnswer("(QLEN)-COUNT");
            ret1.setAnswer("QLEN-(COUNT)");

            ret1.setAnswer("QLEN - COUNT");
            ret1.setAnswer("QLEN - count");
            ret1.setAnswer("(QLEN - COUNT)");
            ret1.setAnswer("(QLEN) - COUNT");
            ret1.setAnswer("QLEN - (COUNT)");

            ret1.setAnswer("-COUNT + QLEN");
            ret1.setAnswer("(-COUNT + QLEN)");
            ret1.setAnswer("(-COUNT) + QLEN");
            ret1.setAnswer("-COUNT + (QLEN)");

            ret1.setAnswer("-COUNT+QLEN");
            ret1.setAnswer("-COUNT+qlen");
            ret1.setAnswer("(-COUNT+QLEN)");
            ret1.setAnswer("-COUNT+(QLEN)");
            ret1.setAnswer("(-COUNT)+QLEN");

            ret1.setAnswer("-(COUNT-QLEN)");
            ret1.setAnswer("-(COUNT - QLEN)");

            ret = ret1;
        }

        return ret;
    }

    private question getType5Question(ShowFile show) {
        question ret = null;

        int select = ((int)Math.abs(rand.nextInt() )) % 2;

        if (select == 0) { 
            XMLfibQuestion ret1 = new XMLfibQuestion(show, id.next() );
            ret1.setQuestionText("In terms of QLEN, the number of bits in register Q, express the total number of shift operations performed" + 
                " during the exectution of the algorithm.");
            ret1.setAnswer("QLEN");

            ret = ret1;
        }
        else {
            XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
            ret1.setQuestionText("How many shift operations will occur during the execution of the algorithm?");
            ret1.addChoice("" + ((GAIGSregister)trace.get("RegQ")).getSize() );
            ret1.addChoice("" + (2 * ((GAIGSregister)trace.get("RegQ")).getSize() ) );
            ret1.addChoice("It depends on values in Q");
            ret1.addChoice("" + BoothsMultiplication.numLines(((GAIGStrace)trace.get(0, "RegQ")).toString() ) );

            ret = ret1;
        }

        return ret;
    }

    /**
    * Returns a question regarding the mathematical significance of a right sign-preserving shift.
    * Should be called after a shift occurs.
    */
    private question getType6Question(ShowFile show) {
        XMLmcQuestion ret = new XMLmcQuestion(show, id.next() );
        ret.setQuestionText("What is the significance of the arithmetic (sign-preserving) right shift?");
        ret.addChoice("Multiply the value if registers A and Q by 2");
        ret.addChoice("Divide the value of A and Q by 2");
        ret.setAnswer(2);
        ret.addChoice("Adds 1 to the value in A and Q");
        ret.addChoice("Moves unneccessary bits out of registers A and Q");

        return ret;
    }

	/**
	 * Returns a random question of the "Sign in A matches sign of final product" flavor.
	 * Call at the beginnig of a loop iteration.
	 *
	 */
    private question getType7Question(GAIGSregister RegA, ShowFile show) {
        XMLtfQuestion ret = new XMLtfQuestion(show, id.next() );
        ret.setQuestionText("The sign of the value in register A matches the sign of the final product.");
        int curAValue = binStrToInt(RegA.toString() );
        int finalProd = binStrToInt(trace.get(0, "RegQ").toString() ) * binStrToInt(trace.get(0, "RegM").toString() );
        ret.setAnswer(curAValue * finalProd >= 0);

//        System.out.println("" + curAValue + "\t" + finalProd + "\t" + RegA);

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
         
        for (int i= num1.length-1; i >= 0; ++i) {
            csum  = carry + num1[i] + num2[i];
            ret   = ret + (csum % 2);
            carry = csum / 2;
        }   
             
        return ret;
    }

    private String rightSignShift(String num) {return ((new Character(num.charAt(0))).toString() + num.substring(0, num.length()-1) );}

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
