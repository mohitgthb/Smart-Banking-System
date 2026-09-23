package strategy;

public class SavingsInterest implements InterestStrategy {

    @Override
    public double calculate(double balance){
        return balance * 0.04;
    }
}
