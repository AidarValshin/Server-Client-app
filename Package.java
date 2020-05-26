import java.io.Serializable;

public class Package implements Serializable {
    private int first;
    private int second;
    private String operator;
    private int period;

    Package(int first, int second, String operator, int period) {
        this.first = first;
        this.second = second;
        this.operator = operator;
        this.period = period;
    }

    public int getFirst() {
        return first;
    }

    public int getPeriod() {
        return period;
    }

    public int getSecond() {
        return second;
    }

    public String getOperator() {
        return operator;
    }


}
