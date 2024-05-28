import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PDAA {
    static class TransitionRule {
        public String currentState;
        public String inputSymbol;
        public String stackTop;
        public String nextState;
        public String stackAction;

        public TransitionRule(String currentState, String inputSymbol, String stackTop, String nextState,
                String stackAction) {
            this.currentState = currentState;
            this.inputSymbol = inputSymbol;
            this.stackTop = stackTop;
            this.nextState = nextState;
            this.stackAction = stackAction;
        }
    }

    static class PDA {
        private final List<TransitionRule> transitions;
        private final String startState;
        private final Set<String> acceptStates;

        public PDA(List<TransitionRule> transitions, String startState, Set<String> acceptStates) {
            this.transitions = transitions;
            this.startState = startState;
            this.acceptStates = acceptStates;
        }

        public boolean simulate(String input) {
            Stack<String> stack = new Stack<>();
            stack.push("$");

            String currentState = startState;
            int index = 0;
            input = input.replaceAll(" ", "");
            while (index <= input.length()) {
                String inputSymbol = (index < input.length()) ? String.valueOf(input.charAt(index)) : null;
                TransitionRule validTransition = findValidTransition(currentState, inputSymbol, stack.peek());

                if (validTransition == null) {
                    return false;
                }

                currentState = validTransition.nextState;

                if (!Objects.isNull(validTransition.inputSymbol) && validTransition.inputSymbol.equals("eps"))
                    continue;

                if (validTransition.stackAction.equals("pop")) {
                    stack.pop();
                } else if (validTransition.stackAction.startsWith("push")) {
                    stack.push(validTransition.stackAction.substring(5));
                }
                index++;
            }
            return acceptStates.contains(currentState) && stack.peek().equals("$");
        }

        private TransitionRule findValidTransition(String currentState, String inputSymbol, String stackTop) {

            TransitionRule epsRule = null;
            for (TransitionRule rule : transitions) {
                if (rule.currentState.equals(currentState) &&
                        Objects.equals(rule.inputSymbol, inputSymbol) &&
                        rule.stackTop.equals(stackTop)) {
                    return rule;
                } else if (!Objects.isNull(rule.inputSymbol) && rule.currentState.equals(currentState)
                        && rule.inputSymbol.equals("eps")
                        && rule.stackTop.equals(stackTop)) {
                    epsRule = rule;
                }
            }
            return epsRule;
        }

    }

    public static PDA Q1() {
        List<TransitionRule> transitions = new ArrayList<>();
        transitions.add(new TransitionRule("q0", "a", "$", "q1", "push:A"));
        transitions.add(new TransitionRule("q1", "a", "A", "q1", "push:A"));
        transitions.add(new TransitionRule("q1", "b", "A", "q2", "pop"));
        transitions.add(new TransitionRule("q2", "b", "A", "q2", "pop"));
        transitions.add(new TransitionRule("q2", null, "$", "q_accept", ""));

        Set<String> acceptStates = new HashSet<>();
        acceptStates.add("q_accept");

        return new PDA(transitions, "q0", acceptStates);
    }

    public static PDA Q2() {
        List<TransitionRule> transitions = new ArrayList<>();
        transitions.add(new TransitionRule("q0", "a", "$", "q1", ""));
        transitions.add(new TransitionRule("q0", "a", "A", "q1", ""));
        transitions.add(new TransitionRule("q1", "a", "$", "q0", "push:A"));
        transitions.add(new TransitionRule("q1", "a", "A", "q0", "push:A"));
        transitions.add(new TransitionRule("q0", "eps", "A", "q2", ""));
        transitions.add(new TransitionRule("q2", "b", "A", "q3", ""));
        transitions.add(new TransitionRule("q3", "b", "A", "q4", ""));
        transitions.add(new TransitionRule("q4", "b", "A", "q2", "pop"));
        transitions.add(new TransitionRule("q2", null, "$", "q_accept", ""));

        Set<String> acceptStates = new HashSet<>();
        acceptStates.add("q_accept");

        return new PDA(transitions, "q0", acceptStates);
    }

    public static PDA Q3(){
        List<TransitionRule> transitions = new ArrayList<>();
        transitions.add(new TransitionRule("q0", "{", "$", "q0", "push:{"));
        transitions.add(new TransitionRule("q0", "{", "{", "q0", "push:{"));
        transitions.add(new TransitionRule("q0", "}", "{", "q0", "pop"));
        transitions.add(new TransitionRule("q0", null, "$", "q_accept", ""));

        Set<String> acceptStates = new HashSet<>();
        acceptStates.add("q_accept");

        return new PDA(transitions, "q0", acceptStates);
    }

    public static PDA Q4(){
        List<TransitionRule> transitions = new ArrayList<>();
        transitions.add(new TransitionRule("q0", "a", "$", "q0", "push:A"));
        transitions.add(new TransitionRule("q0", "a", "A", "q0", "push:A"));
        transitions.add(new TransitionRule("q0", "b", "A", "q1", "pop"));
        transitions.add(new TransitionRule("q1", "b", "A", "q1", "pop"));
        transitions.add(new TransitionRule("q1", "c", "A", "q2", "pop"));
        transitions.add(new TransitionRule("q2", "c", "A", "q2", "pop"));
        transitions.add(new TransitionRule("q2", null, "$", "q_accept", ""));
        
        Set<String> acceptStates = new HashSet<>();
        acceptStates.add("q_accept");

        return new PDA(transitions, "q0", acceptStates);
    }

    public static void main(String args[]) {

        try {
            ArrayList<PDA> pdas = new ArrayList<>();
            pdas.add(Q1());
            pdas.add(Q2());
            pdas.add(Q3());
            pdas.add(Q4());

            File inputFile = new File("input_pda.txt");
            Scanner myReader = new Scanner(inputFile);
            FileWriter outputFile = new FileWriter("output_pda_1.txt");

            while (myReader.hasNextLine()) {

                String problemNumber;

                while ((problemNumber = myReader.nextLine()).length() == 0)
                    ;

                outputFile.write(problemNumber + "\n");
                int problemNumberInt = Integer.valueOf(problemNumber);
                String line;

                while (myReader.hasNextLine() && !((line = myReader.nextLine()).toLowerCase().contains("end"))) {
                    line = pdas.get(problemNumberInt - 1).simulate(line) ? "accepted" : "not accepted";
                    outputFile.write(line + "\n");
                }

                outputFile.write("end\n");

            }

            outputFile.close();
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}