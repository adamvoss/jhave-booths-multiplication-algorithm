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
			RegisterName regName) {

		int select = ((int)Math.abs(rand.nextInt() )) % 4;
		question ret = null;
		int pcalc = Q0 - Q_1;

		if (select == 0) {
			XMLfibQuestion ret1 = new XMLfibQuestion(show, id.next() );
			ret1.setQuestionText("What will the value in register " + regName + " be on the next slide?");
			ret1.setAnswer(newReg.toString() );

			ret = ret1;
		}
		else if (select == 1) {
			XMLmcQuestion ret1 = new XMLmcQuestion(show, id.next() );
			ret1.setQuestionText("What will the value in register " + regName + " be on the next slide?");
			ret1.addChoice(newReg.toString() );
			ret1.setAnswer(1);
			ret1.addChoice(oldReg.toString() );
			ret1.addChoice(phony.toString()  );
			//TODO add final choice
		}
		else if (select == 2) {
		}
		else {
		}

		return ret;
	}

	//Heavy Duty, tis even its own class
	private static class UniqueIDGen {
		private int id;

		public UniqueIDGen() {
			id = 0;
		}

		public String next() {return ("" + id++);}
	}
}