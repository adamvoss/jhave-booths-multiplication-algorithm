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

	//This is messy and not the best way to do it, but its only a proof of concept
	public question getQuestion(int type){
		if (type == 1){
			GAIGSregister OldQ   = (GAIGSregister)trace.get("RegQ");
			GAIGSregister OldQ_1 = (GAIGSregister)trace.get("Q_1");
			return getType1Question(OldQ.getBit(OldQ.getSize()-1),
									OldQ_1.getBit(0));
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
	}



	/**
	 * Returns a random question of the "Which operation will occur next?" flavor.
	 * Call at the beginnig of a loop iteration.
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

	@SuppressWarnings("unused")
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
